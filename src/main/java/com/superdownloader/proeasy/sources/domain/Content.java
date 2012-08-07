/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.superdownloader.proeasy.sources.domain;

import com.superdownloader.proeasy.core.domain.User;
import com.superdownloader.proeasy.sources.type.MatchableItem;
import javax.persistence.*;

/**
 *
 * @author Farid
 */
@Entity
@Table(name= "CONTENT")
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class Content {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;
    
    @Column(name="NAME")
    private String name;
    
    @Column(name="HISTORY")
    private Boolean history;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID")
    private User user;
    
    @Transient
    private MatchableItem matchableItem;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MatchableItem getMatchableItem() {
        return matchableItem;
    }

    public void setMatchableItem(MatchableItem matchableItem) {
        this.matchableItem = matchableItem;
    }
    
    public void setIsHistory(boolean history){
        this.history = history;
    }
    
    public boolean isHistory(){
        return this.history;
    }

    public Boolean getHistory() {
        return history;
    }

    public void setHistory(Boolean history) {
        this.history = history;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object object){
        if(this == object)
            return true;
        if(object instanceof Content){
            Content content = (Content) object;
            if(this.name.trim().equalsIgnoreCase(content.getName()))
                return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }
    
}
