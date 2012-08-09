/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.superdownloader.proeasy.sources.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Farid
 */
@Entity
@Table(name= "FEEDS")
public class RssFeed {
    @Id
    private Long id;
    
    @Column(name="url")
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
}
