/*******************************************************************************
 * GroupedExchangeAggregationStrategy.java
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
package net.seedboxer.mule.aggregate;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.springframework.stereotype.Component;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
@Component
public class GroupedExchangeAggregationStrategy implements AggregationStrategy {

    @Override
	@SuppressWarnings("unchecked")
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        List<Exchange> list;
        Exchange answer = oldExchange;

        if (oldExchange == null) {
            answer = new DefaultExchange(newExchange);
            list = new ArrayList<Exchange>();
            answer.setProperty(Exchange.GROUPED_EXCHANGE, list);
            answer.setProperty(Exchange.SPLIT_SIZE, newExchange.getProperty(Exchange.SPLIT_SIZE));
        } else {
            list = oldExchange.getProperty(Exchange.GROUPED_EXCHANGE, List.class);
        }

        if (newExchange != null) {
            list.add(newExchange);
        }
        return answer;
    }

}
