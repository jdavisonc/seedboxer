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
package net.seedboxer.seedboxer.mule;

import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
public class MuleCamelTest extends CamelSpringTestSupport {

	private static final String WATCHDOGFILE_ENDPOINT = "direct:watchDogFile";
	private static final String UPLOAD_ENDPOINT = "direct:upload";

	@Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("MuleCamelTest-context.xml");
    }

    @Test
    public void testAdvisedMockEndpoints() throws Exception {
        getMockEndpoint("mock://bean:fileReceiver").expectedBodiesReceived("Hello World");

        template.sendBody(WATCHDOGFILE_ENDPOINT, "Hello World");

        assertMockEndpointsSatisfied();
	}

    @Test
    public void testSendToTranfer() throws Exception {
        getMockEndpoint("mock://bean:ftpSender").expectedBodiesReceived("Hello World");
        getMockEndpoint("mock://direct:postActionsEndpoint").expectedBodiesReceived("Hello World");

        template.sendBody(UPLOAD_ENDPOINT, "Hello World");

        assertMockEndpointsSatisfied();
	}

}
