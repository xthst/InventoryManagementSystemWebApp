package com.xthst.ims.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

import com.xthst.ims.domain.entities.ProductEntity;
import com.xthst.ims.repositories.ProductRepository;
import com.xthst.ims.repositories.TransactionRepository;
import com.xthst.ims.util.TestDataUtil;

@Configuration
@Profile("dev")
public class DatabaseSeeder {
	
//	@Bean
//	CommandLineRunner initDatabase(ProductRepository productRepository, TransactionRepository transactionRepository) {
//		return args -> {
//			if (productRepository.count() == 0) {
//				for (int i = 0; i < 50; i++) {
//					productRepository.save(TestDataUtil.createProductEntityA());
//					productRepository.save(TestDataUtil.createProductEntityB());
//					productRepository.save(TestDataUtil.createProductEntityC());
//				}
//				
//				System.out.println("Database seeded with sample products");
//			}
//			
//			if (transactionRepository.count() == 0) {
//				ProductEntity productEntityA = productRepository.save(TestDataUtil.createProductEntityA());
//				
//				for (int i = 0; i < 50; i++) {
//					transactionRepository.save(TestDataUtil.createTransactionEntityA(productEntityA));
//					transactionRepository.save(TestDataUtil.createTransactionEntityB(productEntityA));
//					transactionRepository.save(TestDataUtil.createTransactionEntityC(productEntityA));
//				}
//				System.out.println("Database seeded with sample transactions");
//			}
//		};
		
		
//	}
}
