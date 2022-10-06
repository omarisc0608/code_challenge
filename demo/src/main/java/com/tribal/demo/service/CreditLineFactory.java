package com.tribal.demo.service;

import com.tribal.demo.domain.FoundingType;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class CreditLineFactory {

    private Map<FoundingType, CreditLineCalculate> strategies = new EnumMap<>(FoundingType.class);

    public CreditLineFactory() {
        initStrategies();
    }

    public CreditLineCalculate getStrategy(FoundingType foundingType) {
        if (foundingType == null || !strategies.containsKey(foundingType)) {
            throw new IllegalArgumentException("Invalid " + foundingType);
        }

        return strategies.get(foundingType);
    }

    private void initStrategies() {
        strategies.put(FoundingType.SME, new SMECalculateCreditLine());
        strategies.put(FoundingType.STARTUP, new StartupCalculateCreditLine());
    }
}


