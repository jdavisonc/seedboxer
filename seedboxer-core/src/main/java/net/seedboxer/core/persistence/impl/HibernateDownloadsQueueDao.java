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
package net.seedboxer.core.persistence.impl;

import java.util.List;

import net.seedboxer.core.domain.DownloadQueueItem;
import net.seedboxer.core.persistence.DownloadsQueueDao;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author Jorge Davison (jdavisonc)
 *
 */
@Repository
@Transactional
public class HibernateDownloadsQueueDao extends HibernateDao implements DownloadsQueueDao {

	@Override
	public void push(DownloadQueueItem item) {
		List<DownloadQueueItem> queue = queue(item.getUser().getId());
		int maxOrder = -1;

		for(DownloadQueueItem queueItem : queue){
			if(queueItem.getQueueOrder() > maxOrder){
				maxOrder = queueItem.getQueueOrder();
			}
		}
		item.setQueueOrder(maxOrder + 1);
		getCurrentSession().save(item);
	}

	@Override
	public void repush(long downloadId) {
		Query query = getCurrentSession().createQuery("update DownloadQueueItem set inProgress = false where id = :id");
		query.setParameter("id", downloadId);
		query.executeUpdate();
	}

	@Override
	public DownloadQueueItem head(long userId) {
		Query query = getCurrentSession().createQuery("from DownloadQueueItem d " +
				"where d.user.id = :userId order by d.queueOrder");

		query.setParameter("userId", userId);
		query.setMaxResults(1);
		return (DownloadQueueItem) query.uniqueResult();
	}

	@Override
	public void setInProgress(Long downloadId) {
		Query query = getCurrentSession().createQuery("update DownloadQueueItem set inProgress = true where id = :id");
		query.setParameter("id", downloadId);
		query.executeUpdate();
	}

	@Override
	public void remove(long downloadId) {
		Query query = getCurrentSession().createQuery("delete from DownloadQueueItem where id = :id");
		query.setParameter("id", downloadId);
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DownloadQueueItem> queue(long userId) {
		Query query = getCurrentSession().createQuery("from DownloadQueueItem d " +
				"where d.user.id = :userId and d.inProgress = false");
		query.setParameter("userId", userId);
		return query.list();
	}

	@Override
	public DownloadQueueItem get(long userId, long downloadId) {
		Query query = getCurrentSession().createQuery("from DownloadQueueItem d " +
				"where d.id = :downloadId and d.user.id = :userId");
		query.setParameter("downloadId", downloadId);
		query.setParameter("userId", userId);
		return (DownloadQueueItem) query.uniqueResult();
	}

	@Override
	public void resetQueues() {
		Query query = getCurrentSession().createQuery("update DownloadQueueItem set inProgress = false");
		query.executeUpdate();
	}

	@Override
	public void updateQueueOrder(List<DownloadQueueItem> queueItems) {
		for(DownloadQueueItem queueItem : queueItems) {
			getCurrentSession().update(queueItem);
		}
	}

	@Override
	public void resetQueue(long userId) {
		Query query = getCurrentSession().createQuery("update DownloadQueueItem " +
				"set inProgress = false where user.id = :userId");

		query.setParameter("userId", userId);
		query.executeUpdate();
	}

}
