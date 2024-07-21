package com.github.supermarket.telegram.command.product;

import com.github.supermarket.service.customer.CustomerService;
import com.github.supermarket.service.storecart.StoreCartService;
import com.github.supermarket.service.storecart.model.StoreCart;
import com.github.supermarket.service.storecart.model.StoreCartDelta;
import org.springframework.stereotype.Component;

@Component
public class ProductAddCallbackCommand extends BaseProductCallbackCommand {
    private final StoreCartService storeCartService;

    public ProductAddCallbackCommand(StoreCartService storeCartService, CustomerService customerService, ProductBuilder productBuilder) {
        super(storeCartService, customerService, productBuilder);
        this.storeCartService = storeCartService;
    }

    @Override
    protected StoreCart modifyStoreCart(StoreCartDelta storeCartDelta) {
        return storeCartService.add(storeCartDelta);
    }

    @Override
    public String getCallbackCommandName() {
        return ADD_PRODUCT_COMMAND;
    }
}
