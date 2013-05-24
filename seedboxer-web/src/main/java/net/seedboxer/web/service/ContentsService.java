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
import net.seedboxer.web.type.UserContent;
import net.seedboxer.web.type.UserContent.ContentType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
@Service
public class ContentsService {
	
	@Autowired
	private ContentManager contentManager;
	
	public List<UserContent> getContents(User user) {
		List<Content> allContents = contentManager.getAllContents(user);
		
		return Lists.transform(allContents, new Function<Content, UserContent>() {

			@Override
			@Nullable
			public UserContent apply(@Nullable Content content) {
				return createUserContentType(content);
			}

		});
	}
	
	public void addContent(User user) {
		
	}
	
	public void deleteContent(User user) {
		
	}
	
	private UserContent createUserContentType(Content content) {
		if (content instanceof TvShow) {
			TvShow show = (TvShow) content;
			return new UserContent(show.getName(), show.getSeason(), show.getEpisode(), show.getQuality().name(), ContentType.TV_SHOW);
		}
		return null;
	}	
}
