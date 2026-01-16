package com.xthst.ims.domain.dto.transaction;

import java.time.Instant;

import com.xthst.ims.domain.dto.product.ProductDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDto {
	private Long id;
	
	private ProductDto product;
	
	private Instant transactionDate;
	
	private String transactionType;
	
	private int quantity;
	
	private String reference;
}
