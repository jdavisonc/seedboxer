/*******************************************************************************
 * AggregateRssFeedEntryStrategy.java
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
package net.seedboxer.seedboxer.sources.processors.rss.aggregator;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.syndication.feed.synd.SyndEntryImpl;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
public class AggregateRssFeedEntryStrategy implements AggregationStrategy {

    protected final transient Logger LOGGER = LoggerFactory.getLogger(AggregateRssFeedEntryStrategy.class);

    @Override
    @SuppressWarnings("unchecked")
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if (oldExchange == null) {
        	ArrayList<SyndEntryImpl> list = new ArrayList<SyndEntryImpl>();
        	list.add((SyndEntryImpl) newExchange.getIn().getBody());
            newExchange.getIn().setBody(list);
        	return newExchange;
        }
		List<SyndEntryImpl> oldEntries = (List<SyndEntryImpl>) oldExchange.getIn().getBody();
        SyndEntryImpl newEntry = (SyndEntryImpl) newExchange.getIn().getBody();
        if (newEntry != null) {
        	oldEntries.add(newEntry);
        } else {
        	LOGGER.debug("Could not merge exchanges. One body was null.");
        }
        return oldExchange;
    }
}
