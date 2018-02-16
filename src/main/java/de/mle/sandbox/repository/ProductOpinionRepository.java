package de.mle.sandbox.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import de.mle.sandbox.domain.ProductOpinion;

@RepositoryRestResource
public interface ProductOpinionRepository extends PagingAndSortingRepository<ProductOpinion, String> {

}