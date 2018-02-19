package de.mle.sandbox.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {
	@GetMapping("/")
	public String welcome() {
		return "Welcome at the sandbox – explore it via the HAL browser, located at /browser/index.html#/productOpinions";
	}
}