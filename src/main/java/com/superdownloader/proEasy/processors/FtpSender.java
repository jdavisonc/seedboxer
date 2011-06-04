
package com.superdownloader.proEasy.processors;

import java.io.File;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.stereotype.Service;

/**
 * @author harley
 *
 */
@Service(value = "ftpSender")
public class FtpSender {

	public static final String TIMEOUT = "60000";

	public String sendToFtp(Exchange exchange) {
		Message msg = exchange.getIn();

		if (exchange.getProperty(Exchange.SLIP_ENDPOINT) == null) {

			/**
			 * TODO: Currently only upload the file get! We need to upload the path in the first
			 * 		 line of this file!!
			 * 		 Also check the remote path
			 */
			List<String> filesToUpload = (List<String>) msg.getHeader(Headers.FILES);
			File fileToUpload = new File(filesToUpload.get(0));
			msg.setBody(fileToUpload);
			msg.setHeader(Exchange.FILE_NAME, fileToUpload.getName());

			String ftp_user = (String) msg.getHeader(Headers.FTP_USERNAME);
			String ftp_pass = (String) msg.getHeader(Headers.FTP_PASSWORD);
			String ftp_url = (String) msg.getHeader(Headers.FTP_URL);
			String ftp_remoteDir = (String) msg.getHeader(Headers.FTP_REMOTE_DIR);

			// Create ftp endoint
			StringBuilder ftpEndpoint = new StringBuilder("ftp://")
												.append(ftp_user)
												.append("@")
												.append(ftp_url)
												.append("/")
												.append(ftp_remoteDir)
												.append("?password=")
												.append(ftp_pass);

			ftpEndpoint.append("&connectTimeout=" + TIMEOUT);
			ftpEndpoint.append("&timeout=" + TIMEOUT);
			ftpEndpoint.append("&soTimeout=" + TIMEOUT);
			ftpEndpoint.append("&throwExceptionOnConnectFailed=true");
			ftpEndpoint.append("&disconnect=true");
			ftpEndpoint.append("&binary=true");

			return  ftpEndpoint.toString();

		} else {
			return null;
		}
	}

}
