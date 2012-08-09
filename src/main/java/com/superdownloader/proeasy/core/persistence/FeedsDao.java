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
 * @author Farid
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