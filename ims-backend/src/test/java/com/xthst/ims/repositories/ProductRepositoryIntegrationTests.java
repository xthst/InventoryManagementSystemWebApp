package com.xthst.ims.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.xthst.ims.domain.entities.ProductEntity;
import com.xthst.ims.util.TestDataUtil;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductRepositoryIntegrationTests {
	private ProductRepository productRepo;
	
	@Autowired
	public ProductRepositoryIntegrationTests(ProductRepository productRepo) {
		this.productRepo = productRepo;
	}
	
	@Test
	public void testThatProductCanBeCreatedAndRecalled() {
		ProductEntity productA = TestDataUtil.createProductEntityA();
		productRepo.save(productA);
		Optional<ProductEntity> result = productRepo.findById(productA.getId());
		assertThat(result).isPresent();
		assertThat(result.get()).isEqualTo(productA);
	}
	
	@Test
	public void testThatMultipleProductsCanBeRecalled() {
		ProductEntity productA = TestDataUtil.createProductEntityA();
		productRepo.save(productA);
		ProductEntity productB = TestDataUtil.createProductEntityB();
		productRepo.save(productB);
		ProductEntity productC = TestDataUtil.createProductEntityC();
		productRepo.save(productC);
			
		Iterable<ProductEntity> results = productRepo.findAll();
		assertThat(results).isNotEmpty();
		assertThat(results)
			.hasSize(3)
			.containsExactly(productA, productB, productC);
	}
	
	@Test
	public void testThatProductCanBeUpdated() {
		ProductEntity productA = TestDataUtil.createProductEntityA();
		productRepo.save(productA);
		
		Optional<ProductEntity> fetchedProduct = productRepo.findById(productA.getId());
		assertThat(fetchedProduct).isPresent();
		
		productA.setName("New Name");
		productA.setDescription("New Description");
		productRepo.save(productA);
		
		fetchedProduct = productRepo.findById(productA.getId());
		assertThat(fetchedProduct).isPresent();
				
		ProductEntity product = fetchedProduct.get();
		productA.setDateUpdated(product.getDateUpdated());
		assertThat(product).isEqualTo(productA);
	}
	
	@Test
	public void testThatProductCanBeDeleted() {
		ProductEntity productA = TestDataUtil.createProductEntityA();
		productRepo.save(productA);
		
		Optional<ProductEntity> fetchedProduct = productRepo.findById(productA.getId());
		assertThat(fetchedProduct).isPresent();
		productRepo.delete(productA);
		
		fetchedProduct = productRepo.findById(productA.getId());
		assertThat(fetchedProduct).isNotPresent();
	}
}
