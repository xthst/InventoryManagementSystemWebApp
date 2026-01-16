package com.xthst.ims.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.xthst.ims.domain.dto.product.ProductCreateRequest;
import com.xthst.ims.domain.dto.product.ProductDto;
import com.xthst.ims.domain.dto.product.ProductUpdateRequest;
import com.xthst.ims.domain.entities.ProductEntity;
import com.xthst.ims.exceptions.ProductLinkedToTransactionException;
import com.xthst.ims.exceptions.ResourceNotFoundException;
import com.xthst.ims.mappers.ProductMapper;
import com.xthst.ims.repositories.ProductRepository;
import com.xthst.ims.repositories.TransactionRepository;
import com.xthst.ims.services.ProductService;

import jakarta.transaction.Transactional;

@Service
public class ProductServiceImpl implements ProductService{
	
	private final ProductRepository productRepo;
	private final TransactionRepository transactionRepo;
	private ProductMapper productMapper;
	
	public ProductServiceImpl(ProductRepository productRepo,
			TransactionRepository transactionRepo,
			ProductMapper productMapper) {
		this.productRepo = productRepo;
		this.transactionRepo = transactionRepo;
		this.productMapper = productMapper;
	}
	
	public ProductDto getProduct(Long id) {
		ProductEntity productEntity = productRepo.findById(id).orElseThrow(() -> 
				new ResourceNotFoundException(ResourceNotFoundException.PRODUCT, id));
		
		return productMapper.toDto(productEntity);
	}
	
	public List<ProductDto> getProducts(){
		List<ProductEntity> products = productRepo.findAll();
		
		return products.stream()
				.map(productMapper::toDto)
				.collect(Collectors.toList());
	}
	
	public ProductDto createProduct(ProductCreateRequest productCreateRequest) {
		ProductEntity productEntity = productMapper.toEntity(productCreateRequest);
		return productMapper.toDto(productRepo.save(productEntity));
	}
	
	public ProductDto editProduct(Long id, ProductUpdateRequest productUpdateRequest) {
		ProductEntity existingProduct = productRepo.findById(id).orElseThrow(() -> 
			new ResourceNotFoundException(ResourceNotFoundException.PRODUCT, id));
		
		productMapper.toEntity(existingProduct, productUpdateRequest);
		
		return productMapper.toDto(productRepo.save(existingProduct));
	}
	
	public void deleteProduct(Long id) {
		ProductEntity existingProduct = productRepo.findById(id).orElseThrow(() -> 
			new ResourceNotFoundException(ResourceNotFoundException.PRODUCT, id)
		);
		
		if (transactionRepo.existsByProductId(id)) {
			throw new ProductLinkedToTransactionException();
		}
				
		productRepo.delete(existingProduct);
	}
	
	@Transactional
	public ProductDto updateProductQuantityOnHand(Long id,
			int quantityChange) {
		ProductEntity existingProduct = productRepo.findById(id).orElseThrow(() -> 
			new ResourceNotFoundException(ResourceNotFoundException.PRODUCT, id)
		);
				
		existingProduct.setQuantityOnHand(existingProduct.getQuantityOnHand() + quantityChange);
		System.out.print("existingProduct in the ProductService = " + existingProduct);
		
		return productMapper.toDto(productRepo.save(existingProduct));
	}
}
