package ua.foxminded.herasimov.warehouse.service;

import ua.foxminded.herasimov.warehouse.model.OrderItem;

public interface OrderItemService extends Service<Integer, OrderItem>{
    void cancelOrderItemsOfOrder(Integer id);
}
