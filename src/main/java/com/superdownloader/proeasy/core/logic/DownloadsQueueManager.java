package com.superdownloader.proeasy.core.logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.superdownloader.proeasy.core.persistence.DownloadsQueueDao;
import com.superdownloader.proeasy.core.type.DownloadQueueItem;
import com.superdownloader.proeasy.core.type.User;

/**
 * @author harley
 *
 */
@Service
public class DownloadsQueueManager {

	private Integer maxDownloadPerUser;

	@Autowired
	private DownloadsQueueDao queueDao;

	@Value("${proeasy.simultaneousDownloadsPerUser}")
	public void setMaxDownloadPerUser(Integer maxDownloadPerUser) {
		this.maxDownloadPerUser = maxDownloadPerUser;
	}

	public List<DownloadQueueItem> pop() {
		List<DownloadQueueItem> inQueue = queueDao.pop(maxDownloadPerUser);

		// update to set in progress
		List<Long> setInProgress = new ArrayList<Long>();
		for (Iterator<DownloadQueueItem> iterator = inQueue.iterator(); iterator.hasNext();) {
			DownloadQueueItem item = iterator.next();
			if (item.isInProgress()) {
				iterator.remove();
			} else {
				setInProgress.add(item.getId());
			}
		}

		if (!setInProgress.isEmpty()) {
			queueDao.setInProgress(setInProgress);
		}
		return inQueue;
	}

	public void remove(DownloadQueueItem item) {
		queueDao.remove(item);
	}

	public void remove(User user, long downloadId) {
		DownloadQueueItem item = queueDao.get(user.getId(), downloadId);
		if (item == null) {
			throw new IllegalArgumentException("There is not download in queue with the given id");
		}
		queueDao.remove(item);
	}

	public void repush(DownloadQueueItem item) {
		queueDao.repush(item);
	}

	public void push(User user, String download) {
		queueDao.push(new DownloadQueueItem(user, download));
	}

	public List<DownloadQueueItem> userQueue(User user) {
		return queueDao.queue(user.getId());
	}

}
