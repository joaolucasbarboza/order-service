package com.joaobarboza.orderservice.core.mapper;

import com.joaobarboza.orderservice.core.dto.order.OrderResponse;
import com.joaobarboza.orderservice.core.repository.mongo.document.OrderDocument;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderResponse toDto(OrderDocument document) {
        return new OrderResponse(
                document.getId(),
                document.getProducts(),
                document.getCreatedAt(),
                document.getUpdatedAt(),
                document.getTransactionId(),
                document.getTotalAmount(),
                document.getTotalItems()
        );
    }
}
