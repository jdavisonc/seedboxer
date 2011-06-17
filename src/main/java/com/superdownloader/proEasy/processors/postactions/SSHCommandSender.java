package com.superdownloader.proEasy.processors.postactions;

import java.io.IOException;
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

/**
 * @author harley
 *
 */
@Service(value = "sshCommandSender")
public class SSHCommandSender implements Processor {

	private static final Logger LOGGER = LoggerFactory.getLogger(SSHCommandSender.class);

	@Value("${proEasy.ssh.timeToJoin}")
	private int timeToJoin;

	@Override
	public void process(Exchange exchange) throws Exception {
		Message msg = exchange.getIn();
		if (msg.getHeaders().containsKey(Headers.SSH_CMD)) {
	        String ssh_url = (String) msg.getHeader(Headers.SSH_URL);
	        String ssh_username = (String) msg.getHeader(Headers.SSH_USERNAME);
	        String ssh_password = (String) msg.getHeader(Headers.SSH_PASSWORD);
	        String ssh_cmd = (String) msg.getHeader(Headers.SSH_CMD);

			try {
				sendSSHCmd(ssh_url, ssh_username, ssh_password, ssh_cmd);
			} catch (IOException e) {
				LOGGER.warn("Error sending ssh SUCCESS command.", e);
			}
		}
	}

	private void sendSSHCmd(String url, String username, String password, String command) throws IOException {
		final SSHClient ssh = new SSHClient();
        ssh.loadKnownHosts();
        ssh.connect(url);
        try {
        	ssh.authPassword(username, password);
            final Session session = ssh.startSession();
            try {
                final Command cmd = session.exec(command);
                LOGGER.debug("{}", IOUtils.readFully(cmd.getInputStream()).toString());
                cmd.join(timeToJoin, TimeUnit.SECONDS);
                LOGGER.info("Exit status: {}", cmd.getExitStatus());
            } finally {
                session.close();
            }
        } finally {
            ssh.disconnect();
        }
	}

}
