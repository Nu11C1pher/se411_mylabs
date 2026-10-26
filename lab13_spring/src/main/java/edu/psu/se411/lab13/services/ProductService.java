package edu.psu.se411.lab13.services;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import jakarta.validation.constraints.Positive;
import edu.psu.se411.lab13.model.Product;
import edu.psu.se411.lab13.repositories.ProductRepository;

@Service
@Validated
public class ProductService {
	private final ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public Iterable<Product> findAll() {
		return productRepository.findAll();
	}

	public Product findById(@Positive Integer id) {
		return productRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
	}
}
