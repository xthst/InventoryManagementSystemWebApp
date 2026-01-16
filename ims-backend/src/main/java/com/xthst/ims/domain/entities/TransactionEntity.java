package com.xthst.ims.domain.entities;

import java.time.Instant;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="transactions")
public class TransactionEntity {
	
	public enum TransactionType {
		INBOUND,
		OUTBOUND
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="transaction_id_seq")
	private Long id;
	
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "product_id")
	private ProductEntity product;
	
	private Instant transactionDate;
	
	private TransactionType transactionType;
	
	private int quantity;
	
	private String reference;
	
}
