package net.seedboxer.camel.component.sftp2;

import static org.apache.camel.util.ObjectHelper.isNotEmpty;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.camel.Exchange;
import org.apache.camel.InvalidPayloadException;
import org.apache.camel.component.file.FileComponent;
import org.apache.camel.component.file.GenericFile;
import org.apache.camel.component.file.GenericFileEndpoint;
import org.apache.camel.component.file.GenericFileExist;
import org.apache.camel.component.file.GenericFileOperationFailedException;
import org.apache.camel.component.file.remote.RemoteFileConfiguration;
import org.apache.camel.component.file.remote.RemoteFileOperations;
import org.apache.camel.component.file.remote.SftpConfiguration;
import org.apache.camel.component.file.remote.SftpEndpoint;
import org.apache.camel.util.FileUtil;
import org.apache.camel.util.IOHelper;
import org.apache.camel.util.ObjectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Files;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

public class Sftp2Operations implements RemoteFileOperations<ChannelSftp.LsEntry> {

    private static final transient Logger LOG = LoggerFactory.getLogger(Sftp2Operations.class);
    private SftpEndpoint endpoint;
    private ChannelSftp channel;
    private Session session;

    /**
     * Extended user info which supports interactive keyboard mode, by entering the password.
     */
    public interface ExtendedUserInfo extends UserInfo, UIKeyboardInteractive {
    }

    @Override
	public void setEndpoint(GenericFileEndpoint<ChannelSftp.LsEntry> endpoint) {
        this.endpoint = (SftpEndpoint) endpoint;
    }
    
    protected ChannelSftp getChannel() {
    	return this.channel;
    }

    @Override
	public boolean connect(RemoteFileConfiguration configuration) throws GenericFileOperationFailedException {
        if (isConnected()) {
            // already connected
            return true;
        }

        boolean connected = false;
        int attempt = 0;

        while (!connected) {
            try {
                if (LOG.isTraceEnabled() && attempt > 0) {
                    LOG.trace("Reconnect attempt #{} connecting to + {}", attempt, configuration.remoteServerInformation());
                }

                if (channel == null || !channel.isConnected()) {
                    if (session == null || !session.isConnected()) {
                        LOG.trace("Session isn't connected, trying to recreate and connect.");
                        session = createSession(configuration);
                        if (endpoint.getConfiguration().getConnectTimeout() > 0) {
                            LOG.trace("Connecting use connectTimeout: " + endpoint.getConfiguration().getConnectTimeout() + " ...");
                            session.connect(endpoint.getConfiguration().getConnectTimeout());
                        } else {
                            LOG.trace("Connecting ...");
                            session.connect();
                        }
                    }

                    LOG.trace("Channel isn't connected, trying to recreate and connect.");
                    channel = (ChannelSftp) session.openChannel("sftp");

                    if (endpoint.getConfiguration().getConnectTimeout() > 0) {
                        LOG.trace("Connecting use connectTimeout: " + endpoint.getConfiguration().getConnectTimeout() + " ...");
                        channel.connect(endpoint.getConfiguration().getConnectTimeout());
                    } else {
                        LOG.trace("Connecting ...");
                        channel.connect();
                    }
                    LOG.info("Connected to " + configuration.remoteServerInformation());
                }

                // yes we could connect
                connected = true;
            } catch (Exception e) {
                // check if we are interrupted so we can break out
                if (Thread.currentThread().isInterrupted()) {
                    throw new GenericFileOperationFailedException("Interrupted during connecting", new InterruptedException("Interrupted during connecting"));
                }

                GenericFileOperationFailedException failed = new GenericFileOperationFailedException("Cannot connect to " + configuration.remoteServerInformation(), e);
                LOG.trace("Cannot connect due: {}", failed.getMessage());
                attempt++;
                if (attempt > endpoint.getMaximumReconnectAttempts()) {
                    throw failed;
                }
                if (endpoint.getReconnectDelay() > 0) {
                    try {
                        Thread.sleep(endpoint.getReconnectDelay());
                    } catch (InterruptedException ie) {
                        // we could potentially also be interrupted during sleep
                        Thread.currentThread().interrupt();
                        throw new GenericFileOperationFailedException("Interrupted during sleeping", ie);
                    }
                }
            }
        }

        return true;
    }

