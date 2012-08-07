
package com.superdownloader.proeasy.sources.parser;

import com.superdownloader.proeasy.sources.domain.Content;
import com.superdownloader.proeasy.sources.type.MatchableItem;

/**
 *
 * @author Farid
 */

public abstract class ContentParser<T extends Content> {
    
    public T parse(MatchableItem item){
        T parsedContent = parse(item.getTitle());
        if(parsedContent != null)
            parsedContent.setMatchableItem(item);
        return parsedContent;
    }
    
    protected abstract T parse(String input);
   
}
