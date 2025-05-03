package com.example.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.entities.Category;
import com.example.repositories.CategoryRepository;

import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

	@Autowired
	private CategoryRepository categoryRepository;

	@GetMapping
	public Page<Category> getAllCategories(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {
		Pageable pageable = PageRequest.of(page, size);
		return categoryRepository.findAll(pageable);
	}

	@PostMapping
	public Category createCategory(@RequestBody Category category) {
		return categoryRepository.save(category);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
		Optional<Category> category = categoryRepository.findById(id);
		return category.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PutMapping("/{id}")
	public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category categoryDetails) {
		return categoryRepository.findById(id).map(category -> {
			category.setName(categoryDetails.getName());
			return ResponseEntity.ok(categoryRepository.save(category));
		}).orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteCategory(@PathVariable Long id) {
		return categoryRepository.findById(id).map(category -> {
			categoryRepository.delete(category);
			return ResponseEntity.ok().build();
		}).orElse(ResponseEntity.notFound().build());
	}
}
