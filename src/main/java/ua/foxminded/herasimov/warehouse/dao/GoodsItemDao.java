package ua.foxminded.herasimov.warehouse.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.herasimov.warehouse.model.GoodsItem;

@Repository
public interface GoodsItemDao extends JpaRepository<GoodsItem, Integer> {

}
