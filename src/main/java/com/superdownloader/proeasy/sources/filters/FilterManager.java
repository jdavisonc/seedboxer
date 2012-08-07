/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.superdownloader.proeasy.sources.filters;

import com.superdownloader.proeasy.core.domain.User;
import com.superdownloader.proeasy.core.persistence.ContentDao;
import com.superdownloader.proeasy.sources.domain.Content;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Farid
 */
public class FilterManager {
    
    @Autowired
    private ContentDao genericDao;
    
    private List<ContentFilter> filters;

    public List<ContentFilter> getFilters() {
        return filters;
    }

    public void setFilters(List<ContentFilter> filters) {
        this.filters = filters;
    }
    
    private List<Content> getAllContent(){
        List<Content> userContent = new ArrayList<Content>();
        for(ContentFilter filter : filters){
            userContent.addAll(genericDao.getContentHistory(filter.getType(),false));
        }
        return userContent;
        
    }
    
    public Map<Content,List<User>> filterContent(List<Content> parsedContentList){
        List<Content> userContentList = getAllContent();
        Map<Content,List<User>> mappedContent = new HashMap<Content,List<User>>();
        for(Content parsedContent : parsedContentList){
            if(mappedContent.containsKey(parsedContent))
                continue;
            List<User> usersWantingThisParsedContent = new ArrayList<User>();
            for(Content userContent : userContentList){
                for(ContentFilter filter : filters){
                    Boolean wantedContent = filter.filterIfPossible(userContent, parsedContent);
                    if(wantedContent != null && wantedContent){
                        usersWantingThisParsedContent.add(userContent.getUser());
                    }
                }
            }
            if(!usersWantingThisParsedContent.isEmpty()){
                mappedContent.put(parsedContent,usersWantingThisParsedContent);
            }
            
        }
        return mappedContent;
    }
    
    
    
    
}
