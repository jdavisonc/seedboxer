package com.superdownloader.proeasy.core.persistence;

import com.superdownloader.proeasy.core.type.User;

public interface UsersDao {

	boolean isValidUser(String username, String password);

	User get(String username);

	User get(int userId);

	void save(User user);

}
