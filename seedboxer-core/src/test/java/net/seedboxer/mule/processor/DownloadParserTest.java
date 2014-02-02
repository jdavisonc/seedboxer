/*******************************************************************************
 * DownloadParserTest.java
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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import net.seedboxer.core.domain.Configuration;
import net.seedboxer.sources.parser.ParserManager;
import net.seedboxer.sources.type.MatchableItem;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DownloadParserTest {

	private static final String FILE_NAME = "FileName";

	@InjectMocks
	private final DownloadParser parser = new DownloadParser();
	
	@Mock
	private ParserManager parserManager;

	@Mock
	private Exchange exchange;

	@Mock
	private Message msg;
	
	@Before
	public void setUp() throws Exception {
		when(exchange.getIn()).thenReturn(msg);
		when(msg.getHeader(Configuration.FILE_NAME, String.class)).thenReturn(FILE_NAME);
	}

	@Test
	public void shouldParserFileNames() throws Exception {
		parser.process(exchange);
		
		verify(parserManager).parseMatchableItem(any(MatchableItem.class));
	}

}
