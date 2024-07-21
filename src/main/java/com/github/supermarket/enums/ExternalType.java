package com.github.supermarket.enums;

public enum ExternalType {
    TELEGRAM(1);

    private Integer value;

    ExternalType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
