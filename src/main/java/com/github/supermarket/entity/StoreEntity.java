package com.github.supermarket.entity;

import com.github.supermarket.common.domain.PrimaryKey;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "STORE")
public class StoreEntity implements PrimaryKey<Long> {
    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false, columnDefinition = "Уникальный идентификатор")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORG_ID", nullable = false)
    private OrgEntity org;

    @Column(name = "NAME", nullable = false, length = 250)
    private String name;

    @Column(name = "TELEGRAM_USERNAME", length = 250)
    private String telegramUsername;

    @Column(name = "TELEGRAM_TOKEN", length = 250)
    private String telegramToken;

    @Column(name = "TELEGRAM_IS_ACTIVE", nullable = false)
    private Boolean telegramIsActive;
}