package com.superdownloader.proeasy.core.logic;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.superdownloader.proeasy.core.persistence.UsersDao;
import com.superdownloader.proeasy.mule.processor.Headers;

/**
 * @author harley
 *
 */
@Service
public class UsersController {

	@Autowired
	private UsersDao usersDao;

	public void registerDevice(String username, String registrationId, String deviceId) {
		Map<String, String> configs = new HashMap<String, String>();
		configs.put(Headers.NOTIFICATION_C2DM_DEVICEID, deviceId);
		configs.put(Headers.NOTIFICATION_C2DM_REGISTRATIONID, registrationId);
		usersDao.saveUserConfigs(username, configs);
	}

	public Map<String, String> userConfiguration(String username) {
		return usersDao.getUserConfigs(username);
	}

	public Map<String, String> userConfiguration(int userId) {
		return usersDao.getUserConfigs(userId);
	}

	public int getUserId(String username) {
		Integer userId = usersDao.getUserId(username);
		if (userId == null) {
			throw new IllegalArgumentException("Username doesn't exist");
		}
		return userId;
	}

}
