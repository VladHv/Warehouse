package ua.foxminded.herasimov.warehouse.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.foxminded.herasimov.warehouse.dao.OrderDao;
import ua.foxminded.herasimov.warehouse.dao.SupplierDao;
import ua.foxminded.herasimov.warehouse.exception.ServiceException;
import ua.foxminded.herasimov.warehouse.model.Order;
import ua.foxminded.herasimov.warehouse.model.OrderStatus;
import ua.foxminded.herasimov.warehouse.model.Supplier;
import ua.foxminded.herasimov.warehouse.service.OrderService;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderDao orderDao;
    private SupplierDao supplierDao;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao, SupplierDao supplierDao) {
        this.orderDao = orderDao;
        this.supplierDao = supplierDao;
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
    public void setSupplierAndStatusNew(Integer orderId, Integer supplierId) {
        Order order = orderDao.findById(orderId).orElseThrow(
            () -> new ServiceException("Order not found by ID to set status NEW and add supplier. ID:" + orderId));
        Supplier supplier = supplierDao.findById(supplierId).orElseThrow(
            () -> new ServiceException("Supplier #" + supplierId + " not found to add it to order #" + orderId));
        order.setStatus(OrderStatus.NEW);
        order.setSupplier(supplier);
        orderDao.save(order);
    }
}
