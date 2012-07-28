/**
 * 
 */
package com.superdownloader.proeasy.core.persistence;

import java.util.Map;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author harley
 *
 */
@Repository
public class HibernateUsersDao implements UsersDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public boolean isValidUser(String username, String password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Map<String, String> getUserConfigs(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getUserConfigs(int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveUserConfigs(String username, Map<String, String> configs) {
		// TODO Auto-generated method stub

	}

	@Override
	public Integer getUserId(String username) {
		// TODO Auto-generated method stub
		return null;
	}

}
