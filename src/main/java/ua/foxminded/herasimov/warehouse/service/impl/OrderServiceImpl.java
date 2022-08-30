package ua.foxminded.herasimov.warehouse.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.foxminded.herasimov.warehouse.dao.OrderDao;
import ua.foxminded.herasimov.warehouse.exception.ServiceException;
import ua.foxminded.herasimov.warehouse.model.Order;
import ua.foxminded.herasimov.warehouse.model.Supplier;
import ua.foxminded.herasimov.warehouse.service.OrderService;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderDao dao;

    @Autowired
    public OrderServiceImpl(OrderDao dao) {
        this.dao = dao;
    }

    @Override
    public Order createEmptyOrder() {
        return dao.save(new Order());
    }

    @Override
    public void create(Order entity) {
        dao.save(entity);
    }

    @Override
    public Order findById(Integer id) {
        return dao.findById(id).orElseThrow(() -> new ServiceException("Order not found by ID: " + id));
    }

    @Override
    public void update(Order entity) {
        Order orderFromDb = dao.findById(entity.getId()).orElseThrow(
            () -> new ServiceException("Order for update not found by ID: " + entity.getId()));
        orderFromDb.setItems(entity.getItems());
        orderFromDb.setStatus(entity.getStatus());
        orderFromDb.setSupplier(entity.getSupplier());
        dao.save(orderFromDb);
    }

    @Override
    public void delete(Integer id) {
        dao.deleteById(id);
    }

    @Override
    public void delete(Order entity) {
        dao.delete(entity);
    }

    @Override
    public List<Order> findAll() {
        return dao.findAll();
    }
}
