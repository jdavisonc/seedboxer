
package com.superdownloader.proEasy.processors.filters;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.camel.component.file.GenericFile;
import org.apache.camel.component.file.GenericFileFilter;
import org.springframework.beans.factory.annotation.Autowired;

import com.superdownloader.proEasy.processors.UploadSessionManager;

/**
 * @author jdavison
 *
 */
public class FileFilter implements GenericFileFilter<Object> {

	private Pattern pattern = null;

	@Autowired
	private UploadSessionManager uploadSessionManager;

	public void setPattern(String pattern) {
		this.pattern = Pattern.compile(pattern);
	}

	@Override
	public boolean accept(GenericFile<Object> file) {
		String relativePath = file.getAbsoluteFilePath().substring((file.getEndpointPath() + File.separator).length());
		if (pattern != null) {
			Matcher m = pattern.matcher(relativePath);
			if (m.matches()) {
				return uploadSessionManager.addUserUpload(m.group(1), m.group(2));
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

}