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

import com.superdownloader.proeasy.core.types.Download;

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

	private final Map<Integer, Map<Integer, Download>> downloadsPerUser;

	private final Object lock;

	public DownloadsSessionManager() {
		downloadsPerUser = new HashMap<Integer, Map<Integer, Download>>();
		lock = new Object();
	}

	public boolean addUserDownload(String username, int downloadId, String filename) {
		Integer userId = userController.getUserId(username);
		return addUserDownload(userId, downloadId, filename);
	}

	public boolean addUserDownload(int userId, int downloadId, String filename) {
		synchronized (lock) {
			Map<Integer, Download> userUploads = downloadsPerUser.get(userId);
			if (userUploads == null) {
				userUploads = new HashMap<Integer, Download>();
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

	public void setUserDownloadSize(int userId, int downloadId, long size) {
		synchronized (lock) {
			Map<Integer, Download> userUploads = downloadsPerUser.get(userId);
			if (userUploads != null) {
				Download upload = userUploads.get(downloadId);
				if (upload != null) {
					upload.setSize(size);
				}
			}
		}
	}

	public void setUserDownloadProgress(int userId, int downloadId, long transferred) {
		synchronized (lock) {
			Map<Integer, Download> userUploads = downloadsPerUser.get(userId);
			if (userUploads != null) {
				Download upload = userUploads.get(downloadId);
				if (upload != null) {
					upload.setTransferred(transferred);
				}
			}
		}
	}

	public List<Download> getUserDownloads(String username) {
		Integer userId = userController.getUserId(username);
		return getUserDownloads(userId);
	}

	public List<Download> getUserDownloads(int userId) {
		synchronized (lock) {
			Map<Integer, Download> userUploads = downloadsPerUser.get(userId);
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

	public Download removeUserDownload(int userId, int downloadId) {
		synchronized (lock) {
			Map<Integer, Download> userUploads = downloadsPerUser.get(userId);
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
