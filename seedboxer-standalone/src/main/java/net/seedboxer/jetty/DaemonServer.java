/*******************************************************************************
 * DaemonServer.java
 * 
 * Copyright (c) 2012 SeedBoxer Team.
 * 
 * This file is part of SeedBoxer.
 * 
 * SeedBoxer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * SeedBoxer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SeedBoxer.  If not, see <http ://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.seedboxer.jetty;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.ProtectionDomain;

import org.apache.commons.io.FileUtils;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * @author harley
 * 
 */
public class DaemonServer {

	private static final String WS_PORT_PROP = "ws.port";
	private static final String PROEASY_PROPS = "seedboxer.properties";
	private static final String CONTEXT_PATH = "/";
	private static final String CONFIG_PATH = "seedboxer.config.path";
	private final int port;

	public static void main(String[] args) throws Exception {
		DaemonServer sc = new DaemonServer();
		sc.start();
	}

	private void registerShutdownHook(final Server server, final WebAppContext context) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				System.out.println("Shutdown hook ran!");
				try { context.stop(); } catch (Exception ignored) {}
				try { server.stop();  } catch (Exception ignored) {}
			}
		});
	}

	public DaemonServer() {
		try {
			String configFile = System.getProperty(CONFIG_PATH + File.separator + PROEASY_PROPS);
			System.getProperties().load(new FileInputStream(configFile));
		} catch (Exception ignored) {}

		port = Integer.parseInt(System.getProperty(WS_PORT_PROP, "8080"));
	}

	private void start() {
		// Start a Jetty server with some sensible(?) defaults
		try {
			Server srv = new Server();
			srv.setStopAtShutdown(true);

			// Allow 5 seconds to complete.
			// Adjust this to fit with your own webapp needs.
			// Remove this if you wish to shut down immediately (i.e. kill <pid> or Ctrl+C).
			srv.setGracefulShutdown(5000);

			// Increase thread pool
			QueuedThreadPool threadPool = new QueuedThreadPool();
			threadPool.setMaxThreads(5);
			srv.setThreadPool(threadPool);

			// Ensure using the non-blocking connector (NIO)
			Connector connector = new SelectChannelConnector();
			connector.setPort(port);
			connector.setMaxIdleTime(30000);
			srv.setConnectors(new Connector[]{connector});

			// Get the war-file
			ProtectionDomain protectionDomain = this.getClass().getProtectionDomain();
			String warFile = protectionDomain.getCodeSource().getLocation().toExternalForm();
			String currentDir = new File(protectionDomain.getCodeSource().getLocation().getPath()).getParent();

			// Handle signout/signin in BigIP-cluster

			// Add the warFile (this jar)
			// Add the warFile (this jar)
			WebAppContext context = new WebAppContext(warFile, CONTEXT_PATH);
			context.setServer(srv);
			resetTempDirectory(context, currentDir);

			// Add the handlers
			HandlerList handlers = new HandlerList();
			handlers.addHandler(context);
			srv.setHandler(handlers);

			// Register shutdown hook
			registerShutdownHook(srv, context);

			srv.start();
			srv.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void resetTempDirectory(WebAppContext context, String currentDir) throws IOException {
		File workDir = new File(currentDir, "work");
		FileUtils.deleteDirectory(workDir);
		context.setTempDirectory(workDir);
	}

}
