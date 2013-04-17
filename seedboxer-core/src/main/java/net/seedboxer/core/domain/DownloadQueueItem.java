/*******************************************************************************
 * DownloadQueueItem.java
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
package net.seedboxer.core.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * @author Jorge Davison (jdavisonc)
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

	@Column(name="queue_order")
	private int queueOrder;

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

	public int getQueueOrder() {
		return queueOrder;
	}

	public void setQueueOrder(int queueOrder) {
		this.queueOrder = queueOrder;
	}

	@Override
	public String toString() {
		return "DownloadQueueItem [id=" + id + ", user=" + user
				+ ", download=" + download + ", inProgress=" + inProgress + "]";
	}

}
