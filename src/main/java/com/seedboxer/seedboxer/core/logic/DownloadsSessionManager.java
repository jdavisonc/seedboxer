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

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.seedboxer.seedboxer.core.type.Download;

/**
 * @author harley
 *
 */
@Service
public class DownloadsSessionManager {

	private static final String FILE_EXTENSION = ".upl";

	@Value("${simultaneousDownloadsPerUser}")
	private int simultaneousDownloadsPerUser;

	@Autowired
	private UsersController userController;

	private final Map<Long, Map<Long, Download>> downloadsPerUser;

	private final Object lock;

	public DownloadsSessionManager() {
		downloadsPerUser = new HashMap<Long, Map<Long, Download>>();
		lock = new Object();
	}

	public boolean addUserDownload(long userId, long downloadId, String filename, long size) {
		synchronized (lock) {
			Map<Long, Download> userUploads = downloadsPerUser.get(userId);
			if (userUploads == null) {
				userUploads = new HashMap<Long, Download>();
				downloadsPerUser.put(userId, userUploads);
			}
			String file = fixFilename(filename);
			if (userUploads.size() < simultaneousDownloadsPerUser && !userUploads.containsKey(file)) {
				userUploads.put(downloadId, new Download(file, size));
				return true;
			} else {
				return false;
			}
		}
	}

	public void setUserDownloadProgress(long userId, long downloadId, long transferred) {
		synchronized (lock) {
			Map<Long, Download> userUploads = downloadsPerUser.get(userId);
			if (userUploads != null) {
				Download upload = userUploads.get(downloadId);
				if (upload != null) {
					upload.setTransferred(transferred);
				}
			}
		}
	}

	public List<Download> getUserDownloads(long userId) {
		synchronized (lock) {
			Map<Long, Download> userUploads = downloadsPerUser.get(userId);
			if (userUploads != null) {
				try {
					Collection<Download> uploads = userUploads.values();
					List<Download> ret = new ArrayList<Download>(uploads.size());
					for (Download upload : uploads) {
						ret.add(upload.clone());
					}
					return ret;
				} catch (CloneNotSupportedException e) {
					return Collections.emptyList();
				}
			} else {
				return Collections.emptyList();
			}
		}
	}

	public Download removeUserDownload(long userId, long downloadId) {
		synchronized (lock) {
			Map<Long, Download> userUploads = downloadsPerUser.get(userId);
			if (userUploads != null) {
				return userUploads.remove(downloadId);
			} else {
				return null;
			}
		}
	}

	private static String fixFilename(String filename) {
		String file = new File(filename).getName();
		if (file.endsWith(FILE_EXTENSION)){
			return file.substring(0, file.length() - 4);
		} else {
			return file;
		}
	}

}
