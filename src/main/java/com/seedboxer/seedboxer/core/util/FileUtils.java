/*******************************************************************************
 * FileUtils.java
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
package com.seedboxer.seedboxer.core.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.seedboxer.seedboxer.core.type.FileValue;

/**
 *
 * @author Jorge Davison (jdavisonc)
 */
public class FileUtils {

	public static final long  MEGABYTE = 1024L * 1024L;

	/**
	 * Calculate the size of a file or directory
	 * @param file
	 * @return
	 */
	public static long calculateSize(File file) {
		if (file.isDirectory()) {
			long lengthDir = 0L;
			for (File fileInside : file.listFiles()) {
				lengthDir += calculateSize(fileInside);
			}
			return lengthDir;
		} else {
			return file.length() / MEGABYTE;
		}
	}


	public static File getFile(String name, String path) {
		return new File(path + File.separator + name);
	}

	public static List<FileValue> listFiles(String path) {
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
