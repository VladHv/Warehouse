package ua.foxminded.herasimov.warehouse.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.herasimov.warehouse.model.Goods;
import ua.foxminded.herasimov.warehouse.model.GoodsItem;

import java.util.Optional;

@Repository
public interface GoodsItemDao extends JpaRepository<GoodsItem, Integer> {

    Optional<GoodsItem> findFirstByGoods(Goods goods);
}
