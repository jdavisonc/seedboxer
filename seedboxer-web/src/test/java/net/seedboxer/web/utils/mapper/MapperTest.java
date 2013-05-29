/*******************************************************************************
 * MapperTest.java
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
package net.seedboxer.web.utils.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import net.seedboxer.core.domain.Movie;
import net.seedboxer.core.type.Quality;
import net.seedboxer.web.type.dto.MovieInfo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/application-context.xml"})
public class MapperTest {
	
	@Autowired
	private Mapper mapper;

	@Test
	public void shouldDetectAnnotationAndMapDTOToObject() {
		Movie m = new Movie("Iron Man 3", 2013, Quality.HD);
		Object dto = mapper.map(m);
		assertTrue(dto instanceof MovieInfo);
		assertEquals("Iron Man 3", ((MovieInfo)dto).getName());
		assertEquals(new Integer(2013), ((MovieInfo)dto).getYear());
		assertEquals("HD", ((MovieInfo)dto).getQuality());
	}
	
	@Test
	public void shouldDetectAnnotationAndMapObjectToDTO() {
		MovieInfo m = new MovieInfo("Iron Man 3", 2013, "HD");
		Object dto = mapper.map(m);
		assertTrue(dto instanceof Movie);
		assertEquals("Iron Man 3", ((Movie)dto).getName());
		assertEquals(new Integer(2013), ((Movie)dto).getYear());
		assertEquals(Quality.HD, ((Movie)dto).getQuality());
	}

}
