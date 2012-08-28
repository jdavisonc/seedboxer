/*******************************************************************************
 * FeedsManager.java
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

package com.seedboxer.seedboxer.core.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seedboxer.seedboxer.core.domain.RssFeed;
import com.seedboxer.seedboxer.core.persistence.FeedsDao;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
@Service
public class FeedsManager {

	@Autowired
	private FeedsDao feedsDao;

	public List<RssFeed> getAllFeeds() {
		return feedsDao.getAllFeeds();
	}

}
