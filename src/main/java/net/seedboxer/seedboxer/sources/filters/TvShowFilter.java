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

package net.seedboxer.seedboxer.sources.filters;

import net.seedboxer.seedboxer.core.domain.TvShow;

import org.springframework.stereotype.Component;


/**
 *
 * @author The-Sultan
 */
@Component
public class TvShowFilter extends ContentFilter<TvShow>{

	@Override
	public Class<TvShow> getType() {
		return TvShow.class;
	}

	@Override
	protected boolean applyFilter(TvShow userContent, TvShow parsedContent) {
		if(sameName(userContent, parsedContent) &&
				sameSeason(userContent, parsedContent) &&
				sameEpisode(userContent, parsedContent) &&
				sameQuality(userContent, parsedContent)) {
			return true;
		}
		return false;
	}

	private boolean sameName(TvShow userContent, TvShow parsedContent) {
		return userContent.getName().equalsIgnoreCase(parsedContent.getName());
	}

	private boolean sameQuality(TvShow userContent, TvShow parsedContent) {
		return ((userContent.getQuality() != null && userContent.getQuality().equals(parsedContent.getQuality()))
				||
				userContent.getQuality() == null);
	}

	private boolean sameEpisode(TvShow userContent, TvShow parsedContent) {
		return ((userContent.getEpisode() != null && userContent.getEpisode() == parsedContent.getEpisode())
				||
				userContent.getEpisode() == null);
	}

	private boolean sameSeason(TvShow userContent, TvShow parsedContent) {
		return ((userContent.getSeason() != null && userContent.getSeason() == parsedContent.getSeason())
				||
				userContent.getSeason() == null);
	}

}
