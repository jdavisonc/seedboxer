package com.superdownloader.proEasy.processors;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.superdownloader.proEasy.types.Upload;

/**
 * @author harley
 *
 */
@Service(value = "uploadSessionManager")
public class UploadSessionManager implements Processor {

	private final static String FILE_EXTENSION = ".upl";

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
			if (userUploads.size() < simultaneousUploadsPerUser && !userUploads.containsKey(filename)) {
				userUploads.put(filename, new Upload(filename));
				return true;
			} else {
				return false;
			}
		}
	}

	public Upload getUserUpload(String username, String filename) {
		synchronized (lock) {
			Map<String, Upload> userUploads = uploadsPerUser.get(username);
			if (userUploads != null) {
				return userUploads.get(filename);
			} else {
				return null;
			}
		}
	}

	public Upload removeUserUpload(String username, String filename) {
		synchronized (lock) {
			Map<String, Upload> userUploads = uploadsPerUser.get(username);
			if (userUploads != null) {
				return userUploads.remove(filename);
			} else {
				return null;
			}
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
		File file = new File(filepath);
		removeUserUpload(username, file.getName().replace(FILE_EXTENSION, ""));
	}

}
