package com.example.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.entities.Category;
import com.example.entities.Product;
import com.example.repositories.CategoryRepository;
import com.example.repositories.ProductRepository;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@GetMapping
	public Page<Product> getAllProducts(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size // admin-defined default
	) {
		Pageable pageable = PageRequest.of(page, size);
		return productRepository.findAll(pageable);
	}

	@PostMapping
	public ResponseEntity<Product> createProduct(@RequestBody Product product) {
		if (product.getCategory() != null) {
			Optional<Category> category = categoryRepository.findById(product.getCategory().getId());
			if (category.isPresent()) {
				product.setCategory(category.get());
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
		return ResponseEntity.ok(productRepository.save(product));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable Long id) {
		Optional<Product> product = productRepository.findById(id);
		return product.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/{id}")
	public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
		return productRepository.findById(id).map(product -> {
			product.setName(productDetails.getName());
			product.setPrice(productDetails.getPrice());
			if (productDetails.getCategory() != null) {
				Optional<Category> category = categoryRepository.findById(productDetails.getCategory().getId());
				category.ifPresent(product::setCategory);
			}
			return ResponseEntity.ok(productRepository.save(product));
		}).orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteProduct(@PathVariable Long id) {
		return productRepository.findById(id).map(product -> {
			productRepository.delete(product);
			return ResponseEntity.ok().build();
		}).orElse(ResponseEntity.notFound().build());
	}
}
