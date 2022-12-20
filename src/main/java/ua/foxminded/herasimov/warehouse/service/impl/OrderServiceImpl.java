package ua.foxminded.herasimov.warehouse.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.foxminded.herasimov.warehouse.dao.OrderDao;
import ua.foxminded.herasimov.warehouse.exception.ServiceException;
import ua.foxminded.herasimov.warehouse.model.Order;
import ua.foxminded.herasimov.warehouse.service.OrderService;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final OrderDao orderDao;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public Order create(Order entity) {
        logger.info("Saving order: {}", entity);
        return orderDao.save(entity);
    }

    @Override
    public Order findById(Integer id) {
        logger.info("Finding order by id: {}", id);
        return orderDao.findById(id).orElseThrow(() -> new ServiceException("Order not found by ID: " + id));
    }

    @Override
    public Order update(Order entity, Integer id) {
        logger.info("Updating order {} by id: {}", entity, id);
        Order orderFromDb = orderDao.findById(id).orElseThrow(
            () -> new ServiceException("Order for update not found by ID: " + id));
        logger.info("Order by id: {} found: {}", id, orderFromDb);
        orderFromDb.setOrderItems(entity.getOrderItems());
        orderFromDb.setStatus(entity.getStatus());
        orderFromDb.setSupplier(entity.getSupplier());
        logger.info("Saving updated order: {}", orderFromDb);
        return orderDao.save(orderFromDb);
    }

    @Override
    public void delete(Integer id) {
        logger.info("Checking order by id {} in DB", id);
        orderDao.findById(id).orElseThrow(
            () -> new ServiceException("Order for delete not found by ID: " + id));
        logger.info("Deleting order by id: {}", id);
        orderDao.deleteById(id);
    }

    @Override
    public void delete(Order entity) {
        logger.info("Checking order by id {} in DB", entity.getId());
        orderDao.findById(entity.getId()).orElseThrow(
            () -> new ServiceException("Order for delete not found by ID: " + entity.getId()));
        logger.info("Deleting order: {}", entity);
        orderDao.delete(entity);
    }

    @Override
    public List<Order> findAll() {
        logger.trace("Getting all orders");
        return orderDao.findAll();
    }

}
