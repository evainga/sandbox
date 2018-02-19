package de.mle.sandbox.service;

import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;

import de.mle.sandbox.domain.ProductOpinion;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProductOpinionEventListener extends AbstractRepositoryEventListener<ProductOpinion> {
	@Override
	public void onBeforeCreate(ProductOpinion productOpinion) {
		log.warn("Before creating product opinion {}", productOpinion);
	}

	@Override
	public void onAfterCreate(ProductOpinion productOpinion) {
		log.warn("After creating product opinion {}", productOpinion);
	}

}
