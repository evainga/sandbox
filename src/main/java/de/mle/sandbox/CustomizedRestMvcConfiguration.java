package de.mle.sandbox;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import de.mle.sandbox.domain.BeforeCreateValidator;

@Configuration
public class CustomizedRestMvcConfiguration extends RepositoryRestConfigurerAdapter {

	@Override
	/** Use the {@code config} object to configure Spring Data REST */
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
	}

	@Override
	public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {
		validatingListener.addValidator("beforeCreate", new BeforeCreateValidator());
		validatingListener.addValidator("beforeCreate", validator());
	}

	@Bean
	@Primary
	/**
	 * Create a validator to use in bean validation - primary to be able to autowire
	 * without qualifier
	 */
	Validator validator() {
		return new LocalValidatorFactoryBean();
	}
}
