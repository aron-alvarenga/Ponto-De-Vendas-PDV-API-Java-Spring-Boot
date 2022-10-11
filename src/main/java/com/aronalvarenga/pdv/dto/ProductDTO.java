package com.aronalvarenga.pdv.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    @Column(name = "product_id")
    private long productid;
    private int quantity;
}