    protected Session createSession(final RemoteFileConfiguration configuration) throws JSchException {
        final JSch jsch = new JSch();
        JSch.setLogger(new JSchLogger());

        SftpConfiguration sftpConfig = (SftpConfiguration) configuration;

        if (isNotEmpty(sftpConfig.getCiphers())) {
            LOG.debug("Using ciphers: {}", sftpConfig.getCiphers());
            Hashtable<String, String> ciphers = new Hashtable<String, String>();
            ciphers.put("cipher.s2c", sftpConfig.getCiphers());
            ciphers.put("cipher.c2s", sftpConfig.getCiphers());
            JSch.setConfig(ciphers);
        }
        
        if (isNotEmpty(sftpConfig.getPrivateKeyFile())) {
            LOG.debug("Using private keyfile: {}", sftpConfig.getPrivateKeyFile());
            if (isNotEmpty(sftpConfig.getPrivateKeyFilePassphrase())) {
                jsch.addIdentity(sftpConfig.getPrivateKeyFile(), sftpConfig.getPrivateKeyFilePassphrase());
            } else {
                jsch.addIdentity(sftpConfig.getPrivateKeyFile());
            }
        }

        if (isNotEmpty(sftpConfig.getKnownHostsFile())) {
            LOG.debug("Using knownhosts file: {}", sftpConfig.getKnownHostsFile());
            jsch.setKnownHosts(sftpConfig.getKnownHostsFile());
        }

        final Session session = jsch.getSession(configuration.getUsername(), configuration.getHost(), configuration.getPort());

        if (isNotEmpty(sftpConfig.getStrictHostKeyChecking())) {
            LOG.debug("Using StrickHostKeyChecking: {}", sftpConfig.getStrictHostKeyChecking());
            session.setConfig("StrictHostKeyChecking", sftpConfig.getStrictHostKeyChecking());
        }
        
        session.setServerAliveInterval(sftpConfig.getServerAliveInterval());
        session.setServerAliveCountMax(sftpConfig.getServerAliveCountMax());

        // compression
        if (sftpConfig.getCompression() > 0) {
            LOG.debug("Using compression: {}", sftpConfig.getCompression());
            session.setConfig("compression.s2c", "zlib@openssh.com, zlib, none");
            session.setConfig("compression.c2s", "zlib@openssh.com, zlib, none");
            session.setConfig("compression_level", Integer.toString(sftpConfig.getCompression()));
        }

        // set user information
        session.setUserInfo(new ExtendedUserInfo() {
            @Override
			public String getPassphrase() {
                return null;
            }

            @Override
			public String getPassword() {
                return configuration.getPassword();
            }

            @Override
			public boolean promptPassword(String s) {
                return true;
            }

            @Override
			public boolean promptPassphrase(String s) {
                return true;
            }

            @Override
			public boolean promptYesNo(String s) {
                LOG.warn("Server asks for confirmation (yes|no): " + s + ". Camel will answer no.");
                // Return 'false' indicating modification of the hosts file is disabled.
                return false;
            }

            @Override
			public void showMessage(String s) {
                LOG.trace("Message received from Server: " + s);
            }

            @Override
			public String[] promptKeyboardInteractive(String destination, String name,
                                                      String instruction, String[] prompt, boolean[] echo) {
                return new String[]{configuration.getPassword()};
            }

        });
        return session;
    }

    private static final class JSchLogger implements com.jcraft.jsch.Logger {

        @Override
		public boolean isEnabled(int level) {
            switch (level) {
            case FATAL:
                // use ERROR as FATAL
                return LOG.isErrorEnabled();
            case ERROR:
                return LOG.isErrorEnabled();
            case WARN:
                return LOG.isWarnEnabled();
            case INFO:
                return LOG.isInfoEnabled();
            default:
                return LOG.isDebugEnabled();
            }
        }

        @Override
		public void log(int level, String message) {
            switch (level) {
            case FATAL:
                // use ERROR as FATAL
                LOG.error("JSCH -> " + message);
                break;
            case ERROR:
                LOG.error("JSCH -> " + message);
                break;
            case WARN:
                LOG.warn("JSCH -> " + message);
                break;
            case INFO:
                LOG.info("JSCH -> " + message);
                break;
            default:
                LOG.debug("JSCH -> " + message);
                break;
            }
        }
    }

