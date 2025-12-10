package com.joaobarboza.orderservice.core.document;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class History {

    private String source;
    private String status;
    private String message;
    private LocalDateTime createdAt;
}
