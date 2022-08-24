package ua.foxminded.herasimov.warehouse.model;

import java.util.List;

public class Order {

    private Integer id;
    private List<OrderItem> items;
    private OrderStatus status;
    private Supplier supplier;

}
