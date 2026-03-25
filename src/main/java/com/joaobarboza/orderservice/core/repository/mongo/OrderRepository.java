package com.joaobarboza.orderservice.core.repository.mongo;

import com.joaobarboza.orderservice.core.repository.mongo.document.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {
}
