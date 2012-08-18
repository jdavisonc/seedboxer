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

import com.seedboxer.seedboxer.sources.domain.Content;

/**
 *
 * @author The-Sultan
 */

public abstract class ContentFilter<T extends Content> {

	private Class<T> _type;

	public void setType(Class<T> _type){
		this._type = _type;
	}

	public Class<T> getType(){
		return this._type;
	}

	public final Boolean filterIfPossible(Content userContent, Content parsedContent){
		//if(content.is)
			if(_type.isInstance(userContent) && _type.isInstance(parsedContent)) {
				return applyFilter((_type.cast(userContent)),_type.cast(parsedContent));
			}
			else
				return null;

	}

	protected abstract boolean applyFilter(T content1, T content2);

}
