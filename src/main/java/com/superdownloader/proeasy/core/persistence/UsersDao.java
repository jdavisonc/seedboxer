package com.superdownloader.proeasy.core.persistence;

import java.util.List;

import com.superdownloader.proeasy.core.domain.User;
import com.superdownloader.proeasy.core.domain.UserConfiguration;

public interface UsersDao {

	boolean isValidUser(String username, String password);

	User get(String username);

	User get(long userId);

	void save(User user);

	void saveUserConfig(long userId, UserConfiguration config);

	List<UserConfiguration> getUserConfig(long userId);

}
