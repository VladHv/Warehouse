package ua.foxminded.herasimov.warehouse.service;

import ua.foxminded.herasimov.warehouse.model.Order;
import ua.foxminded.herasimov.warehouse.model.OrderItem;

import java.util.List;

public interface OrderService extends Service<Integer, Order>{

    Order createEmptyOrder();

}
