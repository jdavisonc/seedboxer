/*******************************************************************************
 * TransferRouter.java
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

import java.io.IOException;

import net.seedboxer.core.domain.Configuration;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.stereotype.Component;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
@Component
public class TransferRouter {

	private static final String COMPONENTS_CONF = "fileExist=Append&" +
													 "binary=true&" +
													 "disconnect=true&" +
													 "fastExistsCheck=true&" +
													 "throwExceptionOnConnectFailed=true&" +
													 "copyStreamListener=";

	public String slip(Exchange ex) throws IOException {
	    if (ex.getProperty("splitted") != null) {
	    	return null;
	    }

	    ex.setProperty("splitted", true);
	    
	    Message msg = ex.getIn();
		String user = (String) msg.getHeader(Configuration.TRANSFER_USERNAME);
		String pass = (String) msg.getHeader(Configuration.TRANSFER_PASSWORD);
		String server = (String) msg.getHeader(Configuration.TRANSFER_URL);
		String remoteDir = (String) msg.getHeader(Configuration.TRANSFER_REMOTE_DIR);
		
	    if (server.startsWith("ftp://")) {
	        return ftp(user, pass, server.substring(6), remoteDir);
	    } else if (server.startsWith("ftps://")) {
	        return ftps(user, pass, server.substring(7), remoteDir);
	    } else if (server.startsWith("sftp://")) {
	        return sftp(user, pass, server.substring(7), remoteDir);
	    }

	    return null;
	}

	private String sftp(String user, String pass, String server,
			String remoteDir) {
		return "sftp://"+user+"@"+server+"/"+remoteDir+"?password="+pass+"&"+COMPONENTS_CONF;
	}

	private String ftps(String user, String pass, String server,
			String remoteDir) {
		return "ftps://"+user+"@"+server+"/"+remoteDir+"?password="+pass+"&"+COMPONENTS_CONF;
	}

	private String ftp(String user, String pass, String server, String remoteDir) {
		return "ftp2://"+user+"@"+server+"/"+remoteDir+"?password="+pass+"&"+COMPONENTS_CONF;
	}
	
}
