package ua.foxminded.herasimov.warehouse.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.herasimov.warehouse.model.Order;
import ua.foxminded.herasimov.warehouse.model.OrderItem;

@Repository
public interface OrderItemDao extends JpaRepository<OrderItem, Integer> {

    void deleteByOrder(Order order);
}
