package de.mle.sandbox.domain;

import java.util.UUID;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
	@NotBlank
	private String name;
	@Email
	@NotBlank
	private String email;
	@NotBlank
	private String subject;
	@Min(0)
	@Max(5)
	private int ratingCount;
	@NotBlank
	private String comment;
	@NotBlank
	private String clientIp;
	@NotBlank
	private String hostName;
	@NotNull
	private State state;
}
