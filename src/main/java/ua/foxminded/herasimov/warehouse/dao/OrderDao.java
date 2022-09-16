package ua.foxminded.herasimov.warehouse.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.herasimov.warehouse.model.Order;
import ua.foxminded.herasimov.warehouse.model.OrderStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDao extends JpaRepository<Order, Integer> {

    Optional<Order> findByStatusIsNull();

    List<Order> findByStatusIsNotNull();

    List<Order> findByStatusNotAndStatusNot(OrderStatus status, OrderStatus status1);

}
