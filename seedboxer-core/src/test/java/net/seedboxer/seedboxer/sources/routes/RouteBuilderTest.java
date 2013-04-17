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

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
public class RouteBuilderTest extends CamelTestSupport {

	private static final String RSS_TIMER_ENDPOINT = "direct:rss";

	private static final String MERGE_FEEDS_ENDPOINT = "mock:direct:feeds";

	private RouteBuilder routeBuilder;

	@Mock
	private FeedsManager feedsManager;

    @EndpointInject(uri = MERGE_FEEDS_ENDPOINT)
    protected MockEndpoint resultEndpoint;

    @Produce(uri=RSS_TIMER_ENDPOINT)
    protected ProducerTemplate template;

	@Override
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		RssFeed feed = new RssFeed();
		feed.setUrl("mock:direct:feed1");
		when(feedsManager.getAllFeeds()).thenReturn(Collections.singletonList(feed));

		super.setUp();
	}

    @Test
    public void shouldProcessAllFeedWhenItsFirstTime() throws Exception {
        resultEndpoint.expectedMessageCount(4);

        template.sendBody(getClass().getResource("feed.rss"));

        resultEndpoint.assertIsSatisfied();
    }

    @Test
    public void shouldProcessOnlyNewFeedWhenItsSecondTime() throws Exception {
        resultEndpoint.expectedMessageCount(5);

        template.sendBody(getClass().getResource("feed.rss"));
        template.sendBody(getClass().getResource("feed2.rss"));

        resultEndpoint.assertIsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        routeBuilder = new RouteBuilder();
        routeBuilder.setFeedsManager(feedsManager);
        routeBuilder.setRssTimerEndpoint(RSS_TIMER_ENDPOINT);
        routeBuilder.setMergeFeedsEndoint(MERGE_FEEDS_ENDPOINT);
        return routeBuilder;
    }

}
