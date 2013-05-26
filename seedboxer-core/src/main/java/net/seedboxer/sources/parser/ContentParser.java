/*******************************************************************************
 * ContentParser.java
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
package net.seedboxer.sources.parser;

import net.seedboxer.core.domain.Content;
import net.seedboxer.sources.type.MatchableItem;

/**
 * This class is the superclass for all specific content parsers.
 * The parse method on the superclass is called for every parser
 * on the context, and returns a specific content class.
 * For instance, the TvShowParser will return a TvShowContent if
 * Te item being parsed refers to a TvShow, null otherwise.
 * This way, you can interact with all the parsers just by calling
 * the method on the superclass, and will get in return the specific
 * content.
 * 
 * @author The-Sultan
 */

public abstract class ContentParser<T extends Content> {
    
    
    public T parse(MatchableItem item){
        T parsedContent = parse(item.getTitle());
        if(parsedContent != null)
            parsedContent.setMatchableItem(item);
        return parsedContent;
    }
    
    protected abstract T parse(String input);

	protected String getName(String group) {
		String name = group;
		if (name.indexOf('.') != -1) {
			name = name.replace('.', ' ');
		}
		return name.trim();
	}
}
