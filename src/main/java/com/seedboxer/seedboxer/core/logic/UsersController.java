/*******************************************************************************
 * UsersController.java
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
package com.seedboxer.seedboxer.core.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seedboxer.seedboxer.core.domain.Configuration;
import com.seedboxer.seedboxer.core.domain.Status;
import com.seedboxer.seedboxer.core.domain.User;
import com.seedboxer.seedboxer.core.domain.UserConfiguration;
import com.seedboxer.seedboxer.core.persistence.UsersDao;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
@Service
public class UsersController {

	@Autowired
	private UsersDao usersDao;

	public void registerDevice(String username, String registrationId) {
		User user = usersDao.get(username);
		if (user != null) {
			usersDao.saveUserConfig(user.getId(), new UserConfiguration(Configuration.NOTIFICATION_GCM_REGISTRATIONID, registrationId));
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

	public boolean setUserStatus(String username, Status newStatus) {
		User user = getUser(username);
		if (!newStatus.equals(user.getStatus())) {
			user.setStatus(newStatus);
			usersDao.save(user);
			return true;
		} else {
			return false;
		}
	}

}
