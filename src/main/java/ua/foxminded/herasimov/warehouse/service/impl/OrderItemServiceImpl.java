package ua.foxminded.herasimov.warehouse.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(OrderItemServiceImpl.class);
    private final OrderItemDao orderItemDao;
    private final OrderDao orderDao;

    @Autowired
    public OrderItemServiceImpl(OrderItemDao orderItemDao, OrderDao orderDao) {
        this.orderItemDao = orderItemDao;
        this.orderDao = orderDao;
    }

    @Override
    public OrderItem create(OrderItem entity) {
        logger.info("Saving order item: {}", entity);
        return orderItemDao.save(entity);
    }

    @Override
    public OrderItem findById(Integer id) {
        logger.info("Finding order item by id: {}", id);
        return orderItemDao.findById(id).orElseThrow(() -> new ServiceException("OrderItem not found by ID: " + id));
    }

    @Override
    public OrderItem update(OrderItem entity, Integer id) {
        logger.info("Updating order item {} by id: {}", entity, id);
        OrderItem orderItemFromDb = orderItemDao.findById(id).orElseThrow(
            () -> new ServiceException("OrderItem for update not found by ID: " + id));
        logger.info("Order item by id: {} found: {}", id, orderItemFromDb);
        orderItemFromDb.setOrder(entity.getOrder());
        orderItemFromDb.setGoods(entity.getGoods());
        orderItemFromDb.setAmount(entity.getAmount());
        logger.info("Saving updated order item: {}", orderItemFromDb);
        return orderItemDao.save(orderItemFromDb);
    }

    @Override
    public void delete(Integer id) {
        logger.info("Checking order item by id {} in DB", id);
        orderItemDao.findById(id).orElseThrow(
            () -> new ServiceException("OrderItem for delete not found by ID: " + id));
        logger.info("Deleting order item by id: {}", id);
        orderItemDao.deleteById(id);
    }

    @Override
    public void delete(OrderItem entity) {
        logger.info("Checking order item by id {} in DB", entity.getId());
        orderItemDao.findById(entity.getId()).orElseThrow(
            () -> new ServiceException("OrderItem for delete not found by ID: " + entity.getId()));
        logger.info("Deleting order item {}", entity);
        orderItemDao.delete(entity);
    }

    @Override
    public List<OrderItem> findAll() {
        logger.trace("Getting all order items");
        return orderItemDao.findAll();
    }

    @Override
    public List<OrderItem> findAllFromOrder(Integer orderId) {
        logger.info("Checking existence in DB of order with id: {}", orderId);
        orderDao.findById(orderId).orElseThrow(
            () -> new ServiceException("Order not found by ID: " + orderId));
        logger.info("Getting all order items from order with id: {}", orderId);
        return orderItemDao.findAllByOrderId(orderId);
    }

    @Override
    public OrderItem createOnOrder(OrderItem orderItem, Integer orderId) {
        logger.info("Checking existence in DB of order with id: {}", orderId);
        Order order = orderDao.findById(orderId).orElseThrow(
            () -> new ServiceException("Order not found by ID: " + orderId));
        logger.info("Setting order {} to order item {}", order, orderItem);
        orderItem.setOrder(order);
        logger.info("Saving order item: {}", orderItem);
        return create(orderItem);
    }

    @Override
    public void deleteFromOrder(Integer orderItemId, Integer orderId) {
        logger.trace("Deleting order item from order");
        if (isOrderItemPresentInOrder(orderItemId, orderId)) {
            logger.info("Deleting order item with id {} from order with id {}", orderItemId, orderId);
            delete(orderItemId);
        } else {
            logger.info("Order item with id {} is absent in order with id {} ", orderItemId, orderId);
            throw new ServiceException("OrderItem with ID " + orderItemId + " not found in order id " + orderId);
        }
    }

    @Override
    public OrderItem updateOnOrder(OrderItem orderItem, Integer orderItemId, Integer orderId) {
        logger.trace("Updating order item in order");
        if (isOrderItemPresentInOrder(orderItemId, orderId)) {
            logger.info("Updating order item {} in order with id {}", orderItem, orderId);
            return update(orderItem, orderItemId);
        } else {
            logger.info("Order item with id {} is absent in order with id {} ", orderItemId, orderId);
            throw new ServiceException("OrderItem with ID " + orderItemId + " not found in order id " + orderId);
        }
    }

    @Override
    public OrderItem findByIdInOrder(Integer orderItemId, Integer orderId) {
        logger.trace("Finding order item in order");
        if (isOrderItemPresentInOrder(orderItemId, orderId)) {
            logger.info("Finding order item with id {} in order with id {}", orderItemId, orderId);
            return findById(orderItemId);
        } else {
            logger.info("Order item with id {} is absent in order with id {} ", orderItemId, orderId);
            throw new ServiceException("OrderItem with ID " + orderItemId + " not found in order id " + orderId);
        }
    }

    private boolean isOrderItemPresentInOrder(Integer orderItemId, Integer orderId) {
        logger.info("Checking is order item with id {} present in order with id {}", orderItemId, orderId);
        return orderDao.findById(orderId).orElseThrow(() -> new ServiceException("Order not found by ID: " + orderId))
                       .getOrderItems().stream()
                       .anyMatch(item -> item.getId().equals(orderItemId));
    }
}