    @Override
	public boolean isConnected() throws GenericFileOperationFailedException {
        return session != null && session.isConnected() && channel != null && channel.isConnected();
    }

    @Override
	public void disconnect() throws GenericFileOperationFailedException {
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
        if (channel != null && channel.isConnected()) {
            channel.disconnect();
        }
    }

    @Override
	public boolean deleteFile(String name) throws GenericFileOperationFailedException {
        LOG.debug("Deleting file: {}", name);
        try {
            channel.rm(name);
            return true;
        } catch (SftpException e) {
            throw new GenericFileOperationFailedException("Cannot delete file: " + name, e);
        }
    }

    @Override
	public boolean renameFile(String from, String to) throws GenericFileOperationFailedException {
        LOG.debug("Renaming file: {} to: {}", from, to);
        try {
            channel.rename(from, to);
            return true;
        } catch (SftpException e) {
            throw new GenericFileOperationFailedException("Cannot rename file from: " + from + " to: " + to, e);
        }
    }

    @Override
	public boolean buildDirectory(String directory, boolean absolute) throws GenericFileOperationFailedException {
        // must normalize directory first
        directory = endpoint.getConfiguration().normalizePath(directory);

        LOG.trace("buildDirectory({},{})", directory, absolute);
        // ignore absolute as all dirs are relative with FTP
        boolean success = false;

        String originalDirectory = getCurrentDirectory();
        try {
            // maybe the full directory already exists
            try {
                channel.cd(directory);
                success = true;
            } catch (SftpException e) {
                // ignore, we could not change directory so try to create it instead
            }

            if (!success) {
                LOG.debug("Trying to build remote directory: {}", directory);

                try {
                    channel.mkdir(directory);
                    success = true;
                } catch (SftpException e) {
                    // we are here if the server side doesn't create intermediate folders
                    // so create the folder one by one
                    success = buildDirectoryChunks(directory);
                }
            }
        } catch (IOException e) {
            throw new GenericFileOperationFailedException("Cannot build directory: " + directory, e);
        } catch (SftpException e) {
            throw new GenericFileOperationFailedException("Cannot build directory: " + directory, e);
        } finally {
            // change back to original directory
            if (originalDirectory != null) {
                changeCurrentDirectory(originalDirectory);
            }
        }

        return success;
    }

    private boolean buildDirectoryChunks(String dirName) throws IOException, SftpException {
        final StringBuilder sb = new StringBuilder(dirName.length());
        final String[] dirs = dirName.split("/|\\\\");

        boolean success = false;
        for (String dir : dirs) {
            sb.append(dir).append('/');
            // must normalize the directory name
            String directory = endpoint.getConfiguration().normalizePath(sb.toString());

            // do not try to build root folder (/ or \)
            if (!(directory.equals("/") || directory.equals("\\"))) {
                try {
                    LOG.trace("Trying to build remote directory by chunk: {}", directory);

                    channel.mkdir(directory);
                    success = true;
                } catch (SftpException e) {
                    // ignore keep trying to create the rest of the path
                }
            }
        }

        return success;
    }

    @Override
	public String getCurrentDirectory() throws GenericFileOperationFailedException {
        LOG.trace("getCurrentDirectory()");
        try {
            return channel.pwd();
        } catch (SftpException e) {
            throw new GenericFileOperationFailedException("Cannot get current directory", e);
        }
    }

    @Override
	public void changeCurrentDirectory(String path) throws GenericFileOperationFailedException {
        LOG.trace("changeCurrentDirectory({})", path);
        if (ObjectHelper.isEmpty(path)) {
            return;
        }

        // must compact path so FTP server can traverse correctly
        path = FileUtil.compactPath(path);

        // not stepwise should change directory in one operation
        if (!endpoint.getConfiguration().isStepwise()) {
            doChangeDirectory(path);
            return;
        }

        // if it starts with the root path then a little special handling for that
        if (FileUtil.hasLeadingSeparator(path)) {
            // change to root path
            doChangeDirectory(path.substring(0, 1));
            path = path.substring(1);
        }

        // split into multiple dirs
        final String[] dirs = path.split("/|\\\\");

        if (dirs == null || dirs.length == 0) {
            // path was just a relative single path
            doChangeDirectory(path);
            return;
        }

        // there are multiple dirs so do this in chunks
        for (String dir : dirs) {
            doChangeDirectory(dir);
        }
    }

