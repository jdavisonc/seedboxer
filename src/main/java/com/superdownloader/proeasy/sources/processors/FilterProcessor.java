/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.superdownloader.proeasy.sources.processors;

import com.superdownloader.proeasy.core.domain.User;
import com.superdownloader.proeasy.mule.processor.DownloadReceiver;
import com.superdownloader.proeasy.sources.domain.Content;
import com.superdownloader.proeasy.sources.filters.FilterManager;
import com.superdownloader.proeasy.sources.type.DownloadableItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 *
 * @author Farid
 */
@Component
public class FilterProcessor implements Processor{

    @Autowired
    FilterManager filterManager;
    
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(DownloadReceiver.class);
    
    @Override
    public void process(Exchange exchange) throws Exception {
        Message msg = exchange.getIn();
        List<Content> parsedContent = (List<Content>) msg.getBody();
        Map<Content, List<User>> mappedContent = filterManager.filterContent(parsedContent);
        List<DownloadableItem> downloadbleItems = new ArrayList<DownloadableItem>();
        for(Content content : mappedContent.keySet()){
            LOGGER.info(content + "-->"+mappedContent.get(content));
            LOGGER.info(content.getMatchableItem().getUrl().toString());
            downloadbleItems.add(new DownloadableItem(content, mappedContent.get(content)));
        }
        exchange.getOut().setBody(downloadbleItems);
        
    }
}
