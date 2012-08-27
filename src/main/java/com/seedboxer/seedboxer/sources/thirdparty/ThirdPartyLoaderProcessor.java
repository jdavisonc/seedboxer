/*******************************************************************************
 * ThirdPartyLoaderProcessor.java
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

package com.seedboxer.seedboxer.sources.thirdparty;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Joiner;
import com.seedboxer.seedboxer.core.domain.Configuration;
import com.seedboxer.seedboxer.core.domain.User;
import com.seedboxer.seedboxer.core.domain.UserConfiguration;
import com.seedboxer.seedboxer.core.persistence.UsersDao;
import com.seedboxer.seedboxer.core.util.ConfigurationUtils;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
@Component
public class ThirdPartyLoaderProcessor implements Processor {

	@Autowired
	private UsersDao usersDao;

	@Override
	public void process(Exchange exchange) throws Exception {
		Message msg = exchange.getIn();
		User user = (User) msg.getBody();
		List<UserConfiguration> userConfig = usersDao.getUserConfig(user.getId());

		// Load all user configuration in message
		msg.setHeaders(ConfigurationUtils.toMap(userConfig));
		msg.setHeader(Configuration.USER, user);

		// Set Third Party endpoint for routing slip
		ArrayList<String> thirdPartyEndpoints = new ArrayList<String>();
		String thirdParty = (String) msg.getHeader(Configuration.THIRD_PARTY);
		for (String type : thirdParty.split(",")) {
			thirdPartyEndpoints.add("direct:" + type);
		}
		msg.setHeader(Configuration.THIRD_PARTY, Joiner.on(',').join(thirdPartyEndpoints));
	}

}
