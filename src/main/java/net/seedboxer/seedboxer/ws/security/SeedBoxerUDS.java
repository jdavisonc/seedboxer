/*******************************************************************************
 * SeedBoxerUDS.java
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

import net.seedboxer.seedboxer.core.domain.User;
import net.seedboxer.seedboxer.core.logic.UsersController;
import net.seedboxer.seedboxer.ws.security.SeedBoxerGrantedAuthority.SeedBoxerAuthority;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
@Component
public class SeedBoxerUDS implements UserDetailsService {

	@Autowired
	private UsersController userController;

	public void setUserController(UsersController userController) {
		this.userController = userController;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			User u = userController.getUser(username);
			Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			authorities.add(new SeedBoxerGrantedAuthority(SeedBoxerAuthority.LEECHER));
			if (u.isAdmin()) {
				authorities.add(new SeedBoxerGrantedAuthority(SeedBoxerAuthority.ADMIN));
			}

			return new org.springframework.security.core.userdetails.User(username, u.getPassword(), authorities);
		} catch (IllegalArgumentException e) {
			throw new UsernameNotFoundException("User "+username+" not found", e);
		}
	}

}
