/*******************************************************************************
 * DownloadsSessionManagerTest.java
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
package net.seedboxer.core.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import net.seedboxer.core.type.Download;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
public class DownloadsSessionManagerTest {

	private static final Long USER_ID = 1L;
	private static final String FILENAME = "This.Is.A.Test-SEEDBOXER.avi";
	private DownloadsSessionManager manager;

	@Before
	public void setUp() throws Exception {
		manager = new DownloadsSessionManager();
	}

	@Test
	public void shouldAddSession() throws Exception {
		manager.addSession(USER_ID, FILENAME, 1000);
	}

	@Test
	public void shouldFindSession() throws Exception {
		manager.addSession(USER_ID, FILENAME, 1000);
		assertNotNull(manager.getSession("/some/path/that/finish/with/"+FILENAME));
	}

	@Test
	public void shouldGetSession() throws Exception {
		manager.addSession(USER_ID, FILENAME, 1000);
		assertNotNull(manager.getDownload(USER_ID));
	}

	@Test
	public void shouldGetSessionWithValidData() throws Exception {
		manager.addSession(USER_ID, FILENAME, 1000);
		Download download = manager.getDownload(USER_ID);
		assertEquals(FILENAME, download.getFileName());
		assertEquals(1000, download.getSize());
	}

}
