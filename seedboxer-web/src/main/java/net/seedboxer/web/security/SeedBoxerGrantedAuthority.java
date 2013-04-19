/*******************************************************************************
 * SeedBoxerGrantedAuthority.java
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
package net.seedboxer.web.security;

import org.springframework.security.core.GrantedAuthority;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
public class SeedBoxerGrantedAuthority implements GrantedAuthority {

	private static final long serialVersionUID = 2939234974542921581L;

	private final SeedBoxerAuthority authority;

	public enum SeedBoxerAuthority {
		ADMIN, LEECHER;
	}

	public SeedBoxerGrantedAuthority(SeedBoxerAuthority authority) {
		this.authority = authority;
	}

	@Override
	public String getAuthority() {
		return authority.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((authority == null) ? 0 : authority.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SeedBoxerGrantedAuthority other = (SeedBoxerGrantedAuthority) obj;
		if (authority != other.authority)
			return false;
		return true;
	}

}
