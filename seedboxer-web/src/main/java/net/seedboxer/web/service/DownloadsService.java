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
package net.seedboxer.web.service;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import net.seedboxer.bencode.TorrentUtils;
import net.seedboxer.core.domain.Configuration;
import net.seedboxer.core.domain.DownloadQueueItem;
import net.seedboxer.core.domain.Status;
import net.seedboxer.core.domain.User;
import net.seedboxer.core.domain.UserConfiguration;
import net.seedboxer.core.logic.DownloadsQueueManager;
import net.seedboxer.core.logic.DownloadsSessionManager;
import net.seedboxer.core.logic.UsersController;
import net.seedboxer.core.type.Download;
import net.seedboxer.core.type.FileValue;
import net.seedboxer.core.util.FileUtils;
import net.seedboxer.web.type.UserConfig;
import net.seedboxer.web.type.UserStatusAPIResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.Lists;


@Service
public class DownloadsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DownloadsService.class);

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
	 * @param user
	 * @param fileNames
	 * @throws Exception
	 */
	public void putToDownload(User user, List<String> fileNames, boolean checkExistence) throws Exception {
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
	public List<FileValue> downloadsInQueue(User user) throws Exception {
		List<DownloadQueueItem> userQueue = downloadsQueueManager.userQueue(user);

		List<FileValue> queue = new ArrayList<FileValue>();
		for (DownloadQueueItem inQueue : userQueue) {
			String withoutPrefixPath = inQueue.getDownload().replace(completePath + File.separator, "");
			queue.add(new FileValue(withoutPrefixPath, inQueue.getId(),inQueue.getQueueOrder()));
		}
		return queue;
	}

	/**
	 * Save torrent file to watch-dog directory of downloader application (rTorrent or uTorrent) and
	 * add the same torrent to the user queue.
	 *
	 * @param fileName
	 * @param torrentFileInStream
	 * @throws Exception
	 */
	public void addTorrent(User user, String fileName, final InputStream torrentFileInStream) throws Exception {
		String finalPath = watchDownloaderPath + File.separator + fileName;
		File torrent = FileUtils.copyFile(torrentFileInStream, finalPath, true, true);

		String name = TorrentUtils.getTorrentName(torrent);
		putToDownload(user, Collections.singletonList(name), false);
	}

	/**
	 * Delete a download from the queue.
	 *
	 * @param username
	 * @param fileName
	 * @return
	 */
	public void deleteDownloadInQueue(User user, long downloadId) {
		downloadsQueueManager.remove(user, downloadId);
	}

	/**
	 * Returns the downloads status for the user.
	 * 
	 * @param user
	 * @return
	 */
	public UserStatusAPIResponse getUserStatus(User user) {
		Download download = downloadSessionManager.getDownload(user.getId());
		return new UserStatusAPIResponse(user.getStatus(), download);
	}

	/**
	 * Reorder user queue.
	 * 
	 * @param user
	 * @param queueItems
	 */
	public void updateQueue(User user, List<FileValue> queueItems){
		List<DownloadQueueItem> queueItemsFromDB = downloadsQueueManager.userQueue(user);
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

	/**
	 * Stop the downloads for the user.
	 * 
	 * @param user
	 */
	public void stopDownloads(User user) {
		boolean success = usersController.setUserStatus(user, Status.STOPPED);
		if (success) {
			try {
				downloadSessionManager.abortDownloadSession(user.getId());
				downloadsQueueManager.resetQueue(user);
				LOGGER.info("Download stopped for user {}", user.getUsername());
			} catch (Exception e) {
				LOGGER.info("Download stopped for user {}, but cannot abort current download", user.getUsername(), e);
			}
		} else {
			LOGGER.info("Download already stopped for user {}", user.getUsername());
		}
	}

	/**
	 * Begin the downloads for the user.
	 * 
	 * @param user
	 */
	public void startDownloads(User user) {
		boolean success = usersController.setUserStatus(user, Status.STARTED);
		if (success) {
			LOGGER.info("Download started for user {}", user.getUsername());
		} else {
			LOGGER.info("Download already started for user {}", user.getUsername());
		}
	}

	/**
	 * Generate an APIKEY for the user.
	 * 
	 * @param user
	 * @return
	 */
	public User generateAPIKey(User user) {
		return usersController.generateAPIKey(user);
	}

	/**
	 * Return all user configurations
	 * 
	 * @param user
	 * @return
	 */
	public List<UserConfig> getUserConfigs(User user) {
		List<UserConfig> configs = new ArrayList<UserConfig>();
		List<UserConfiguration> userConfig = usersController.getUserConfig(user.getId());
		for (UserConfiguration userConfiguration : userConfig) {
			configs.add(new UserConfig(userConfiguration.getName(), userConfiguration.getValue()));
		}
		return configs;
	}
	
	/**
	 * Return all types of user configurations
	 * 
	 * @param user
	 * @return
	 */
	public List<UserConfig> getUserConfigTypes() {
		return Lists.transform(Configuration.values, new Function<String, UserConfig>() {

			@Override
			@Nullable
			public UserConfig apply(@Nullable String type) {
				return new UserConfig(type, null);
			}
		});
	}

	/**
	 * Save user configurations, if the key not exists in user configurations set, then will be added.
	 * If the key exists, then the configuration will be updated.
	 * 
	 * @param user
	 * @param name
	 * @param value
	 * @return
	 */
	public void saveUserConfigs(User user, String name, String value) {
		usersController.saveUserConf(user, new UserConfiguration(name, value));
	}

	/**
	 * Delete user configuration if exists.
	 * 
	 * @param user
	 * @param name
	 */
	public void deleteUserConfigs(User user, String name) {
		usersController.deleteUserConf(user, name);
	}

}
