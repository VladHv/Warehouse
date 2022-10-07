package ua.foxminded.herasimov.warehouse.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.foxminded.herasimov.warehouse.dao.OrderDao;
import ua.foxminded.herasimov.warehouse.exception.ServiceException;
import ua.foxminded.herasimov.warehouse.model.Order;
import ua.foxminded.herasimov.warehouse.service.OrderService;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderDao orderDao;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public Order create(Order entity) {
        return orderDao.save(entity);
    }

    @Override
    public Order findById(Integer id) {
        return orderDao.findById(id).orElseThrow(() -> new ServiceException("Order not found by ID: " + id));
    }

    @Override
    public Order update(Order entity, Integer id) {
        Order orderFromDb = orderDao.findById(id).orElseThrow(
            () -> new ServiceException("Order for update not found by ID: " + id));
        orderFromDb.setOrderItems(entity.getOrderItems());
        orderFromDb.setStatus(entity.getStatus());
        orderFromDb.setSupplier(entity.getSupplier());
        return orderDao.save(orderFromDb);
    }

    @Override
    public void delete(Integer id) {
        orderDao.findById(id).orElseThrow(
            () -> new ServiceException("Order for delete not found by ID: " + id));
        orderDao.deleteById(id);
    }

    @Override
    public void delete(Order entity) {
        orderDao.delete(entity);
    }

    @Override
    public List<Order> findAll() {
        return orderDao.findAll();
    }

}
