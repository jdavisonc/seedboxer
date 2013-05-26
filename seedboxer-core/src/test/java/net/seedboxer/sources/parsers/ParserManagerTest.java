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
package net.seedboxer.sources.parsers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.seedboxer.core.domain.Content;
import net.seedboxer.core.domain.Movie;
import net.seedboxer.core.domain.TvShow;
import net.seedboxer.core.type.Quality;
import net.seedboxer.sources.parser.ContentParser;
import net.seedboxer.sources.parser.MovieParser;
import net.seedboxer.sources.parser.ParserManager;
import net.seedboxer.sources.parser.TvShowParser;
import net.seedboxer.sources.type.MatchableItem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 * @author The Sultan
 */

@RunWith(MockitoJUnitRunner.class)
public class ParserManagerTest {
    private ParserManager parserManager;
    
    private static final String movieInput1 = "Oz the Great and Powerful 2013 1080p BluRay DTS-HDMA 7 1 x264-LEGi0N";
    
    private static final String tvShowInput1 = "Almost Naked Animals S03E09 A Luck Be A Robot 720p HDTV x264-DEADPOOL";

    private static final String tvShowInput2 = "The Half Hour S02E07 Jonah Ray 720p HDTV x264-YesTV";

    private static final String sampleUrl = "http://www.example.com";
    private MatchableItem match1;
    private MatchableItem match2;
    private MatchableItem match3;
    
    private Movie movie = new Movie("Oz the Great and Powerful", 2013, Quality.FULLHD);
    private TvShow tvShow1 = new TvShow("Almost Naked Animals", 3, 9, Quality.HD);
    private TvShow tvShow2 = new TvShow("The Half Hour", 2, 7, Quality.HD);
    private List<Content> expectedParsedContent = Arrays.asList(movie, tvShow1, tvShow2);
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Before
    public void setUp() throws Exception {
	parserManager = new ParserManager();
	List<ContentParser> parsers = new ArrayList();
	parsers.add(new TvShowParser());
	parsers.add(new MovieParser());
	parserManager.setParsers(parsers);
	match1 = new MatchableItem(movieInput1, sampleUrl);
	match2 = new MatchableItem(tvShowInput1, sampleUrl);
	match3 = new MatchableItem(tvShowInput2, sampleUrl);
    }
    
    @Test
    public void testTvShowParsing() throws Exception {
	List<MatchableItem> matchableItems = Arrays.asList(match1, match2, match3);
	List<Content> parsedContents = parserManager.parseMatchableItems(matchableItems);
	for(int i=0; i<parsedContents.size();i++){
	    Assert.assertEquals(expectedParsedContent.get(i),parsedContents.get(i));
	}
	
	
    }
}
