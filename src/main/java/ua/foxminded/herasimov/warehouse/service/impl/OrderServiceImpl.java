package ua.foxminded.herasimov.warehouse.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.foxminded.herasimov.warehouse.dao.OrderDao;
import ua.foxminded.herasimov.warehouse.exception.ServiceException;
import ua.foxminded.herasimov.warehouse.model.Order;
import ua.foxminded.herasimov.warehouse.model.OrderStatus;
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
    public void create(Order entity) {
        orderDao.save(entity);
    }

    @Override
    public Order findById(Integer id) {
        return orderDao.findById(id).orElseThrow(() -> new ServiceException("Order not found by ID: " + id));
    }

    @Override
    public void update(Order entity) {
        Order orderFromDb = orderDao.findById(entity.getId()).orElseThrow(
            () -> new ServiceException("Order for update not found by ID: " + entity.getId()));
        orderFromDb.setItems(entity.getItems());
        orderFromDb.setStatus(entity.getStatus());
        orderFromDb.setSupplier(entity.getSupplier());
        orderDao.save(orderFromDb);
    }

    @Override
    public void delete(Integer id) {
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

    @Override
    public Order getUnregisteredOrder() {
        return orderDao.findByStatusIsNull().orElseGet(() -> orderDao.save(new Order()));
    }

    @Override
    public void setStatusNewToOrder(Integer id) {
        Order order = orderDao.findById(id).orElseThrow(
            () -> new ServiceException("Order not found by ID to set status NEW. ID:" + id));
        order.setStatus(OrderStatus.NEW);
        orderDao.save(order);
    }
}
