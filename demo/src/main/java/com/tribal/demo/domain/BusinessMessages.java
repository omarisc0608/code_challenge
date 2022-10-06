package com.tribal.demo.domain;

public enum BusinessMessages {

    SALES_CONTACT("A sales agent will contact you"),
    INVALID_FOUNDING_TYPE("FoundingType is null"),
    INVALID_MONTHLY_REVENUE("Monthly Revenue should not be null or Zero"),
    INVALID_CASH_BALANCE("Cash Balance should not be null or Zero"),
    INVALID_CREDIT_LINE("Credit Line should not be null or Zero"),
    INVALID_GREATER_CREDIT_LINE("Request Credit Line should not be greater than cash balance or Monthly Revenue "),
    MANY_TIMES_404_MESSAGE("Too Many Time Requests Status Code 404"),
    CREDIT_LINE_AUTHORIZED("Credit Line Authorized"),
    CREDIT_LINE_REJECTED("Credit Line Rejected");

    private String message;

    BusinessMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
