package com.superdownloader.proeasy.core.persistence;

import java.util.List;

import com.superdownloader.proeasy.core.type.DownloadQueueItem;

/**
 * @author harley
 *
 */
public interface DownloadsQueueDao {

	void push(DownloadQueueItem item);

	void repush(int userId, int id);

	List<DownloadQueueItem> pop(int maxDownloadPerUser);

	void setInProgress(List<Integer> idsToUpdate);

	boolean remove(int userId, int id);

	List<DownloadQueueItem> queue(int userId);

}
