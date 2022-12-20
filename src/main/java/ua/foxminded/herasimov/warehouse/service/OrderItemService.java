package ua.foxminded.herasimov.warehouse.service;

import ua.foxminded.herasimov.warehouse.model.OrderItem;

import java.util.List;

public interface OrderItemService extends Service<Integer, OrderItem>{

    List<OrderItem> findAllFromOrder(Integer orderId);

    OrderItem createOnOrder(OrderItem orderItem, Integer orderId);

    void deleteFromOrder(Integer orderItemId, Integer orderId);

    OrderItem updateOnOrder(OrderItem orderItem, Integer orderItemId, Integer orderId);

    OrderItem findByIdInOrder(Integer orderItemId, Integer orderId);
}
