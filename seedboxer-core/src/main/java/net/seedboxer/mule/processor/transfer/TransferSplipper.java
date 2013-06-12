/*******************************************************************************
 * TransferSplipper.java
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
package net.seedboxer.mule.processor.transfer;

import java.util.List;

import net.seedboxer.core.domain.Configuration;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.stereotype.Component;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
@Component
public class TransferSplipper {

	private static final String FTP_COMPONENT_CONF = "fileExist=Append&" +
													 "binary=true&" +
													 "disconnect=true&" +
													 "fastExistsCheck=true&" +
													 "throwExceptionOnConnectFailed=true";

	public String slip(Exchange ex) {
	    if (ex.getProperty("splitted") != null) {
	    	return null;
	    }

	    ex.setProperty("splitted", true);
	    
	    Message msg = ex.getIn();
		String user = (String) msg.getHeader(Configuration.TRANSFER_USERNAME);
		String pass = (String) msg.getHeader(Configuration.TRANSFER_PASSWORD);
		String server = (String) msg.getHeader(Configuration.TRANSFER_URL);
		String remoteDir = (String) msg.getHeader(Configuration.TRANSFER_REMOTE_DIR);
		
		List<String> filesToUpload = (List<String>) msg.getHeader(Configuration.FILES);
		
		//msg.setHeader(Exchange.FILE_LOCAL_WORK_PATH, "");
		msg.setHeader(Exchange.FILE_NAME, filesToUpload.get(0));
	    
	    if (server.startsWith("ftp://")) {
	        return ftp(user, pass, server.substring(5), remoteDir);
	    } else if (server.startsWith("ftps://")) {
	        return ftps(user, pass, server.substring(6), remoteDir);
	    } else if (server.startsWith("sftp://")) {
	        return sftp(user, pass, server.substring(6), remoteDir);
	    }

	    return null;
	}

	private String sftp(String user, String pass, String server,
			String remoteDir) {
		return "ftp://"+user+"@"+server+"/"+remoteDir+"?password="+pass+"&"+FTP_COMPONENT_CONF;
	}

	private String ftps(String user, String pass, String server,
			String remoteDir) {
		return "ftp://"+user+"@"+server+"/"+remoteDir+"?password="+pass;
	}

	private String ftp(String user, String pass, String server, String remoteDir) {
		return "ftp://"+user+"@"+server+"/"+remoteDir+"?password="+pass;
	}
	
}
