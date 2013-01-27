/*******************************************************************************
 * SeedBoxerUserDetailsTest.java
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

import java.util.ArrayList;
import java.util.List;

import junitx.framework.ListAssert;
import net.seedboxer.seedboxer.core.domain.User;
import net.seedboxer.seedboxer.ws.security.SeedBoxerGrantedAuthority.SeedBoxerAuthority;

import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
public class SeedBoxerUserDetailsTest {

	private static final String PASSWORD = "seedboxer";
	private static final String USERNAME = "seedboxer";

	@Test
	public void shouldCreateAuthoritiesWhenUserIsAdmin() {
		User user = new User();
		user.setAdmin(true);
		user.setPassword(PASSWORD);
		user.setUsername(USERNAME);

		SeedBoxerUserDetails ud = new SeedBoxerUserDetails(user);
		assertEquals(USERNAME, ud.getUsername());
		assertEquals(PASSWORD, ud.getPassword());
		assertEquals(user, ud.getUser());

		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SeedBoxerGrantedAuthority(SeedBoxerAuthority.LEECHER));
		authorities.add(new SeedBoxerGrantedAuthority(SeedBoxerAuthority.ADMIN));
		ListAssert.assertEquals(authorities, new ArrayList<GrantedAuthority>(ud.getAuthorities()));
	}

	@Test
	public void shouldCreateAuthoritiesWhenUserIsNotAdmin() {
		User user = new User();
		user.setAdmin(false);
		user.setPassword(PASSWORD);
		user.setUsername(USERNAME);

		SeedBoxerUserDetails ud = new SeedBoxerUserDetails(user);
		assertEquals(USERNAME, ud.getUsername());
		assertEquals(PASSWORD, ud.getPassword());
		assertEquals(user, ud.getUser());

		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SeedBoxerGrantedAuthority(SeedBoxerAuthority.LEECHER));
		ListAssert.assertEquals(authorities, new ArrayList<GrantedAuthority>(ud.getAuthorities()));
	}

}
