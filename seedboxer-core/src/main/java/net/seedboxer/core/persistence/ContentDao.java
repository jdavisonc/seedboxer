/*******************************************************************************
 * ContentDao.java
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

package net.seedboxer.core.persistence;

import java.util.List;

import net.seedboxer.core.domain.Content;
import net.seedboxer.core.domain.User;


/**
 *
 * @author The-Sultan
 */
public interface ContentDao {

	void save(Content content);

	/**
	 * Returns all the content of a user that <b>is not history</b>
	 * @param user
	 * @return
	 */
	List<Content> getAllContent(User user);

	<T extends Content> List<T> getContentHistory(Class<T> clazz, boolean isHistory);

	<T extends Content> List<T> getHistoryContentFilteredByNameAndUser(Class<T> clazz, String name, User user);

	<T extends Content> List<T> getAllContentWithName(String name, Class<? extends Content> contentType);

}
