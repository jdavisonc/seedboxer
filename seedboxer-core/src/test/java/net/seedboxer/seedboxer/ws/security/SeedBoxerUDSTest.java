/*******************************************************************************
 * SeedBoxerUDSTest.java
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
package net.seedboxer.seedboxer.ws.security;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import net.seedboxer.seedboxer.core.domain.User;
import net.seedboxer.seedboxer.core.logic.UsersController;

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
public class SeedBoxerUDSTest {

	private static final String USERNAME = "seedboxer";
	private static final String APIKEY = "1234";
	private static final String PASSWORD = "seedboxer";

	@Mock
	private UsersController userController;

	private SeedBoxerUDS seedBoxerUDS;

	private User user;

	@Before
	public void setUp() throws Exception {
		seedBoxerUDS = new SeedBoxerUDS();
		seedBoxerUDS.setUserController(userController);

		user = new User();
		user.setAdmin(true);
		user.setPassword(PASSWORD);
		user.setUsername(USERNAME);

		when(userController.getUser(USERNAME)).thenReturn(user);
		when(userController.getUserFromAPIKey(APIKEY)).thenReturn(user);

	}

	@Test
	public void shouldReturnSBUserDetailed() throws Exception {
		SeedBoxerUserDetails userDetails = (SeedBoxerUserDetails) seedBoxerUDS.loadUserByUsername(USERNAME);
		assertEquals(user, userDetails.getUser());
	}

	@Test
	public void shouldReturnSBUserDetailedByApiKey() throws Exception {
		SeedBoxerUserDetails userDetails = (SeedBoxerUserDetails) seedBoxerUDS.loadUserByAPIKey(APIKEY);
		assertEquals(user, userDetails.getUser());
	}


}
