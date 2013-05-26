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
package net.seedboxer.sources.parser;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
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

import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author The Sultan
 */
public class ParserManagerTest {
	
    private ParserManager parserManager;

    private static final String sampleUrl = "http://www.example.com";
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Before
    public void setUp() throws Exception {
		parserManager = new ParserManager();
		List<ContentParser> parsers = new ArrayList();
		parsers.add(new TvShowParser());
		parsers.add(new MovieParser());
		parserManager.setParsers(parsers);
    }
    
    @Test
    public void shouldParseOneMovieAndTwoTvShowsWhenParseTwoInputsOfTvSHowsAndOneOfMovie() throws Exception {
        String movieInput1 = "Oz the Great and Powerful 2013 1080p BluRay DTS-HDMA 7 1 x264-LEGi0N";
        String tvShowInput1 = "Almost Naked Animals S03E09 A Luck Be A Robot 720p HDTV x264-DEADPOOL";
        String tvShowInput2 = "The Half Hour S02E07 Jonah Ray 720p HDTV x264-YesTV";
        List<MatchableItem> matchableItems = Arrays.asList(getMatchItem(movieInput1), getMatchItem(tvShowInput1), getMatchItem(tvShowInput2));
        
        Movie movie = new Movie("Oz the Great and Powerful", 2013, Quality.FULLHD);
        TvShow tvShow1 = new TvShow("Almost Naked Animals", 3, 9, Quality.HD);
        TvShow tvShow2 = new TvShow("The Half Hour", 2, 7, Quality.HD);
    	List<Content> expectedParsedContent = Arrays.asList(movie, tvShow1, tvShow2);  	
    	
		List<Content> parsedContents = parserManager.parseMatchableItems(matchableItems);
		assertEquals(expectedParsedContent, parsedContents);
    }

	private MatchableItem getMatchItem(String input)
			throws MalformedURLException {
		return new MatchableItem(input, sampleUrl);
	}
    
}
