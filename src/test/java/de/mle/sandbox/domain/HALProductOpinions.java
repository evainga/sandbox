package de.mle.sandbox.domain;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HALProductOpinions {
	@JsonProperty("_embedded")
	private EmbeddedProductOpinions embedded;
	@JsonProperty("_links")
	private Map<String, Object> links;
}
