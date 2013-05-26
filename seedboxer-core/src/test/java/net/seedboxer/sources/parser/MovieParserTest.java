/*******************************************************************************
 * MovieParserTest.java
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

import net.seedboxer.core.domain.Movie;
import net.seedboxer.core.type.Quality;
import net.seedboxer.sources.parser.MovieParser;
import net.seedboxer.sources.type.MatchableItem;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
public class MovieParserTest {

	private MovieParser movieParser;

	@Before
	public void setUp() throws Exception {
		movieParser = new MovieParser();
	}

	@Test
	public void shouldReturnMovieWhenParseMovieTypeOfInput1() throws MalformedURLException {
		String movieInput = "Oz the Great and Powerful 2013 1080p BluRay DTS-HDMA 7 1 x264-LEGi0N";
		assertEquals(getExpectedMovie(), movieParser.parse(getMatchItem(movieInput)));
	}
	
	@Test
	public void shouldReturnMovieWhenParseMovieInputWithDots() throws MalformedURLException {
		String movieInput = "Oz.the.Great.and.Powerful.2013.1080p.BluRay.DTS-HDMA.7.1.x264-LEGi0N";
		assertEquals(getExpectedMovie(), movieParser.parse(getMatchItem(movieInput)));
	}

	private MatchableItem getMatchItem(String movieInput)
			throws MalformedURLException {
		return new MatchableItem(movieInput, "http://www.example.com");
	}

	private Movie getExpectedMovie() {
		return new Movie("Oz the Great and Powerful", 2013, Quality.FULLHD);
	}
}
