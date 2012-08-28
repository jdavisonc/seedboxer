/*******************************************************************************
 * FtpSender.java
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
package com.seedboxer.seedboxer.mule.processor;

import java.io.File;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.seedboxer.seedboxer.core.domain.Configuration;
import com.seedboxer.seedboxer.core.logic.DownloadsSessionManager;
import com.seedboxer.seedboxer.mule.exception.TransportException;
import com.superdownloader.common.ftp.FtpUploader;
import com.superdownloader.common.ftp.FtpUploaderCommons;
import com.superdownloader.common.ftp.FtpUploaderListener;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
@Component
public class FtpSender implements Processor {

	private static final long  MEGABYTE = 1024L * 1024L;

	@Autowired
	private DownloadsSessionManager uploadSessionManager;

	private static final Logger LOGGER = LoggerFactory.getLogger(FtpSender.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		Message msg = exchange.getIn();
		final Long downloadId = (Long) msg.getHeader(Configuration.DOWNLOAD_ID);
		final Long userId = (Long) msg.getHeader(Configuration.USER_ID);

		FtpUploader ftpUploader = new FtpUploaderCommons();

		String user = (String) msg.getHeader(Configuration.FTP_USERNAME);
		String pass = (String) msg.getHeader(Configuration.FTP_PASSWORD);
		String server = (String) msg.getHeader(Configuration.FTP_URL);
		String remoteDir = (String) msg.getHeader(Configuration.FTP_REMOTE_DIR);

		@SuppressWarnings("unchecked")
		List<String> filesToUpload = (List<String>) msg.getHeader(Configuration.FILES);

		try {
			ftpUploader.configure(server, user, pass, remoteDir);
			// Connect and Upload
			ftpUploader.connect();
			LOGGER.info("Connected to {}", server);
			for (String toUpload : filesToUpload) {
				LOGGER.info("Uploading {}...", toUpload);
				ftpUploader.upload(new File(toUpload), new FtpStatusListener(userId, downloadId));
			}
		} catch (Exception e) {
			throw new TransportException("Error at uploading file via FTP", e);
		} finally {
			// Disconnect and Exit
			ftpUploader.disconnect();
			LOGGER.info("Disconnected");
		}
	}

	private final class FtpStatusListener implements FtpUploaderListener {

		private double transferredInMbs = 0L;

		private long downloadId;

		private long userId;

		public FtpStatusListener(long userId, long downloadId) {
			this.userId = userId;
			this.downloadId = downloadId;
		}

		@Override
		public void bytesTransferred(long bytesTransferred) {
			// totalBytesTransferred is a Mb
			double transferred = transferredInMbs + ((double) bytesTransferred / (double) MEGABYTE);

			if (((long)transferred) > ((long)transferredInMbs)) {
				uploadSessionManager.setUserDownloadProgress(userId, downloadId, (long)transferred);
			}
			transferredInMbs = transferred;
		}

	}

}
