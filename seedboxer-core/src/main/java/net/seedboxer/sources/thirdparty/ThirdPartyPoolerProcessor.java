/*******************************************************************************
 * ThirdPartyPoolerProcessor.java
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

package net.seedboxer.sources.thirdparty;

import java.util.List;

import net.seedboxer.core.domain.Configuration;
import net.seedboxer.core.domain.User;
import net.seedboxer.core.persistence.UsersDao;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author Jorge Davison (jdavisonc)
 *
 */
@Component
public class ThirdPartyPoolerProcessor implements Processor {

	@Autowired
	private UsersDao usersDao;

	@Override
	public void process(Exchange exchange) throws Exception {
		List<User> users = usersDao.getUserWithConfig(Configuration.THIRD_PARTY);
		exchange.getIn().setBody(users);
	}

}
