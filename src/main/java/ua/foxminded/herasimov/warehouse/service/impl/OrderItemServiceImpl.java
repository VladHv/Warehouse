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

    private OrderItemDao orderItemDao;
    private OrderDao orderDao;

    @Autowired
    public OrderItemServiceImpl(OrderItemDao orderItemDao, OrderDao orderDao) {
        this.orderItemDao = orderItemDao;
        this.orderDao = orderDao;
    }

    @Override
    public void create(OrderItem entity) {
        orderItemDao.save(entity);
    }

    @Override
    public OrderItem findById(Integer id) {
        return orderItemDao.findById(id).orElseThrow(() -> new ServiceException("OrderItem not found by ID: " + id));
    }

    @Override
    public void update(OrderItem entity) {
        OrderItem orderItemFromDb = orderItemDao.findById(entity.getId()).orElseThrow(
            () -> new ServiceException("OrderItem for update not found by ID: " + entity.getId()));
        orderItemFromDb.setOrder(entity.getOrder());
        orderItemFromDb.setGoods(entity.getGoods());
        orderItemFromDb.setQuantity(entity.getQuantity());
        orderItemDao.save(orderItemFromDb);
    }

    @Override
    public void delete(Integer id) {
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
    public void cancelOrderItemsOfOrder(Integer orderId) {
        // TODO: 10.09.2022 check if it's working
        Order order = orderDao.findById(orderId).orElseThrow(
            () -> new ServiceException(
                "Order for order items removing not found by ID:" + orderId));
        orderItemDao.deleteByOrder(order);
    }
}
