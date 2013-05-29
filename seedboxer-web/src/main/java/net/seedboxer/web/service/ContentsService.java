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
import net.seedboxer.core.domain.User;
import net.seedboxer.core.logic.ContentManager;
import net.seedboxer.web.type.dto.ContentInfo;
import net.seedboxer.web.utils.mapper.Mapper;

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
	
	@Autowired
	private Mapper mapper;
	
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
		return (ContentInfo) mapper.map(content);
	}
	
	private Content createUserContent(ContentInfo contentInfo) {
		return (Content) mapper.map(contentInfo);
	}
}
