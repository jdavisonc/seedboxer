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
package net.seedboxer.seedboxer.sources.thirdparty.trakt;

import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
public class MuleCamelTest  extends CamelSpringTestSupport {

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("MuleCamelTest-context.xml");
    }

    @Test @Ignore
    public void testAdvisedMockEndpoints() throws Exception {
        getMockEndpoint("mock://file:/home/test/seedboxer").expectedBodiesReceived("Hello World");
        getMockEndpoint("mock://bean:fileReceiver").expectedBodiesReceived("Hello World");

        template.sendBody("file:/home/test/seedboxer", "Hello World");

        assertMockEndpointsSatisfied();
	}

}
