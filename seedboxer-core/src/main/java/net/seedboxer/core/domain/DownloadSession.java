/*******************************************************************************
 * DownloadsSession.java
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
package net.seedboxer.core.domain;

import java.util.concurrent.atomic.AtomicLong;

import net.seedboxer.core.type.Download;

/**
 * 
 * Class for represent a in-progress download.
 * 
 * @author Jorge Davison (jdavisonc)
 */
public class DownloadSession {

	protected AtomicLong transferredInMbs = new AtomicLong(0);

	protected long totalSize = 0L;

	private final String fileName;

	public DownloadSession(String fileName, long totalSize) {
		this.totalSize = totalSize;
		this.fileName = fileName;
	}
	
	public long getTransferredInMbs() {
		return transferredInMbs.get();
	}

	public void setTransferredInMbs(long transferredInMbs) {
		this.transferredInMbs.set(transferredInMbs);
	}

	public long getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
	}

	public String getFileName() {
		return fileName;
	}

	public Download getDownloadType() {
		return new Download(fileName, totalSize, transferredInMbs.get());
	}

}
