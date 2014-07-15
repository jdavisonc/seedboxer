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

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import net.seedboxer.core.domain.Content;
import net.seedboxer.core.domain.User;
import net.seedboxer.core.logic.ContentManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * @author The-Sultan
 * @author Jorge Davison (jdavisonc)
 */
@Component
@SuppressWarnings("rawtypes")
public class FilterManager {

  private static final Logger LOGGER = LoggerFactory.getLogger(FilterManager.class);

  private ContentManager contentManager;

  private List<ContentFilter> filters;

  private Cache<Content, List<User>> cache;

  @Value("${filter.cache.timeToLive}")
  private int cacheTimeToLive = 10;

  @Autowired
  public void setFilters(List<ContentFilter> filters) {
    this.filters = filters;
  }

  @Autowired
  public void setContentManager(ContentManager contentManager) {
    this.contentManager = contentManager;
  }

  public void setCacheTimeToLive(int cacheTimeToLive) {
    this.cacheTimeToLive = cacheTimeToLive;
  }

  @PostConstruct
  public void init() {
    cache = CacheBuilder.newBuilder().maximumSize(500)
        .expireAfterWrite(cacheTimeToLive, TimeUnit.MINUTES).build();
  }

  public synchronized Map<Content, List<User>> filterContent(List<Content> parsedContentList) {
    Map<Content, List<User>> mappedContent = mapContentWithUsers(parsedContentList);
    return filterContentWithHistory(filterContentWithCache(mappedContent));
  }

  /**
   * Maps the newly parsed content with the users, using the configured content for each user on the
   * Database.
   */
  private Map<Content, List<User>> mapContentWithUsers(List<Content> parsedContentList) {
    Map<Content, List<User>> mappedContent = new HashMap<Content, List<User>>();
    for (Content parsedContent : parsedContentList) {
      if (mappedContent.containsKey(parsedContent)) {
        continue;
      }

      List<User> usersWantingThisContent = findUsersWantingThisContent(parsedContent);

      if (!usersWantingThisContent.isEmpty()) {
        mappedContent.put(parsedContent, usersWantingThisContent);
      }
    }
    return mappedContent;
  }

  private List<User> findUsersWantingThisContent(Content parsedContent) {
    List<User> usersWantingThisContent = new ArrayList<User>();

    for (ContentFilter filter : filters) {
      for (Content content : getContentWithName(parsedContent.getName(), filter)) {

        Boolean wantedContent = filter.filterIfPossible(content, parsedContent);
        if (wantedContent != null && wantedContent) {
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
   * After having the content mapped to each user, this method filters the users for each content
   * using the user's history, if history content is equal to a matchedContent, then the user is
   * removed.
   */
  private Map<Content, List<User>> filterContentWithHistory(
      Map<Content, List<User>> mappedContents) {
    List<Content> toRemove = new ArrayList<Content>();

    for (Map.Entry<Content, List<User>> entries : mappedContents.entrySet()) {

      Content content = entries.getKey();
      List<User> users = entries.getValue();
      List<User> usersThatAlreadyHaveThisContent = contentManager
          .getUsersWithContentInHistory(content, users);

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

  /**
   * After having the content mapped to each user, this method filters the users for each content
   * using the user's cache (10 minutes by default to avoid dual downloads), if cache content is
   * equal to a matchedContent, then the user is removed.
   */
  private Map<Content, List<User>> filterContentWithCache(Map<Content, List<User>> mappedContents) {
    LOGGER.debug("Before filter with cache {}", mappedContents);

    List<Content> toRemove = new ArrayList<Content>();
    for (Map.Entry<Content, List<User>> entries : mappedContents.entrySet()) {

      Content content = entries.getKey();
      List<User> users = entries.getValue();
      List<User> usersThatAlreadyHaveThisContent = cache.getIfPresent(content);

      if (usersThatAlreadyHaveThisContent != null) {
        if (users.size() == usersThatAlreadyHaveThisContent.size()) {
          toRemove.add(content);
        } else {
          users.removeAll(usersThatAlreadyHaveThisContent);
        }
      } else {
        cache.put(content, users);
      }
    }

    for (Content content : toRemove) {
      mappedContents.remove(content);
    }
    LOGGER.debug("After filter with cache {}", mappedContents);
    return mappedContents;
  }

}
