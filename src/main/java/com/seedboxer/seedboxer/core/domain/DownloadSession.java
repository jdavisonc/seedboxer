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
package com.seedboxer.seedboxer.core.domain;

import com.seedboxer.seedboxer.core.type.Download;

/**
 * 
 * Class for represent a in-progress download.
 * 
 * @author Jorge Davison (jdavisonc)
 */
public abstract class DownloadSession {

	public static final long MEGABYTE = 1024L * 1024L;

	protected double transferredInMbs = 0L;

	protected double totalSize = 0L;

	private String fileName;

	public DownloadSession(String fileName, double totalSize) {
		this.totalSize = totalSize;
		this.fileName = fileName;
	}

	public double getMbsTransferred() {
		return transferredInMbs;
	}

	public abstract void abort();

	public Download getDownloadType() {
		return new Download(fileName, (long)totalSize, (long)transferredInMbs);
	}

}
