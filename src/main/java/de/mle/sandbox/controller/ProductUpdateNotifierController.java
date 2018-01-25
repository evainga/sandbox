package de.mle.sandbox.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import de.mle.sandbox.domain.Product;
import de.mle.sandbox.kafka.producer.Sender;

@RestController
public class ProductUpdateNotifierController {
	@Autowired
	private Sender sender;

	@GetMapping("/notifyProductUpdate/{productId}")
	public ResponseEntity<?> notifyUpdate(@PathVariable long productId) {
		// TODO: load via DB
		Product product = new Product(productId, "n/a");
		sender.send("productUpdateNotification", product);
		return ResponseEntity.noContent().build();
	}
}
