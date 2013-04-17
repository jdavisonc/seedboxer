/*******************************************************************************
 * TvShowParser.java
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

package net.seedboxer.sources.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.seedboxer.core.domain.TvShow;
import net.seedboxer.core.type.Quality;

import org.springframework.stereotype.Component;


/**
 *
 * @author The-Sultan
 */
@Component
public class TvShowParser extends ContentParser<TvShow>{

	private final String QUALITY_HD = "720p";
	private final String QUALITY_FULLHD = "1080p";
	private final Pattern pattern = Pattern.compile(
			"(.*?)[\\.\\s_-]+S?(\\d{1,2})[Ex]{1}(\\d{2})[\\.\\s_-]+(\\d{3,4}p)?(.*)[\\.\\s_-]+(.*)" );

	@Override
	public TvShow parse(String input) {
		Matcher matcher = pattern.matcher(input);
		if(matcher.matches()){
			String name = matcher.group(1).trim();
			Integer season = Integer.valueOf(matcher.group(2));
			Integer episode = Integer.valueOf(matcher.group(3));

			String quality = matcher.group(4);
			Quality qualityEnum = Quality.STANDARD;
			if(quality != null){
				if(quality.equals(QUALITY_HD))
					qualityEnum = Quality.HD;
				else if(quality.equals(QUALITY_FULLHD))
					qualityEnum = Quality.FULLHD;
			}

			return new TvShow(name, season, episode, qualityEnum);
		} else {
			return null;
		}
	}

}
