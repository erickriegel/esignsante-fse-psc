package fr.ans.api.sign.esignsante.psc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
/*
 * POur interception et trace des requêtes entrantes.
 * associé à la propriété Logging.level.org.springframework.web.filter.CommonsRequestLoggingFilter de applicationproperties*/

public class RequestLoggingFilterConfig {
	 public CommonsRequestLoggingFilter logFilter() {
	        CommonsRequestLoggingFilter filter
	          = new CommonsRequestLoggingFilter();
	        filter.setIncludeQueryString(true);
	        filter.setIncludePayload(true);
	        filter.setMaxPayloadLength(10000);
	        filter.setIncludeHeaders(false);
	        filter.setAfterMessagePrefix("REQUEST DATA : ");
	        return filter;
	    }
}
