package com.superdownloader.proEasy.logic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.superdownloader.proEasy.types.FileValue;

@Service
public class FilesController {

	@Value(value="${downloader.completePath}")
	private String completePath;

	@Value(value="${downloader.inProgressPath}")
	private String inProgressPath;

	public List<FileValue> getCompletedFiles() {
		return listFiles(completePath);
	}

	public List<FileValue> getInProgressFiles() {
		return listFiles(inProgressPath);
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
