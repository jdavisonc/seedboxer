/*******************************************************************************
 * DownloadsSessionManager.java
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
package com.seedboxer.seedboxer.core.logic;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.seedboxer.seedboxer.core.domain.DownloadSession;
import com.seedboxer.seedboxer.core.type.Download;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
@Service
public class DownloadsSessionManager {

	private final Map<Long, DownloadSession> downloadsPerUser;

	private final Object lock;

	public DownloadsSessionManager() {
		downloadsPerUser = new HashMap<Long, DownloadSession>();
		lock = new Object();
	}

	public Download getDownload(long userId) {
		synchronized (lock) {
			DownloadSession downloadSession = downloadsPerUser.get(userId);
			if (downloadSession != null) {
				return downloadSession.getDownloadType();
			} else {
				return null;
			}
		}
	}

	public void removeDownloadSession(long userId) {
		synchronized (lock) {
			downloadsPerUser.remove(userId);
		}
	}

	public void addDownloadSession(Long userId, DownloadSession downloadSession) {
		synchronized (lock) {
			downloadsPerUser.put(userId, downloadSession);
		}
	}

	public void abortDownloadSession(Long userId) {
		synchronized (lock) {
			DownloadSession downloadSession = downloadsPerUser.get(userId);
			if (downloadSession != null) {
				downloadSession.abort();
			}
		}
	}

}
