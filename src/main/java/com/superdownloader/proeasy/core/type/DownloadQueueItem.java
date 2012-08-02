package com.superdownloader.proeasy.core.type;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * @author harley
 *
 */
@Entity
@Table(name="downloads_queue")
public class DownloadQueueItem {

	@Id
	private long id;

	@ManyToOne
	private User user;

	private String download;

	private boolean inProgress;

	public DownloadQueueItem() { }

	public DownloadQueueItem(User user, String download) {
		this.user = user;
		this.download = download;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDownload() {
		return download;
	}

	public void setDownload(String download) {
		this.download = download;
	}

	public boolean isInProgress() {
		return inProgress;
	}

	public void setInProgress(boolean inProgress) {
		this.inProgress = inProgress;
	}

	@Override
	public String toString() {
		return "DownloadQueueItem [id=" + id + ", user=" + user
				+ ", download=" + download + ", inProgress=" + inProgress + "]";
	}

}
