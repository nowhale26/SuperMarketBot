package com.github.supermarket.common.domain;

@FunctionalInterface
public interface Converter {
    <T, R> R convert(T object);
}
