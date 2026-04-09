package com.joaobarboza.orderservice.core.dto.order;

import com.joaobarboza.orderservice.core.repository.mongo.document.OrderProducts;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    private List<OrderProducts> products;
}
