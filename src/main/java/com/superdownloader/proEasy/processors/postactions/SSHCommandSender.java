package com.superdownloader.proEasy.processors.postactions;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.superdownloader.proEasy.processors.Headers;

import freemarker.template.TemplateException;

/**
 * @author harley
 *
 */
@Service(value = "sshCommandSender")
public class SSHCommandSender implements Processor {

	private static final Logger LOGGER = LoggerFactory.getLogger(SSHCommandSender.class);

	@Value("${proEasy.ssh.timeToJoin}")
	private int timeToJoin;

	@Value("${proEasy.ssh.variableNameInCmd}")
	private String variableNameInCmd;

	@Override
	public void process(Exchange exchange) {
		Message msg = exchange.getIn();
        String url = (String) msg.getHeader(Headers.SSH_URL);
        String username = (String) msg.getHeader(Headers.SSH_USERNAME);
        String password = (String) msg.getHeader(Headers.SSH_PASSWORD);
        String cmd = (String) msg.getHeader(Headers.SSH_CMD);

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
		final SSHClient ssh = new SSHClient();
        ssh.loadKnownHosts();
        ssh.connect(url);
        try {
        	ssh.authPassword(username, password);
            final Session session = ssh.startSession();
            try {
    			LOGGER.info("Sending ssh command: {} to {}", command, url);
                final Command cmd = session.exec(command);
                LOGGER.debug("Ssh response: {}", IOUtils.readFully(cmd.getInputStream()).toString());
                cmd.join(timeToJoin, TimeUnit.SECONDS);
                LOGGER.info("Exit status: {}", cmd.getExitStatus());
            } finally {
                session.close();
            }
        } finally {
            ssh.disconnect();
        }
	}

	private String processTemplate(String command, Map<String, Object> templateVars)
			throws IOException, TemplateException {

		List<String> files = (List<String>) templateVars.get(Headers.FILES);
		StringBuffer sb = new StringBuffer();
		for (String file : files) {
			sb.append(new File(file).getName());
			sb.append(",");
		}
		sb.deleteCharAt(sb.length()-1);

		return command.replaceAll(variableNameInCmd, sb.toString());
	}

}
