/*******************************************************************************
 * FilterManager.java
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

package net.seedboxer.sources.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.seedboxer.core.domain.Content;
import net.seedboxer.core.domain.User;
import net.seedboxer.core.logic.ContentManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 *
 * @author The-Sultan
 * @author Jorge Davison (jdavisonc)
 */
@Component
@SuppressWarnings("rawtypes")
public class FilterManager {

	private ContentManager contentManager;

	private List<ContentFilter> filters;

	@Autowired
	public void setFilters(List<ContentFilter> filters) {
		this.filters = filters;
	}

	@Autowired
	public void setContentManager(ContentManager contentManager) {
		this.contentManager = contentManager;
	}

	public Map<Content,List<User>> filterContent(List<Content> parsedContentList){
		Map<Content, List<User>> mappedContent = mapContentWithUsers(parsedContentList);
		return filterContentWithHistory(mappedContent);
	}

	/**
	 * Maps the newly parsed content with the users, using the configured
	 * content for each user on the Database.
	 *
	 * @param allUsersContents
	 * @param parsedContentList
	 * @return
	 */
	private Map<Content,List<User>> mapContentWithUsers(List<Content> parsedContentList){
		Map<Content, List<User>> mappedContent = new HashMap<Content, List<User>>();
		for(Content parsedContent : parsedContentList){
			if(mappedContent.containsKey(parsedContent)) {
				continue;
			}

			List<User> usersWantingThisContent = findUsersWantingThisContent(parsedContent);

			if(!usersWantingThisContent.isEmpty()){
				mappedContent.put(parsedContent, usersWantingThisContent);
			}
		}
		return mappedContent;
	}

	private List<User> findUsersWantingThisContent(Content parsedContent) {
		List<User> usersWantingThisContent = new ArrayList<User>();

		for(ContentFilter filter : filters){
			for(Content content : getContentWithName(parsedContent.getName(), filter)){

				Boolean wantedContent = filter.filterIfPossible(content, parsedContent);
				if(wantedContent != null && wantedContent){
					usersWantingThisContent.add(content.getUser());
				}
			}
		}
		return usersWantingThisContent;
	}

	private List<Content> getContentWithName(String name, ContentFilter<?> filter) {
		return contentManager.getAllContentOfTypeAndName(name, filter.getType());
	}

	/**
	 * After having the content mapped to each user, this method filters the
	 * users for each content using the user's history, if history content is
	 * equal to a matchedContent, then the user is removed.
	 *
	 * @param mappedContent
	 * @return
	 */
	private Map<Content, List<User>> filterContentWithHistory(Map<Content, List<User>> mappedContents){
		List<Content> toRemove = new ArrayList<Content>();

		for (Map.Entry<Content, List<User>> entries : mappedContents.entrySet()) {

			Content content = entries.getKey();
			List<User> users = entries.getValue();
			List<User> usersThatAlreadyHaveThisContent = contentManager.getUsersWithContentInHistory(content, users);

			if (users.size() == usersThatAlreadyHaveThisContent.size()) {
				toRemove.add(content);
			} else {
				users.removeAll(usersThatAlreadyHaveThisContent);
			}
		}

		for (Content content : toRemove) {
			mappedContents.remove(content);
		}
		return mappedContents;
	}
}
