
package com.superdownloader.proEasy.filters;

import java.io.File;
import java.util.regex.Pattern;

import org.apache.camel.component.file.GenericFile;
import org.apache.camel.component.file.GenericFileFilter;

/**
 * @author jdavison
 *
 */
public class FileFilter implements GenericFileFilter<Object> {

	private Pattern pattern = null;

	public void setPattern(String pattern) {
		this.pattern = Pattern.compile(pattern);
	}

	@Override
	public boolean accept(GenericFile<Object> file) {
		String relativePath = file.getAbsoluteFilePath().substring((file.getEndpointPath() + File.separator).length());
		if (pattern != null) {
			return pattern.matcher(relativePath).matches();
		} else {
			return false;
		}
	}

}