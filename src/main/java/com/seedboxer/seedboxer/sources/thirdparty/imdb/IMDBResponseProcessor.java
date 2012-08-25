/*******************************************************************************
 * IMDBResponseProcessor.java
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

package com.seedboxer.seedboxer.sources.thirdparty.imdb;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author harley
 *
 */
@Component("imdbResponseProcessor")
public class IMDBResponseProcessor implements Processor {

	private static final Logger LOGGER = LoggerFactory.getLogger(IMDBResponseProcessor.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		// TODO Generate a list of Content with the result of calling IMDB and user configuration
		List<List<String>> imdbResults = (List<List<String>>) exchange.getIn().getBody();

		for (List<String> result : imdbResults) {
			LOGGER.debug("IMDB Content {}", result.get(5));
		}

	}

}
