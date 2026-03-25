package com.joaobarboza.orderservice.core.repository.postgres;

import com.joaobarboza.orderservice.core.repository.postgres.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxRepository extends JpaRepository<EventEntity, Long> {
}
