package de.mle.sandbox.domain;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmbeddedProductOpinions {
	private List<ProductOpinion> productOpinions;
}
