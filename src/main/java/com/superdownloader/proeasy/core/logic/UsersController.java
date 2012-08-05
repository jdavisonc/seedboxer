package com.superdownloader.proeasy.core.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.superdownloader.proeasy.core.domain.User;
import com.superdownloader.proeasy.core.domain.UserConfiguration;
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
		User user = usersDao.get(username);
		if (user != null) {
			usersDao.saveUserConfig(user.getId(), new UserConfiguration(Headers.NOTIFICATION_C2DM_REGISTRATIONID, registrationId));
			usersDao.saveUserConfig(user.getId(), new UserConfiguration(Headers.NOTIFICATION_C2DM_DEVICEID, deviceId));
		} else {
			throw new IllegalArgumentException("Username doesn't exist");
		}
	}

	public User getUser(String username) {
		User user = usersDao.get(username);
		if (user == null) {
			throw new IllegalArgumentException("Username doesn't exist");
		}
		return user;
	}

	public User getUser(long userId) {
		User user = usersDao.get(userId);
		if (user == null) {
			throw new IllegalArgumentException("Username doesn't exist");
		}
		return user;
	}

	public long getUserId(String username) {
		return getUser(username).getId();
	}

	public List<UserConfiguration> getUserConfig(long userId) {
		return usersDao.getUserConfig(userId);
	}

}
