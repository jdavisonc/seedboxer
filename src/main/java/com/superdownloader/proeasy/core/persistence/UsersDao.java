package com.superdownloader.proeasy.core.persistence;

import java.util.Map;

public interface UsersDao {

	public boolean isValidUser(String username, String password);

	public Map<String, String> getUserConfigs(String username);

	public Map<String, String> getUserConfigs(int userId);

	public void saveUserConfigs(String username, Map<String, String> configs);

	public Integer getUserId(String username);

}
