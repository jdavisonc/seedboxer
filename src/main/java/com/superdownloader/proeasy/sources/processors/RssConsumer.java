/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.superdownloader.proeasy.sources.processors;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.superdownloader.proeasy.sources.type.MatchableItem;
import java.util.ArrayList;
import java.util.List;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

/**
 *
 * @author Farid
 */
@Component
public class RssConsumer implements Processor{

    @Override
    public void process(Exchange exchange) throws Exception {
        Message msg = exchange.getIn();
        SyndFeed feed = (SyndFeed) msg.getBody();
        List<SyndEntry > entries = feed.getEntries();
        List<MatchableItem> items = new ArrayList<MatchableItem>();
        for(SyndEntry  entry : entries){
            items.add(new MatchableItem(entry.getTitle(),entry.getLink()));
        }
        exchange.getOut().setBody(items, items.getClass());
    }
}
