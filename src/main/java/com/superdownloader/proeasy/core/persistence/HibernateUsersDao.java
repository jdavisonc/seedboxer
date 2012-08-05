/**
 * 
 */
package com.superdownloader.proeasy.core.persistence;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.superdownloader.proeasy.core.domain.User;
import com.superdownloader.proeasy.core.domain.UserConfiguration;

/**
 * @author harley
 *
 */
@Repository
public class HibernateUsersDao implements UsersDao {

	@Autowired
	private SessionFactory sessionFactory;

	protected final Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	@Transactional
	public boolean isValidUser(String username, String password) {
		Query query = getCurrentSession().createQuery("select 1 from User where username = :username and password = MD5(:password)");
		query.setParameter("username", username);
		query.setParameter("password", password);
		Integer result = (Integer) query.uniqueResult();
		return (result != null && result == 1);
	}

	@Override
	@Transactional
	public void save(User user) {
		// TODO: make save again without session open
		//getCurrentSession().save(user);
	}

	@Override
	@Transactional
	public User get(long userId) {
		return (User) getCurrentSession().get(User.class, userId);
	}

	@Override
	@Transactional
	public User get(String username) {
		Query query = getCurrentSession().createQuery("from User where username = :username");
		query.setParameter("username", username);
		return (User) query.uniqueResult();
	}

	@Override
	@Transactional
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
	@Transactional
	public List<UserConfiguration> getUserConfig(long userId) {
		Query query = getCurrentSession().createQuery("from UserConfiguration where user.id = :userId");
		query.setParameter("userId", userId);
		return query.list();
	}

}
