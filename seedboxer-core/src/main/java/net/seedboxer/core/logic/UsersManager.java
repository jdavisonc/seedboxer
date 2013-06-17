/*******************************************************************************
 * UsersManager.java
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
package net.seedboxer.core.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.seedboxer.core.domain.Status;
import net.seedboxer.core.domain.Token;
import net.seedboxer.core.domain.User;
import net.seedboxer.core.domain.UserConfiguration;
import net.seedboxer.core.persistence.UsersDao;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author Jorge Davison (jdavisonc)
 *
 */
@Service
public class UsersManager {

	@Autowired
	private UsersDao usersDao;

	public void saveUserConf(User user, String name, String value) {
		usersDao.saveUserConfig(user.getId(), new UserConfiguration(name, value));
	}

	public void deleteUserConf(User user, String name) {
		usersDao.deleteUserConfig(user.getId(), name);
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

	public Map<String, String> getUserConfig(long userId) {
		Map<String, String> configs = new HashMap<String, String>();
		for (UserConfiguration uc : usersDao.getUserConfig(userId)) {
			configs.put(uc.getName(), uc.getValue());
		}
		return configs;
	}

	public boolean setUserStatus(User user, Status newStatus) {
		if (!newStatus.equals(user.getStatus())) {
			user.setStatus(newStatus);
			usersDao.save(user);
			return true;
		} else {
			return false;
		}
	}

	public User getUserFromAPIKey(String apikey) {
		User user = usersDao.getFromAPIKey(apikey);
		if (user == null) {
			throw new IllegalArgumentException("Username doesn't exist");
		}
		return user;
	}

	public User generateAPIKey(User user) {
		if (user.getApikey() == null) {
			user.setApikey(Token.generate());
			usersDao.save(user);
		}
		return user;
	}

	public void saveUser(User user) {
		user.setPassword(DigestUtils.shaHex(user.getPassword()));
		usersDao.save(user);
	}

	public void deleteUser(User user) {
		usersDao.delete(user);
	}

	public List<User> getAllUsers() {
		return usersDao.getAllUsers();
	}

}
