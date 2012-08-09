/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.superdownloader.proeasy.sources.routes;

import com.superdownloader.proeasy.core.persistence.FeedsDao;
import com.superdownloader.proeasy.sources.domain.RssFeed;
import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Farid
 */
@Component
public class RouteBuilder extends SpringRouteBuilder{
    
    @Autowired
    private FeedsDao feedsDao;
    
    @Override
    public void configure() throws Exception {
        List<RssFeed> feeds = feedsDao.getAllFeeds(RssFeed.class);
        String inputTemplate = "rss:%s?consumer.delay=300s&splitEntries=true&throttleEntries=false";
        for(RssFeed feed : feeds){
            from(String.format(inputTemplate, feed.getUrl()))
            .to("direct:mergeFeeds");
        }
    }
}
