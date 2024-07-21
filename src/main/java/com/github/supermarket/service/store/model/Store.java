package com.github.supermarket.service.store.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Store {
    private Long id;
    private Long orgId;
    private String name;
    private String telegramUsername;
    private String telegramToken;
    private Boolean telegramIsActive;
}