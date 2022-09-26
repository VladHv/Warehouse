package ua.foxminded.herasimov.warehouse.service;

import ua.foxminded.herasimov.warehouse.model.Order;

import java.util.List;

public interface OrderService extends Service<Integer, Order> {

    Order getUnregisteredOrder();

    List<Order> findAllCreatedOrder();

    void setStatusInProcess(Integer id);

    void setStatusCompleted(Integer id);

    void setStatusCancel(Integer id);

    void closeCompletedOrder(Integer id);

    List<Order> findOrdersForSupplier();
}
