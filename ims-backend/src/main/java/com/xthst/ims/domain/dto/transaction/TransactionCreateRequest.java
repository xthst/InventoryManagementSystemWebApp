package com.xthst.ims.domain.dto.transaction;

import java.time.Instant;

import com.xthst.ims.domain.entities.TransactionEntity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionCreateRequest {
	
	public static final String PRODUCT_ID_NULL_ERROR = "Product Id should be provided";
	public static final String PRODUCT_ID_VALUE_ERROR = "Product Id should be a negative integer";
	public static final String TRANSACTION_DATE_NULL_ERROR = "Transaction Date should be provided";
	public static final String TRANSACTION_TYPE_NULL_ERROR = "Invalid Transaction Type";
	public static final String QUANTITY_NULL_ERROR = "Quantity on Hand should not be null";
	public static final String QUANTITY_VALUE_ERROR = "Quantity on Hand should be atleast 1";
	public static final int MIN_QUANTITY = 0;
	
	@NotNull(message = PRODUCT_ID_NULL_ERROR)
	@Min(value = 0, message = PRODUCT_ID_NULL_ERROR)
	private Long productId;
	
	@NotNull(message = TRANSACTION_DATE_NULL_ERROR)
	private Instant transactionDate;
	
	@NotNull(message = TRANSACTION_TYPE_NULL_ERROR)
	private TransactionEntity.TransactionType transactionType;
	
	@NotNull(message = QUANTITY_NULL_ERROR)
	@Min(value = MIN_QUANTITY, message = QUANTITY_VALUE_ERROR)
	private int quantity;
	
	private String reference;
}
