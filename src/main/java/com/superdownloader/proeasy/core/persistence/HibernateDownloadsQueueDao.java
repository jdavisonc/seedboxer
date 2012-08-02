package com.superdownloader.proeasy.core.persistence;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.superdownloader.proeasy.core.type.DownloadQueueItem;

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
	public void push(DownloadQueueItem item) {
		getCurrentSession().save(item);
	}

	@Override
	public void repush(DownloadQueueItem item) {
		item.setInProgress(false);
		getCurrentSession().save(item);
	}

	@Override
	public List<DownloadQueueItem> pop(int maxDownloadPerUser) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInProgress(List<Integer> idsToUpdate) {
		Query query = getCurrentSession().createQuery("update DownloadQueueItem " +
				"set inProgress = true where id IN (:ids)");
		query.setParameter("ids", idsToUpdate);
		query.executeUpdate();
	}

	@Override
	public void remove(DownloadQueueItem item) {
		getCurrentSession().delete(item);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DownloadQueueItem> queue(int userId) {
		Query query = getCurrentSession().createQuery("from DownloadQueueItem d " +
				"where d.user.id = :userId and d.inProgress = false");
		query.setParameter("userId", userId);
		return query.list();
	}

}
