package de.mle.sandbox.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpringValidationError {
	private String entity;
	private String property;
	private String invalidValue;
	private String message;
}
