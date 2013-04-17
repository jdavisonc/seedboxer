/*******************************************************************************
 * RssSplitterTest.java
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
package net.seedboxer.seedboxer.sources.processors.rss.splitter;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;

/**
 *
 * @author Jorge Davison
 */
@RunWith(MockitoJUnitRunner.class)
public class RssSplitterTest {

	private RssSplitter rssSplitter;

	@Mock
	private SyndFeed feed;

	@Mock
	private List<SyndEntry> entries;

	@Before
	public void setUp() throws Exception {
		rssSplitter = new RssSplitter();
		when(feed.getEntries()).thenReturn(entries);
	}

	@Test
	public void shouldSplitFeedEntries() throws Exception {
		assertEquals(entries, rssSplitter.split(feed));
	}

}
