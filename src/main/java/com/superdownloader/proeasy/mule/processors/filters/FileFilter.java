package com.superdownloader.proeasy.mule.processors.filters;

import java.io.File;
import java.util.regex.Matcher;
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
			Matcher m = pattern.matcher(relativePath);
			return m.matches();
		} else {
			return false;
		}
	}

}