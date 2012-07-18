package com.superdownloader.proeasy.core.types;

/**
 * @author harley
 *
 */
public class DownloadQueueItem {

	private int id;

	private int userId;

	private String download;

	private boolean inProgress;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
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
		return "DownloadQueueItem [id=" + id + ", userId=" + userId
				+ ", download=" + download + ", inProgress=" + inProgress + "]";
	}

}
