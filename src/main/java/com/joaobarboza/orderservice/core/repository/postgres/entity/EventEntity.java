package com.joaobarboza.orderservice.core.repository.postgres.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.joaobarboza.orderservice.core.EStatusEvent;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "events")
@Schema(name = "event")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter

// todo: renomear para OutboxEventEntity
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_seq")
    private Long id;

    @Enumerated(EnumType.STRING)
    private EStatusEvent status;

    private String topic;

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private JsonNode payload;
}
