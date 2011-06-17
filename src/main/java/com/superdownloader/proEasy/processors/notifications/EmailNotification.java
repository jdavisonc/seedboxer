package com.superdownloader.proEasy.processors.notifications;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.superdownloader.proEasy.processors.Headers;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 *
 * @author harley
 *
 */
@Service(value = "emailNotification")
public class EmailNotification extends Notification {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailNotification.class);

	private static final String TO_HEADER = "To";
	private static final String SUBJECT_HEADER = "Subject";
	private static final String CAUSE_VAR = "Cause";

	private static final String SUCCESS_SUBJECT_FTL = "success-subject.ftl";
	private static final String SUCCESS_BODY_FTL = "success-body.ftl";
	private static final String DISCARDED_SUBJECT_FTL = "discarded-subject.ftl";
	private static final String DISCARDED_BODY_FTL = "discarded-body.ftl";

	@Autowired
	private Configuration freemarkerConfiguration;

	@Override
	protected void processSuccessNotification(Message msg) {
		setEmailProperties(msg, SUCCESS_SUBJECT_FTL, SUCCESS_BODY_FTL, getParams(msg));
	}

	@Override
	protected void processFailNotification(Message msg, Throwable t) {
		Map<String, Object> params = getParams(msg);
		params.put(CAUSE_VAR, t);
		setEmailProperties(msg, DISCARDED_SUBJECT_FTL, DISCARDED_BODY_FTL, params);
	}

	/**
	 * Return parameters of Message for create Email
	 * @param msg
	 * @return
	 */
	private Map<String, Object> getParams(Message msg) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.putAll(msg.getHeaders());
		params.put(Headers.END_TIME, new Date());
		return params;
	}

	/**
	 * Set properties to be able for send a Email
	 * @param msg
	 * @param subjectTpl
	 * @param bodyTpl
	 * @param params
	 */
	private void setEmailProperties(Message msg, String subjectTpl, String bodyTpl, Map<String, Object> params) {
		String email = (String) msg.getHeader(Headers.NOTIFICATION_EMAIL);
		msg.setHeader(TO_HEADER, email);
		msg.setHeader(SUBJECT_HEADER, getProcessedTemplate(subjectTpl, params));
		msg.setBody(getProcessedTemplate(bodyTpl, params));
	}

	/**
	 * Process a Freemarker template and return the String created with it.
	 * @param template
	 * @param templateVars
	 * @return
	 */
	private String getProcessedTemplate(String template, Map<String, Object> templateVars) {
		try {
		    return FreeMarkerTemplateUtils.processTemplateIntoString(
		    		freemarkerConfiguration.getTemplate(template), templateVars);
		} catch (IOException e) {
			LOGGER.warn("Error at processing template", e);
		} catch (TemplateException e) {
			LOGGER.warn("Error at processing template", e);
		}
		return null;
	}
}
