/*******************************************************************************
 * ConfigurationUtils.java
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
package com.seedboxer.seedboxer.core.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.seedboxer.seedboxer.core.domain.UserConfiguration;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
public class ConfigurationUtils {

	/**
	 * Transform a collection of {@link UserConfiguration} to a Map
	 * @param userConfig
	 * @return
	 */
	public static Map<String, Object> toMap(List<UserConfiguration> userConfig) {
		Map<String, Object> map = new HashMap<String, Object>();

		for (UserConfiguration conf : userConfig) {
			map.put(conf.getName(), conf.getValue());
		}
		return map;
	}

}
