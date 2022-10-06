package com.tribal.demo.domain;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class CreditLineResponse {
    private boolean statusCreditLine;
    private String message;
    private LocalDateTime requestDate;
}
