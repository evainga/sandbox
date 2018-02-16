package de.mle.sandbox;

import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.stereotype.Component;

@Component
public class CustomizedRestMvcConfiguration extends RepositoryRestConfigurerAdapter {

	@Override
	/** Use the {@code config} object to configure Spring Data REST */
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
	}
}
