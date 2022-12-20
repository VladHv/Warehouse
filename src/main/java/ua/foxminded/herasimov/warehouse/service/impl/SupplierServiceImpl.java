package ua.foxminded.herasimov.warehouse.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.foxminded.herasimov.warehouse.dao.SupplierDao;
import ua.foxminded.herasimov.warehouse.exception.ServiceException;
import ua.foxminded.herasimov.warehouse.model.Supplier;
import ua.foxminded.herasimov.warehouse.service.SupplierService;

import java.util.List;

@Service
public class SupplierServiceImpl implements SupplierService {

    private static final Logger logger = LoggerFactory.getLogger(SupplierServiceImpl.class);
    private final SupplierDao dao;

    @Autowired
    public SupplierServiceImpl(SupplierDao dao) {
        this.dao = dao;
    }


    @Override
    public Supplier create(Supplier entity) {
        logger.info("Saving supplier: {}", entity);
        return dao.save(entity);
    }

    @Override
    public Supplier findById(Integer id) {
        logger.info("Finding supplier by id: {}", id);
        return dao.findById(id).orElseThrow(() -> new ServiceException("Supplier not found by ID: " + id));
    }

    @Override
    public Supplier update(Supplier entity, Integer id) {
        logger.info("Updating supplier {} by id: {}", entity, id);
        Supplier supplierFromDb = dao.findById(id).orElseThrow(
            () -> new ServiceException("Supplier for update not found by ID: " + id));
        logger.info("Supplier by id: {} found: {}", id, supplierFromDb);
        supplierFromDb.setFirstName(entity.getFirstName());
        supplierFromDb.setLastName(entity.getLastName());
        supplierFromDb.setOrders(entity.getOrders());
        logger.info("Saving updated supplier: {}", supplierFromDb);
        return dao.save(supplierFromDb);
    }

    @Override
    public void delete(Integer id) {
        logger.info("Checking supplier by id {} in DB", id);
        dao.findById(id).orElseThrow(
            () -> new ServiceException("Supplier for delete not found by ID: " + id));
        logger.info("Deleting supplier by id: {}", id);
        dao.deleteById(id);
    }

    @Override
    public void delete(Supplier entity) {
        logger.info("Checking supplier by id {} in DB", entity.getId());
        dao.findById(entity.getId()).orElseThrow(
            () -> new ServiceException("Supplier for delete not found by ID: " + entity.getId()));
        logger.info("Deleting supplier: {}", entity);
        dao.delete(entity);
    }

    @Override
    public List<Supplier> findAll() {
        logger.trace("Getting all suppliers");
        return dao.findAll();
    }
}
