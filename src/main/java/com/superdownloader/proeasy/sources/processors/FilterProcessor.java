/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.superdownloader.proeasy.sources.processors;

import com.superdownloader.proeasy.sources.domain.Content;
import com.superdownloader.proeasy.sources.filters.FilterManager;
import java.util.List;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.slf4j.Logger;
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
    
    @Override
    public void process(Exchange exchange) throws Exception {
        Message msg = exchange.getIn();
        List<Content> parsedContent = (List<Content>) msg.getBody();
        filterManager.filterContent(parsedContent);
        
    }
}
