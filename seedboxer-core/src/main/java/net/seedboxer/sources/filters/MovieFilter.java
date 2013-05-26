/*******************************************************************************
 * MovieFilter.java
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

package net.seedboxer.sources.filters;

import net.seedboxer.core.domain.Movie;
import org.springframework.stereotype.Component;


/**
 *
 * @author The-Sultan
 */
@Component
public class MovieFilter extends ContentFilter<Movie>{

	@Override
	public Class<Movie> getType() {
		return Movie.class;
	}

	@Override
	protected boolean applyFilter(Movie userContent, Movie parsedContent) {
		if(userContent.getName().trim().equalsIgnoreCase(parsedContent.getName().trim())){
			if((userContent.getYear() != null && userContent.getYear().equals(parsedContent.getYear()))
					||
					userContent.getYear() == null)
				
			    if((userContent.getQuality() != null && userContent.getQuality().equals(parsedContent.getQuality()))
					    ||
					    userContent.getQuality() == null)
				return true;

		}
		return false;
	}

}
