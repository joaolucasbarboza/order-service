package com.joaobarboza.orderservice.core.dto.order;

import com.joaobarboza.orderservice.core.repository.mongo.document.OrderProducts;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        String id,
        List<OrderProducts> products,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String transactionId,
        double totalAmount,
        Integer totalItems
){}
