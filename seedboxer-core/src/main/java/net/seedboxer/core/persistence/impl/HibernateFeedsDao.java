/*******************************************************************************
 * HibernateFeedsDao.java
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

package net.seedboxer.core.persistence.impl;

import java.util.List;

import net.seedboxer.core.domain.RssFeed;
import net.seedboxer.core.persistence.FeedsDao;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


/**
 * 
 * @author Jorge Davison (jdavisonc)
 *
 */
@Transactional
@Repository
public class HibernateFeedsDao extends HibernateDao implements FeedsDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<RssFeed> getAllFeeds(){
		Criteria criteria = getCurrentSession().createCriteria(RssFeed.class);
		return criteria.list();
	}

	@Override
	public void save(RssFeed feed) {
		getCurrentSession().saveOrUpdate(feed);
	}

	@Override
	public void delete(RssFeed feed) {
		getCurrentSession().delete(feed);
	}

}
