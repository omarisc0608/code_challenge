package com.tribal.demo.service;

import com.tribal.demo.domain.CreditLineRequest;
import com.tribal.demo.domain.CreditLineResponse;
import com.tribal.demo.domain.FoundingType;
import com.tribal.demo.validator.RequestCreditLineValidator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.tribal.demo.domain.BusinessMessages.CREDIT_LINE_AUTHORIZED;
import static com.tribal.demo.domain.BusinessMessages.CREDIT_LINE_REJECTED;

@Service
public class SMECalculateCreditLine implements CreditLineCalculate, RequestCreditLineValidator {


    @Override
    public CreditLineResponse calculateCreditLine(CreditLineRequest creditLineRequest) {

        boolean isApprovedCreditLine;
        BigDecimal amountCalculatedGreater;

        try {

            validateRequestCreditLine(creditLineRequest);
            BigDecimal amountCalculatedCashBalance = creditLineRequest.getCashBalance().divide(new BigDecimal(3), 2, RoundingMode.CEILING);
            BigDecimal amountCalculatedMonthlyRevenue = creditLineRequest.getMonthlyRevenue().divide(new BigDecimal(5), 2, RoundingMode.CEILING);

            if (amountCalculatedCashBalance.compareTo(amountCalculatedMonthlyRevenue) >= 1) {
                amountCalculatedGreater = amountCalculatedCashBalance;
            } else {
                amountCalculatedGreater = amountCalculatedMonthlyRevenue;
            }

            isApprovedCreditLine = amountCalculatedGreater.compareTo(creditLineRequest.getRequestedCreditLine()) > 0;

            if (isApprovedCreditLine) {
                return CreditLineResponse.builder()
                        .message(CREDIT_LINE_AUTHORIZED.getMessage())
                        .statusCreditLine(true)
                        .requestDate(creditLineRequest.getRequestDate())
                        .build();
            }


            return CreditLineResponse.builder()
                    .message(CREDIT_LINE_REJECTED.getMessage())
                    .statusCreditLine(false)
                    .requestDate(creditLineRequest.getRequestDate())
                    .build();

        } catch (Exception e) {
            return CreditLineResponse.builder()
                    .message(e.getMessage())
                    .statusCreditLine(false)
                    .requestDate(creditLineRequest.getRequestDate())
                    .build();
        }
    }

    @Override
    public FoundingType getFoundingTypeEnum() {
        return FoundingType.SME;
    }
}
