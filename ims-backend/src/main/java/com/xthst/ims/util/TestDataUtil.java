package com.xthst.ims.util;

import java.time.Instant;

import com.xthst.ims.domain.dto.product.ProductCreateRequest;
import com.xthst.ims.domain.dto.product.ProductUpdateRequest;
import com.xthst.ims.domain.dto.transaction.TransactionCreateRequest;
import com.xthst.ims.domain.entities.ProductEntity;
import com.xthst.ims.domain.entities.TransactionEntity;
import com.xthst.ims.domain.entities.TransactionEntity.TransactionType;

public final class TestDataUtil {
	
	// =========================== PRODUCT ===========================
	public static ProductEntity createProductEntityA() {
		return ProductEntity.builder()
				.name("Product X")
				.description("Product X is a revolutionary product.")
				.unitOfMeasure("PCS")
				.reorderPoint(10)
				.quantityOnHand(20)
				.build();
	}
	
	public static ProductEntity createProductEntityB() {
		return ProductEntity.builder()
				.name("Product Y")
				.unitOfMeasure("EA")
				.reorderPoint(50)
				.quantityOnHand(20)
				.build();
	}
	
	public static ProductEntity createProductEntityC() {
		return ProductEntity.builder()
				.name("Product Z")
				.description("Product Z is a specialized, limited-run item.")
				.unitOfMeasure("BOX")
				.reorderPoint(15)
				.quantityOnHand(0)
				.build();
	}
	
	// ProductCreateRequest
	public static ProductCreateRequest createValidProductCreateRequestA() {
		return ProductCreateRequest.builder()
				.name("Valid Product A")
				.description("Valid description")
				.unitOfMeasure("PCS")
				.reorderPoint(10)
				.quantityOnHand(100)
				.build();
	}
	
	public static ProductCreateRequest createValidProductCreateRequestB() {
		return ProductCreateRequest.builder()
				.name("Valid Product B")
				.unitOfMeasure("BOX")
				.reorderPoint(20)
				.quantityOnHand(144)
				.build();
	}
	
	public static ProductCreateRequest createInvalidProductCreateRequest() {
		return ProductCreateRequest.builder()
				.name("")
				.unitOfMeasure("")
				.reorderPoint(0)
				.quantityOnHand(-1)
				.build();
	}
	
	// ProductUpdateRequest
	public static ProductUpdateRequest createUpdateProductRequest() {
		return ProductUpdateRequest.builder()
				.name("Updated Name")
				.description("Update description")
				.unitOfMeasure("PIECES")
				.reorderPoint(100)
				.quantityOnHand(1)
				.build();
	}
	
	// =========================== TRANSACTIONS ===========================
	public static TransactionEntity createTransactionEntityA(ProductEntity product) {
		return TransactionEntity.builder()
				.product(product)
				.transactionDate(Instant.parse("2020-01-01T08:24:30+08:00"))
				.transactionType(TransactionType.INBOUND)
				.quantity(100)
				.reference("Test Reference")
				.build();
	}
	
	public static TransactionEntity createTransactionEntityB(ProductEntity product) {		
		return TransactionEntity.builder()
				.product(product)
				.transactionDate(Instant.parse("2020-01-01T08:24:30+08:00"))
				.transactionType(TransactionType.OUTBOUND)
				.quantity(2000)
				.reference("Reference 2")
				.build();
	}
	
	public static TransactionEntity createTransactionEntityC(ProductEntity product) {		
		return TransactionEntity.builder()
				.product(product)
				.transactionDate(Instant.parse("2020-01-01T08:24:30+08:00"))
				.transactionType(TransactionType.OUTBOUND)
				.quantity(1)
				.reference("Outbound ProductA")
				.build();
	}
	
	// TransactionCreateRequest
	public static TransactionCreateRequest createValidTransactionCreateRequestA(Long productId) {
		return TransactionCreateRequest.builder()
				.productId(productId)
				.transactionDate(Instant.parse("2020-01-01T08:24:30+08:00"))
				.transactionType(TransactionType.INBOUND)
				.quantity(33)
				.build();
	}
	
	public static TransactionCreateRequest createValidTransactionCreateRequestB(Long productId) {
		return TransactionCreateRequest.builder()
				.productId(productId)
				.transactionDate(Instant.parse("2020-01-01T08:24:30+08:00"))
				.transactionType(TransactionType.OUTBOUND)
				.quantity(67)
				.build();
	}
	
	public static TransactionCreateRequest createInvalidTransactionCreateRequest(Long productId) {
		return TransactionCreateRequest.builder()
				.productId(null)
				.transactionDate(null)
				.transactionType(null)
				.quantity(-1)
				.build();
	}
	
}
