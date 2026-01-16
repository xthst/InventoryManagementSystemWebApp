package com.xthst.ims.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.xthst.ims.domain.dto.product.ProductCreateRequest;
import com.xthst.ims.domain.dto.product.ProductDto;
import com.xthst.ims.domain.dto.product.ProductUpdateRequest;

@Service
public interface ProductService {
	
	public ProductDto getProduct(Long id);
	
	public List<ProductDto> getProducts();
	
	public ProductDto createProduct(ProductCreateRequest productCreateRequest);
	
	public ProductDto editProduct(Long id, 
			ProductUpdateRequest productUpdateRequest);
	
	public void deleteProduct(Long id);
	
	public ProductDto updateProductQuantityOnHand(Long id, 
			int quantity);
	
}
