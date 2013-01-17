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

package net.seedboxer.seedboxer.sources.filters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.seedboxer.seedboxer.core.domain.Content;
import net.seedboxer.seedboxer.core.domain.User;
import net.seedboxer.seedboxer.core.logic.ContentManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Optional;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;


/**
 *
 * @author The-Sultan
 */
@Component
@SuppressWarnings("rawtypes")
public class FilterManager {

	@Autowired
	private ContentManager contentManager;

	private List<ContentFilter> filters;

	public List<ContentFilter> getFilters() {
		return filters;
	}

	@Autowired
	public void setFilters(List<ContentFilter> filters) {
		this.filters = filters;
	}

	private List<Content> getAllContent(){
		List<Content> userContent = new ArrayList<Content>();
		for(ContentFilter filter : filters){
			@SuppressWarnings("unchecked")
			List<Content> contentHistory = contentManager.getAllContentOfType(filter.getType());
			userContent.addAll(contentHistory);
		}
		return userContent;

	}

	public Map<Content,List<User>> filterContent(List<Content> parsedContentList){
		List<Content> allUsersContents = getAllContent();
		Map<Content, List<User>> mappedContent = mapContentWithUsers(allUsersContents, parsedContentList);
		mappedContent = filterContentWithHistory(mappedContent);
		updateHistory(mappedContent);
		return mappedContent;
	}

	/**
	 * Maps the newly parsed content with the users, using the configured
	 * content for each user on the Database.
	 *
	 * @param allUsersContents
	 * @param parsedContentList
	 * @return
	 */
	private Map<Content,List<User>> mapContentWithUsers(List<Content> allUsersContents, List<Content> parsedContentList){
		Map<Content, List<User>> mappedContent = new HashMap<Content, List<User>>();
		for(Content parsedContent : parsedContentList){
			if(mappedContent.containsKey(parsedContent)) {
				continue;
			}

			List<User> usersWantingThisContent = findUsersWantingThisContent(allUsersContents, parsedContent);

			if(!usersWantingThisContent.isEmpty()){
				mappedContent.put(parsedContent, usersWantingThisContent);
			}
		}
		return mappedContent;
	}

	private List<User> findUsersWantingThisContent(List<Content> allUsersContents, Content parsedContent) {
		List<User> usersWantingThisParsedContent = new ArrayList<User>();
		for(Content userContent : allUsersContents){
			for(ContentFilter filter : filters){
				Boolean wantedContent = filter.filterIfPossible(userContent, parsedContent);
				if(wantedContent != null && wantedContent){
					usersWantingThisParsedContent.add(userContent.getUser());
				}
			}
		}
		return usersWantingThisParsedContent;
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
			List<User> usersThatAlreadyHaveThisContent = filterUserContentByHistory(content, users);

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

	private List<User> filterUserContentByHistory(Content content, List<User> users) {
		List<User> usersThatAlreadyHaveThisContent = new ArrayList<User>();
		for(User user : users){

			List<Content> historyContentOfType =
					contentManager.getHistoryContentOfType(content.getClass(), content.getName(), user);

			if (isContentIn(content, historyContentOfType)) {
				usersThatAlreadyHaveThisContent.add(user);
			}
		}
		return usersThatAlreadyHaveThisContent;
	}

	private boolean isContentIn(Content content,
			List<Content> historyContentOfType) {
		Optional<Content> find = Iterables.tryFind(historyContentOfType, Predicates.equalTo(content));
		return find.isPresent();
	}


	private void updateHistory(Map<Content, List<User>> mappedContent){
		for(Content content : mappedContent.keySet()){
			content.setHistory(Boolean.TRUE);
			for(User user : mappedContent.get(content)){
				contentManager.saveContent(content, user);
			}

		}
	}
}
