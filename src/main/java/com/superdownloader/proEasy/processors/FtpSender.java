
package com.superdownloader.proEasy.processors;

import java.io.File;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.superdownloader.common.ftp.FtpUploader;
import com.superdownloader.common.ftp.FtpUploaderCommons;
import com.superdownloader.common.ftp.FtpUploaderListener;
import com.superdownloader.proEasy.exceptions.TransportException;

/**
 * @author harley
 *
 */
@Service(value = "ftpSender")
public class FtpSender implements Processor {

	private static final Logger LOGGER = LoggerFactory.getLogger(FtpSender.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		Message msg = exchange.getIn();

		FtpUploader ftpUploader = new FtpUploaderCommons();

		String user = (String) msg.getHeader(Headers.FTP_USERNAME);
		String pass = (String) msg.getHeader(Headers.FTP_PASSWORD);
		String server = (String) msg.getHeader(Headers.FTP_URL);
		String remoteDir = (String) msg.getHeader(Headers.FTP_REMOTE_DIR);

		@SuppressWarnings("unchecked")
		List<String> filesToUpload = (List<String>) msg.getHeader(Headers.FILES);

		try {
			ftpUploader.configure(server, user, pass, remoteDir);
			// Connect and Upload
			ftpUploader.connect();
			LOGGER.info("Connected to {}", server);
			for (String toUpload : filesToUpload) {
				LOGGER.info("Uploading {}...", toUpload);
				ftpUploader.upload(new File(toUpload), new FtpUploaderListener() {

					@Override
					public void bytesTransferred(long totalBytesTransferred,
							int bytesTransferred, long streamSize) {
						LOGGER.debug("Transfer: {}%", (totalBytesTransferred*100)/streamSize);

					}
				});
			}
		} catch (Exception e) {
			throw new TransportException("Error at uploading file via FTP", e);
		} finally {
			// Disconnect and Exit
			ftpUploader.disconnect();
			LOGGER.info("Disconnected");
		}
	}

	/**
	 * TODO: This was implemented based in ftp endpoint that has Camel (Couldn't get it work properly)
	 * 		 Currently only upload the file get! We need to upload the path in the first
	 * 		 line of this file!!
	 * 		 Also check the remote path
	 *
	 public String sendToFtp(Exchange exchange) {
		Message msg = exchange.getIn();

		if (exchange.getProperty(Exchange.SLIP_ENDPOINT) == null) {

			List<String> filesToUpload = (List<String>) msg.getHeader(Headers.FILES);
			File f = new File(filesToUpload.get(0));

			LOGGER.debug("{}", exchange.getProperties());
			LOGGER.debug("{}", msg.getHeaders());

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
												//.append(ftp_remoteDir)
												.append("?password=")
												.append(ftp_pass);

			ftpEndpoint.append("&connectTimeout=" + TIMEOUT);
			ftpEndpoint.append("&timeout=" + TIMEOUT);
			ftpEndpoint.append("&soTimeout=" + TIMEOUT);
			ftpEndpoint.append("&throwExceptionOnConnectFailed=true");
			ftpEndpoint.append("&disconnect=true");
			ftpEndpoint.append("&binary=true");
			ftpEndpoint.append("&recursive=true");
			ftpEndpoint.append("&doneFileName="+f.getName());
			ftpEndpoint.append("&fileName="+filesToUpload.get(0));

			return  ftpEndpoint.toString();

		} else {
			return null;
		}
	}*/

}
