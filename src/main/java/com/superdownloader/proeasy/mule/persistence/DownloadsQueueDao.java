package com.superdownloader.proeasy.mule.persistence;

import java.util.List;

import com.superdownloader.proeasy.core.types.DownloadQueueItem;

/**
 * @author harley
 *
 */
public interface DownloadsQueueDao {

	void push(DownloadQueueItem item);

	void repush(int id);

	List<DownloadQueueItem> pop(int maxDownloadPerUser);

	void setInProgress(List<Integer> idsToUpdate);

	void remove(int id);

	List<DownloadQueueItem> queue(int user_id);

}
