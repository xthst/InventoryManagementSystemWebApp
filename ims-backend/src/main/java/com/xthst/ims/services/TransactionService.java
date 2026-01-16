package com.xthst.ims.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.xthst.ims.domain.dto.transaction.TransactionCreateRequest;
import com.xthst.ims.domain.dto.transaction.TransactionDto;

@Service
public interface TransactionService {
	
	public TransactionDto getTransaction(Long id);
	
	public List<TransactionDto> getTransactions();
	
	public TransactionDto createTransaction(TransactionCreateRequest transactionCreateRequest);
	
	public void deleteTransaction(Long id);
}
