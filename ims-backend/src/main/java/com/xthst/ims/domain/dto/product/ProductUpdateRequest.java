package com.xthst.ims.domain.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductUpdateRequest {
    private String name;
    
    private String description;
    
    private String unitOfMeasure;
    
    private int reorderPoint;
    
    private int quantityOnHand;
}
