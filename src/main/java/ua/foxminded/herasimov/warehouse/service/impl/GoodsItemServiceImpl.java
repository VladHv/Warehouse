package ua.foxminded.herasimov.warehouse.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.foxminded.herasimov.warehouse.dao.GoodsItemDao;
import ua.foxminded.herasimov.warehouse.exception.ServiceException;
import ua.foxminded.herasimov.warehouse.model.GoodsItem;
import ua.foxminded.herasimov.warehouse.service.GoodsItemService;

import java.util.List;

@Service
public class GoodsItemServiceImpl implements GoodsItemService {

    private GoodsItemDao dao;

    @Autowired
    public GoodsItemServiceImpl(GoodsItemDao dao) {
        this.dao = dao;
    }

    @Override
    public void create(GoodsItem entity) {
        dao.save(entity);
    }

    @Override
    public GoodsItem findById(Integer id) {
        return dao.findById(id).orElseThrow(() -> new ServiceException("GoodsItem not found by ID: " + id));
    }

    @Override
    public void update(GoodsItem entity) {
        GoodsItem goodsItemFromDb = dao.findById(entity.getId()).orElseThrow(
            () -> new ServiceException("GoodsItem for update not found by ID: " + entity.getId()));
        goodsItemFromDb.setGoods(entity.getGoods());
        goodsItemFromDb.setAmount(entity.getAmount());
        dao.save(goodsItemFromDb);
    }

    @Override
    public void delete(Integer id) {
        dao.deleteById(id);
    }

    @Override
    public void delete(GoodsItem entity) {
        dao.delete(entity);
    }

    @Override
    public List<GoodsItem> findAll() {
        return dao.findAll();
    }
}
