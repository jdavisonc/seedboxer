/*******************************************************************************
 * ContentManager.java
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

package net.seedboxer.core.logic;

import java.util.ArrayList;
import java.util.List;

import net.seedboxer.core.domain.Content;
import net.seedboxer.core.domain.User;
import net.seedboxer.core.persistence.ContentDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Optional;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;


/**
 * @author Jorge Davison (jdavisonc)
 *
 */
@Service
public class ContentManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContentManager.class);

	@Autowired
	private ContentDao contentDao;

	public void updateContents(User user, List<Content> toUpdate) {
		List<Content> userContents = contentDao.getAllContents(user);
		for (Content content : toUpdate) {
			Optional<Content> find = Iterables.tryFind(userContents, Predicates.equalTo(content));
			if (!find.isPresent()) {
				content.setUser(user);
				contentDao.save(content);
				LOGGER.debug("New content {} for user {}", content.getName(), user.getId());
			}
		}
	}
	
	public List<Content> getAllContents(User user) {
		return contentDao.getAllContents(user);
	}

	public List<Content> getAllContentOfTypeAndName(String name, Class<? extends Content> contentType) {
		return contentDao.getAllContentsWithName(name, contentType);
	}

	@SuppressWarnings("unchecked")
	public List<Content> getHistoryContentOfType(Class<? extends Content> type, String name, User user) {
		return (List<Content>) contentDao.getHistoryContentsFilteredByNameAndUser(type, name, user);
	}

	public void saveContent(Content content, User user) {
		content.setUser(user);
		contentDao.save(content);
	}

	public void saveInHistory(Content content, User user) {
		content.setHistory(Boolean.TRUE);
		content.setUser(user);
		contentDao.save(content);
	}

	public List<User> getUsersWithContentInHistory(Content content, List<User> users) {
		List<User> usersWithContent = new ArrayList<User>();
		for(User user : users){
			List<Content> history = getHistoryContentOfType(content.getClass(), content.getName(), user);

			if (isContentIn(content, history)) {
				usersWithContent.add(user);
			}
		}
		return usersWithContent;
	}

	private boolean isContentIn(Content content, List<Content> historyContentOfType) {
		Optional<Content> find = Iterables.tryFind(historyContentOfType, Predicates.equalTo(content));
		return find.isPresent();
	}

	public void deleteContent(Content content, User user) {
		content.setUser(user);
		contentDao.delete(content);
	}

	public List<Content> getHistory(User user) {
		return contentDao.getHistoryContents(user);
	}

}
