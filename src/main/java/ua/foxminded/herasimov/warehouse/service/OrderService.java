package ua.foxminded.herasimov.warehouse.service;

import ua.foxminded.herasimov.warehouse.model.Order;

public interface OrderService extends Service<Integer, Order> {

    Order getUnregisteredOrder();

    void setStatusNewToOrder(Integer id);

}
