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
package net.seedboxer.mule.processor.transfer;

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.seedboxer.common.ftp.FtpUploader;
import net.seedboxer.common.ftp.FtpUploaderCommons;
import net.seedboxer.common.ftp.exception.AbortedTransferException;
import net.seedboxer.common.ftp.exception.FtpException;
import net.seedboxer.core.domain.Configuration;
import net.seedboxer.core.logic.DownloadsSessionManager;
import net.seedboxer.core.util.FileUtils;
import net.seedboxer.mule.exception.AbortedDownloadException;
import net.seedboxer.mule.exception.TransportException;
import net.seedboxer.mule.util.FtpSession;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author Jorge Davison (jdavisonc)
 *
 */
@Component
public class FtpSender implements Processor {

	@Autowired
	private DownloadsSessionManager downloadSessionManager;

	private static final Logger LOGGER = LoggerFactory.getLogger(FtpSender.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		Message msg = exchange.getIn();
		final Long userId = (Long) msg.getHeader(Configuration.USER_ID);

		FtpUploader ftpUploader = new FtpUploaderCommons();

		String user = (String) msg.getHeader(Configuration.FTP_USERNAME);
		String pass = (String) msg.getHeader(Configuration.FTP_PASSWORD);
		String server = (String) msg.getHeader(Configuration.FTP_URL);
		String remoteDir = (String) msg.getHeader(Configuration.FTP_REMOTE_DIR);

		@SuppressWarnings("unchecked")
		List<String> filesToUpload = (List<String>) msg.getHeader(Configuration.FILES);

		try {
			ftpUploader.configure(server, user, pass, remoteDir, false);
			// Connect and Upload
			ftpUploader.connect();
			LOGGER.info("Connected to {}", server);
			for (String toUpload : filesToUpload) {
				uploadToUser(userId, ftpUploader, toUpload);
			}
		} catch (AbortedTransferException e) {
			throw new AbortedDownloadException(e);
		} catch (FtpException e) {
			throw new TransportException("Error at uploading file via FTP", e);
		} finally {
			// Disconnect and Exit
			ftpUploader.disconnect();
			LOGGER.info("Disconnected from {}", server);
		}
	}

	private void uploadToUser(final Long userId, FtpUploader ftpUploader, String toUpload) throws IOException {
		try {
			File fileToDownload = new File(toUpload);
			long size = FileUtils.calculateSize(fileToDownload);
			LOGGER.info("Uploading {}, size={} MBs", toUpload, size);
			FtpSession ftpStatus = new FtpSession(fileToDownload.getName(), size, ftpUploader);

			downloadSessionManager.addDownloadSession(userId, ftpStatus);
			ftpUploader.upload(fileToDownload, ftpStatus);
		} finally {
			downloadSessionManager.removeDownloadSession(userId);
		}
	}

}
