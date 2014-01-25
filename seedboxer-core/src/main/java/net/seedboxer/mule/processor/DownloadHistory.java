
package net.seedboxer.mule.processor;

import net.seedboxer.core.domain.Configuration;
import net.seedboxer.core.domain.Content;
import net.seedboxer.core.domain.User;
import net.seedboxer.sources.filter.FilterManager;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
@Component
public class DownloadHistory implements Processor {
	
	@Autowired
	private FilterManager filterManager;

	@Override
	public void process(Exchange exchange) throws Exception {
		Message msg = exchange.getIn();
		
		User user = msg.getHeader(Configuration.USER, User.class);
		Content content = msg.getHeader(Configuration.CONTENT, Content.class);
		filterManager.saveToHistory(content, user);
	}
	
	

}
