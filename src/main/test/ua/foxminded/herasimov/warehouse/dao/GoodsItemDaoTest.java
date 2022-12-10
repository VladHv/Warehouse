package ua.foxminded.herasimov.warehouse.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.foxminded.herasimov.warehouse.model.Goods;
import ua.foxminded.herasimov.warehouse.model.GoodsItem;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class GoodsItemDaoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GoodsItemDao goodsItemDao;

    @Test
    void whenFindFirstByGoods_thenReturnOptionalWithGoodsItem() {
        Goods tomato = new Goods.Builder().withName("tomato").withPrice(99).build();
        GoodsItem tomatoItem = new GoodsItem.Builder().withGoods(tomato).withAmount(5).build();
        entityManager.persist(tomato);
        entityManager.persist(tomatoItem);
        entityManager.flush();

        Optional<GoodsItem> result = goodsItemDao.findFirstByGoods(tomato);

        Assertions.assertEquals(Optional.of(tomatoItem), result);

    }
}
