/*******************************************************************************
 * TransferSplitter.java
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

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.seedboxer.common.ftp.filter.DirectoryFileFilter;
import net.seedboxer.common.ftp.filter.NormalFileFilter;
import net.seedboxer.core.domain.Configuration;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
@Component
public class TransferSplitter {
	
	private final FileFilter directoryFileFilter = new DirectoryFileFilter();

	private final FileFilter normalFileFilter = new NormalFileFilter();

    public List<Message> splitMessage(Exchange ex) throws IOException {
    	List<Message> files = new ArrayList<Message>();
    	
    	List<String> downloadParentFiles = (List<String>) ex.getIn().getHeader(Configuration.FILES);
        for (String downloadParentFile : downloadParentFiles) {
			Collection<Message> copies = getFilesIS(ex.getIn(), new File(downloadParentFile));
			files.addAll(copies);
		}
        return files;
    }

	private Collection<Message> getFilesIS(Message msg, File downloadParentFile) throws IOException {
		if (downloadParentFile.isDirectory()) {
			List<Message> filesIS = new ArrayList<Message>();

			// Upload all directories first
			for (File childFile : downloadParentFile.listFiles(directoryFileFilter)){
				filesIS.addAll(getFilesIS(msg, childFile));
			}
			// Upload all files
			for (File childFile : downloadParentFile.listFiles(normalFileFilter)){
				filesIS.add(createCopyWithBody(msg, childFile));
			}
			
			return filesIS;
		} else {
			return ImmutableList.of(createCopyWithBody(msg, downloadParentFile));
		}
	}

	private InputStream getIS(File download) throws IOException {
		return Files.newInputStreamSupplier(download).getInput();
	}
	
	private Message createCopyWithBody(Message msg, File file) throws IOException {
		Message copy = msg.copy();
		copy.setBody(getIS(file));
		copy.setHeader(Exchange.FILE_NAME, file.getAbsolutePath());
		copy.setHeader(Exchange.FILE_LOCAL_WORK_PATH, "/home/harley/torrents");
		return copy;
	}
	
    
}
