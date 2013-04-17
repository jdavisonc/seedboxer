package net.seedboxer.seedboxer.core.persistence.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class HibernateDao {

    @Autowired
    private SessionFactory sessionFactory;
    
    protected final Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
	
}
