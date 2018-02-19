package de.mle.sandbox.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import de.mle.sandbox.domain.ProductOpinion;
import de.mle.sandbox.domain.State;

@RepositoryRestResource
/**
 * Watch out that org.springframework.data.repository.reactive.ReactiveCrudRepository is not yet supported by Spring Data REST,
 * see https://jira.spring.io/browse/DATAREST-933 for further progress!
 */
public interface ProductOpinionRepository extends PagingAndSortingRepository<ProductOpinion, String> {
	List<ProductOpinion> findByState(State state);
}