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
package net.seedboxer.camel.component.file.remote.ftp2;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import net.seedboxer.camel.component.file.remote.TransferListener;

import org.apache.camel.Exchange;
import org.apache.camel.InvalidPayloadException;
import org.apache.camel.component.file.GenericFileExist;
import org.apache.camel.component.file.GenericFileOperationFailedException;
import org.apache.camel.component.file.remote.FtpOperations;
import org.apache.camel.component.file.remote.RemoteFileConfiguration;
import org.apache.camel.util.FileUtil;
import org.apache.camel.util.IOHelper;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.io.CopyStreamEvent;
import org.apache.commons.net.io.CopyStreamException;
import org.apache.commons.net.io.CopyStreamListener;

import com.google.common.io.Files;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
public class Ftp2Operations extends FtpOperations {

	private final FileFilter directoryFileFilter = new DirectoryFileFilter();

	private final FileFilter normalFileFilter = new NormalFileFilter();

	public Ftp2Operations(FTPClient client,
			FTPClientConfig clientConfig) {
		super(client, clientConfig);
	}
	
	private class TransferStreamListener implements CopyStreamListener {
		
		private String source;
		
		TransferListener transferListener;
		
		public TransferStreamListener(TransferListener transferListener) {
			this.transferListener = transferListener;
		}
		
		public void setSource(String source) {
			this.source = source;
		}
		
		@Override
		public void bytesTransferred(long totalBytesTransferred, int bytesTransferred, long streamSize) {
			transferListener.bytesTransfered(source, bytesTransferred);
		}
		
		@Override
		public void bytesTransferred(CopyStreamEvent event) {
			transferListener.bytesTransfered(source, event.getBytesTransferred());
		}
	};
	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean connect(RemoteFileConfiguration configuration)
			throws GenericFileOperationFailedException {
		boolean res = super.connect(configuration);
		
		final TransferListener transferListener = ((Ftp2Endpoint)endpoint).getConfiguration().getTransferListener();
		client.setCopyStreamListener(new TransferStreamListener(transferListener));
		return res;
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
    
    private void setSourceCopyStreamListener(String source) {
    	CopyStreamListener copyStreamListener = client.getCopyStreamListener();
    	((TransferStreamListener)copyStreamListener).setSource(source);
    }

    private boolean doStoreFile(String name, String targetName, Exchange exchange) throws GenericFileOperationFailedException {
        log.trace("doStoreFile({})", targetName);
        
        setSourceCopyStreamListener(targetName);

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

        try {
        	
        	Object body = exchange.getIn().getMandatoryBody();
        	if (body instanceof InputStream) {
        		return doStoreFileFromIS(name, targetName, (InputStream) body);
        	} else if (body instanceof File) {
        		return doStoreFileFromFile(name, targetName, (File) body);
        	} else {
        		throw new InvalidPayloadException(exchange, InputStream.class);
        	}

        } catch (IOException e) {
            throw new GenericFileOperationFailedException(client.getReplyCode(), client.getReplyString(), e.getMessage(), e);
        } catch (InvalidPayloadException e) {
            throw new GenericFileOperationFailedException("Cannot store file: " + name, e);
        }
    }

	private boolean doStoreFileFromIS(String name, String targetName, InputStream is)
			throws IOException {
		try {
			if (endpoint.getFileExist() == GenericFileExist.Append && existsFile(targetName)) {
			    // Append + Resume
				log.trace("Client resumeFile: {}", targetName);
				return resume(targetName, is);
			} else {
			    log.trace("Client storeFile: {}", targetName);
			    return client.storeFile(targetName, is);
			}
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
					copyStream(is, outs, client.getBufferSize(), CopyStreamEvent.UNKNOWN_STREAM_SIZE, client.getCopyStreamListener());
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

	private boolean doStoreFileFromFile(String name, String targetName,
			File file) throws IOException {
		if (file.isDirectory()) {
			return doStoreDirectory(file);
		} else {
			return doStoreFileFromIS(name, targetName, getIS(file));
		}
	}

	private boolean doStoreDirectory(File file) throws IOException {
		boolean result = true;
		buildDirectory(file.getName(), false);
		String currentDir = null;
		try {
		    currentDir = getCurrentDirectory();
		    changeCurrentDirectory(file.getName());
		    
			// Upload all directories first
			for (File childFile : file.listFiles(directoryFileFilter)){
				result &= doStoreFileFromFile(childFile.getName(), childFile.getName(), childFile);
			}
			// Upload all files
			for (File childFile : file.listFiles(normalFileFilter)){
				result &= doStoreFileFromFile(childFile.getName(), childFile.getName(), childFile);
			}
		    
			return result;
		} finally {
			// change back to current directory if we changed directory
		    if (currentDir != null) {
		        changeCurrentDirectory(currentDir);
		    }
		}
	}
	
	private InputStream getIS(File file) throws IOException {
		return Files.newInputStreamSupplier(file).getInput();
	}
	
	private class NormalFileFilter implements FileFilter {

		@Override
		public boolean accept(File file) {
			return file.isFile();
		}

	}
	
	private class DirectoryFileFilter implements FileFilter {

		@Override
		public boolean accept(File file) {
			return file.isDirectory();
		}

	}
	
}
