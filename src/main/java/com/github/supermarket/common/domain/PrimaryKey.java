package com.github.supermarket.common.domain;


public interface PrimaryKey<ID> {

    ID getId();

    void setId(ID id);
}