    private void doChangeDirectory(String path) {
        if (path == null || ".".equals(path) || ObjectHelper.isEmpty(path)) {
            return;
        }

        LOG.trace("Changing directory: {}", path);
        try {
            channel.cd(path);
        } catch (SftpException e) {
            throw new GenericFileOperationFailedException("Cannot change directory to: " + path, e);
        }
    }

    @Override
	public void changeToParentDirectory() throws GenericFileOperationFailedException {
        LOG.trace("changeToParentDirectory()");
        String current = getCurrentDirectory();

        String parent = FileUtil.compactPath(current + "/..");
        // must start with absolute
        if (!parent.startsWith("/")) {
            parent = "/" + parent;
        }

        changeCurrentDirectory(parent);
    }

    @Override
	public List<ChannelSftp.LsEntry> listFiles() throws GenericFileOperationFailedException {
        return listFiles(".");
    }

    @Override
	public List<ChannelSftp.LsEntry> listFiles(String path) throws GenericFileOperationFailedException {
        LOG.trace("listFiles({})", path);
        if (ObjectHelper.isEmpty(path)) {
            // list current directory if file path is not given
            path = ".";
        }

        try {
            final List<ChannelSftp.LsEntry> list = new ArrayList<ChannelSftp.LsEntry>();

            @SuppressWarnings("rawtypes")
            Vector files = channel.ls(path);
            // can return either null or an empty list depending on FTP servers
            if (files != null) {
                for (Object file : files) {
                    list.add((ChannelSftp.LsEntry) file);
                }
            }
            return list;
        } catch (SftpException e) {
            throw new GenericFileOperationFailedException("Cannot list directory: " + path, e);
        }
    }

    @Override
	public boolean retrieveFile(String name, Exchange exchange) throws GenericFileOperationFailedException {
        LOG.trace("retrieveFile({})", name);
        if (ObjectHelper.isNotEmpty(endpoint.getLocalWorkDirectory())) {
            // local work directory is configured so we should store file content as files in this local directory
            return retrieveFileToFileInLocalWorkDirectory(name, exchange);
        } else {
            // store file content directory as stream on the body
            return retrieveFileToStreamInBody(name, exchange);
        }
    }

    @SuppressWarnings("unchecked")
    private boolean retrieveFileToStreamInBody(String name, Exchange exchange) throws GenericFileOperationFailedException {
        OutputStream os = null;
        try {
            os = new ByteArrayOutputStream();
            GenericFile<ChannelSftp.LsEntry> target =
                    (GenericFile<ChannelSftp.LsEntry>) exchange.getProperty(FileComponent.FILE_EXCHANGE_FILE);
            ObjectHelper.notNull(target, "Exchange should have the " + FileComponent.FILE_EXCHANGE_FILE + " set");
            target.setBody(os);

            String remoteName = name;
            String currentDir = null;
            if (endpoint.getConfiguration().isStepwise()) {
                // remember current directory
                currentDir = getCurrentDirectory();

                // change directory to path where the file is to be retrieved
                // (must do this as some FTP servers cannot retrieve using absolute path)
                String path = FileUtil.onlyPath(name);
                if (path != null) {
                    changeCurrentDirectory(path);
                }
                // remote name is now only the file name as we just changed directory
                remoteName = FileUtil.stripPath(name);
            }

            // use input stream which works with Apache SSHD used for testing
            InputStream is = channel.get(remoteName);
            IOHelper.copyAndCloseInput(is, os);

            // change back to current directory
            if (endpoint.getConfiguration().isStepwise()) {
                changeCurrentDirectory(currentDir);
            }

            return true;
        } catch (IOException e) {
            throw new GenericFileOperationFailedException("Cannot retrieve file: " + name, e);
        } catch (SftpException e) {
            throw new GenericFileOperationFailedException("Cannot retrieve file: " + name, e);
        } finally {
            IOHelper.close(os, "retrieve: " + name, LOG);
        }
    }

