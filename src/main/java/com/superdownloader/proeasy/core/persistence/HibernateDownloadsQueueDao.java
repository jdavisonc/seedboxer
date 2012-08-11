/*******************************************************************************
 * HibernateDownloadsQueueDao.java
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
package com.superdownloader.proeasy.core.persistence;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.superdownloader.proeasy.core.domain.DownloadQueueItem;

/**
 * @author harley
 *
 */
@Repository
public class HibernateDownloadsQueueDao implements DownloadsQueueDao {

	@Autowired
	private SessionFactory sessionFactory;

	protected final Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	@Transactional
	public void push(DownloadQueueItem item) {
		getCurrentSession().save(item);
	}

	@Override
	@Transactional
	public void repush(long downloadId) {
		DownloadQueueItem itemdb = (DownloadQueueItem) getCurrentSession()
				.get(DownloadQueueItem.class, downloadId);
		itemdb.setInProgress(false);
		getCurrentSession().save(itemdb);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<DownloadQueueItem> pop(long maxDownloadPerUser) {
		Query query = getCurrentSession().createQuery("from DownloadQueueItem d where " +
				"(select count(*) from DownloadQueueItem f where " +
				"f.user.id = d.user.id AND f.id < d.id) <= :maxDownloadPerUser");
		query.setParameter("maxDownloadPerUser", maxDownloadPerUser);
		return query.list();
	}

	@Override
	@Transactional
	public void setInProgress(List<Long> idsToUpdate) {
		Query query = getCurrentSession().createQuery("update DownloadQueueItem " +
				"set inProgress = true where id in (:ids)");
		query.setParameterList("ids", idsToUpdate);
		query.executeUpdate();
	}

	@Override
	@Transactional
	public void remove(long downloadId) {
		Query query = getCurrentSession().createQuery("delete from DownloadQueueItem where id = :id");
		query.setParameter("id", downloadId);
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<DownloadQueueItem> queue(long userId) {
		Query query = getCurrentSession().createQuery("from DownloadQueueItem d " +
				"where d.user.id = :userId and d.inProgress = false");
		query.setParameter("userId", userId);
		return query.list();
	}

	@Override
	@Transactional
	public DownloadQueueItem get(long userId, long downloadId) {
		Query query = getCurrentSession().createQuery("from DownloadQueueItem d " +
				"where d.id = :downloadId and d.user.id = :userId");
		query.setParameter("downloadId", downloadId);
		query.setParameter("userId", userId);
		return (DownloadQueueItem) query.uniqueResult();
	}

}
