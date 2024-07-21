package com.github.supermarket.telegram.session;

import com.github.supermarket.enums.ExternalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Информация о текущей сессии пользователя в телеграмм
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserSession {
    /**
     * Идентификатор магазина
     */
    private Long storeId;

    /**
     * Тип внешней системы
     */
    private ExternalType externalType;

    /**
     * Идентификатор покупателя во внешней системе
     */
    private String externalId;

    public UserSession(Long storeId) {
        this.storeId = storeId;
    }
}
