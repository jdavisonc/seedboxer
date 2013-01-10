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
package net.seedboxer.seedboxer.core.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.seedboxer.seedboxer.core.type.FileValue;

import com.google.common.io.Files;
import com.google.common.io.InputSupplier;

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

	public static File copyFile(final InputStream in, String finalPath,
			boolean readPermissions, boolean writePermissions) throws IOException {

		File copyFile = new File(finalPath);
		Files.copy(new InputSupplier<InputStream>() {
			@Override
			public InputStream getInput() throws IOException {
				return in;
			}
		}, copyFile);

		if (readPermissions) {
			copyFile.setReadable(true, false);
		}
		if (writePermissions) {
			copyFile.setWritable(true, false);
		}

		return copyFile;
	}

}
