/*******************************************************************************
 * Ftp2Endpoint.java
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
package net.seedboxer.camel.component;

import org.apache.camel.component.file.remote.FtpEndpoint;
import org.apache.camel.component.file.remote.RemoteFileComponent;
import org.apache.camel.component.file.remote.RemoteFileConfiguration;
import org.apache.camel.component.file.remote.RemoteFileOperations;
import org.apache.camel.util.IntrospectionSupport;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
public class Ftp2Endpoint<T extends FTPFile> extends FtpEndpoint<T> {

    public Ftp2Endpoint() {
    }

    public Ftp2Endpoint(String uri, RemoteFileComponent<FTPFile> component, RemoteFileConfiguration configuration) {
        super(uri, component, configuration);
    }
    
    @Override
	public RemoteFileOperations<FTPFile> createRemoteFileOperations() throws Exception {
        // configure ftp client
        FTPClient client = ftpClient;
        
        if (client == null) {
            // must use a new client if not explicit configured to use a custom client
            client = createFtpClient();
        }

        // set any endpoint configured timeouts
        if (getConfiguration().getConnectTimeout() > -1) {
            client.setConnectTimeout(getConfiguration().getConnectTimeout());
        }
        if (getConfiguration().getSoTimeout() > -1) {
            soTimeout = getConfiguration().getSoTimeout();
        }
        dataTimeout = getConfiguration().getTimeout();

        // then lookup ftp client parameters and set those
        if (ftpClientParameters != null) {
            // setting soTimeout has to be done later on FTPClient (after it has connected)
            Object timeout = ftpClientParameters.remove("soTimeout");
            if (timeout != null) {
                soTimeout = getCamelContext().getTypeConverter().convertTo(int.class, timeout);
            }
            // and we want to keep data timeout so we can log it later
            timeout = ftpClientParameters.remove("dataTimeout");
            if (timeout != null) {
                dataTimeout = getCamelContext().getTypeConverter().convertTo(int.class, dataTimeout);
            }
            IntrospectionSupport.setProperties(client, ftpClientParameters);
        }
        
        if (ftpClientConfigParameters != null) {
            // client config is optional so create a new one if we have parameter for it
            if (ftpClientConfig == null) {
                ftpClientConfig = new FTPClientConfig();
            }
            IntrospectionSupport.setProperties(ftpClientConfig, ftpClientConfigParameters);
        }

        if (dataTimeout > 0) {
            client.setDataTimeout(dataTimeout);
        }

        if (log.isDebugEnabled()) {
            log.debug("Created FTPClient [connectTimeout: {}, soTimeout: {}, dataTimeout: {}]: {}", 
                    new Object[]{client.getConnectTimeout(), getSoTimeout(), dataTimeout, client});
        }

        Ftp2Operations operations = new Ftp2Operations(client, getFtpClientConfig());
        operations.setEndpoint(this);
        return operations;
    } 
}
