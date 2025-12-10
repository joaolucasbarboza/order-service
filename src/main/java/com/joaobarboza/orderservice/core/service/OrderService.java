package com.joaobarboza.orderservice.core.service;

import com.joaobarboza.orderservice.core.document.Event;
import com.joaobarboza.orderservice.core.document.Order;
import com.joaobarboza.orderservice.core.dto.OrderRequest;
import com.joaobarboza.orderservice.core.producer.SagaProducer;
import com.joaobarboza.orderservice.core.repository.OrderRepository;
import com.joaobarboza.orderservice.core.utils.JsonUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderService {

    private static final String TRANSACTION_ID_PATTERN = "%s_%s";

    private final EventService eventService;
    private final SagaProducer sagaProducer;
    private final JsonUtil jsonUtil;
    private final OrderRepository orderRepository;

    public Order createOrder(OrderRequest orderRequest) {
        Order order = Order.builder()
                .products(orderRequest.getProducts())
                .transactionId(
                        String.format(TRANSACTION_ID_PATTERN, Instant.now().toEpochMilli(), UUID.randomUUID())
                )
                .build();
        orderRepository.save(order);
        sagaProducer.sendEvent(jsonUtil.toJson(createPayload(order)));

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
