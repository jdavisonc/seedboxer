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
package net.seedboxer.core.logic;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.seedboxer.core.domain.DownloadSession;
import net.seedboxer.core.type.Download;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


/**
 * @author Jorge Davison (jdavisonc)
 *
 */
@Service
public class DownloadsSessionManager {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DownloadsSessionManager.class);

	private final Map<Long, DownloadSession> sessionsPerUser;
	private final Map<Long, String> filesPerUser;

	public DownloadsSessionManager() {
		sessionsPerUser = new ConcurrentHashMap<Long, DownloadSession>();
		filesPerUser = new ConcurrentHashMap<Long, String>();
	}

	public Download getDownload(long userId) {
		DownloadSession downloadSession = sessionsPerUser.get(userId);
		if (downloadSession != null) {
			return downloadSession.getDownloadType();
		} else {
			return null;
		}
	}

	public void removeSession(long userId) {
		sessionsPerUser.remove(userId);
		filesPerUser.remove(userId);
	}

	public void setSessionProgress(Long userId, long mbsTransfered) {
		DownloadSession session = sessionsPerUser.get(userId);
		if (session != null) {
			session.setTransferredInMbs(mbsTransfered);	
		}
	}
	
	public void addSession(long userId, String fileName, long totalSize) {
		sessionsPerUser.put(userId, new DownloadSession(fileName, totalSize));
		filesPerUser.put(userId, fileName);
		LOGGER.debug("Session added: {} {}", userId, fileName);
	}
	
	public Long searchUserFromFile(String filename) {
		for (Map.Entry<Long, String> entry : filesPerUser.entrySet()) {
			if (filename.contains(entry.getValue())) {
				LOGGER.debug("Session found: {} {}", entry.getKey(), filename);
				return entry.getKey();
			}
		}
		LOGGER.warn("Session not found: {}", filename);
		return null;
	}

	public void abortSession(long userId) {
		// TODO
	}

}
