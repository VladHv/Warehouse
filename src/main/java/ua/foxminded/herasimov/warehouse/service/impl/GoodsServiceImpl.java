package ua.foxminded.herasimov.warehouse.service.impl;

import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.foxminded.herasimov.warehouse.dao.GoodsDao;
import ua.foxminded.herasimov.warehouse.model.Goods;
import ua.foxminded.herasimov.warehouse.service.GoodsService;

import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {

    private static final Logger logger = LoggerFactory.getLogger(GoodsServiceImpl.class);
    private final GoodsDao dao;

    @Autowired
    public GoodsServiceImpl(GoodsDao dao) {
        this.dao = dao;
    }

    @Override
    public Goods create(Goods entity) {
        logger.info("Saving goods: {}", entity);
        return dao.save(entity);
    }

    @Override
    public Goods findById(Integer id) {
        logger.info("Finding goods by id: {}", id);
        return dao.findById(id).orElseThrow(() -> new ServiceException("Goods not found by ID: " + id));
    }

    @Override
    public Goods update(Goods entity, Integer id) {
        logger.info("Updating goods {} by id: {}", entity, id);
        Goods goodsFromDb = dao.findById(id).orElseThrow(
            () -> new ServiceException("Goods for update not found by ID: " + id));
        logger.info("Goods by id: {} found: {}", id, goodsFromDb);
        goodsFromDb.setName(entity.getName());
        goodsFromDb.setPrice(entity.getPrice());
        logger.info("Saving updated goods: {}", goodsFromDb);
        return dao.save(goodsFromDb);
    }

    @Override
    public void delete(Integer id) {
        logger.info("Checking goods by id {} in DB", id);
        dao.findById(id).orElseThrow(
            () -> new ServiceException("Goods for delete not found by ID: " + id));
        logger.info("Deleting goods by id: {}", id);
        dao.deleteById(id);
    }

    @Override
    public void delete(Goods entity) {
        logger.info("Checking goods by id {} in DB", entity.getId());
        dao.findById(entity.getId()).orElseThrow(
            () -> new ServiceException("Goods for delete not found by ID: " + entity.getId()));
        logger.info("Deleting goods: {}", entity);
        dao.delete(entity);
    }

    @Override
    public List<Goods> findAll() {
        logger.trace("Getting all goods");
        return dao.findAll();
    }
}
