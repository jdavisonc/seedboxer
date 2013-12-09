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
package net.seedboxer.camel.component.ftp2;

import java.net.URI;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.component.file.GenericFileEndpoint;
import org.apache.camel.component.file.remote.FtpComponent;
import org.apache.camel.component.file.remote.FtpConfiguration;
import org.apache.commons.net.ftp.FTPFile;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
public class Ftp2Component extends FtpComponent {

    public Ftp2Component() {
    }

    public Ftp2Component(CamelContext context) {
        super(context);
    }
    
    @Override
    protected GenericFileEndpoint<FTPFile> buildFileEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        String baseUri = getBaseUri(uri);

        // lets make sure we create a new configuration as each endpoint can customize its own version
        // must pass on baseUri to the configuration (see above)
        FtpConfiguration config = new FtpConfiguration(new URI(baseUri));

        Ftp2Endpoint<FTPFile> answer = new Ftp2Endpoint<FTPFile>(uri, this, config);
        extractAndSetFtpClientConfigParameters(parameters, answer);
        extractAndSetFtpClientParameters(parameters, answer);

        return answer;
    }
    
}
