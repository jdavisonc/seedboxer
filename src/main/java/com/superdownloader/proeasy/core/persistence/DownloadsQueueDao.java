package com.superdownloader.proeasy.core.persistence;

import java.util.List;

import com.superdownloader.proeasy.core.domain.DownloadQueueItem;

/**
 * @author harley
 *
 */
public interface DownloadsQueueDao {

	void push(DownloadQueueItem item);

	void repush(long downloadId);

	List<DownloadQueueItem> pop(long maxDownloadPerUser);

	void setInProgress(List<Long> idsToUpdate);

	void remove(long downloadId);

	DownloadQueueItem get(long userId, long downloadId);

	List<DownloadQueueItem> queue(long userId);

}
