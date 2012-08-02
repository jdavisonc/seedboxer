package com.superdownloader.proeasy.core.logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.superdownloader.proeasy.core.persistence.DownloadsQueueDao;
import com.superdownloader.proeasy.core.type.DownloadQueueItem;

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
		List<Integer> setInProgress = new ArrayList<Integer>();
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

	public boolean remove(int userId, int downloadId) {
		return queueDao.remove(userId, downloadId);
	}

	public void repush(int userId, int downloadId) {
		queueDao.repush(userId, downloadId);
	}

	public void push(int userId, String download) {
		queueDao.push(new DownloadQueueItem(userId, download));
	}

	public List<DownloadQueueItem> userQueue(int userId) {
		return queueDao.queue(userId);
	}

}
