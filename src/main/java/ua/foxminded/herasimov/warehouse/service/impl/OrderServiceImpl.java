package ua.foxminded.herasimov.warehouse.service.impl;

import org.springframework.stereotype.Service;
import ua.foxminded.herasimov.warehouse.model.Order;
import ua.foxminded.herasimov.warehouse.service.OrderService;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Override
    public Order createNewOrder() {
        return null;
    }

    @Override
    public void create(Order entity) {
        //todo: probably should be removed
    }

    @Override
    public Order findById(Integer id) {
        return null;
    }

    @Override
    public void update(Order entity) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public void delete(Order entity) {

    }

    @Override
    public List<Order> findAll() {
        return null;
    }
}
