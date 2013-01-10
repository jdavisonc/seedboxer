/*******************************************************************************
 * UsersDao.java
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
package net.seedboxer.seedboxer.core.persistence;

import java.util.List;

import net.seedboxer.seedboxer.core.domain.Status;
import net.seedboxer.seedboxer.core.domain.User;
import net.seedboxer.seedboxer.core.domain.UserConfiguration;


public interface UsersDao {

	boolean isValidUser(String username, String password);

	User get(String username);

	User get(long userId);

	void save(User user);

	void saveUserConfig(long userId, UserConfiguration config);

	List<UserConfiguration> getUserConfig(long userId);

	List<User> getUserWithConfig(String configName);

	List<User> getUsersByStatus(Status status);

}
