/*******************************************************************************
 * DownloadTransferListenerTest.java
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
package net.seedboxer.mule.processor.transfer;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import net.seedboxer.core.domain.DownloadSession;
import net.seedboxer.core.logic.DownloadsSessionManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * 
 * @author Jorge Davison
 */
@RunWith(MockitoJUnitRunner.class)
public class DownloadTransferListenerTest {

	private static final String FILENAME = "file.txt";

	private DownloadTransferListener listener;

	@Mock
	private DownloadsSessionManager downloadsSessionManager;

	@Mock
	private DownloadSession session;

	@Before
	public void setUp() throws Exception {
		listener = new DownloadTransferListener();
		listener.setDownloadsSessionManager(downloadsSessionManager);

		when(downloadsSessionManager.getSession(FILENAME)).thenReturn(session);
	}

	@Test
	public void shouldNotPublishByteTransferedWhenAreNotSufficient() throws Exception {
		listener.bytesTransfered(FILENAME, 1000);
		verify(session, never()).setTransferredInMbs(1000);
	}

	@Test
	public void shouldPublishByteTransferedWhenAreMultipleOfMB() throws Exception {
		listener.bytesTransfered(FILENAME, 3145728);
		verify(session).setTransferredInMbs(3);
	}

}