    @SuppressWarnings("unchecked")
    private boolean retrieveFileToFileInLocalWorkDirectory(String name, Exchange exchange) throws GenericFileOperationFailedException {
        File temp;
        File local = new File(endpoint.getLocalWorkDirectory());
        OutputStream os;
        GenericFile<ChannelSftp.LsEntry> file =
                (GenericFile<ChannelSftp.LsEntry>) exchange.getProperty(FileComponent.FILE_EXCHANGE_FILE);
        ObjectHelper.notNull(file, "Exchange should have the " + FileComponent.FILE_EXCHANGE_FILE + " set");
        try {
            // use relative filename in local work directory
            String relativeName = file.getRelativeFilePath();

            temp = new File(local, relativeName + ".inprogress");
            local = new File(local, relativeName);

            // create directory to local work file
            local.mkdirs();

            // delete any existing files
            if (temp.exists()) {
                if (!FileUtil.deleteFile(temp)) {
                    throw new GenericFileOperationFailedException("Cannot delete existing local work file: " + temp);
                }
            }
            if (local.exists()) {
                if (!FileUtil.deleteFile(local)) {
                    throw new GenericFileOperationFailedException("Cannot delete existing local work file: " + local);
                }
            }

            // create new temp local work file
            if (!temp.createNewFile()) {
                throw new GenericFileOperationFailedException("Cannot create new local work file: " + temp);
            }

            // store content as a file in the local work directory in the temp handle
            os = new FileOutputStream(temp);

            // set header with the path to the local work file
            exchange.getIn().setHeader(Exchange.FILE_LOCAL_WORK_PATH, local.getPath());
        } catch (Exception e) {
            throw new GenericFileOperationFailedException("Cannot create new local work file: " + local);
        }

        try {
            // store the java.io.File handle as the body
            file.setBody(local);

            String remoteName = name;
            String currentDir = null;
            if (endpoint.getConfiguration().isStepwise()) {
                // remember current directory
                currentDir = getCurrentDirectory();

                // change directory to path where the file is to be retrieved
                // (must do this as some FTP servers cannot retrieve using absolute path)
                String path = FileUtil.onlyPath(name);
                if (path != null) {
                    changeCurrentDirectory(path);
                }
                // remote name is now only the file name as we just changed directory
                remoteName = FileUtil.stripPath(name);
            }

            channel.get(remoteName, os);

            // change back to current directory
            if (endpoint.getConfiguration().isStepwise()) {
                changeCurrentDirectory(currentDir);
            }

        } catch (SftpException e) {
            LOG.trace("Error occurred during retrieving file: {} to local directory. Deleting local work file: {}", name, temp);
            // failed to retrieve the file so we need to close streams and delete in progress file
            // must close stream before deleting file
            IOHelper.close(os, "retrieve: " + name, LOG);
            boolean deleted = FileUtil.deleteFile(temp);
            if (!deleted) {
                LOG.warn("Error occurred during retrieving file: " + name + " to local directory. Cannot delete local work file: " + temp);
            }
            throw new GenericFileOperationFailedException("Cannot retrieve file: " + name, e);
        } finally {
            IOHelper.close(os, "retrieve: " + name, LOG);
        }

        LOG.debug("Retrieve file to local work file result: true");

        // operation went okay so rename temp to local after we have retrieved the data
        LOG.trace("Renaming local in progress file from: {} to: {}", temp, local);
        try {
            if (!FileUtil.renameFile(temp, local, false)) {
                throw new GenericFileOperationFailedException("Cannot rename local work file from: " + temp + " to: " + local);
            }
        } catch (IOException e) {
            throw new GenericFileOperationFailedException("Cannot rename local work file from: " + temp + " to: " + local, e);
        }

        return true;
    }

