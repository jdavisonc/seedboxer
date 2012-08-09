/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.superdownloader.proeasy.sources.type;

import com.superdownloader.proeasy.core.domain.User;
import com.superdownloader.proeasy.sources.domain.Content;
import java.util.List;

/**
 *
 * @author Farid
 */
public class DownloadableItem {
    
    private Content content;
    
    private List<User> users;

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public DownloadableItem(Content content, List<User> users) {
        this.content = content;
        this.users = users;
    }
    
    
}
