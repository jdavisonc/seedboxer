/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.superdownloader.proeasy.sources.type;

import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author Farid
 */
public class MatchableItem {
    private String title;
    private URL url;
    private String path;

    public MatchableItem(String title, String url) throws MalformedURLException{
        this.title = title;
        this.url = new URL(url);
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    
    
}
