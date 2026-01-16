package com.xthst.ims.domain.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {
    private Long id;

    private String name;
    
    private String description;
    
    private String unitOfMeasure;
    
    private int reorderPoint;
    
    private int quantityOnHand;
}
