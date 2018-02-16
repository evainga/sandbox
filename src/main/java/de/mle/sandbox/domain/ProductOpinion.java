package de.mle.sandbox.domain;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
public class ProductOpinion {
	@Id
	private final String id = UUID.randomUUID().toString();
	private String name;
	private String email;
	private String subject;
	private String rating;
	private String comment;
}
