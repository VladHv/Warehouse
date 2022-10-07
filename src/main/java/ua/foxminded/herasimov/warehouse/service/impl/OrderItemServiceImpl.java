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
        if (isOrderItemPresentInOrder(orderItemId, orderId)) {
            delete(orderItemId);
        } else {
            throw new ServiceException("OrderItem with ID "+ orderItemId +" not found in order id " + orderId);
        }
    }

    @Override
    public OrderItem updateOnOrder(OrderItem orderItem, Integer orderItemId, Integer orderId) {
        if (isOrderItemPresentInOrder(orderItemId, orderId)) {
            return update(orderItem, orderItemId);
        } else {
            throw new ServiceException("OrderItem with ID "+ orderItemId +" not found in order id " + orderId);
        }
    }

    @Override
    public OrderItem findByIdOnOrder(Integer orderItemId, Integer orderId) {
        if (isOrderItemPresentInOrder(orderItemId, orderId)) {
            return findById(orderItemId);
        } else {
            throw new ServiceException("OrderItem with ID "+ orderItemId +" not found in order id " + orderId);
        }
    }

    private boolean isOrderItemPresentInOrder(Integer orderItemId, Integer orderId) {
        return orderDao.findById(orderId).orElseThrow(() -> new ServiceException("Order not found by ID: " + orderId))
                       .getOrderItems().stream()
                       .anyMatch(item -> item.getId().equals(orderItemId));
    }
}
