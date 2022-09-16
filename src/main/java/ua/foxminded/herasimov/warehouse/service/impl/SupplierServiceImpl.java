package ua.foxminded.herasimov.warehouse.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.foxminded.herasimov.warehouse.dao.SupplierDao;
import ua.foxminded.herasimov.warehouse.exception.ServiceException;
import ua.foxminded.herasimov.warehouse.model.Supplier;
import ua.foxminded.herasimov.warehouse.service.SupplierService;

import java.util.List;

@Service
public class SupplierServiceImpl implements SupplierService {

    private SupplierDao dao;

    @Autowired
    public SupplierServiceImpl(SupplierDao dao) {
        this.dao = dao;
    }


    @Override
    public void create(Supplier entity) {
        dao.save(entity);
    }

    @Override
    public Supplier findById(Integer id) {
        return dao.findById(id).orElseThrow(() -> new ServiceException("Supplier not found by ID: " + id));
    }

    @Override
    public void update(Supplier entity) {
        Supplier supplierFromDb = dao.findById(entity.getId()).orElseThrow(
            () -> new ServiceException("Supplier for update not found by ID: " + entity.getId()));
        supplierFromDb.setFirstName(entity.getFirstName());
        supplierFromDb.setLastName(entity.getLastName());
        supplierFromDb.setOrders(entity.getOrders());
        dao.save(supplierFromDb);
    }

    @Override
    public void delete(Integer id) {
        dao.deleteById(id);
    }

    @Override
    public void delete(Supplier entity) {
        dao.delete(entity);
    }

    @Override
    public List<Supplier> findAll() {
        return dao.findAll();
    }
}
