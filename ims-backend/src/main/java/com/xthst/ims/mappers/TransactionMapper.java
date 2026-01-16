package com.xthst.ims.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.xthst.ims.domain.dto.transaction.TransactionCreateRequest;
import com.xthst.ims.domain.dto.transaction.TransactionDto;
import com.xthst.ims.domain.entities.TransactionEntity;

@Mapper(componentModel = "spring",
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TransactionMapper {
	
	TransactionDto toDto(TransactionEntity entity);
	
	TransactionEntity toEntity(TransactionDto transactionDto);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "product", ignore = true)
	TransactionEntity toEntity(TransactionCreateRequest transactionCreateRequest);
}
