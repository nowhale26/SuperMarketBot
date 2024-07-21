package com.github.supermarket.common.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@SuperBuilder
public class PageFilter implements Serializable {
    private Pagination pagination;
    private List<Sort> sorts;
}
