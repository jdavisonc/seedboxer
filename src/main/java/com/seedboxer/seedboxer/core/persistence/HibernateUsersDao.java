/*******************************************************************************
 * HibernateUsersDao.java
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
/**
 * 
 */
package com.seedboxer.seedboxer.core.persistence;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.seedboxer.seedboxer.core.domain.User;
import com.seedboxer.seedboxer.core.domain.UserConfiguration;

/**
 * @author harley
 *
 */
@Repository
@Transactional
public class HibernateUsersDao implements UsersDao {

	@Autowired
	private SessionFactory sessionFactory;

	protected final Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public boolean isValidUser(String username, String password) {
		Query query = getCurrentSession().createQuery("select 1 from User where username = :username and password = MD5(:password)");
		query.setParameter("username", username);
		query.setParameter("password", password);
		Integer result = (Integer) query.uniqueResult();
		return (result != null && result == 1);
	}

	@Override
	public void save(User user) {
		// TODO: make save again without session open
		//getCurrentSession().save(user);
	}

	@Override
	public User get(long userId) {
		return (User) getCurrentSession().get(User.class, userId);
	}

	@Override
	public User get(String username) {
		Query query = getCurrentSession().createQuery("from User where username = :username");
		query.setParameter("username", username);
		return (User) query.uniqueResult();
	}

	@Override
	public void saveUserConfig(long userId, UserConfiguration config) {
		User user = get(userId);
		config.setUser(user);
		UserConfiguration fromDb;
		if (config.getId() != 0) {
			fromDb = (UserConfiguration) getCurrentSession().get(UserConfiguration.class, config.getId());
		} else {
			Query query = getCurrentSession().createQuery("from UserConfiguration where user.id = :userId and name = :name");
			query.setParameter("userId", userId);
			query.setParameter("name", config.getName());
			fromDb = (UserConfiguration) query.uniqueResult();
		}

		if (fromDb == null) {
			fromDb = config;
		} else {
			fromDb.setValue(config.getValue());
		}

		getCurrentSession().save(fromDb);
	}

	@Override
	public List<UserConfiguration> getUserConfig(long userId) {
		Query query = getCurrentSession().createQuery("from UserConfiguration where user.id = :userId");
		query.setParameter("userId", userId);
		return query.list();
	}

	@Override
	public List<User> getUserWithConfig(String configName) {
		Query query = getCurrentSession().createQuery("select c.user from UserConfiguration c where c.name = :name");
		query.setParameter("name", configName);
		return query.list();
	}

}
