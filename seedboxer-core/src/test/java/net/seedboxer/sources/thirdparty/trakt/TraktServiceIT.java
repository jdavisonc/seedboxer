/*******************************************************************************
 * TraktServiceTest.java
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
package net.seedboxer.sources.thirdparty.trakt;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
public class TraktServiceIT {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TraktServiceIT.class);

	private static final String AUTH_KEY = "e2c47ad00b7ad043c455cb9a8978a9fb";
	private TraktService traktService;

	@Before
	public void setUp() throws Exception {
		traktService = new TraktService(AUTH_KEY);
	}

	@Test
	public void shouldConnectAndReturnResultWhenSearchForTVShow() throws Exception {
		String url = traktService.searchTvShow("The Big Bang Theory");
		LOGGER.error(url);
		assertNotNull(url);
	}

}
