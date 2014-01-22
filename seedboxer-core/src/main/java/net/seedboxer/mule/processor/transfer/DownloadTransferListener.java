/*******************************************************************************
 * DownloadTransferListener.java
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

import net.seedboxer.camel.component.file.remote.TransferListener;
import net.seedboxer.core.domain.DownloadSession;
import net.seedboxer.core.logic.DownloadsSessionManager;
import net.seedboxer.core.util.FileUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
@Component
@Scope("prototype")
public class DownloadTransferListener implements TransferListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(DownloadTransferListener.class);

	@Autowired
	private DownloadsSessionManager downloadsSessionManager;

	private long totalBytes;

	private DownloadSession session;

	public void setDownloadsSessionManager(
			DownloadsSessionManager downloadsSessionManager) {
		this.downloadsSessionManager = downloadsSessionManager;
	}

	@Override
	public void bytesTransfered(String file, long bytesTransfered) {
		totalBytes += bytesTransfered;

		if (totalBytes % 1024 == 0) {
			long sizeInMbs = FileUtils.byteCountToDisplaySize(totalBytes);
			LOGGER.trace("Transfered '{}': {} Mb", file, sizeInMbs);

			if (session == null) {
				initSession(file);
			}
			session.setTransferredInMbs(sizeInMbs);
		}
	}

	private void initSession(String file) {
		session = downloadsSessionManager.getSession(file);
	}

}
