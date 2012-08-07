
package com.superdownloader.proeasy.sources.filters;

import com.superdownloader.proeasy.sources.domain.Content;

/**
 *
 * @author Farid
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
