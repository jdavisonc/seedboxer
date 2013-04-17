/*******************************************************************************
 * Token.java
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
package net.seedboxer.core.domain;

import org.apache.commons.lang.RandomStringUtils;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
public class Token {

	public static final int TOKEN_LENGTH = 8;

	public static String generate() {
		return RandomStringUtils.randomAlphanumeric(TOKEN_LENGTH);
	}

	public static boolean validate(String token) {
		return (token != null) && (token.length() == TOKEN_LENGTH);
	}

}
