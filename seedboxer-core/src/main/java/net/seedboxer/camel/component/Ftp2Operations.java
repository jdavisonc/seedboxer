/*******************************************************************************
 * Ftp2Operations.java
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.InvalidPayloadException;
import org.apache.camel.component.file.GenericFileExist;
import org.apache.camel.component.file.GenericFileOperationFailedException;
import org.apache.camel.component.file.remote.FtpOperations;
import org.apache.camel.util.FileUtil;
import org.apache.camel.util.IOHelper;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.io.CopyStreamEvent;
import org.apache.commons.net.io.CopyStreamException;
import org.apache.commons.net.io.CopyStreamListener;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
public class Ftp2Operations extends FtpOperations {

	public Ftp2Operations(FTPClient client,
			FTPClientConfig clientConfig) {
		super(client, clientConfig);
	}


    @Override
	public boolean storeFile(String name, Exchange exchange) throws GenericFileOperationFailedException {
        // must normalize name first
        name = endpoint.getConfiguration().normalizePath(name);

        log.trace("storeFile({})", name);

        boolean answer = false;
        String currentDir = null;
        String path = FileUtil.onlyPath(name);
        String targetName = name;

        try {
            if (path != null && endpoint.getConfiguration().isStepwise()) {
                // must remember current dir so we stay in that directory after the write
                currentDir = getCurrentDirectory();

                // change to path of name
                changeCurrentDirectory(path);

                // the target name should be without path, as we have changed directory
                targetName = FileUtil.stripPath(name);
            }

            // store the file
            answer = doStoreFile(name, targetName, exchange);
        } finally {
            // change back to current directory if we changed directory
            if (currentDir != null) {
                changeCurrentDirectory(currentDir);
            }
        }

        return answer;
    }

    private boolean doStoreFile(String name, String targetName, Exchange exchange) throws GenericFileOperationFailedException {
        log.trace("doStoreFile({})", targetName);

        // if an existing file already exists what should we do?
        if (endpoint.getFileExist() == GenericFileExist.Ignore || endpoint.getFileExist() == GenericFileExist.Fail) {
            boolean existFile = existsFile(targetName);
            if (existFile && endpoint.getFileExist() == GenericFileExist.Ignore) {
                // ignore but indicate that the file was written
                log.trace("An existing file already exists: {}. Ignore and do not override it.", name);
                return true;
            } else if (existFile && endpoint.getFileExist() == GenericFileExist.Fail) {
                throw new GenericFileOperationFailedException("File already exist: " + name + ". Cannot write new file.");
            }
        }

        InputStream is = null;
        try {
            is = exchange.getIn().getMandatoryBody(InputStream.class);
            if (endpoint.getFileExist() == GenericFileExist.Append && existsFile(targetName)) {
                // Append + Resume
            	log.trace("Client resumeFile: {}", targetName);
            	return resume(targetName, is);
            } else {
                log.trace("Client storeFile: {}", targetName);
                return client.storeFile(targetName, is);
            }
        } catch (IOException e) {
            throw new GenericFileOperationFailedException(client.getReplyCode(), client.getReplyString(), e.getMessage(), e);
        } catch (InvalidPayloadException e) {
            throw new GenericFileOperationFailedException("Cannot store file: " + name, e);
        } finally {
            IOHelper.close(is, "store: " + name, log);
        }
    }
    
    private boolean resume(String targetName, InputStream is) throws IOException {
		Long size = getSize(targetName);
		
		// Set the offset
		client.setRestartOffset(size);
		// Create stream and skip first SIZE bytes
		is.skip(size);

		try {
			OutputStream outs = client.storeFileStream(targetName);
			if (outs != null) {
				try {
					copyStream(is, outs, client.getBufferSize(), CopyStreamEvent.UNKNOWN_STREAM_SIZE, null);
				} finally {
					outs.close();
				}
			}
		} finally {
			is.close();
		}

		return client.completePendingCommand();
	}

	protected Long getSize(String targetName) {
    	List<FTPFile> listFiles = listFiles();
    	for (FTPFile ftpFile : listFiles) {
			if (targetName.equals(ftpFile.getName())) {
				return ftpFile.getSize();
			}
		}
    	return null;
    }
	
	private void copyStream(InputStream source, OutputStream dest,
            int bufferSize, long streamSize,
            CopyStreamListener listener) throws CopyStreamException {
        int bytes;
        long total;
        byte[] buffer;

        buffer = new byte[bufferSize];
        total = 0;

        try {
            while ((bytes = source.read(buffer)) != -1) {
                if (bytes == 0) {
                    bytes = source.read();
                    if (bytes < 0) {
                        break;
                    }
                    dest.write(bytes);
                    dest.flush();
                    ++total;
                    if (listener != null) {
                        listener.bytesTransferred(total, 1, streamSize);
                    }
                    continue;
                }

                dest.write(buffer, 0, bytes);
                dest.flush();
                total += bytes;
                if (listener != null) {
                    listener.bytesTransferred(total, bytes, streamSize);
                }
            }
        } catch (IOException e) {
            throw new CopyStreamException("IOException caught while copying.", total, e);
        }
	}

}
