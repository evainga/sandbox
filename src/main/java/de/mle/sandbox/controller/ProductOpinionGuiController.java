package de.mle.sandbox.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import de.mle.sandbox.repository.ProductOpinionRepository;

@RestController
public class ProductOpinionGuiController {

	@Autowired
	private ProductOpinionRepository productOpinionRepository;

	@GetMapping(path = "/productOpinions/gui")
	public ModelAndView productOpinionsGui() {
		return new ModelAndView("opinions", "opinions", productOpinionRepository.findAll());
	}

}