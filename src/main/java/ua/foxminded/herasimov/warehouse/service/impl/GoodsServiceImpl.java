package ua.foxminded.herasimov.warehouse.service.impl;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.foxminded.herasimov.warehouse.dao.GoodsDao;
import ua.foxminded.herasimov.warehouse.model.Goods;
import ua.foxminded.herasimov.warehouse.service.GoodsService;

import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {

    private GoodsDao dao;

    @Autowired
    public GoodsServiceImpl(GoodsDao dao) {
        this.dao = dao;
    }

    @Override
    public void create(Goods entity) {
        dao.save(entity);
    }

    @Override
    public Goods findById(Integer id) {
        return dao.findById(id).orElseThrow(() -> new ServiceException("Goods not found by ID: " + id));
    }

    @Override
    public Goods update(Goods entity, Integer id) {
        Goods goodsFromDb = dao.findById(id).orElseThrow(
            () -> new ServiceException("Goods for update not found by ID: " + entity.getId()));
        goodsFromDb.setName(entity.getName());
        goodsFromDb.setPrice(entity.getPrice());
        return dao.save(goodsFromDb);
    }

    @Override
    public void delete(Integer id) {
        dao.findById(id).orElseThrow(
            () -> new ServiceException("Goods for delete not found by ID: " + id));
        dao.deleteById(id);
    }

    @Override
    public void delete(Goods entity) {
        dao.delete(entity);
    }

    @Override
    public List<Goods> findAll() {
        return dao.findAll();
    }
}
