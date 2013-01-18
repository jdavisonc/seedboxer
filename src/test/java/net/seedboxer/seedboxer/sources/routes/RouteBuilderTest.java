/*******************************************************************************
 * RouteBuilderTest.java
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
package net.seedboxer.seedboxer.sources.routes;

import static org.mockito.Mockito.when;

import java.util.Collections;

import net.seedboxer.seedboxer.core.domain.RssFeed;
import net.seedboxer.seedboxer.core.logic.FeedsManager;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.direct.DirectEndpoint;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
public class RouteBuilderTest extends CamelTestSupport {

	private RouteBuilder routeBuilder;

	@Mock
	private FeedsManager feedsManager;

    @EndpointInject(uri = "mock:direct:mergeFeeds")
    protected MockEndpoint resultEndpoint;

    @Produce(uri="rssTimer")
    protected ProducerTemplate template;

    @Override
    protected CamelContext createCamelContext() throws Exception {
    	CamelContext context = super.createCamelContext();
    	context.addEndpoint("rssTimer", new DirectEndpoint("direct:start", null));
    	return context;
    }

	@Override
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		RssFeed feed = new RssFeed();
		feed.setUrl("file:" + getClass().getResource("feed.rss").getPath());
		when(feedsManager.getAllFeeds()).thenReturn(Collections.singletonList(feed));

		super.setUp();
	}

    @Test @Ignore
    public void testSendNotMatchingMessage() throws Exception {
        resultEndpoint.expectedMessageCount(4);

        template.sendBody(getClass().getResource("feed.rss"));

        resultEndpoint.assertIsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        routeBuilder = new RouteBuilder();
        routeBuilder.setFeedsManager(feedsManager);
        return routeBuilder;
    }

}
