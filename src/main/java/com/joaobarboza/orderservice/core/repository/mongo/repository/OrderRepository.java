package com.joaobarboza.orderservice.core.repository.mongo.repository;

import com.joaobarboza.orderservice.core.repository.mongo.document.OrderDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<OrderDocument, String> {
}
