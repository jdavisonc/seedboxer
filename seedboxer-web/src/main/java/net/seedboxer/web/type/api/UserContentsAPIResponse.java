/*******************************************************************************
 * UserContentsAPIResponse.java
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
package net.seedboxer.web.type.api;

import java.util.List;

import net.seedboxer.web.type.ContentInfo;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
public class UserContentsAPIResponse extends APIResponse {

	private List<ContentInfo> contents;
	
	public UserContentsAPIResponse(List<ContentInfo> contents) {
		this.contents = contents;
	}

	public List<ContentInfo> getContents() {
		return contents;
	}

	public void setContents(List<ContentInfo> contents) {
		this.contents = contents;
	}
	
}