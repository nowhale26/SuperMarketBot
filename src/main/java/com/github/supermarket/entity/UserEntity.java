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
@Table(name = "USER")
public class UserEntity implements PrimaryKey<Long> {
    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false, columnDefinition = "Уникальный идентификатор")
    private Long id;

    @Column(name = "EMAIL", nullable = false, length = 250)
    private String email;

    @Column(name = "PASSWORD", nullable = false, length = 60)
    private String password;
}
