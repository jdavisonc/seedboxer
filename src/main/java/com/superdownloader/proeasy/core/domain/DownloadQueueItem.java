package com.superdownloader.proeasy.core.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

	@ManyToOne(fetch=FetchType.EAGER)
	private User user;

	private String download;

	@Column(name="in_progress")
	private boolean inProgress;

	@Column(name="created_on")
	private Date createdOn;

	public DownloadQueueItem() { }

	public DownloadQueueItem(User user, String download) {
		this.user = user;
		this.download = download;
		createdOn = new Date();
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

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Override
	public String toString() {
		return "DownloadQueueItem [id=" + id + ", user=" + user
				+ ", download=" + download + ", inProgress=" + inProgress + "]";
	}

}
