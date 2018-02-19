package de.mle.sandbox.domain;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class BeforeCreateValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.isAssignableFrom(ProductOpinion.class);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ProductOpinion opinion = (ProductOpinion) target;
		conditionalValidation(opinion, errors);
	}

	private void conditionalValidation(ProductOpinion opinion, Errors errors) {
		if (opinion.getRatingCount() == 0 && opinion.getComment().contains("good"))
			errors.rejectValue("comment", "comment.rating-count.mismatch", "0 stars and a good comment do not fit together!");
	}

}
