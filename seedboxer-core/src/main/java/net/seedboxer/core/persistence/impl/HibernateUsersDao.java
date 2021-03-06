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
package net.seedboxer.core.persistence.impl;

import java.util.List;

import net.seedboxer.core.domain.Status;
import net.seedboxer.core.domain.User;
import net.seedboxer.core.domain.UserConfiguration;
import net.seedboxer.core.persistence.UsersDao;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author Jorge Davison (jdavisonc)
 *
 */
@Repository
@Transactional
public class HibernateUsersDao extends HibernateDao implements UsersDao {

	@Override
	public void save(User user) {
		getCurrentSession().saveOrUpdate(user);
	}

	@Override
	public User get(long userId) {
		return (User) getCurrentSession().get(User.class, userId);
	}

	@Override
	public User get(String username) {
		Query query = getCurrentSession().createQuery("from User where username = :username");
		query.setString("username", username);
		return (User) query.uniqueResult();
	}

	@Override
	public User getFromAPIKey(String apikey) {
		Query query = getCurrentSession().createQuery("from User where apikey is not null and apikey = :apikey");
		query.setString("apikey", apikey);
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
	public void deleteUserConfig(long userId, String name) {
		Query query = getCurrentSession().createQuery("from UserConfiguration where user.id = :userId and name = :name");
		query.setParameter("userId", userId);
		query.setParameter("name", name);
		if (query.uniqueResult() != null) {
			getCurrentSession().delete(query.uniqueResult());
		}
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
		query.setString("name", configName);
		return query.list();
	}

	@Override
	public List<User> getUsersByStatus(Status status) {
		Query query = getCurrentSession().createQuery("from User where status = :status");
		query.setParameter("status", status);
		return query.list();
	}

	@Override
	public void delete(User user) {
		getCurrentSession().delete(user);
		Query query = getCurrentSession().createQuery("delete from UserConfiguration where user.id = :userId");
		query.setParameter("userId", user.getId());
		query.executeUpdate();
	}

	@Override
	public List<User> getAllUsers() {
		return getCurrentSession().createCriteria(User.class).list();
	}

}
