/*******************************************************************************
 * FeedsDao.java
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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.superdownloader.proeasy.core.persistence;


import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author The-Sultan
 */

@Transactional
@Repository
public class FeedsDao {
    
    @Autowired
    private SessionFactory sessionFactory;
    
    protected final Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
    
    public <T> List<T> getAllFeeds(Class<T> clazz){
        Criteria criteria = getCurrentSession().createCriteria(clazz);
        return criteria.list();
    }
    
}
