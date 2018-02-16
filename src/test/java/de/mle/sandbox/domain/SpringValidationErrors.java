package de.mle.sandbox.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpringValidationErrors {
	private List<SpringValidationError> errors;
}
