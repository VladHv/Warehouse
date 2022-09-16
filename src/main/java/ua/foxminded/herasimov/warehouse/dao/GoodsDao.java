package ua.foxminded.herasimov.warehouse.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.herasimov.warehouse.model.Goods;

@Repository
public interface GoodsDao extends JpaRepository<Goods, Integer> {
}
