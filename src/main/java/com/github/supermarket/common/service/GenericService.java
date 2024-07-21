package com.github.supermarket.common.service;

import com.github.supermarket.common.dao.GenericDao;
import com.github.supermarket.common.domain.PrimaryKey;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Transactional
@Validated
public class GenericService<DTO, Entity extends PrimaryKey<ID>, ID> extends GenericReadService<DTO, Entity, ID> {
    private final GenericDao<Entity, ID> dao;
    private final GenericMapper<DTO, Entity> mapper;

    public GenericService(GenericDao<Entity, ID> dao, GenericMapper<DTO, Entity> mapper) {
        super(dao, mapper);
        this.dao = dao;
        this.mapper = mapper;
    }

    public DTO create(@Valid DTO dto) {
        var entity = dao.create(mapper.map(dto));
        dao.flush();
        return mapper.map(entity);
    }

    public DTO update(@Valid DTO dto) {
        var entity = dao.update(mapper.map(dto));
        dao.flush();
        return mapper.map(entity);
    }

    protected void delete(ID id) {
        dao.delete(id);
        dao.flush();
    }
}
