package com.superdownloader.proeasy.ws.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.io.Files;
import com.google.common.io.InputSupplier;
import com.superdownloader.proeasy.core.logic.DownloadsQueueManager;
import com.superdownloader.proeasy.core.logic.UsersController;
import com.superdownloader.proeasy.core.type.DownloadQueueItem;
import com.superdownloader.proeasy.core.type.FileValue;
import com.superdownloader.proeasy.core.type.User;
import com.superdownloader.proeasy.core.util.TorrentUtils;

@Service
public class DownloadsController {

	@Autowired
	private UsersController usersController;

	@Autowired
	private DownloadsQueueManager downloadsQueueManager;

	@Value(value="${proeasy.completePath}")
	private String completePath;

	@Value(value="${proeasy.inProgressPath}")
	private String inProgressPath;

	@Value(value="${proeasy.watchDownloaderPath}")
	private String watchDownloaderPath;

	public List<FileValue> getCompletedFiles() {
		return listFiles(completePath);
	}

	public List<FileValue> getInProgressFiles() {
		return listFiles(inProgressPath);
	}

	/**
	 * Add a download to the user queue.
	 * 
	 * @param username
	 * @param fileNames
	 * @throws Exception
	 */
	public void putToDownload(String username, List<String> fileNames) throws Exception {
		putToDownload(getUser(username), fileNames, true);
	}

	private void putToDownload(User user, List<String> fileNames, boolean checkExistence) throws Exception {
		for (String name : fileNames) {

			File inProgressFile = getFile(name, inProgressPath);
			File completeFile = getFile(name, completePath);

			if (checkExistence && !(inProgressFile.exists() || completeFile.exists())) {
				throw new IllegalArgumentException("The files not exist. Paths: "
						+ inProgressFile.getAbsolutePath() + " or " +
						completeFile.getAbsolutePath());
			}

			downloadsQueueManager.push(user, completePath + File.separator + name);
		}
	}

	/**
	 * List all downloads in the user queue.
	 * 
	 * @param username
	 * @return
	 * @throws Exception
	 */
	public List<FileValue> downloadsInQueue(String username) throws Exception {
		List<DownloadQueueItem> userQueue = downloadsQueueManager.userQueue(getUser(username));

		List<FileValue> queue = new ArrayList<FileValue>();
		for (DownloadQueueItem inQueue : userQueue) {
			String withoutPrefixPath = inQueue.getDownload().replace(completePath + File.separator, "");
			queue.add(new FileValue(withoutPrefixPath, inQueue.getId()));
		}
		return queue;
	}

	private User getUser(String username) {
		return usersController.getUser(username);
	}

	/**
	 * Save torrent file to watch-dog directory of downloader application (rTorrent or uTorrent) and
	 * add the same torrent to the user queue.
	 * 
	 * @param fileName
	 * @param torrentFileInStream
	 * @throws Exception
	 */
	public void addTorrent(String username, String fileName, final InputStream torrentFileInStream) throws Exception {
		User user = getUser(username);

		File torrent = new File(watchDownloaderPath + File.separator + fileName);
		Files.copy(new InputSupplier<InputStream>() {
			@Override
			public InputStream getInput() throws IOException {
				return torrentFileInStream;
			}
		}, torrent);

		String name = TorrentUtils.getName(torrent);
		putToDownload(user, Collections.singletonList(name), false);
	}

	/**
	 * Delete a download from the queue.
	 * 
	 * @param username
	 * @param fileName
	 * @return
	 */
	public void deleteDownloadInQueue(String username, long downloadId) {
		downloadsQueueManager.remove(getUser(username), downloadId);
	}

	/*
	 * Extra functions
	 */

	private File getFile(String name, String path) {
		return new File(path + File.separator + name);
	}

	private List<FileValue> listFiles(String path) {
		List<FileValue> files = new ArrayList<FileValue>();
		File dir = new File(path);

		for (String name : dir.list()) {
			if (!name.startsWith(".")) {
				files.add(new FileValue(name, false));
			}
		}
		return files;
	}

}
