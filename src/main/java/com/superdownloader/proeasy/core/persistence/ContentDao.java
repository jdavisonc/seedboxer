/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.superdownloader.proeasy.core.persistence;


import com.superdownloader.proeasy.sources.domain.Content;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Farid
 */

@Transactional
@Repository
public class ContentDao {
    
    @Autowired
    private SessionFactory sessionFactory;
    
    protected final Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
    
    public void save(Content content){
        getCurrentSession().save(content);
    }
    
    /*
   public <T extends Content> List<T> getFilters(Class<T> _type){
       //getCurrentSession().loa
   }*/
    
    public <T extends Content> List<T> getContentHistory(Class<T> clazz, boolean isHistory){
        Criteria criteria = getCurrentSession().createCriteria(clazz);
        criteria.add(Restrictions.eq("history", isHistory));
        return criteria.list();
    }
}
