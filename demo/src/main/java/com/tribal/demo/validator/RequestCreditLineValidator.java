package com.tribal.demo.validator;


import com.tribal.demo.domain.CreditLineRequest;
import com.tribal.demo.exception.MessageRequestException;

import java.math.BigDecimal;
import java.util.Objects;

import static com.tribal.demo.domain.BusinessMessages.*;

public interface RequestCreditLineValidator {

    default void validateRequestCreditLine(CreditLineRequest creditLineRequest) throws MessageRequestException {

        if (Objects.isNull(creditLineRequest.getFoundingType())) {
            throw new MessageRequestException(INVALID_FOUNDING_TYPE.getMessage());
        }

        if (Objects.isNull(creditLineRequest.getMonthlyRevenue()) || creditLineRequest.getMonthlyRevenue().compareTo(new BigDecimal(0)) <= 0) {
            throw new MessageRequestException(INVALID_MONTHLY_REVENUE.getMessage());
        }

        if (Objects.isNull(creditLineRequest.getCashBalance()) || creditLineRequest.getCashBalance().compareTo(new BigDecimal(0)) <= 0) {
            throw new MessageRequestException(INVALID_CASH_BALANCE.getMessage());
        }

        if (Objects.isNull(creditLineRequest.getRequestedCreditLine()) || creditLineRequest.getRequestedCreditLine().compareTo(new BigDecimal(0)) <= 0) {
            throw new MessageRequestException(INVALID_CREDIT_LINE.getMessage());
        }

        if (creditLineRequest.getRequestedCreditLine().compareTo(creditLineRequest.getCashBalance()) > 0 || creditLineRequest.getRequestedCreditLine().compareTo(creditLineRequest.getMonthlyRevenue()) > 0) {
            throw new MessageRequestException(INVALID_GREATER_CREDIT_LINE.getMessage());
        }
    }
}
