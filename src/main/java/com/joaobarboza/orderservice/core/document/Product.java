package com.joaobarboza.orderservice.core.document;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    private String code;
    private double unitValue;
}
