/*******************************************************************************
 * MuleCamelTest.java
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
package net.seedboxer.mule;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;

import java.util.Collections;

import net.seedboxer.core.domain.Configuration;
import net.seedboxer.core.type.Download;
import net.seedboxer.mule.processor.DownloadPusher;
import net.seedboxer.mule.processor.DownloadReceiver;
import net.seedboxer.mule.processor.DownloadRemover;
import net.seedboxer.mule.processor.QueuePooler;
import net.seedboxer.mule.processor.notification.EmailNotification;
import net.seedboxer.mule.processor.notification.GCMNotification;
import net.seedboxer.mule.processor.postaction.SSHCommandSender;
import net.seedboxer.mule.processor.transfer.TransferRouter;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
public class MuleCamelTest extends CamelSpringTestSupport {

	private static final String WATCHDOGFILE_ENDPOINT = "direct:watchDogFile";
	private static final String UPLOAD_ENDPOINT = "direct:upload";
	private static final Object FILE = "File";
	private static final String POOLING_ENDPOINT = "direct:pooling";
	private static final Object EMPTY_MESSAGE = "";

	private QueuePooler queuePooler;
	private DownloadReceiver downloadReceiver;
	private DownloadPusher downloadPusher;
	private DownloadRemover downloadRemover;
	private SSHCommandSender sshCommandSender;
	private EmailNotification emailNotification;
	private GCMNotification gcmNotification;
	private TransferRouter transferRouter;
	
	@Mock
	private Message msg;

	@Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("MuleCamelTest-context.xml");
    }

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		
		MockitoAnnotations.initMocks(this);
		
		queuePooler = applicationContext.getBean(QueuePooler.class);
		downloadReceiver = applicationContext.getBean(DownloadReceiver.class);
		downloadPusher = applicationContext.getBean(DownloadPusher.class);
		downloadRemover = applicationContext.getBean(DownloadRemover.class);
		sshCommandSender = applicationContext.getBean(SSHCommandSender.class);
		emailNotification = applicationContext.getBean(EmailNotification.class);
		gcmNotification = applicationContext.getBean(GCMNotification.class);
		transferRouter = applicationContext.getBean(TransferRouter.class);
		
		Mockito.when(msg.getBody()).thenReturn("Hello World");
	}

    @Test
    public void shouldNotContinueIfThereIsNoDownloadInQueue() throws Exception {
        getMockEndpoint("mock://bean:queuePooler").expectedBodiesReceived(EMPTY_MESSAGE);
        getMockEndpoint("mock://direct:processDownload").expectedMessageCount(0);

        template.sendBody(POOLING_ENDPOINT, EMPTY_MESSAGE);

        assertMockEndpointsSatisfied();
    }

    @Test
    public void shouldTransferADownloadAndNotify() throws Exception {
    	final Download download = new Download();
    	doAnswer(new Answer<Object>() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				Exchange ex = (Exchange) invocation.getArguments()[0];
				ex.getIn().setBody(Collections.singletonList(download));
				ex.getIn().setHeader(Configuration.DOWNLOAD_ID, 1);
				ex.getIn().setHeader(Configuration.SSH_CMD, "ls -l");
				ex.getIn().setHeader(Configuration.NOTIFICATION_GCM, "not null");
				ex.getIn().setHeader(Configuration.NOTIFICATION_EMAIL, "root@gmail.com");
				return null;
			}
		}).when(queuePooler).process(any(Exchange.class));

        getMockEndpoint("mock://bean:queuePooler").expectedBodiesReceived(EMPTY_MESSAGE);
        getMockEndpoint("mock://direct:processDownload").expectedBodiesReceived(download);
        getMockEndpoint("mock://bean:downloadReceiver").expectedBodiesReceived(download);
        getMockEndpoint("mock://bean:downloadParser").expectedBodiesReceived(download);
        getMockEndpoint("mock://bean:downloadHistory").expectedBodiesReceived(download);
        getMockEndpoint("mock://bean:sshCommandSender").expectedBodiesReceived(download);
        getMockEndpoint("mock://bean:emailNotification").expectedBodiesReceived(download);
        getMockEndpoint("mock://direct:email").expectedBodiesReceived(download);
        //getMockEndpoint("mock://bean:gcmNotification").expectedBodiesReceived(download);
        getMockEndpoint("mock://bean:downloadRemover").expectedBodiesReceived(download);
        getMockEndpoint("mock://direct:upload").expectedBodiesReceived(download);

        template.sendBody(POOLING_ENDPOINT, EMPTY_MESSAGE);

        assertMockEndpointsSatisfied();
	}

    @Test @Ignore
    public void shouldFailOnTransferADownloadAndNotify() throws Exception {
    	final Download download = new Download();
    	doAnswer(new Answer<Object>() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				Exchange ex = (Exchange) invocation.getArguments()[0];
				ex.getIn().setBody(Collections.singletonList(download));
				ex.getIn().setHeader(Configuration.DOWNLOAD_ID, 1);
				ex.getIn().setHeader(Configuration.SSH_CMD, "ls -l");
				ex.getIn().setHeader(Configuration.NOTIFICATION_GCM, "not null");
				ex.getIn().setHeader(Configuration.NOTIFICATION_EMAIL, "root@gmail.com");
				return null;
			}
		}).when(queuePooler).process(any(Exchange.class));

        getMockEndpoint("mock://bean:queuePooler").expectedBodiesReceived(EMPTY_MESSAGE);
        getMockEndpoint("mock://direct:processDownload").expectedBodiesReceived(download);
        getMockEndpoint("mock://bean:downloadReceiver").expectedBodiesReceived(download);
        getMockEndpoint("mock://bean:sshCommandSender").expectedMessageCount(0);
        getMockEndpoint("mock://bean:emailNotification").expectedBodiesReceived(download);
        getMockEndpoint("mock://direct:email").expectedBodiesReceived(download);
        //getMockEndpoint("mock://bean:gcmNotification").expectedBodiesReceived(download);
        getMockEndpoint("mock://bean:downloadRemover").expectedBodiesReceived(download);
        getMockEndpoint("mock://direct:upload").expectedBodiesReceived(download);

        template.sendBody(POOLING_ENDPOINT, EMPTY_MESSAGE);

        assertMockEndpointsSatisfied();
	}

}
