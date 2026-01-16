package com.xthst.ims.repositories;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import com.xthst.ims.domain.entities.TransactionEntity;

@Repository
public interface TransactionRepository extends ListCrudRepository<TransactionEntity, Long> {
	
	boolean existsByProductId(Long productId);
}
