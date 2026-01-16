package com.xthst.ims.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.xthst.ims.domain.dto.product.ProductCreateRequest;
import com.xthst.ims.domain.dto.product.ProductDto;
import com.xthst.ims.domain.dto.product.ProductUpdateRequest;
import com.xthst.ims.domain.entities.ProductEntity;


@Mapper(componentModel = "spring", 
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {
	
	ProductDto toDto(ProductEntity entity);
	
	@Mapping(target = "dateCreated", ignore = true)
	@Mapping(target = "dateUpdated", ignore = true)
	ProductEntity toEntity(ProductDto productDto);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "dateCreated", ignore = true)
	@Mapping(target = "dateUpdated", ignore = true)
	ProductEntity toEntity(ProductCreateRequest productCreateRequest);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "dateCreated", ignore = true)
	@Mapping(target = "dateUpdated", ignore = true)
	void toEntity(@MappingTarget ProductEntity productEntity, 
			ProductUpdateRequest productUpdateRequest);
}

