/*******************************************************************************
 * SSHCommandSender.java
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
package net.seedboxer.seedboxer.mule.processor.postaction;

import java.io.File;
import java.io.IOException;
import java.security.PublicKey;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import net.schmizz.sshj.transport.verification.HostKeyVerifier;
import net.seedboxer.seedboxer.core.domain.Configuration;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import freemarker.template.TemplateException;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
@Component(value = "sshCommandSender")
public class SSHCommandSender implements Processor {

	private static final Logger LOGGER = LoggerFactory.getLogger(SSHCommandSender.class);

	@Value("${ssh.timeToJoin}")
	private int timeToJoin;

	@Value("${ssh.variableNameInCmd}")
	private String variableNameInCmd;

	@Override
	public void process(Exchange exchange) {
		Message msg = exchange.getIn();
		String url = (String) msg.getHeader(Configuration.SSH_URL);
		String username = (String) msg.getHeader(Configuration.SSH_USERNAME);
		String password = (String) msg.getHeader(Configuration.SSH_PASSWORD);
		String cmd = (String) msg.getHeader(Configuration.SSH_CMD);

		try {
			String cmdProcessed = processTemplate(cmd, msg.getHeaders());
			sendSSHCmd(url, username, password, cmdProcessed);
		} catch (IOException e) {
			LOGGER.warn("Error sending ssh command.", e);
		} catch (TemplateException e) {
			LOGGER.warn("Error processing template of ssh command.", e);
		}
	}

	private void sendSSHCmd(String url, String username, String password, String command)
			throws IOException {
		final SSHClient sshClient = new SSHClient();

		// TODO: Try to change this to loadKnownHost() or something like this
		sshClient.addHostKeyVerifier(new HostKeyVerifier() {
			@Override
			public boolean verify(String arg0, int arg1, PublicKey arg2) {
				return true; // don't bother verifying
			}
		});

		try {
			sshClient.setConnectTimeout((timeToJoin+5) * 1000); // Time to join plus 5 seconds
			sshClient.setTimeout((timeToJoin+5) * 1000);
			sshClient.connect(url);
			sshClient.authPassword(username, password);

			final Session session = sshClient.startSession();
			try {
				LOGGER.info("Sending ssh command: {} to {}", command, url);
				final Command cmd = session.exec(command);
				LOGGER.debug("Ssh response: {}", IOUtils.readFully(cmd.getInputStream()).toString());
				cmd.join(timeToJoin, TimeUnit.SECONDS);
				LOGGER.debug("Exit status: {}", cmd.getExitStatus());
			} finally {
				session.close();
			}
		} finally {
			sshClient.disconnect();
		}
	}

	@SuppressWarnings("unchecked")
	private String processTemplate(String command, Map<String, Object> templateVars)
			throws IOException, TemplateException {

		List<String> files = (List<String>) templateVars.get(Configuration.FILES);
		StringBuffer sb = new StringBuffer();
		for (String file : files) {
			sb.append(new File(file).getName());
			sb.append(",");
		}
		sb.deleteCharAt(sb.length()-1);

		return command.replaceAll(variableNameInCmd, sb.toString());
	}

}
