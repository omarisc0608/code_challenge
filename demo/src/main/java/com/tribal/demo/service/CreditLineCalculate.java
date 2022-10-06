package com.tribal.demo.service;


import com.tribal.demo.domain.CreditLineRequest;
import com.tribal.demo.domain.CreditLineResponse;
import com.tribal.demo.domain.FoundingType;

public interface CreditLineCalculate {
    public CreditLineResponse calculateCreditLine(CreditLineRequest creditLineRequest);

    public FoundingType getFoundingTypeEnum();

}
