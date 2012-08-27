/*******************************************************************************
 * TvShowFilter.java
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

package com.seedboxer.seedboxer.sources.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.seedboxer.seedboxer.core.domain.TvShow;

/**
 *
 * @author The-Sultan
 */
@Component
public class TvShowFilter extends ContentFilter<TvShow>{


	@Override
	protected boolean applyFilter(TvShow userContent, TvShow parsedContent) {
		if(userContent.getName().trim().equalsIgnoreCase(parsedContent.getName().trim())){
			if((userContent.getSeason() != null && userContent.getSeason() == parsedContent.getSeason())
					||
					userContent.getSeason() == null)
				if((userContent.getEpisode() != null && userContent.getEpisode() == parsedContent.getEpisode())
						||
						userContent.getSeason() == null)
					if((userContent.getQuality() != null && userContent.getQuality().equals(parsedContent.getQuality()))
							||
							userContent.getQuality() == null)
						return true;

		}
		return false;
	}

	@Autowired
	public void setType(TvShow tvshow){
		super.setType(TvShow.class);
	}

}
