package com.xthst.ims.repositories;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import com.xthst.ims.domain.entities.ProductEntity;

@Repository
public interface ProductRepository extends ListCrudRepository<ProductEntity, Long> {
	
}
