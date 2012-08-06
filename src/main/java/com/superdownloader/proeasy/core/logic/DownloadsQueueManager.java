package com.superdownloader.proeasy.core.logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.superdownloader.proeasy.core.domain.DownloadQueueItem;
import com.superdownloader.proeasy.core.domain.User;
import com.superdownloader.proeasy.core.persistence.DownloadsQueueDao;

/**
 * @author harley
 *
 */
@Service
public class DownloadsQueueManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(DownloadsQueueManager.class);

	private Integer maxDownloadPerUser;

	@Autowired
	private DownloadsQueueDao queueDao;

	@Value("${proeasy.simultaneousDownloadsPerUser}")
	public void setMaxDownloadPerUser(Integer maxDownloadPerUser) {
		this.maxDownloadPerUser = maxDownloadPerUser;
	}

	public List<DownloadQueueItem> pop() {
		try {
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

		} catch (Exception e) {
			LOGGER.warn("error making pooling", e);
			return null;
		}
	}

	public void remove(long downloadId) {
		queueDao.remove(downloadId);
	}

	public void remove(User user, long downloadId) {
		DownloadQueueItem item = queueDao.get(user.getId(), downloadId);
		if (item == null) {
			throw new IllegalArgumentException("There is not download in queue with the given id");
		}
		queueDao.remove(item.getId());
	}

	public void repush(long downloadId) {
		queueDao.repush(downloadId);
	}

	public void push(User user, String download) {
		queueDao.push(new DownloadQueueItem(user, download));
	}

	public List<DownloadQueueItem> userQueue(User user) {
		return queueDao.queue(user.getId());
	}

}
