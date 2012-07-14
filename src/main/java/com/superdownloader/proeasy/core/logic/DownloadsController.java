package com.superdownloader.proeasy.core.logic;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.io.Files;
import com.google.common.io.InputSupplier;
import com.superdownloader.proeasy.core.types.FileValue;
import com.superdownloader.proeasy.core.utils.TorrentUtils;

@Service
public class DownloadsController {

	private static final String MAGIC_EXTENSION = ".upl";
	private static final String MAGIC_FOLDER = "to-home-server";

	@Value(value="${proeasy.completePath}")
	private String completePath;

	@Value(value="${proeasy.inProgressPath}")
	private String inProgressPath;

	@Value(value="${proeasy.watchDownloaderPath}")
	private String watchDownloaderPath;

	@Value(value="${proeasy.basePath}")
	private String basePath;

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
	public void putToDownload(String username, List<String> fileNames, boolean checkExistence) throws Exception {
		for (String name : fileNames) {

			File inProgressFile = getFile(name, inProgressPath);
			File completeFile = getFile(name, completePath);

			if (checkExistence && !(inProgressFile.exists() || completeFile.exists())) {
				throw new IllegalArgumentException("The files not exist. Paths: "
						+ inProgressFile.getAbsolutePath() + " or " +
						completeFile.getAbsolutePath());
			}

			File download = new File(getWorkingFolder(username) + File.separator + name + MAGIC_EXTENSION);
			if (download.createNewFile()) {
				Files.append(completePath + File.separator + name, download, Charset.defaultCharset());
			}
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
		List<String> inQueue = new ArrayList<String>();
		File directory = new File(getWorkingFolder(username));
		if (directory.exists()) {
			for (File file: directory.listFiles()) {
				if (file.isFile() && file.getName().endsWith(MAGIC_EXTENSION)) {
					inQueue.addAll(Files.readLines(file, Charset.defaultCharset()));
				}
			}
		}

		List<FileValue> queue = new ArrayList<FileValue>();
		for (String name : inQueue) {
			queue.add(new FileValue(name.replace(completePath + File.separator, ""), false));
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
	public void addTorrent(String username, String fileName, final InputStream torrentFileInStream) throws Exception {
		File torrent = new File(watchDownloaderPath + File.separator + fileName);
		Files.copy(new InputSupplier<InputStream>() {
			@Override
			public InputStream getInput() throws IOException {
				return torrentFileInStream;
			}
		}, torrent);

		String name = TorrentUtils.getName(torrent);
		putToDownload(username, Collections.singletonList(name), false);
	}

	/**
	 * Delete a download from the queue.
	 * 
	 * @param username
	 * @param fileName
	 * @return
	 */
	public boolean deleteDownloadInQueue(String username, String fileName) {
		File fileInQueue = new File(getWorkingFolder(username) + File.separator + fileName + MAGIC_EXTENSION);
		return fileInQueue.delete();
	}

	/*
	 * Extra functions
	 */

	private String getWorkingFolder(String username) {
		return basePath + File.separator + username + File.separator
				+ MAGIC_FOLDER;
	}

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
