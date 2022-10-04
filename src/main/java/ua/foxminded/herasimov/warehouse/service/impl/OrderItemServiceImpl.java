package ua.foxminded.herasimov.warehouse.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.foxminded.herasimov.warehouse.dao.OrderDao;
import ua.foxminded.herasimov.warehouse.dao.OrderItemDao;
import ua.foxminded.herasimov.warehouse.exception.ServiceException;
import ua.foxminded.herasimov.warehouse.model.Order;
import ua.foxminded.herasimov.warehouse.model.OrderItem;
import ua.foxminded.herasimov.warehouse.service.OrderItemService;

import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemDao orderItemDao;
    private final OrderDao orderDao;

    @Autowired
    public OrderItemServiceImpl(OrderItemDao orderItemDao, OrderDao orderDao) {
        this.orderItemDao = orderItemDao;
        this.orderDao = orderDao;
    }

    @Override
    public OrderItem create(OrderItem entity) {
        return orderItemDao.save(entity);
    }

    @Override
    public OrderItem findById(Integer id) {
        return orderItemDao.findById(id).orElseThrow(() -> new ServiceException("OrderItem not found by ID: " + id));
    }

    @Override
    public OrderItem update(OrderItem entity, Integer id) {
        OrderItem orderItemFromDb = orderItemDao.findById(id).orElseThrow(
            () -> new ServiceException("OrderItem for update not found by ID: " + id));
        orderItemFromDb.setOrder(entity.getOrder());
        orderItemFromDb.setGoods(entity.getGoods());
        orderItemFromDb.setAmount(entity.getAmount());
        return orderItemDao.save(orderItemFromDb);
    }

    @Override
    public void delete(Integer id) {
        orderItemDao.findById(id).orElseThrow(
            () -> new ServiceException("OrderItem for delete not found by ID: " + id));
        orderItemDao.deleteById(id);
    }

    @Override
    public void delete(OrderItem entity) {
        orderItemDao.delete(entity);
    }

    @Override
    public List<OrderItem> findAll() {
        return orderItemDao.findAll();
    }

    @Override
    public List<OrderItem> findAllFromOrder(Integer orderId) {
        orderDao.findById(orderId).orElseThrow(
            () -> new ServiceException("Order not found by ID: " + orderId));
        return orderItemDao.findAllByOrderId(orderId);
    }

    @Override
    public OrderItem createOnOrder(OrderItem orderItem, Integer orderId) {
        Order order = orderDao.findById(orderId).orElseThrow(
            () -> new ServiceException("Order not found by ID: " + orderId));
        orderItem.setOrder(order);
        return create(orderItem);
    }

    @Override
    public void deleteFromOrder(Integer orderItemId, Integer orderId) {
        orderDao.findById(orderId).orElseThrow(
            () -> new ServiceException("Order not found by ID: " + orderId));
        delete(orderItemId);
    }

    @Override
    public OrderItem updateOnOrder(OrderItem orderItem, Integer orderItemId, Integer orderId) {
        orderDao.findById(orderId).orElseThrow(
            () -> new ServiceException("Order not found by ID: " + orderId));
        return update(orderItem, orderItemId);
    }

    @Override
    public OrderItem findByIdOnOrder(Integer orderItemId, Integer orderId) {
        Order order = orderDao.findById(orderId).orElseThrow(
            () -> new ServiceException("Order not found by ID: " + orderId));
        order.getOrderItems().stream()
             .filter(orderItem -> orderItem.getId().equals(orderItemId))
             .findFirst().orElseThrow(() -> new ServiceException("Order has no order items with id: " + orderItemId));
        return findById(orderItemId);
    }
}
