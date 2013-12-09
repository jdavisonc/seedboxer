/*******************************************************************************
 * Sftp2Component.java
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
package net.seedboxer.camel.component.sftp2;

import java.net.URI;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.component.file.GenericFileEndpoint;
import org.apache.camel.component.file.remote.SftpComponent;
import org.apache.camel.component.file.remote.SftpConfiguration;

import com.jcraft.jsch.ChannelSftp;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
public class Sftp2Component extends SftpComponent {

    public Sftp2Component() {
    }

    public Sftp2Component(CamelContext context) {
        super(context);
    }

    @Override
    protected GenericFileEndpoint<ChannelSftp.LsEntry> buildFileEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        // get the base uri part before the options as they can be non URI valid such as the expression using $ chars
        // and the URI constructor will regard $ as an illegal character and we dont want to enforce end users to
        // to escape the $ for the expression (file language)
        String baseUri = uri;
        if (uri.indexOf("?") != -1) {
            baseUri = uri.substring(0, uri.indexOf("?"));
        }

        // lets make sure we create a new configuration as each endpoint can
        // customize its own version
        SftpConfiguration config = new SftpConfiguration(new URI(baseUri));

        return new Sftp2Endpoint(uri, this, config);
    }

    @Override
	protected void afterPropertiesSet(GenericFileEndpoint<ChannelSftp.LsEntry> endpoint) throws Exception {
        // noop
    }
    
}
