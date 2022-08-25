package ua.foxminded.herasimov.warehouse.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.foxminded.herasimov.warehouse.dao.OrderItemDao;
import ua.foxminded.herasimov.warehouse.exception.ServiceException;
import ua.foxminded.herasimov.warehouse.model.OrderItem;
import ua.foxminded.herasimov.warehouse.service.OrderItemService;

import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    private OrderItemDao dao;

    @Autowired
    public OrderItemServiceImpl(OrderItemDao dao) {
        this.dao = dao;
    }

    @Override
    public void create(OrderItem entity) {
        dao.save(entity);
    }

    @Override
    public OrderItem findById(Integer id) {
        return dao.findById(id).orElseThrow(() -> new ServiceException("OrderItem not found by ID: " + id));
    }

    @Override
    public void update(OrderItem entity) {
        OrderItem orderItemFromDb = dao.findById(entity.getId()).orElseThrow(
            () -> new ServiceException("OrderItem for update not found by ID: " + entity.getId()));
        orderItemFromDb.setOrder(entity.getOrder());
        orderItemFromDb.setGoods(entity.getGoods());
        orderItemFromDb.setQuantity(entity.getQuantity());
        dao.save(orderItemFromDb);
    }

    @Override
    public void delete(Integer id) {
        dao.deleteById(id);
    }

    @Override
    public void delete(OrderItem entity) {
        dao.delete(entity);
    }

    @Override
    public List<OrderItem> findAll() {
        return dao.findAll();
    }
}
