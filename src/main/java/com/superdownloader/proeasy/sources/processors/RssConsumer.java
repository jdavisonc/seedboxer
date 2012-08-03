/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.superdownloader.proEasy.sources.processors;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import java.util.List;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author Farid
 */
@Component
public class RssConsumer implements Processor{

    private static final Logger LOGGER = LoggerFactory.getLogger(RssConsumer.class);
    
    @Override
    public void process(Exchange exchange) throws Exception {
        Message msg = exchange.getIn();
        SyndFeed feed = (SyndFeed) msg.getBody();
        List<SyndEntry > entries = feed.getEntries();
        for(SyndEntry  entry : entries){
            LOGGER.info(entry.getTitle());
        }
        //LOGGER.info();


    }
}
