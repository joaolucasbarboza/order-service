package com.joaobarboza.orderservice.core.service;

import com.joaobarboza.orderservice.config.exception.ValidationException;
import com.joaobarboza.orderservice.core.document.Event;
import com.joaobarboza.orderservice.core.dto.EventFilters;
import com.joaobarboza.orderservice.core.repository.EventRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public void notifyEnding(Event event) {
        event.setOrderId(event.getOrderId());
        eventRepository.save(event);
        log.info("Event saved, event:{}, transactionId:{}", event, event.getTransactionId());
    }

    public List<Event> findAll() {
        return eventRepository.findAllByOrderByCreatedAtDesc();
    }

    public Event findByFilters(EventFilters filters) {
        validateEmptyFilters(filters);

        if (!isEmpty(filters.orderId())) {
            return findByOrderId(filters.orderId());
        }

        return findByTransactionId(filters.transactionId());
    }

    private Event findByOrderId(String orderId) {
        return eventRepository
                .findTopByOrderIdOrderByCreatedAtDesc(orderId)
                .orElseThrow(() -> new ValidationException("Order not found by orderId: " + orderId));
    }

    private Event findByTransactionId(String transactionId) {
        return eventRepository
                .findTopByTransactionIdOrderByCreatedAtDesc(transactionId)
                .orElseThrow(() -> new ValidationException("Order not found by transactionId: " + transactionId));
    }

    private void validateEmptyFilters(EventFilters filters) {
        if (isEmpty(filters.orderId()) && isEmpty(filters.transactionId())) {
            throw new ValidationException("At least one filter must be provided");
        }
    }

    public void save(Event event) {
        eventRepository.save(event);
    }
}
