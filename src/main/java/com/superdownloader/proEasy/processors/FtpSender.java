
package com.superdownloader.proEasy.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.stereotype.Service;

/**
 * @author harley
 *
 */
@Service(value = "ftpSender")
public class FtpSender {

	public String sendToFtp(Exchange exchange) {
		Message msg = exchange.getIn();

		if (msg.getHeader(Headers.FTP_SENT) == null) {

			String ftp_user = (String) msg.getHeader(Headers.FTP_USERNAME);
			String ftp_pass = (String) msg.getHeader(Headers.FTP_PASSWORD);
			String ftp_url = (String) msg.getHeader(Headers.FTP_URL);
			String ftp_remoteDir = (String) msg.getHeader(Headers.FTP_REMOTE_DIR);

			msg.setHeader(Headers.FTP_SENT, true);

			return "ftp://" + ftp_user + "@" + ftp_url + "/" + ftp_remoteDir + "?password=" + ftp_pass + "&binary=true&delay=60000";

		} else {
			return null;
		}
	}

}
