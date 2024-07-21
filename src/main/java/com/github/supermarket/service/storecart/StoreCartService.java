package com.github.supermarket.service.storecart;

import com.github.supermarket.common.domain.ListResponse;
import com.github.supermarket.common.service.GenericService;
import com.github.supermarket.entity.StoreCartEntity;
import com.github.supermarket.service.product.mapper.ProductMapper;
import com.github.supermarket.service.product.model.ProductDetail;
import com.github.supermarket.service.storecart.dao.StoreCartDao;
import com.github.supermarket.service.storecart.mapper.StoreCartMapper;
import com.github.supermarket.service.storecart.model.StoreCart;
import com.github.supermarket.service.storecart.model.StoreCartDelta;
import com.github.supermarket.service.storecart.model.StoreCartFilter;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * Корзина с покупками в магазине
 */
@Validated
@Service
@Transactional
public class StoreCartService extends GenericService<StoreCart, StoreCartEntity, Long> {
    private final StoreCartDao storeCartDao;
    private final StoreCartMapper storeCartMapper;
    private final ProductMapper productMapper;

    public StoreCartService(StoreCartDao storeCartDao,
                            StoreCartMapper storeCartMapper, ProductMapper productMapper) {
        super(storeCartDao, storeCartMapper);
        this.storeCartDao = storeCartDao;
        this.storeCartMapper = storeCartMapper;
        this.productMapper = productMapper;
    }

    /**
     * Список товаров в корзине
     *
     * @param storeCartFilter
     * @return
     */
    public ListResponse<StoreCart> getByParams(@Valid StoreCartFilter storeCartFilter) {
        validate(storeCartFilter);
        return storeCartMapper.map(storeCartDao.list(storeCartFilter));
    }

    private void validate(StoreCartFilter storeCartFilter) {
        if (storeCartFilter.getCustomerId() == null && (storeCartFilter.getExternalType() == null || storeCartFilter.getExternalId() == null)) {
            throw new IllegalArgumentException("Must set customerId or externalType and externalId");
        }
    }

    public StoreCart add(@Valid StoreCartDelta storeCartDelta) {
        ListResponse<StoreCart> storeCartsResponse = getByParams(StoreCartFilter.builder()
                .storeId(storeCartDelta.getStoreId())
                .customerId(storeCartDelta.getCustomerId())
                .productDetailId(storeCartDelta.getProductDetailId())
                .build()
        );

        if (storeCartsResponse.getItems().isEmpty()) {
            ProductDetail productDetail = new ProductDetail();
            productDetail.setId(storeCartDelta.getProductDetailId());
            return create(new StoreCart(null, storeCartDelta.getStoreId(), storeCartDelta.getCustomerId(), productDetail, storeCartDelta.getDelta(), 1L));
        } else {
            StoreCart existStoreCart = storeCartsResponse.getItems().get(0);
            BigDecimal storeQuantity = Optional.ofNullable(existStoreCart.getQuantity())
                    .map(quantity -> quantity.add(storeCartDelta.getDelta()))
                    .orElse(storeCartDelta.getDelta());
            existStoreCart.setQuantity(storeQuantity);
            return update(existStoreCart);
        }
    }

    public StoreCart remove(@Valid StoreCartDelta storeCartDelta) {
        ListResponse<StoreCart> storeCartsResponse = getByParams(StoreCartFilter.builder()
                .storeId(storeCartDelta.getStoreId())
                .customerId(storeCartDelta.getCustomerId())
                .productDetailId(storeCartDelta.getProductDetailId())
                .build()
        );
        if (storeCartsResponse.getItems().isEmpty()) {
            ProductDetail productDetail = new ProductDetail();
            productDetail.setId(storeCartDelta.getProductDetailId());
            // TODO заполнить ProductDetail данными;
            return new StoreCart(null, storeCartDelta.getStoreId(), storeCartDelta.getCustomerId(), productDetail, storeCartDelta.getDelta(), 1L);
        } else {
            StoreCart existStoreCart = storeCartsResponse.getItems().get(0);
            BigDecimal storeQuantity = Optional.ofNullable(existStoreCart.getQuantity())
                    .map(quantity -> {
                        var result = quantity.subtract(storeCartDelta.getDelta());
                        if (ObjectUtils.compare(result, BigDecimal.ZERO) < 0) {
                            result = BigDecimal.ZERO;
                        }
                        return result;
                    })
                    .orElse(BigDecimal.ZERO);
            existStoreCart.setQuantity(storeQuantity);
            if (ObjectUtils.compare(existStoreCart.getQuantity(), BigDecimal.ZERO) > 0) {
                return update(existStoreCart);
            } else {
                delete(existStoreCart.getId());
                return existStoreCart;
            }
        }
    }

}
