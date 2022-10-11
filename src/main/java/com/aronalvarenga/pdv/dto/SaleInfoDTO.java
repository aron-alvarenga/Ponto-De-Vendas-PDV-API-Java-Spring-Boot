package com.aronalvarenga.pdv.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleInfoDTO {

    private String user;
    private String datetime;
    private List<ProductInfoDTO> products;
}
