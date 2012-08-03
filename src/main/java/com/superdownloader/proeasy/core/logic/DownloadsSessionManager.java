package com.superdownloader.proeasy.core.logic;

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

import com.superdownloader.proeasy.core.type.Download;
import com.superdownloader.proeasy.core.type.User;

/**
 * @author harley
 *
 */
@Service
public class DownloadsSessionManager {

	private static final String FILE_EXTENSION = ".upl";

	@Value("${proeasy.simultaneousDownloadsPerUser}")
	private int simultaneousDownloadsPerUser;

	@Autowired
	private UsersController userController;

	private final Map<Long, Map<Long, Download>> downloadsPerUser;

	private final Object lock;

	public DownloadsSessionManager() {
		downloadsPerUser = new HashMap<Long, Map<Long, Download>>();
		lock = new Object();
	}

	public boolean addUserDownload(String username, int downloadId, String filename) {
		User user = userController.getUser(username);
		return addUserDownload(user.getId(), downloadId, filename);
	}

	public boolean addUserDownload(long userId, long downloadId, String filename) {
		synchronized (lock) {
			Map<Long, Download> userUploads = downloadsPerUser.get(userId);
			if (userUploads == null) {
				userUploads = new HashMap<Long, Download>();
				downloadsPerUser.put(userId, userUploads);
			}
			String file = fixFilename(filename);
			if (userUploads.size() < simultaneousDownloadsPerUser && !userUploads.containsKey(file)) {
				userUploads.put(downloadId, new Download(file));
				return true;
			} else {
				return false;
			}
		}
	}

	public void setUserDownloadSize(long userId, long downloadId, long size) {
		synchronized (lock) {
			Map<Long, Download> userUploads = downloadsPerUser.get(userId);
			if (userUploads != null) {
				Download upload = userUploads.get(downloadId);
				if (upload != null) {
					upload.setSize(size);
				}
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

	public List<Download> getUserDownloads(String username) {
		User user = userController.getUser(username);
		return getUserDownloads(user.getId());
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
