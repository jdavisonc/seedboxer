package com.superdownloader.proEasy.logic;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.io.Files;
import com.superdownloader.proEasy.types.FileValue;

@Service
public class FilesController {

	private static final String MAGIC_EXTENSION = ".upl";
	private static final String MAGIC_FOLDER = "to-home-server";

	@Value(value="${proEasy.completePath}")
	private String completePath;

	@Value(value="${proEasy.inProgressPath}")
	private String inProgressPath;

	@Value(value="${proEasy.basePath}")
	private String basePath;

	public List<FileValue> getCompletedFiles() {
		return listFiles(completePath);
	}

	public List<FileValue> getInProgressFiles() {
		return listFiles(inProgressPath);
	}

	public void putToDownload(String username, List<String> fileNames) throws Exception {
		for (String name : fileNames) {
			if (fileExists(name, inProgressPath) || fileExists(name, completePath)) {
				File download = new File(getWorkingFolder(username) + File.separator + name + MAGIC_EXTENSION);
				if (download.createNewFile()) {
					Files.append(completePath + File.separator + name, download, Charset.defaultCharset());
				}
			} else {
				throw new IllegalArgumentException("The file does not exist");
			}
		}
	}

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

	private String getWorkingFolder(String username) {
		return basePath + File.separator + username + File.separator
				+ MAGIC_FOLDER;
	}

	private boolean fileExists(String name, String path) {
		File download = new File(path + File.separator + name);
		return download.exists();
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
