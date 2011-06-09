package com.superdownloader.proEasy.processors;

import java.io.IOException;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

@Service(value = "emailProcessor")
public class EmailProcessor implements Processor {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailProcessor.class);

	private static final String TO_HEADER = "To";
	private static final String SUBJECT_HEADER = "Subject";

	private static final String SUCCESS_SUBJECT_FTL = "success-subject.ftl";
	private static final String SUCCESS_BODY_FTL = "success-body.ftl";
	private static final String DISCARDED_SUBJECT_FTL = "discarded-subject.ftl";
	private static final String DISCARDED_BODY_FTL = "discarded-body.ftl";

	@Autowired
	private Configuration freemarkerConfiguration;

	@Override
	public void process(Exchange exchange) throws Exception {
		Message msg = exchange.getIn();

		String email = (String) msg.getHeader(Headers.NOTIFICATION_EMAIL);
		msg.setHeader(TO_HEADER, email);

		if (exchange.getProperty(Exchange.FAILURE_ENDPOINT) == null) {
			msg.setHeader(SUBJECT_HEADER, getProcessedTemplate(SUCCESS_SUBJECT_FTL, msg.getHeaders()));
			msg.setBody(getProcessedTemplate(SUCCESS_BODY_FTL, msg.getHeaders()));
		} else {
			msg.setHeader(SUBJECT_HEADER, getProcessedTemplate(DISCARDED_SUBJECT_FTL, msg.getHeaders()));
			msg.setBody(getProcessedTemplate(DISCARDED_BODY_FTL, exchange.getProperties()));
		}
	}

	private String getProcessedTemplate(String template, Map<String, Object> templateVars) {
		StringBuffer content = new StringBuffer();

		try {
		    content.append(FreeMarkerTemplateUtils.processTemplateIntoString(
		    		freemarkerConfiguration.getTemplate(template), templateVars));
		} catch (IOException e) {
			LOGGER.warn("Error at processing template", e);
		} catch (TemplateException e) {
			LOGGER.warn("Error at processing template", e);
		}
		return content.toString();
	}

}
