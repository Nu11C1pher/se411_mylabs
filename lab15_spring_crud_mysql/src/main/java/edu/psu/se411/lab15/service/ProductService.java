package edu.psu.se411.lab15.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.psu.se411.lab15.exception.ObjectNotFoundException;
import edu.psu.se411.lab15.model.Product;
import edu.psu.se411.lab15.repository.ProductRepository;

@Service
public class ProductService {

	private final ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public Iterable<Product> getAllProducts() {
		return productRepository.findAll();
	}

	public Optional<Product> getProductById(Long id) {
		return productRepository.findById(id);
	}

	public Product createProduct(Product product) {
		return productRepository.save(product);
	}

	@Transactional
	public void updateProduct(Long id, Product product) {
		productRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Product not found"));
		Product updatedProduct = new Product(id, product.name(), product.description(), product.price());
		productRepository.save(updatedProduct);
	}

	@Transactional
	public void deleteProduct(Long id) {
		productRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Product not found"));

		productRepository.deleteById(id);
	}

}
