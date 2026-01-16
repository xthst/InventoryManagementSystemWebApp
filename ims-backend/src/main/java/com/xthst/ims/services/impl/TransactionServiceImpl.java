package com.xthst.ims.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.xthst.ims.domain.dto.product.ProductDto;
import com.xthst.ims.domain.dto.transaction.TransactionCreateRequest;
import com.xthst.ims.domain.dto.transaction.TransactionDto;
import com.xthst.ims.domain.entities.TransactionEntity;
import com.xthst.ims.domain.entities.TransactionEntity.TransactionType;
import com.xthst.ims.exceptions.ResourceNotFoundException;
import com.xthst.ims.mappers.ProductMapper;
import com.xthst.ims.mappers.TransactionMapper;
import com.xthst.ims.repositories.TransactionRepository;
import com.xthst.ims.services.ProductService;
import com.xthst.ims.services.TransactionService;

import jakarta.transaction.Transactional;

@Service
public class TransactionServiceImpl implements TransactionService {
	
	private TransactionRepository transactionRepo;
	private ProductService productService;
	private TransactionMapper transactionMapper;
	private ProductMapper productMapper;
	
	public TransactionServiceImpl(TransactionRepository transactionRepo,
			ProductService productService,
			TransactionMapper transactionMapper,
			ProductMapper productMapper) {
		this.transactionRepo = transactionRepo;
		this.productService = productService;
		this.transactionMapper = transactionMapper;
		this.productMapper = productMapper;
	}
	
	public TransactionDto getTransaction(Long id) {
		TransactionEntity transactionEntity = transactionRepo.findById(id).orElseThrow(() -> 
				new ResourceNotFoundException(ResourceNotFoundException.TRANSACTION, id));
		
		return transactionMapper.toDto(transactionEntity);
	}
	
	public List<TransactionDto> getTransactions() {
		List<TransactionEntity> transactions = transactionRepo.findAll();
		
		return transactions.stream()
				.map(transactionMapper::toDto)
				.collect(Collectors.toList());
	}
	
	@Transactional
	public TransactionDto createTransaction(TransactionCreateRequest transactionCreateRequest) {
		Long productId = transactionCreateRequest.getProductId();
		int quantity = transactionCreateRequest.getQuantity();
		TransactionEntity.TransactionType transactionType = transactionCreateRequest.getTransactionType();
		
		if (transactionType == TransactionType.OUTBOUND) {
			quantity = -quantity;
		}
		
		ProductDto productDto = productService.updateProductQuantityOnHand(productId, quantity);
		System.out.print("productDto in createTransaction = "+ productDto);
		
		TransactionEntity transactionEntity = transactionMapper.toEntity(transactionCreateRequest);
		transactionEntity.setProduct(productMapper.toEntity(productDto));
		return transactionMapper.toDto(transactionRepo.save(transactionEntity));
	}
	
	public void deleteTransaction(Long id) {
		TransactionEntity transactionEntity = transactionRepo.findById(id).orElseThrow(() -> 
				new ResourceNotFoundException(ResourceNotFoundException.TRANSACTION, id));
		
		transactionRepo.delete(transactionEntity);
	}
}
