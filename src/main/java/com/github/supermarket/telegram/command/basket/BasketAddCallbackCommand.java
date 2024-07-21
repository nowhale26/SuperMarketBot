package com.github.supermarket.telegram.command.basket;

import com.github.supermarket.service.customer.CustomerService;
import com.github.supermarket.service.storecart.StoreCartService;
import com.github.supermarket.service.storecart.model.StoreCart;
import com.github.supermarket.service.storecart.model.StoreCartDelta;
import org.springframework.stereotype.Component;

@Component
public class BasketAddCallbackCommand extends BaseBasketCallbackCommand {
    private final StoreCartService storeCartService;

    public BasketAddCallbackCommand(StoreCartService storeCartService, CustomerService customerService, BasketBuilder basketBuilder) {
        super(storeCartService, customerService, basketBuilder);
        this.storeCartService = storeCartService;
    }

    @Override
    protected StoreCart modifyStoreCart(StoreCartDelta storeCartDelta) {
        return storeCartService.add(storeCartDelta);
    }

    @Override
    public String getCallbackCommandName() {
        return ADD_BASKET_COMMAND;
    }
}
