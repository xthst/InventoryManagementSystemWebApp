package com.xthst.ims.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.xthst.ims.domain.entities.ProductEntity;
import com.xthst.ims.domain.entities.TransactionEntity;
import com.xthst.ims.util.TestDataUtil;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TransactionRepositoryIntegrationTests {
	
	private TransactionRepository transactionRepo;
	private ProductRepository productRepo;
	private ProductEntity product;
	
	@Autowired
	public TransactionRepositoryIntegrationTests(TransactionRepository transactionRepo, ProductRepository productRepo) {
		this.transactionRepo = transactionRepo;
		this.productRepo = productRepo;
	}
	
	@BeforeEach
	public void setup() {
		ProductEntity product = TestDataUtil.createProductEntityA();
		productRepo.save(product);
	}
	
	@Test
	public void testThatTransactionCanBeCreatedAndRecalled() {
		TransactionEntity transactionA = TestDataUtil.createTransactionEntityA(this.product);
		transactionRepo.save(transactionA);
		Optional<TransactionEntity> result = transactionRepo.findById(transactionA.getId());
		assertThat(result).isPresent();
		assertThat(result.get()).isEqualTo(transactionA);
	}
	
	@Test
	public void testThatMultipleTransactionsCanBeRecalled() {		
		TransactionEntity transactionA = TestDataUtil.createTransactionEntityA(this.product);
		transactionRepo.save(transactionA);
		TransactionEntity transactionB = TestDataUtil.createTransactionEntityB(this.product);
		transactionRepo.save(transactionB);
		TransactionEntity transactionC = TestDataUtil.createTransactionEntityC(this.product);
		transactionRepo.save(transactionC);
			
		Iterable<TransactionEntity> results = transactionRepo.findAll();
		assertThat(results).isNotEmpty();
		assertThat(results)
			.hasSize(3)
			.containsExactly(transactionA, transactionB, transactionC);
	}
	
	@Test
	public void testThatTransactionCanBeUpdated() {
		TransactionEntity transactionA = TestDataUtil.createTransactionEntityA(this.product);
		transactionRepo.save(transactionA);
		
		Optional<TransactionEntity> result = transactionRepo.findById(transactionA.getId());
		assertThat(result).isPresent();
		
		transactionA.setTransactionDate(Instant.parse("2020-01-01T08:24:30+08:00"));
		transactionA.setQuantity(9999);
		transactionRepo.save(transactionA);
		
		result = transactionRepo.findById(transactionA.getId());
		assertThat(result).isPresent();
		
		TransactionEntity transaction = result.get();
		assertThat(transaction).isEqualTo(transactionA);
	}
	
	@Test
	public void testThatTransactionCanBeDeleted() {
		TransactionEntity transactionA = TestDataUtil.createTransactionEntityA(this.product);
		transactionRepo.save(transactionA);
		
		Optional<TransactionEntity> result = transactionRepo.findById(transactionA.getId());
		assertThat(result).isPresent();
		transactionRepo.delete(transactionA);
		
		result = transactionRepo.findById(transactionA.getId());
		assertThat(result).isNotPresent();
	}
}
