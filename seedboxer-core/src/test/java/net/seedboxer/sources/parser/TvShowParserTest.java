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

import com.google.common.collect.Sets;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.UUID;
import net.seedboxer.core.domain.Content;
import net.seedboxer.core.domain.TvShow;
import net.seedboxer.core.type.Quality;
import net.seedboxer.sources.type.MatchableItem;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Jorge Davison (jdavisonc)
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

  @Test
  public void shouldBeSameTVShow() throws MalformedURLException {
    String tvShowInput2 = "Game.of.Thrones.S03E08.720p.HDTV.x264.AC3-Riding High";
    TvShow tvShow2 = tvShowParser.parse(getMatchItem(tvShowInput2));
    assertEquals(getExpectedTvShow(), tvShow2);

    String tvShowInput3 = "Game.of.Thrones.S03E08.720p.HDTV.x264.AC3-Riding High";
    final TvShow tvShow3 = tvShowParser.parse(getMatchItem(tvShowInput3));
    assertEquals(getExpectedTvShow(), tvShow3);

    String tvShowInput4 = "Game of Thrones S03E08 720p HDTV x264 AC3-Riding High";
    final TvShow tvShow4 = tvShowParser.parse(getMatchItem(tvShowInput4));
    assertEquals(getExpectedTvShow(), tvShow4);

    final HashSet<Content> tvShows = Sets.<Content>newHashSet(tvShow2, tvShow3, tvShow4);
    assertEquals(1, tvShows.size());
  }

  private MatchableItem getMatchItem(String input) throws MalformedURLException {
    return new MatchableItem(input, "http://www.example.com/" + UUID.randomUUID().toString());
  }

  private TvShow getExpectedTvShow() {
    return new TvShow("Game of Thrones", 3, 8, Quality.HD);
  }

}
