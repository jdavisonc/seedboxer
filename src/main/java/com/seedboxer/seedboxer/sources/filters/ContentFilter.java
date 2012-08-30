/*******************************************************************************
 * ContentFilter.java
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
package com.seedboxer.seedboxer.sources.filters;

import com.seedboxer.seedboxer.core.domain.Content;

/**
 *
 * @author The-Sultan
 */

public abstract class ContentFilter<T extends Content> {


	public final Boolean filterIfPossible(Content userContent, Content parsedContent){
		Class<T> type = getType();
		if(type.isInstance(userContent) && type.isInstance(parsedContent)) {
			return applyFilter((type.cast(userContent)),type.cast(parsedContent));
		} else {
			return null;
		}
	}

	public abstract Class<T> getType();

	protected abstract boolean applyFilter(T content1, T content2);

}
