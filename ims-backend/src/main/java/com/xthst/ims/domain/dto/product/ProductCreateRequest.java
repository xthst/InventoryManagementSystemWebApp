package com.xthst.ims.domain.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductCreateRequest {

	public static final String NAME_BLANK_ERROR = "Name should not be blank";
	public static final String UNIT_OF_MEASURE_BLANK_ERROR = "Unit of Measure should not be blank";
	public static final String REORDER_POINT_NULL_ERROR = "Reorder Point should not be null";
	public static final String REORDER_POINT_VALUE_ERROR = "Reorder Point should be atleast 1";
	public static final String QUANTITY_NULL_ERROR = "Quantity on Hand should not be null";
	public static final String QUANTITY_VALUE_ERROR = "Quantity on Hand should be atleast 1";
	public static final int MIN_REORDER_POINT = 1;
	public static final int MIN_QUANTITY = 0;
	
	@NotBlank(message = NAME_BLANK_ERROR)
    private String name;
    
    private String description;
    
    @NotBlank(message = UNIT_OF_MEASURE_BLANK_ERROR)
    private String unitOfMeasure;
    
    @NotNull(message = REORDER_POINT_NULL_ERROR)
    @Min(value = MIN_REORDER_POINT, message = REORDER_POINT_VALUE_ERROR)
    private int reorderPoint;
    
    @NotNull(message = QUANTITY_NULL_ERROR)
    @Min(value = MIN_QUANTITY, message = QUANTITY_VALUE_ERROR)
    private int quantityOnHand;
}
