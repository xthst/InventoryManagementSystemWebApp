package com.xthst.ims.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.xthst.ims.domain.dto.product.ProductCreateRequest;
import com.xthst.ims.domain.dto.product.ProductDto;
import com.xthst.ims.domain.dto.product.ProductUpdateRequest;
import com.xthst.ims.services.ProductService;

import jakarta.validation.Valid;

@RestController
public class ProductController {
	
	private final ProductService productService;
	
	public ProductController(ProductService productService) {
		this.productService = productService;
	}
	
	@GetMapping("/product/{id}")
	public ResponseEntity<ProductDto> getProduct(@PathVariable("id") Long id){
		return ResponseEntity.ok(productService.getProduct(id));
	}
	
	@GetMapping("/products")
	public ResponseEntity<List<ProductDto>> getProducts() {
		return ResponseEntity.ok(productService.getProducts());
	}
	
	@PostMapping("/product")
	public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductCreateRequest productCreateRequest) {
		ProductDto productDto = productService.createProduct(productCreateRequest);
		URI locationUri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(productDto.getId())
				.toUri();
		
		return ResponseEntity.created(locationUri)
				.body(productDto);
	}
	
	@PutMapping("/product/{id}")
	public ResponseEntity<ProductDto> editProduct(@PathVariable("id") Long id,
			@Valid @RequestBody ProductUpdateRequest productUpdateRequest) {
		return ResponseEntity.ok(productService.editProduct(id, productUpdateRequest));
	}
	
	@DeleteMapping("/product/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) {
		productService.deleteProduct(id);
		return ResponseEntity.noContent().build();
	}
}
