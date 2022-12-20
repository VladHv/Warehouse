package ua.foxminded.herasimov.warehouse.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.foxminded.herasimov.warehouse.dao.GoodsItemDao;
import ua.foxminded.herasimov.warehouse.exception.ServiceException;
import ua.foxminded.herasimov.warehouse.model.GoodsItem;
import ua.foxminded.herasimov.warehouse.service.GoodsItemService;

import java.util.List;

@Service
public class GoodsItemServiceImpl implements GoodsItemService {

    private static final Logger logger = LoggerFactory.getLogger(GoodsItemServiceImpl.class);
    private final GoodsItemDao dao;

    @Autowired
    public GoodsItemServiceImpl(GoodsItemDao dao) {
        this.dao = dao;
    }

    @Override
    public GoodsItem create(GoodsItem entity) {
        logger.info("Saving goods item: {}", entity);
        return dao.save(entity);
    }

    @Override
    public GoodsItem findById(Integer id) {
        logger.info("Finding goods item by id: {}", id);
        return dao.findById(id).orElseThrow(() -> new ServiceException("GoodsItem not found by ID: " + id));
    }

    @Override
    public GoodsItem update(GoodsItem entity, Integer id) {
        logger.info("Updating goods item {} by id: {}", entity, id);
        GoodsItem goodsItemFromDb = dao.findById(id).orElseThrow(
            () -> new ServiceException("GoodsItem for update not found by ID: " + id));
        logger.info("Goods item by id: {} found: {}", id, goodsItemFromDb);
        goodsItemFromDb.setGoods(entity.getGoods());
        goodsItemFromDb.setAmount(entity.getAmount());
        logger.info("Saving updated goods item: {}", goodsItemFromDb);
        return dao.save(goodsItemFromDb);
    }

    @Override
    public void delete(Integer id) {
        logger.info("Checking goods item by id {} in DB", id);
        dao.findById(id).orElseThrow(
            () -> new ServiceException("GoodsItem for delete not found by ID: " + id));
        logger.info("Deleting goods item by id: {}", id);
        dao.deleteById(id);
    }

    @Override
    public void delete(GoodsItem entity) {
        logger.info("Checking goods item by id {} in DB", entity.getId());
        dao.findById(entity.getId()).orElseThrow(
            () -> new ServiceException("GoodsItem for delete not found by ID: " + entity.getId()));
        logger.info("Deleting goods item {}", entity);
        dao.delete(entity);
    }

    @Override
    public List<GoodsItem> findAll() {
        logger.trace("Getting all goods items");
        return dao.findAll();
    }
}
