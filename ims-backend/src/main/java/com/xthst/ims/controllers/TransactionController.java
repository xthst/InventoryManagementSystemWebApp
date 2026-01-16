package com.xthst.ims.controllers;

import java.util.List;
import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.xthst.ims.domain.dto.transaction.TransactionCreateRequest;
import com.xthst.ims.domain.dto.transaction.TransactionDto;
import com.xthst.ims.services.TransactionService;

import jakarta.validation.Valid;

@RestController
public class TransactionController {
	private final TransactionService transactionService;
	
	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}
	
	@GetMapping("/transaction/{id}")
	public ResponseEntity<TransactionDto> getTransaction(@PathVariable("id") Long id){
		return ResponseEntity.ok(transactionService.getTransaction(id));
	}
	
	@GetMapping("/transactions")
	public ResponseEntity<List<TransactionDto>> getProducts() {
		return ResponseEntity.ok(transactionService.getTransactions());
	}
	
	@PostMapping("/transaction")
	public ResponseEntity<TransactionDto> createTransaction(@Valid @RequestBody TransactionCreateRequest transactionCreateRequest) {
		TransactionDto transactionDto = transactionService.createTransaction(transactionCreateRequest);
		URI locationUri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(transactionDto.getId())
				.toUri();
		
		return ResponseEntity.created(locationUri)
				.body(transactionDto);
	}
	
	@DeleteMapping("/transaction/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) {
		transactionService.deleteTransaction(id);
		return ResponseEntity.noContent().build();
	}
}
