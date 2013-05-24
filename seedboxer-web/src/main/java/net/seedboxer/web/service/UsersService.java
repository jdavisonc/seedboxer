/*******************************************************************************
 * UsersService.java
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
package net.seedboxer.web.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.seedboxer.core.domain.Configuration;
import net.seedboxer.core.domain.User;
import net.seedboxer.core.domain.UserConfiguration;
import net.seedboxer.core.logic.UsersController;
import net.seedboxer.web.type.UserConfigInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
@Service
public class UsersService {

	@Autowired
	private UsersController usersController;

	/**
	 * Generate an APIKEY for the user.
	 * 
	 * @param user
	 * @return
	 */
	public User generateAPIKey(User user) {
		return usersController.generateAPIKey(user);
	}

	/**
	 * Return all user configurations
	 * 
	 * @param user
	 * @return
	 */
	public List<UserConfigInfo> getUserConfigs(User user) {
		List<UserConfigInfo> configs = new ArrayList<UserConfigInfo>();
		List<UserConfiguration> userConfig = usersController.getUserConfig(user.getId());
		for (UserConfiguration userConfiguration : userConfig) {
			configs.add(new UserConfigInfo(userConfiguration.getName(), userConfiguration.getValue()));
		}
		return configs;
	}
	
	/**
	 * Return all types of user configurations
	 * 
	 * @param user
	 * @return
	 */
	public List<UserConfigInfo> getUserConfigTypes() {
		return Lists.transform(Configuration.values, new Function<String, UserConfigInfo>() {

			@Override
			@Nullable
			public UserConfigInfo apply(@Nullable String type) {
				return new UserConfigInfo(type, null);
			}
		});
	}

	/**
	 * Save user configurations, if the key not exists in user configurations set, then will be added.
	 * If the key exists, then the configuration will be updated.
	 * 
	 * @param user
	 * @param name
	 * @param value
	 * @return
	 */
	public void saveUserConfigs(User user, String name, String value) {
		usersController.saveUserConf(user, new UserConfiguration(name, value));
	}

	/**
	 * Delete user configuration if exists.
	 * 
	 * @param user
	 * @param name
	 */
	public void deleteUserConfigs(User user, String name) {
		usersController.deleteUserConf(user, name);
	}
	
}
