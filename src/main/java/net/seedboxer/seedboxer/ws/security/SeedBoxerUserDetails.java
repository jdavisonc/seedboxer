/*******************************************************************************
 * SeedBoxerUserDetails.java
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

import java.util.ArrayList;
import java.util.Collection;

import net.seedboxer.seedboxer.ws.security.SeedBoxerGrantedAuthority.SeedBoxerAuthority;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
public class SeedBoxerUserDetails extends User {

	private static final long serialVersionUID = -3257428259960598654L;

	private final net.seedboxer.seedboxer.core.domain.User user;

	public SeedBoxerUserDetails(net.seedboxer.seedboxer.core.domain.User user) {
		super(user.getUsername(), user.getPassword(), createAuthorities(user));
		this.user = user;
	}

	private static Collection<? extends GrantedAuthority> createAuthorities(net.seedboxer.seedboxer.core.domain.User user) {
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SeedBoxerGrantedAuthority(SeedBoxerAuthority.LEECHER));
		if (user.isAdmin()) {
			authorities.add(new SeedBoxerGrantedAuthority(SeedBoxerAuthority.ADMIN));
		}
		return authorities;
	}

	public net.seedboxer.seedboxer.core.domain.User getUser() {
		return user;
	}

}
