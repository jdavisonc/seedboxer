/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.superdownloader.proeasy.sources.parser;

import com.superdownloader.proeasy.sources.domain.Content;
import com.superdownloader.proeasy.sources.type.MatchableItem;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Farid
 */
public class ParserManager {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ParserManager.class);
    private List<ContentParser> parsers = new ArrayList<ContentParser>();

    public List<ContentParser> getParsers() {
        return parsers;
    }

    public void setParsers(List<ContentParser> parsers) {
        this.parsers = parsers;
    }
    
    public List<Content> parseMatchableItems(List<MatchableItem> items){
        List<Content> parsedContentList = new ArrayList<Content>();
        for(MatchableItem item : items){
            int count = parsedContentList.size();
            for(ContentParser parser : parsers){
                Content parsedContent = parser.parse(item);
                if(parsedContent != null){
                    parsedContentList.add(parsedContent);
                    //LOGGER.info("P:"+item.getTitle()+"-->"+parsedContent.getName()+parsedContent.getClass().getSimpleName());
                    break;
                }
                
            }
            //if(count == parsedContentList.size())
                //LOGGER.info("NP:"+item.getTitle());
        }
        return parsedContentList;
    }
    
    
}
