package com.joaobarboza.orderservice.core.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joaobarboza.orderservice.config.exception.ValidationException;
import com.joaobarboza.orderservice.core.EStatusEvent;
import com.joaobarboza.orderservice.core.dto.order.OrderRequest;
import com.joaobarboza.orderservice.core.dto.order.OrderResponse;
import com.joaobarboza.orderservice.core.mapper.OrderMapper;
import com.joaobarboza.orderservice.core.repository.mongo.document.Event;
import com.joaobarboza.orderservice.core.repository.mongo.document.OrderDocument;
import com.joaobarboza.orderservice.core.repository.mongo.repository.OrderRepository;
import com.joaobarboza.orderservice.core.repository.postgres.entity.EventEntity;
import com.joaobarboza.orderservice.core.repository.postgres.repository.OutboxRepository;
import com.joaobarboza.orderservice.core.utils.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private static final String TRANSACTION_ID_PATTERN = "%s_%s";

    @Value("${spring.kafka.topic.start-saga}")
    private String startSagaTopic;

    private final EventService eventService;
    private final OrderMapper orderMapper;
    private final JsonUtil jsonUtil;
    private final OrderRepository orderRepository;
    private final OutboxRepository outboxRepository;

    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {
        OrderDocument order = buildOrder(orderRequest);

        try {
            Event event = buildEvent(order);
            eventService.save(event);

            JsonNode jsonNode = new ObjectMapper()
                    .readTree(jsonUtil.toJson(event));

            EventEntity entity = buildEventEntity(jsonNode);
            orderRepository.save(order);
            outboxRepository.save(entity);

            log.info("Order created, orderId:{}, targetTopic:{}", order.getId(), entity.getTopic());
        } catch (Exception e) {
            throw new ValidationException(e.getMessage());
        }
        return orderMapper.toDto(order);
    }

    private OrderDocument buildOrder(OrderRequest order) {
        String transactionId = createTransactionId();

        return OrderDocument.builder()
                .products(order.getProducts())
                .transactionId(transactionId).build();
    }

    private Event buildEvent(OrderDocument order) {
        return Event.builder()
                .orderId(order.getId())
                .transactionId(order.getTransactionId())
                .payload(order)
                .build();
    }

    private EventEntity buildEventEntity(JsonNode payload) {
        return EventEntity.builder()
                .topic(startSagaTopic)
                .payload(payload)
                .status(EStatusEvent.PENDING)
                .build();
    }

    private String createTransactionId() {
       return  String.format(TRANSACTION_ID_PATTERN, Instant.now().toEpochMilli(), UUID.randomUUID());
    }
}
