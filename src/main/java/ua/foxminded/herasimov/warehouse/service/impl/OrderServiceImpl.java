package ua.foxminded.herasimov.warehouse.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.foxminded.herasimov.warehouse.dao.GoodsItemDao;
import ua.foxminded.herasimov.warehouse.dao.OrderDao;
import ua.foxminded.herasimov.warehouse.exception.ServiceException;
import ua.foxminded.herasimov.warehouse.model.GoodsItem;
import ua.foxminded.herasimov.warehouse.model.Order;
import ua.foxminded.herasimov.warehouse.model.OrderItem;
import ua.foxminded.herasimov.warehouse.model.OrderStatus;
import ua.foxminded.herasimov.warehouse.service.OrderService;

import java.util.List;
import java.util.Set;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderDao orderDao;
    private GoodsItemDao goodsItemDao;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao, GoodsItemDao goodsItemDao) {
        this.orderDao = orderDao;
        this.goodsItemDao = goodsItemDao;
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
        orderFromDb.setOrderItems(entity.getOrderItems());
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
    public List<Order> findAllCreatedOrder() {
        return orderDao.findByStatusIsNotNull();
    }

    @Override
    public void setStatusInProcess(Integer id) {
        Order order = orderDao.findById(id).orElseThrow(
            () -> new ServiceException("Order not found by ID to set status IN_PROCESS. ID:" + id));
        order.setStatus(OrderStatus.IN_PROGRESS);
        orderDao.save(order);
    }

    @Override
    public void setStatusCompleted(Integer id) {
        Order order = orderDao.findById(id).orElseThrow(
            () -> new ServiceException("Order not found by ID to set status COMPLETED. ID:" + id));
        order.setStatus(OrderStatus.COMPLETED);
        orderDao.save(order);
    }

    @Override
    public void setStatusCancel(Integer id) {
        Order order = orderDao.findById(id).orElseThrow(
            () -> new ServiceException("Order not found by ID to set status CANCELED. ID:" + id));
        order.setStatus(OrderStatus.CANCELED);
        orderDao.save(order);
    }

    @Override
    public void closeCompletedOrder(Integer id) {
        Order order = orderDao.findById(id).orElseThrow(
            () -> new ServiceException("Order not found by ID to close order. ID:" + id));
        order.setStatus(OrderStatus.CLOSED);

        Set<OrderItem> orderItems = order.getOrderItems();
        for (OrderItem orderItem : orderItems) {
            GoodsItem goodsItem = goodsItemDao.findFirstByGoods(orderItem.getGoods()).orElseThrow(
                () -> new ServiceException("GoodsItem not found by goods_id: " + orderItem.getGoods().getId()));
            goodsItem.setAmount(goodsItem.getAmount() + orderItem.getAmount());
            goodsItemDao.save(goodsItem);
        }

    }

    @Override
    public List<Order> findOrdersForSupplier() {
        return orderDao.findByStatusNotAndStatusNot(OrderStatus.CANCELED, OrderStatus.CLOSED);
    }
}
