/*******************************************************************************
 * FtpSession.java
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
package net.seedboxer.mule.util;

import java.io.IOException;

import net.seedboxer.common.ftp.FtpUploader;
import net.seedboxer.common.ftp.FtpUploaderListener;
import net.seedboxer.core.domain.DownloadSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 *
 * FTP Session that represents the current status of the download.
 *
 * @author Jorge Davison (jdavisonc)
 */
public class FtpSession extends DownloadSession implements FtpUploaderListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(FtpSession.class);

	private FtpUploader uploader;

	public FtpSession(String fileName, double totalSize, FtpUploader uploader) {
		super(fileName, totalSize);
		this.uploader = uploader;
	}

	@Override
	public void bytesTransferred(long bytesTransferred) {
		transferredInMbs += (bytesTransferred / (double) MEGABYTE);
	}

	@Override
	public void abort() {
		try {
			uploader.abort();
		} catch (IOException e) {
			LOGGER.warn("Error aborting download", e);
		}
	}

}
