/*******************************************************************************
 * DownloadsQueueDao.java
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
package net.seedboxer.seedboxer.core.persistence;

import java.util.List;

import net.seedboxer.seedboxer.core.domain.DownloadQueueItem;


/**
 * @author Jorge Davison (jdavisonc)
 *
 */
public interface DownloadsQueueDao {

	void push(DownloadQueueItem item);

	void repush(long downloadId);

	DownloadQueueItem head(long userId);

	void setInProgress(Long downloadId);

	void remove(long downloadId);

	DownloadQueueItem get(long userId, long downloadId);

	List<DownloadQueueItem> queue(long userId);

	void resetQueues();

	void resetQueue(long userId);

	void updateQueueOrder(List<DownloadQueueItem> queueItems);

}
