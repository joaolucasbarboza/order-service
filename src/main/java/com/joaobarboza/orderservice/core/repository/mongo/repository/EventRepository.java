package com.joaobarboza.orderservice.core.repository.mongo.repository;

import com.joaobarboza.orderservice.core.repository.mongo.document.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends MongoRepository<Event, String> {

    List<Event> findAllByOrderByCreatedAtDesc();

    Optional<Event> findTopByOrderIdOrderByCreatedAtDesc(String orderId);

    Optional<Event> findTopByTransactionIdOrderByCreatedAtDesc(String orderId);
}
