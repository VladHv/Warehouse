package ua.foxminded.herasimov.warehouse.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.herasimov.warehouse.model.Goods;
import ua.foxminded.herasimov.warehouse.model.Order;
import ua.foxminded.herasimov.warehouse.model.OrderItem;

import java.util.Optional;

@Repository
public interface OrderItemDao extends JpaRepository<OrderItem, Integer> {

    void deleteByOrder(Order order);

    Optional<OrderItem> findByOrderAndGoods(Order order, Goods goods);


}
