package de.mle.sandbox.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import de.mle.sandbox.EmbeddedKafkaInitializer;
import de.mle.sandbox.domain.ProductOpinion;
import de.mle.sandbox.domain.State;

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
		// given
		ProductOpinion savedProductOpinion = repository.save(new ProductOpinion("name", "email", "subject", 3, "comment", "127.0.0.1", "www.hostname.de",
				State.NEW));

		// when
		ProductOpinion foundOpinion = repository.findById(savedProductOpinion.getId()).get();

		assertThat(savedProductOpinion.getId()).isEqualTo(foundOpinion.getId());
		assertThat(savedProductOpinion.getRatingCount()).isEqualTo(foundOpinion.getRatingCount());
	}

	@Test
	public void findProductOpinionByState() {
		// given
		repository.save(new ProductOpinion("new one", "email", "subject", 3, "comment", "127.0.0.1", "www.hostname.de", State.NEW));
		repository.save(new ProductOpinion("approved one", "email", "subject", 3, "comment", "127.0.0.1", "www.hostname.de", State.APPROVED));

		// when
		List<ProductOpinion> opinions = repository.findByState(State.APPROVED);

		// then
		assertThat(opinions).hasSize(1);
		assertThat(opinions.iterator().next().getState()).isEqualTo(State.APPROVED);
	}
}