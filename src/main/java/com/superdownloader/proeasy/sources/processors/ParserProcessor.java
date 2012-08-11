/*******************************************************************************
 * ParserProcessor.java
 * 
 * Copyright (c) 2012 SeedBoxer Team.
 * 
 * This file is part of SeedBoxer.
 * 
 * SeedBoxer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * SeedBoxer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SeedBoxer.  If not, see <http ://www.gnu.org/licenses/>.
 ******************************************************************************/
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