    @Override
	public boolean storeFile(String name, Exchange exchange) throws GenericFileOperationFailedException {
        // must normalize name first
        name = endpoint.getConfiguration().normalizePath(name);

        LOG.trace("storeFile({})", name);

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

    @SuppressWarnings("deprecation")
	private boolean doStoreFile(String name, String targetName, Exchange exchange) throws GenericFileOperationFailedException {
        LOG.trace("doStoreFile({})", targetName);

        // if an existing file already exists what should we do?
        if (endpoint.getFileExist() == GenericFileExist.Ignore || endpoint.getFileExist() == GenericFileExist.Fail) {
            boolean existFile = existsFile(targetName);
            if (existFile && endpoint.getFileExist() == GenericFileExist.Ignore) {
                // ignore but indicate that the file was written
                LOG.trace("An existing file already exists: {}. Ignore and do not override it.", name);
                return true;
            } else if (existFile && endpoint.getFileExist() == GenericFileExist.Fail) {
                throw new GenericFileOperationFailedException("File already exist: " + name + ". Cannot write new file.");
            }
        }

        InputStream is = null;
        try {
        	Object body = exchange.getIn().getMandatoryBody();
        	if (body instanceof InputStream) {
        		is = (InputStream) body;
        	} else if (body instanceof File) {
        		is = getIS((File)body);
        	} else {
        		throw new InvalidPayloadException(exchange, InputStream.class);
        	}
        	
            if (endpoint.getFileExist() == GenericFileExist.Append) {
                channel.put(is, targetName, ChannelSftp.RESUME);
            } else {
                // override is default
                channel.put(is, targetName);
            }
            
            
            // after storing file, we may set chmod on the file
            String mode = endpoint.getConfiguration().getChmod();
            if (ObjectHelper.isNotEmpty(mode)) {
                // parse to int using 8bit mode
                int permissions = Integer.parseInt(mode, 8);
                LOG.trace("Setting chmod: {} on file: ", mode, targetName);
                channel.chmod(permissions, targetName);
            }

            return true;
        
        } catch (SftpException e) {
            throw new GenericFileOperationFailedException("Cannot store file: " + name, e);
        } catch (InvalidPayloadException e) {
            throw new GenericFileOperationFailedException("Cannot store file: " + name, e);
        } catch (IOException e) {
        	throw new GenericFileOperationFailedException("Cannot store file: " + name, e);
		} finally {
            IOHelper.close(is, "store: " + name, LOG);
        }
    }
	
	private InputStream getIS(File file) throws IOException {
		return Files.newInputStreamSupplier(file).getInput();
	}
	
    @Override
	public boolean existsFile(String name) throws GenericFileOperationFailedException {
        LOG.trace("existsFile({})", name);
        if (endpoint.isFastExistsCheck()) {
            return fastExistsFile(name);
        }
        // check whether a file already exists
        String directory = FileUtil.onlyPath(name);
        if (directory == null) {
            // assume current dir if no path could be extracted
            directory = ".";
        }
        String onlyName = FileUtil.stripPath(name);

        try {
            @SuppressWarnings("rawtypes")
            Vector files = channel.ls(directory);
            // can return either null or an empty list depending on FTP servers
            if (files == null) {
                return false;
            }
            for (Object file : files) {
                ChannelSftp.LsEntry entry = (ChannelSftp.LsEntry) file;
                String existing = entry.getFilename();
                LOG.trace("Existing file: {}, target file: {}", existing, name);
                existing = FileUtil.stripPath(existing);
                if (existing != null && existing.equals(onlyName)) {
                    return true;
                }
            }
            return false;
        } catch (SftpException e) {
            // or an exception can be thrown with id 2 which means file does not exists
            if (ChannelSftp.SSH_FX_NO_SUCH_FILE == e.id) {
                return false;
            }
            // otherwise its a more serious error so rethrow
            throw new GenericFileOperationFailedException(e.getMessage(), e);
        }
    }

    protected boolean fastExistsFile(String name) throws GenericFileOperationFailedException {
        LOG.trace("fastExistsFile({})", name);
        try {
            @SuppressWarnings("rawtypes")
            Vector files = channel.ls(name);
            if (files == null) {
                return false;
            }
            return files.size() >= 1;
        } catch (SftpException e) {
            // or an exception can be thrown with id 2 which means file does not exists
            if (ChannelSftp.SSH_FX_NO_SUCH_FILE == e.id) {
                return false;
            }
            // otherwise its a more serious error so rethrow
            throw new GenericFileOperationFailedException(e.getMessage(), e);
        }

    }

    @Override
	public boolean sendNoop() throws GenericFileOperationFailedException {
        // is not implemented
        return true;
    }

    @Override
	public boolean sendSiteCommand(String command) throws GenericFileOperationFailedException {
        // is not implemented
        return true;
    }
}
