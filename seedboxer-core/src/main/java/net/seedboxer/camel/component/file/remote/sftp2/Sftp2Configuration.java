/*******************************************************************************
 * Sftp2Configuration.java
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
package net.seedboxer.camel.component.file.remote.sftp2;

import java.net.URI;

import net.seedboxer.camel.component.file.remote.TransferListener;

import org.apache.camel.component.file.remote.SftpConfiguration;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
public class Sftp2Configuration extends SftpConfiguration {
	
	private TransferListener transferListener;
    
	public Sftp2Configuration() {
        setProtocol("sftp");
    }

    public Sftp2Configuration(URI uri) {
        super(uri);
    }
    
	public TransferListener getTransferListener() {
		return transferListener;
	}

	public void setTransferListener(TransferListener transferListener) {
		this.transferListener = transferListener;
	}

}
