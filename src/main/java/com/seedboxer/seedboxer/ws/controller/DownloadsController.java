/*******************************************************************************
 * DownloadsController.java
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
package com.seedboxer.seedboxer.ws.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.io.Files;
import com.google.common.io.InputSupplier;
import com.seedboxer.seedboxer.core.domain.DownloadQueueItem;
import com.seedboxer.seedboxer.core.domain.Status;
import com.seedboxer.seedboxer.core.domain.User;
import com.seedboxer.seedboxer.core.logic.DownloadsQueueManager;
import com.seedboxer.seedboxer.core.logic.DownloadsSessionManager;
import com.seedboxer.seedboxer.core.logic.UsersController;
import com.seedboxer.seedboxer.core.type.Download;
import com.seedboxer.seedboxer.core.type.FileValue;
import com.seedboxer.seedboxer.core.util.FileUtils;
import com.seedboxer.seedboxer.core.util.TorrentUtils;
import com.seedboxer.seedboxer.ws.type.UserStatus;

@Service
public class DownloadsController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DownloadsController.class);

	@Autowired
	private UsersController usersController;

	@Autowired
	private DownloadsQueueManager downloadsQueueManager;

	@Autowired
	private DownloadsSessionManager downloadSessionManager;

	@Value(value="${completePath}")
	private String completePath;

	@Value(value="${inProgressPath}")
	private String inProgressPath;

	@Value(value="${watchDownloaderPath}")
	private String watchDownloaderPath;

	public List<FileValue> getCompletedFiles() {
		return FileUtils.listFiles(completePath);
	}

	public List<FileValue> getInProgressFiles() {
		return FileUtils.listFiles(inProgressPath);
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

			File inProgressFile = FileUtils.getFile(name, inProgressPath);
			File completeFile = FileUtils.getFile(name, completePath);

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
			queue.add(new FileValue(withoutPrefixPath, inQueue.getId(),inQueue.getQueueOrder()));
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
		torrent.setReadable(true, false);
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

	public UserStatus getUserStatus(String username) {
		User user = getUser(username);
		Download download = downloadSessionManager.getDownload(user.getId());
		return new UserStatus(user.getStatus(), download);
	}

	public void updateQueue(List<FileValue> queueItems, String username){
		List<DownloadQueueItem> queueItemsFromDB = downloadsQueueManager.userQueue(getUser(username));
		Map<Long,FileValue> queueItemsMap = new HashMap<Long,FileValue>();
		for(FileValue queueItem : queueItems){
			queueItemsMap.put(queueItem.getQueueId(), queueItem);
		}
		for(DownloadQueueItem queueItemFromDB : queueItemsFromDB){
			FileValue queueItem = queueItemsMap.get(queueItemFromDB.getId());
			queueItemFromDB.setQueueOrder(queueItem.getOrder());
		}
		downloadsQueueManager.updateQueueOrder(queueItemsFromDB);
	}

	public void stopDownloads(String username) {
		boolean success = usersController.setUserStatus(username, Status.STOPPED);
		if (success) {
			User user = usersController.getUser(username);
			try {
				downloadSessionManager.abortDownloadSession(user.getId());
				downloadsQueueManager.resetQueue(user);
				LOGGER.info("Download stopped for user {}", username);
			} catch (Exception e) {
				LOGGER.info("Download stopped for user {}, but cannot abort current download", username, e);
			}
		} else {
			LOGGER.info("Download already stopped for user {}", username);
		}
	}

	public void startDownloads(String username) {
		boolean success = usersController.setUserStatus(username, Status.STARTED);
		if (success) {
			LOGGER.info("Download started for user {}", username);
		} else {
			LOGGER.info("Download already started for user {}", username);
		}
	}

}
