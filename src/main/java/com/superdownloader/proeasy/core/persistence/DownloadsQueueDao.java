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

	void setInProgress(List<Long> idsToUpdate);

	void remove(DownloadQueueItem item);

	DownloadQueueItem get(long userId, long downloadId);

	List<DownloadQueueItem> queue(long userId);

}
