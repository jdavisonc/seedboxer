package com.superdownloader.proeasy.core.persistence;

import java.util.List;

import com.superdownloader.proeasy.core.types.DownloadQueueItem;

/**
 * @author harley
 *
 */
public interface DownloadsQueueDao {

	void push(DownloadQueueItem item);

	void repush(int id);

	List<DownloadQueueItem> pop();

	void remove(int id);

	List<DownloadQueueItem> queue(int user_id);

}
