/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.superdownloader.proeasy.sources.processors;

import com.superdownloader.proeasy.sources.parser.ParserManager;
import com.superdownloader.proeasy.sources.type.MatchableItem;
import java.util.List;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 *
 * @author Farid
 */
@Component
public class ParserProcessor implements Processor{

    @Autowired
    ParserManager parserManager;
    
    @Override
    public void process(Exchange exchange) throws Exception {
        Message msg = exchange.getIn();
        List<MatchableItem> items = (List<MatchableItem>) msg.getBody();
        exchange.getOut().setBody(parserManager.parseMatchableItems(items));
    }
}
