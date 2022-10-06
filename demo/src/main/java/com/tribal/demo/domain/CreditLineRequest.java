package com.tribal.demo.domain;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder

public class CreditLineRequest {

    private FoundingType foundingType;
    private BigDecimal cashBalance;
    private BigDecimal monthlyRevenue;
    private BigDecimal requestedCreditLine;
    private LocalDateTime requestDate;
}
