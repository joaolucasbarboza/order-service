package com.joaobarboza.orderservice.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EStatusEvent {
    PENDING("pending"),
    SUCCESS("success"),
    FAILED("failed");

    private final String topic;
}
