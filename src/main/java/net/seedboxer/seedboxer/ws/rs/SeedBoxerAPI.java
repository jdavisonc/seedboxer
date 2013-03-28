/*******************************************************************************
 * SeedBoxerAPI.java
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
package net.seedboxer.seedboxer.ws.rs;

import net.seedboxer.seedboxer.core.domain.User;
import net.seedboxer.seedboxer.ws.security.SeedBoxerUserDetails;

import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
public abstract class SeedBoxerAPI {

	protected User getUser() {
		SeedBoxerUserDetails ud = (SeedBoxerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return ud.getUser();
	}

}
