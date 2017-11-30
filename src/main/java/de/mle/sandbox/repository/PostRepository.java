package de.mle.sandbox.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import de.mle.sandbox.domain.Post;

public interface PostRepository extends ReactiveMongoRepository<Post, String> {
}