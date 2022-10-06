package com.tribal.demo.domain;

public enum FoundingType {
    STARTUP("Startup"),
    SME("SME");

    private String value;

    FoundingType(String value) {
        this.value = value;
    }

    public String getFoundingType() {
        return value;
    }
}
