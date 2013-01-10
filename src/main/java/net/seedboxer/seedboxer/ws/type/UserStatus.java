/*******************************************************************************
 * UserStatus.java
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
package net.seedboxer.seedboxer.ws.type;

import javax.xml.bind.annotation.XmlRootElement;

import net.seedboxer.seedboxer.core.domain.Status;
import net.seedboxer.seedboxer.core.type.Download;


/**
 * @author Jorge Davison (jdavisonc)
 *
 */
@XmlRootElement(name="user")
public class UserStatus {

	private Status status;

	private Download download;

	public UserStatus() {
		status = Status.STARTED;
		download = null;
	}

	public UserStatus(Status status, Download download) {
		this.status = status;
		this.download = download;
	}

	public Status getStatus() {
		return status;
	}

	public Download getDownload() {
		return download;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setDownload(Download download) {
		this.download = download;
	}

}
