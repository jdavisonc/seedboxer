/*******************************************************************************
 * DownloadHistoryTest.java
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
package net.seedboxer.mule.processor;

import net.seedboxer.core.domain.Configuration;
import net.seedboxer.core.domain.Content;
import net.seedboxer.core.domain.User;
import net.seedboxer.core.logic.ContentManager;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class DownloadHistoryTest {

	@InjectMocks
	private final DownloadHistory downloadHistory = new DownloadHistory();
	
	@Mock
	private ContentManager contentManager;

	@Mock
	private Exchange exchange;
	
	@Mock
	private Message message;

	@Mock
	private Content content;

	@Mock
	private User user;

	@Test
	public void shouldSaveMsgContentInHIstoryWhenReceivesAMsg() throws Exception {
		Mockito.when(exchange.getIn()).thenReturn(message);
		Mockito.when(message.getHeader(Configuration.USER, User.class)).thenReturn(user);
		Mockito.when(message.getHeader(Configuration.CONTENT, Content.class)).thenReturn(content);
		
		downloadHistory.process(exchange);
		Mockito.verify(contentManager).saveInHistory(content, user);
	}
}
