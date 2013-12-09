package net.seedboxer.camel.component.sftp2;

import org.apache.camel.component.file.remote.RemoteFileConfiguration;
import org.apache.camel.component.file.remote.RemoteFileOperations;
import org.apache.camel.component.file.remote.SftpComponent;
import org.apache.camel.component.file.remote.SftpEndpoint;

import com.jcraft.jsch.ChannelSftp;

public class Sftp2Endpoint extends SftpEndpoint {

	public Sftp2Endpoint() { }

	public Sftp2Endpoint(String uri, SftpComponent component,
			RemoteFileConfiguration configuration) {
		super(uri, component, configuration);
	}

	@Override
	public RemoteFileOperations<ChannelSftp.LsEntry> createRemoteFileOperations() {
		Sftp2Operations operations = new Sftp2Operations();
		operations.setEndpoint(this);
		return operations;
	}

}
