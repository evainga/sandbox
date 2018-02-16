package de.mle.sandbox.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import de.mle.sandbox.EmbeddedKafkaInitializer;
import de.mle.sandbox.domain.ProductOpinion;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProductOpinionRepositoryIT extends EmbeddedKafkaInitializer {
	@Autowired
	private ProductOpinionRepository repository;

	@Before
	@After
	public void clearDb() {
		repository.deleteAll();
	}

	@Test
	public void findProductOpinionById() {
		ProductOpinion productOpinion = new ProductOpinion("name", "email", "subject", "rating", "comment");

		productOpinion = repository.save(productOpinion);

		Optional<ProductOpinion> opinions = repository.findById(productOpinion.getId());

		assertThat(opinions).isPresent();
		assertThat(opinions.get()).isEqualTo(productOpinion);
	}
}