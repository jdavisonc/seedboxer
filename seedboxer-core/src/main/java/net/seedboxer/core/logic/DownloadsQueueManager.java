/*******************************************************************************
 * DownloadsQueueManager.java
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
package net.seedboxer.core.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.seedboxer.core.domain.DownloadQueueItem;
import net.seedboxer.core.domain.Status;
import net.seedboxer.core.domain.User;
import net.seedboxer.core.persistence.DownloadsQueueDao;
import net.seedboxer.core.persistence.UsersDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author Jorge Davison (jdavisonc)
 *
 */
@Service
public class DownloadsQueueManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(DownloadsQueueManager.class);

	@Autowired
	private UsersDao usersDao;

	@Autowired
	private DownloadsQueueDao queueDao;

	public List<DownloadQueueItem> getNexts() {
		try {
			List<DownloadQueueItem> newInQueue = new ArrayList<DownloadQueueItem>();
			List<User> activeUsers = usersDao.getUsersByStatus(Status.STARTED);
			for (User user : activeUsers) {
				DownloadQueueItem inQueue = queueDao.head(user.getId());

				if (inQueue!= null && !inQueue.isInProgress()) {
					queueDao.setInProgress(inQueue.getId());
					newInQueue.add(inQueue);
				}
			}
			return newInQueue;
		} catch (Exception e) {
			LOGGER.warn("error making pooling", e);
			return Collections.emptyList();
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

	public void resetQueues() {
		queueDao.resetQueues();
	}

	public void resetQueue(User user) {
		queueDao.resetQueue(user.getId());
	}

	public void updateQueueOrder(List<DownloadQueueItem> queueItems){
		queueDao.updateQueueOrder(queueItems);
	}


}
