package com.superdownloader.proeasy.mule.logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.superdownloader.proeasy.core.types.DownloadQueueItem;
import com.superdownloader.proeasy.mule.persistence.DownloadsQueueDao;

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

	public void remove(int downloadId) {
		queueDao.remove(downloadId);
	}

	public void repush(int downloadId) {
		queueDao.repush(downloadId);
	}

	public void push(int userId, String download) {
		queueDao.push(new DownloadQueueItem(userId, download));
	}

}
