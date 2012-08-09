/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.superdownloader.proeasy.sources.processors;

import com.superdownloader.proeasy.core.domain.User;
import com.superdownloader.proeasy.core.logic.DownloadsQueueManager;
import com.superdownloader.proeasy.mule.processor.DownloadReceiver;
import com.superdownloader.proeasy.sources.domain.Content;
import com.superdownloader.proeasy.sources.type.DownloadableItem;
import java.io.*;

import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.klomp.snark.bencode.BDecoder;
import org.klomp.snark.bencode.BEValue;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



/**
 *
 * @author Farid
 */
@Component
public class QueueProcessor implements Processor{

    @Autowired
    private DownloadsQueueManager queueManager;
    
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(DownloadReceiver.class);
    
    @Override
    public void process(Exchange exchange) throws Exception {
        DownloadableItem downloadableItem = (DownloadableItem) exchange.getIn().getBody();
        Content content = downloadableItem.getContent();
        URL url = content.getMatchableItem().getUrl();
        String path = "/etc/downloadedTorrentss";
        String filePath = path + "/" + url.getFile();
        File torrentFile  = downloadFile(url,filePath);
        String dirName = getDirNameFromTorrentFile(filePath);
        for(User user : downloadableItem.getUsers()){
            queueManager.push(user, dirName);
        }
    }
    
    private String getDirNameFromTorrentFile(String path) throws FileNotFoundException, IOException{
        BDecoder decoder;
        decoder = new BDecoder(new FileInputStream(path));
        Map map = ((BEValue)decoder.bdecode()).getMap();
        BEValue info = (BEValue) map.get("info");
        Map mapInfo = info.getMap();
        return ((BEValue)mapInfo.get("name")).getString();
        
    }
           
    private File downloadFile(URL url, String path) throws IOException {
            HttpMethod method = new GetMethod(url.toString());
            URLConnection conn =  url.openConnection();
            String fileName = url.getFile();
            InputStream in = conn.getInputStream(); 
            File file =   new File(path);
            OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            byte[] bytes = new byte[1024];
            int read;
            while ((read = in.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
            }
            in.close();
            out.close();
            return file;
    }
}
