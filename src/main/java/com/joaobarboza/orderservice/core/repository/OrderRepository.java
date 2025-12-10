package com.joaobarboza.orderservice.core.repository;

import com.joaobarboza.orderservice.core.document.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {
}
