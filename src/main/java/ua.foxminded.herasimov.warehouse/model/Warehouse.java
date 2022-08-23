package ua.foxminded.herasimov.warehouse.model;

import java.util.List;

public class Warehouse {

    private Integer id;
    private List<Order> orders;
    private List<Goods> goods;
    private Supplier supplier;
    private Manager manager;
}
