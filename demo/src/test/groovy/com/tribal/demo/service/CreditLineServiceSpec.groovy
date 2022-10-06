package com.tribal.demo.service

import com.tribal.demo.domain.BusinessMessages
import com.tribal.demo.domain.CreditLineRequest
import com.tribal.demo.domain.FoundingType
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification
import spock.lang.Unroll

class CreditLineServiceSpec extends Specification {

    @Autowired
    CreditLineFactory creditLineFactory;

    void setup() {
        creditLineFactory = new CreditLineFactory()
    }

    def "Credit Line Authorized - SME scenario"() {
        given:

        when:
        def response = creditLineFactory.getStrategy(FoundingType.SME).calculateCreditLine(CreditLineRequest.builder()
                .cashBalance(new BigDecimal(500))
                .monthlyRevenue(new BigDecimal(400))
                .foundingType(FoundingType.SME)
                .requestedCreditLine(new BigDecimal(100))
                .build())
        then:
        assert response.statusCreditLine
    }

    def "Credit Line Rejected - SME scenario"() {
        given:

        when:
        def response = creditLineFactory.getStrategy(FoundingType.SME).calculateCreditLine(CreditLineRequest.builder()
                .cashBalance(new BigDecimal(200))
                .monthlyRevenue(new BigDecimal(200))
                .foundingType(FoundingType.SME)
                .requestedCreditLine(new BigDecimal(100))
                .build())
        then:
        assert !response.statusCreditLine
    }

    def "Credit Line Authorized - Startup scenario"() {
        given:

        when:
        def response = creditLineFactory.getStrategy(FoundingType.STARTUP).calculateCreditLine(
                CreditLineRequest.builder()
                        .cashBalance(new BigDecimal(600))
                        .monthlyRevenue(new BigDecimal(500))
                        .foundingType(FoundingType.STARTUP)
                        .requestedCreditLine(new BigDecimal(100))
                        .build())
        then:
        assert response.statusCreditLine
    }

    def "Credit Line Rejected - Startup scenario"() {
        given:

        when:
        def response = creditLineFactory.getStrategy(FoundingType.STARTUP).calculateCreditLine(
                CreditLineRequest.builder()
                        .cashBalance(new BigDecimal(300))
                        .monthlyRevenue(new BigDecimal(200))
                        .foundingType(FoundingType.STARTUP)
                        .requestedCreditLine(new BigDecimal(200))
                        .build())
        then:
        assert !response.statusCreditLine
    }

    @Unroll
    def "[Invalid #_scenarios]"() {
        given:
        def requestCreditLine = _requestCreditLine
        when:
        def creditLineResponse = creditLineFactory.getStrategy(FoundingType.SME).calculateCreditLine(requestCreditLine)
        then:
        assert creditLineResponse.getErrorDetails() == _erroMessage
        where:
        _scenarios << [
                "Cash Balance does not Valid",
                "Monthly Revenue does not Valid",
                "Founding Type does not Valid",
                "Request Credit Line does not Valid",
                "Request Credit Line greater than CashBalance"

        ]

        _requestCreditLine << [

                CreditLineRequest.builder()
                        .cashBalance(new BigDecimal(0))
                        .monthlyRevenue(new BigDecimal(200))
                        .foundingType(FoundingType.STARTUP)
                        .requestedCreditLine(new BigDecimal(200))
                        .build(),
                CreditLineRequest.builder()
                        .cashBalance(new BigDecimal(300))
                        .monthlyRevenue(new BigDecimal(0))
                        .foundingType(FoundingType.STARTUP)
                        .requestedCreditLine(new BigDecimal(200))
                        .build(),
                CreditLineRequest.builder()
                        .cashBalance(new BigDecimal(300))
                        .monthlyRevenue(new BigDecimal(200))
                        .requestedCreditLine(new BigDecimal(200))
                        .build(),
                CreditLineRequest.builder()
                        .cashBalance(new BigDecimal(300))
                        .monthlyRevenue(new BigDecimal(200))
                        .foundingType(FoundingType.STARTUP)
                        .requestedCreditLine(new BigDecimal(0))
                        .build(),
                CreditLineRequest.builder()
                        .cashBalance(new BigDecimal(300))
                        .monthlyRevenue(new BigDecimal(200))
                        .foundingType(FoundingType.STARTUP)
                        .requestedCreditLine(new BigDecimal(500))
                        .build()
        ]

        _erroMessage << [
                BusinessMessages.INVALID_CASH_BALANCE.message,
                BusinessMessages.INVALID_MONTHLY_REVENUE.message,
                BusinessMessages.INVALID_FOUNDING_TYPE.message,
                BusinessMessages.INVALID_CREDIT_LINE.message,
                BusinessMessages.INVALID_GREATER_CREDIT_LINE.message
        ]

    }
}