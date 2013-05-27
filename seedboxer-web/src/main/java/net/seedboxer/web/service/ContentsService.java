/*******************************************************************************
 * ContentsService.java
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
package net.seedboxer.web.service;

import java.util.List;

import javax.annotation.Nullable;

import net.seedboxer.core.domain.Content;
import net.seedboxer.core.domain.TvShow;
import net.seedboxer.core.domain.User;
import net.seedboxer.core.logic.ContentManager;
import net.seedboxer.core.type.Quality;
import net.seedboxer.web.exceptions.UnkownContentType;
import net.seedboxer.web.type.ContentInfo;
import net.seedboxer.web.type.TvShowInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import net.seedboxer.core.domain.Movie;
import net.seedboxer.web.type.MovieInfo;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
@Service
public class ContentsService {
	
	@Autowired
	private ContentManager contentManager;
	
	public List<ContentInfo> getUserContents(User user) {
		List<Content> allContents = contentManager.getAllContents(user);
		
		return Lists.transform(allContents, new Function<Content, ContentInfo>() {

			@Override
			@Nullable
			public ContentInfo apply(@Nullable Content content) {
				return createUserContentType(content);
			}

		});
	}
	
	public void saveUserContent(User user, ContentInfo contentInfo) {
		Content content = createUserContent(contentInfo);
		contentManager.saveContent(content, user);
	}

	public void deleteUserContent(User user, ContentInfo contentInfo) {
		Content content = createUserContent(contentInfo);
		contentManager.deleteContent(content, user);		
	}
	
	private ContentInfo createUserContentType(Content content) {
		if (content instanceof TvShow) {
			TvShow show = (TvShow) content;
			return new TvShowInfo(show.getName(), show.getSeason(), show.getEpisode(), show.getQuality().name());
		}
		else if (content instanceof Movie) {
		    Movie movie = (Movie) content;
		    return new MovieInfo(movie.getName(), movie.getYear(), movie.getQuality().name());
		}
		throw new UnkownContentType();
	}
	
	private Content createUserContent(ContentInfo contentInfo) {
		if (contentInfo instanceof TvShowInfo) {
			TvShowInfo show = (TvShowInfo) contentInfo;
			return new TvShow(show.getName(), show.getSeason(), show.getEpisode(), Quality.valueOf(show.getQuality()));
		}
		else if (contentInfo instanceof MovieInfo) {
		    MovieInfo show = (MovieInfo) contentInfo;
		    return new Movie(show.getName(), show.getYear(),Quality.valueOf(show.getQuality()));
		}
		throw new UnkownContentType();
	}
}
