package com.github.supermarket.common.service;

import com.github.supermarket.common.dao.GenericDao;
import com.github.supermarket.common.domain.PrimaryKey;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Transactional
@Validated
public class GenericReadService<DTO, Entity extends PrimaryKey<ID>, ID> {
    private final GenericDao<Entity, ID> dao;
    private final GenericMapper<DTO, Entity> mapper;

    public GenericReadService(GenericDao<Entity, ID> dao, GenericMapper<DTO, Entity> mapper) {
        this.dao = dao;
        this.mapper = mapper;
    }

    public DTO get(ID id) {
        return mapper.map(dao.findById(id).get());
    }
}
