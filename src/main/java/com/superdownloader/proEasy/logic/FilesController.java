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

	public void putToDownload(String username, String name) throws Exception {
		if (fileExists(name, inProgressPath) || fileExists(name, completePath)) {
			File download = new File(getWorkingFolder(username) + name + MAGIC_EXTENSION);
			if (download.createNewFile()) {
				Files.append(completePath + File.separator + name, download, Charset.defaultCharset());
			}
		} else {
			throw new IllegalArgumentException("The file does not exist");
		}
	}

	private String getWorkingFolder(String username) {
		return basePath + File.separator + username + File.separator
				+ MAGIC_FOLDER + File.separator;
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
