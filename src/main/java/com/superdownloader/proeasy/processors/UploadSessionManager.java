package com.superdownloader.proeasy.processors;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.superdownloader.proeasy.types.Upload;

/**
 * @author harley
 *
 */
@Service(value = "uploadSessionManager")
public class UploadSessionManager implements Processor {

	private static final String FILE_EXTENSION = ".upl";

	@Value("${proEasy.simultaneousUploadsPerUser}")
	private int simultaneousUploadsPerUser;

	private final Map<String, Map<String, Upload>> uploadsPerUser;

	private final Object lock;

	public UploadSessionManager() {
		uploadsPerUser = new HashMap<String, Map<String, Upload>>();
		lock = new Object();
	}

	public boolean addUserUpload(String username, String filename) {
		synchronized (lock) {
			Map<String, Upload> userUploads = uploadsPerUser.get(username);
			if (userUploads == null) {
				userUploads = new HashMap<String, Upload>();
				uploadsPerUser.put(username, userUploads);
			}
			String file = fixFilename(filename);
			if (userUploads.size() < simultaneousUploadsPerUser && !userUploads.containsKey(file)) {
				userUploads.put(file, new Upload(file));
				return true;
			} else {
				return false;
			}
		}
	}

	public void setUserUploadSize(String username, String filename, long size) {
		synchronized (lock) {
			Map<String, Upload> userUploads = uploadsPerUser.get(username);
			if (userUploads != null) {
				Upload upload = userUploads.get(fixFilename(filename));
				if (upload != null) {
					upload.setSize(size);
				}
			}
		}
	}

	public void setUserUploadProgress(String username, String filename, long transferred) {
		synchronized (lock) {
			Map<String, Upload> userUploads = uploadsPerUser.get(username);
			if (userUploads != null) {
				Upload upload = userUploads.get(fixFilename(filename));
				if (upload != null) {
					upload.setTransferred(transferred);
				}
			}
		}
	}

	public List<Upload> getUserUploads(String username) {
		synchronized (lock) {
			Map<String, Upload> userUploads = uploadsPerUser.get(username);
			if (userUploads != null) {
				try {
					Collection<Upload> uploads = userUploads.values();
					List<Upload> ret = new ArrayList<Upload>(uploads.size());
					for (Upload upload : uploads) {
						ret.add(upload.clone());
					}
					return ret;
				} catch (CloneNotSupportedException e) {
					return Collections.emptyList();
				}
			} else {
				return Collections.emptyList();
			}
		}
	}

	public Upload removeUserUpload(String username, String filename) {
		synchronized (lock) {
			Map<String, Upload> userUploads = uploadsPerUser.get(username);
			if (userUploads != null) {
				return userUploads.remove(fixFilename(filename));
			} else {
				return null;
			}
		}
	}

	private static String fixFilename(String filename) {
		String file = new File(filename).getName();
		if (file.endsWith(FILE_EXTENSION)){
			return file.substring(0, file.length() - 4);
		} else {
			return file;
		}
	}

	/**
	 * Made a processor for remove upload for the session
	 */
	@Override
	public void process(Exchange exchange) throws Exception {
		Message msg = exchange.getIn();
		String username = (String) msg.getHeader(Headers.USERNAME);
		String filepath = (String) msg.getHeader(Exchange.FILE_PATH);
		removeUserUpload(username, fixFilename(filepath));
	}

}
