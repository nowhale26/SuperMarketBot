package com.github.supermarket.entity;

import com.github.supermarket.common.domain.PrimaryKey;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Data
@Table(name = "ORG")
public class OrgEntity implements PrimaryKey<Long> {
    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false, columnDefinition = "Уникальный идентификатор")
    private Long id;

    @Column(name = "NAME", length = 50, columnDefinition = "Название организации/ИП/Самозанятого")
    private String name;
}
