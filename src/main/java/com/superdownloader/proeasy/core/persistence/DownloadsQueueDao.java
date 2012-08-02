package com.superdownloader.proeasy.core.persistence;

import java.util.List;

import com.superdownloader.proeasy.core.type.DownloadQueueItem;

/**
 * @author harley
 *
 */
public interface DownloadsQueueDao {

	void push(DownloadQueueItem item);

	void repush(DownloadQueueItem item);

	List<DownloadQueueItem> pop(int maxDownloadPerUser);

	void setInProgress(List<Integer> idsToUpdate);

	void remove(DownloadQueueItem item);

	List<DownloadQueueItem> queue(int userId);

}
