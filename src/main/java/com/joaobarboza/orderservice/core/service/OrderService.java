package com.joaobarboza.orderservice.core.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joaobarboza.orderservice.config.exception.ValidationException;
import com.joaobarboza.orderservice.core.EStatusEvent;
import com.joaobarboza.orderservice.core.repository.mongo.document.Event;
import com.joaobarboza.orderservice.core.repository.mongo.document.Order;
import com.joaobarboza.orderservice.core.dto.OrderRequest;
import com.joaobarboza.orderservice.core.repository.mongo.OrderRepository;
import com.joaobarboza.orderservice.core.repository.postgres.OutboxRepository;
import com.joaobarboza.orderservice.core.repository.postgres.entity.EventEntity;
import com.joaobarboza.orderservice.core.utils.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private static final String TRANSACTION_ID_PATTERN = "%s_%s";

    @Value("${spring.kafka.topic.start-saga}")
    private String startSagaTopic;

    private final EventService eventService;
    private final JsonUtil jsonUtil;
    private final OrderRepository orderRepository;
    private final OutboxRepository outboxRepository;

    @Transactional
    public Order createOrder(OrderRequest orderRequest) {
        Order order = Order.builder()
                .products(orderRequest.getProducts())
                .transactionId(
                        String.format(TRANSACTION_ID_PATTERN,
                                Instant.now().toEpochMilli(),
                                UUID.randomUUID())
                )
                .build();

        try {
            JsonNode jsonNode = new ObjectMapper()
                    .readTree(jsonUtil.toJson(createPayload(order)));

            EventEntity entity = EventEntity.builder()
                    .topic(startSagaTopic)
                    .payload(jsonNode)
                    .status(EStatusEvent.PENDING)
                    .build();

            orderRepository.save(order);
            outboxRepository.save(entity);
        } catch (Exception e) {
            throw new ValidationException(e.getMessage());
        }
        return order;
    }

    private Event createPayload(Order order) {
        Event event = Event.builder()
                .orderId(order.getId())
                .transactionId(order.getTransactionId())
                .payload(order)
                .build();
        eventService.save(event);
        return event;
    }
}
