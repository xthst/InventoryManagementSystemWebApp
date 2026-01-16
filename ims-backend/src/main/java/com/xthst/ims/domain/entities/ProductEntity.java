package com.xthst.ims.domain.entities;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="products")
public class ProductEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="product_id_seq")
    private Long id;

    private String name;
    
    private String description;
    
    private String unitOfMeasure;
    
    private int reorderPoint;
    
    private int quantityOnHand;
    
    @CreationTimestamp
    private Instant dateCreated;
    
    @UpdateTimestamp
    private Instant dateUpdated;
    
}
