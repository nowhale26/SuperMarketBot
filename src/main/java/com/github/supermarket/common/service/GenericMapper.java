package com.github.supermarket.common.service;

import com.github.supermarket.common.domain.PrimaryKey;

public interface GenericMapper<DTO, Entity extends PrimaryKey> {

    DTO map(Entity entity);

    Entity map(DTO dto);
}
