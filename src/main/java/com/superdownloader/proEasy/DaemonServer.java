package com.superdownloader.proEasy;

import java.net.URL;
import java.security.ProtectionDomain;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * @author harley
 * 
 */
public class DaemonServer {

	public static void main(String[] args) throws Exception {
		int port = Integer.parseInt(System.getProperty("port", "8080"));
		Server server = new Server(port);

		ProtectionDomain domain = DaemonServer.class.getProtectionDomain();
		URL location = domain.getCodeSource().getLocation();

		WebAppContext webapp = new WebAppContext();
		webapp.setContextPath("/");
		//webapp.setDescriptor(location.toExternalForm() + "/WEB-INF/web.xml");
		webapp.setServer(server);
		webapp.setWar(location.toExternalForm());

		server.setHandler(webapp);
		server.start();
		server.join();
	}

}
