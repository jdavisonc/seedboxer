/*******************************************************************************
 * TvShowParserTest.java
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

import net.seedboxer.core.domain.TvShow;
import net.seedboxer.core.type.Quality;
import net.seedboxer.sources.parser.TvShowParser;
import net.seedboxer.sources.type.MatchableItem;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
public class TvShowParserTest {

	private TvShowParser tvShowParser;

	@Before
	public void setUp() throws Exception {
		tvShowParser = new TvShowParser();
	}

	@Test
	public void shouldReturnTvShowWhenParseTypeOfInput1() throws MalformedURLException {
		String tvShowInput1 = "Game.of.Thrones.S03E08.720p.HDTV.x264.AC3-Riding High";
		assertEquals(getExpectedTvShow(), tvShowParser.parse(getMatchItem(tvShowInput1)));
	}
	
	@Test
	public void shouldReturnTvShowWhenParseTypeOfInput2() throws MalformedURLException {
		String tvShowInput2 = "Game.of.Thrones.S03E08.720p.HDTV.x264.AC3-Riding High";
		assertEquals(getExpectedTvShow(), tvShowParser.parse(getMatchItem(tvShowInput2)));
	}
	
	@Test
	public void shouldReturnTvShowWhenParseTypeOfInput3() throws MalformedURLException {
		String tvShowInput3 = "Game.of.Thrones.S03E08.720p.HDTV.x264.AC3-Riding High";
		assertEquals(getExpectedTvShow(), tvShowParser.parse(getMatchItem(tvShowInput3)));
	}
	
	@Test
	public void shouldReturnTvShowWhenParseInputWithoutDots() throws MalformedURLException {
		String tvShowInput4 = "Game of Thrones S03E08 720p HDTV x264 AC3-Riding High";
		assertEquals(getExpectedTvShow(), tvShowParser.parse(getMatchItem(tvShowInput4)));
	}

	private MatchableItem getMatchItem(String tvShowInput3)
			throws MalformedURLException {
		return new MatchableItem(tvShowInput3, "http://www.example.com");
	}

	private TvShow getExpectedTvShow() {
		return new TvShow("Game of Thrones", 3, 8, Quality.HD);
	}

}
