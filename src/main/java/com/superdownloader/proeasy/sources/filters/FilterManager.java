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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Farid
 */
@Component
public class FilterManager {
    
    @Autowired
    private ContentDao contentDao;
    
    
    private List<ContentFilter> filters;

    public List<ContentFilter> getFilters() {
        return filters;
    }

    @Autowired
    public void setFilters(List<ContentFilter> filters) {
        this.filters = filters;
    }
    
    private List<Content> getAllContent(boolean history){
        List<Content> userContent = new ArrayList<Content>();
        for(ContentFilter filter : filters){
            userContent.addAll(contentDao.getContentHistory(filter.getType(),history));
        }
        return userContent;
        
    }
    
    public Map<Content,List<User>> filterContent(List<Content> parsedContentList){
        List<Content> userContentList = getAllContent(false);
        Map<Content,List<User>> mappedContent = mapContentWithUsers(userContentList, parsedContentList);
        mappedContent = filterContentWithHistory(mappedContent);
        
        return mappedContent;
    }
    
    /**
     * Maps the newly parsed content with the users, using the configured 
     * content for each user on the Database.
     * 
     * @param userContentList
     * @param parsedContentList
     * @return 
     */
    private Map<Content,List<User>> mapContentWithUsers(List<Content> userContentList, List<Content> parsedContentList){
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
    
    /**
     * After having the content mapped to each user, this method filters the 
     * users for each content using the user's history, if history content is
     * equal to a matchedContent, then the user is removed.
     * 
     * @param mappedContent
     * @return 
     */
    private Map<Content,List<User>> filterContentWithHistory(Map<Content,List<User>> mappedContent){
        List<Content> contentInHistory = new ArrayList<Content>();
        for(Content alreadyMappedContent : mappedContent.keySet()){
            List<User> usersThatAlreadyHaveThisContent = new ArrayList<User>();
            for(User user : mappedContent.get(alreadyMappedContent)){
                List<Content> historyContent = new ArrayList<Content>();
                historyContent.addAll(contentDao.
                        getHistoryContentFilteredByNameAndUser(alreadyMappedContent.getClass(),
                        alreadyMappedContent.getName(),
                        user));
                for(Content content : historyContent){
                    if(alreadyMappedContent.equals(content)){
                        usersThatAlreadyHaveThisContent.add(user);
                        break;
                    }
                }
            }
            if(usersThatAlreadyHaveThisContent.size() == mappedContent.get(alreadyMappedContent).size())
                contentInHistory.add(alreadyMappedContent);
            else{
                for(User user : usersThatAlreadyHaveThisContent){
                    mappedContent.get(alreadyMappedContent).remove(user);
                }
            }
        }
        for(Content content : contentInHistory){
            mappedContent.remove(content);
        }
        return mappedContent;
    }
    
}
