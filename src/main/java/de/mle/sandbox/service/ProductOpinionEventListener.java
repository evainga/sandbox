package de.mle.sandbox.service;

import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;
import org.springframework.stereotype.Component;

import de.mle.sandbox.domain.ProductOpinion;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ProductOpinionEventListener extends AbstractRepositoryEventListener<ProductOpinion> {
	@Override
	public void onBeforeCreate(ProductOpinion productOpinion) {
		log.info("Before creating product opinion {}", productOpinion);
	}

	@Override
	public void onAfterCreate(ProductOpinion productOpinion) {
		log.info("After creating product opinion {}", productOpinion);
	}

}
