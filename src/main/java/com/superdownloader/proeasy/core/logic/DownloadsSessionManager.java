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

	public boolean addUserDownload(long userId, long downloadId, String filename, long size) {
		synchronized (lock) {
			Map<Long, Download> downloads = downloadsPerUser.get(userId);
			if (downloads == null) {
				downloads = new HashMap<Long, Download>();
				downloadsPerUser.put(userId, downloads);
			}
			String file = fixFilename(filename);
			if (downloads.size() < simultaneousDownloadsPerUser && !downloads.containsKey(file)) {
				downloads.put(downloadId, new Download(file, size));
				return true;
			} else {
				return false;
			}
		}
	}

	public void setUserDownloadProgress(long userId, long downloadId, long transferred) {
		synchronized (lock) {
			Map<Long, Download> downloads = downloadsPerUser.get(userId);
			if (downloads != null) {
				Download dwnl = downloads.get(downloadId);
				if (dwnl != null) {
					dwnl.setTransferred(transferred);
				}
			}
		}
	}

	public List<Download> getUserDownloads(long userId) {
		synchronized (lock) {
			Map<Long, Download> downloads = downloadsPerUser.get(userId);
			if (downloads != null) {
				try {
					Collection<Download> uploads = downloads.values();
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
			Map<Long, Download> downloads = downloadsPerUser.get(userId);
			if (downloads != null) {
				return downloads.remove(downloadId);
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
