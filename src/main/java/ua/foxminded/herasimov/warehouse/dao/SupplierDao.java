package ua.foxminded.herasimov.warehouse.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.foxminded.herasimov.warehouse.model.Supplier;

public interface SupplierDao extends JpaRepository<Supplier, Integer> {

}
