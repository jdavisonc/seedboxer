/*******************************************************************************
 * IMDBRequestProcessor.java
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

package net.seedboxer.seedboxer.sources.thirdparty.imdb;

import net.seedboxer.seedboxer.core.domain.Configuration;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;



/**
 * @author Jorge Davison (jdavisonc)
 *
 */
@Component("imdbRequestProcessor")
public class IMDBRequestProcessor implements Processor {

	private String imdbUrl = "http://www.imdb.com/list/export?list_id=%s&amp;author_id=%s";

	@Override
	public void process(Exchange exchange) throws Exception {
		Message msg = exchange.getIn();
		String author = (String) msg.getHeader(Configuration.IMDB_AUTHOR);
		String list = (String) msg.getHeader(Configuration.IMDB_LIST);

		msg.setHeader(Exchange.HTTP_URI, String.format(imdbUrl, list, author));
		msg.setBody(null);
	}

}
