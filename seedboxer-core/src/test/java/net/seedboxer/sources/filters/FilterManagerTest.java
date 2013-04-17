/*******************************************************************************
 * FilterManagerTest.java
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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.seedboxer.core.domain.Content;
import net.seedboxer.core.domain.TvShow;
import net.seedboxer.core.domain.User;
import net.seedboxer.core.logic.ContentManager;
import net.seedboxer.core.type.Quality;
import net.seedboxer.sources.filters.ContentFilter;
import net.seedboxer.sources.filters.FilterManager;
import net.seedboxer.sources.filters.TvShowFilter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class FilterManagerTest {

	private static final String CONTENT_NAME = "The Big Bang Theory";

	private FilterManager filterManager;

	@Mock
	private ContentManager contentManager;

	@Mock
	private TvShowFilter contentFilter;

	@Mock
	private Content parsedContent;

	@Mock
	private User user1, user2;

	private TvShow content1, content2;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Before
	public void setUp() throws Exception {
		filterManager = new FilterManager();
		List<ContentFilter> f = Collections.singletonList((ContentFilter)contentFilter);
		filterManager.setFilters(f);

		filterManager.setContentManager(contentManager);
		content1 = new TvShow(CONTENT_NAME, 1, 1, Quality.HD);
		content1.setUser(user1);
		content2 = new TvShow(CONTENT_NAME, 1, 1, Quality.HD);
		content2.setUser(user2);
		when(parsedContent.getName()).thenReturn(CONTENT_NAME);

		when(contentFilter.filterIfPossible(content1, parsedContent)).thenReturn(true);
		when(contentFilter.getType()).thenCallRealMethod();

		when(contentManager.getAllContentOfTypeAndName(eq(CONTENT_NAME), eq(TvShow.class))).thenReturn(Collections.singletonList((Content)content1));

		when(contentManager.getUsersWithContentInHistory(eq(parsedContent), any(List.class))).thenReturn(Collections.<User> emptyList());
	}

	@Test
	public void shouldAddContentWhenUserNotHasInHistory() throws Exception {
		filterManager.filterContent(Collections.singletonList(parsedContent));
		verify(contentManager).saveContent(parsedContent, user1);
	}

	@Test
	public void shouldNotAddContentWhenFilterReturnsFalse() throws Exception {
		when(contentFilter.filterIfPossible(any(Content.class), eq(parsedContent))).thenReturn(false);
		filterManager.filterContent(Collections.singletonList(parsedContent));
		verify(contentManager, never()).saveContent(parsedContent, user1);
	}

	@Test
	public void shouldNotAddContentWhenFilterIsNotPossible() throws Exception {
		when(contentFilter.filterIfPossible(any(Content.class), eq(parsedContent))).thenReturn(null);
		filterManager.filterContent(Collections.singletonList(parsedContent));
		verify(contentManager, never()).saveContent(parsedContent, user1);
	}

	@Test
	public void shouldNotAddContentWhenUserAlreadyHasInHistory() throws Exception {
		when(contentManager.getUsersWithContentInHistory(any(Content.class), any(List.class))).thenReturn(Collections.singletonList(user1));
		filterManager.filterContent(Collections.singletonList(parsedContent));
		verify(contentManager, never()).saveContent(parsedContent, user1);
	}

	@Test
	public void shouldNotAddContentWhenUsersDoesntHaveContentWithName() throws Exception {
		when(contentManager.getAllContentOfTypeAndName(eq(CONTENT_NAME), eq(TvShow.class))).thenReturn(Collections.<Content> emptyList());
		filterManager.filterContent(Collections.singletonList(parsedContent));
		verify(contentManager, never()).saveContent(parsedContent, user1);
	}

	@Test
	public void shouldNotAddContentForUser1AndAddToUser2() throws Exception {
		List<Content> list = new ArrayList<Content>();
		list.add(content1);
		list.add(content2);
		when(contentManager.getAllContentOfTypeAndName(eq(CONTENT_NAME), eq(TvShow.class))).thenReturn(list);
		when(contentManager.getUsersWithContentInHistory(eq(parsedContent), any(List.class))).thenReturn(Collections.singletonList(user1));
		filterManager.filterContent(Collections.singletonList(parsedContent));
		verify(contentManager, never()).saveContent(parsedContent, user1);
		verify(contentManager).saveContent(parsedContent, user2);
	}

}
