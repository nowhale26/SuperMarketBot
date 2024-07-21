package com.github.supermarket.telegram.command.basket;

import com.github.supermarket.service.customer.CustomerService;
import com.github.supermarket.service.storecart.StoreCartService;
import com.github.supermarket.service.storecart.model.StoreCart;
import com.github.supermarket.service.storecart.model.StoreCartDelta;
import org.springframework.stereotype.Component;

@Component
public class BasketRemoveCallbackCommand extends BaseBasketCallbackCommand {
    private final StoreCartService storeCartService;

    public BasketRemoveCallbackCommand(StoreCartService storeCartService, CustomerService customerService, BasketBuilder basketBuilder) {
        super(storeCartService, customerService, basketBuilder);
        this.storeCartService = storeCartService;
    }

    @Override
    protected StoreCart modifyStoreCart(StoreCartDelta storeCartDelta) {
        return storeCartService.remove(storeCartDelta);
    }

    @Override
    public String getCallbackCommandName() {
        return REMOVE_BASKET_COMMAND;
    }
}
