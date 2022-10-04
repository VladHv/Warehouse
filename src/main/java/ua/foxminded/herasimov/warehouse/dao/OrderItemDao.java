package ua.foxminded.herasimov.warehouse.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.herasimov.warehouse.model.OrderItem;

import java.util.List;

@Repository
public interface OrderItemDao extends JpaRepository<OrderItem, Integer> {

    List<OrderItem> findAllByOrderId(Integer orderId);

}
