package ua.foxminded.herasimov.warehouse.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.foxminded.herasimov.warehouse.dao.SupplierDao;
import ua.foxminded.herasimov.warehouse.exception.ServiceException;
import ua.foxminded.herasimov.warehouse.model.Supplier;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class SupplierServiceImplTest {

    @Autowired
    private SupplierServiceImpl supplierService;

    @MockBean
    private SupplierDao dao;

    @Test
    void create_shouldReturnSavedSupplier_whenEntitySavedInDB() {
        Supplier supplier = new Supplier.Builder().withFirstName("Bob").withLastName("Smith").build();
        given(dao.save(supplier)).willReturn(supplier);

        Assertions.assertEquals(supplierService.create(supplier), supplier);
        verify(dao, times(1)).save(supplier);
    }

    @Test
    void findById_shouldReturnSupplier_whenIdIsAccepted() {
        Supplier supplier = new Supplier.Builder().withId(1).withFirstName("Bob").withLastName("Smith").build();
        given(dao.findById(supplier.getId())).willReturn(Optional.of(supplier));

        Assertions.assertEquals(supplierService.findById(supplier.getId()), supplier);
        verify(dao, times(1)).findById(supplier.getId());
    }

    @Test
    void findById_shouldThrowServiceEx_whenIdIsNotAccepted() {
        Supplier supplier = new Supplier.Builder().withId(1).withFirstName("Bob").withLastName("Smith").build();
        given(dao.findById(supplier.getId())).willThrow(
            new ServiceException("Supplier not found by ID: " + supplier.getId()));

        Assertions.assertThrows(ServiceException.class, () -> supplierService.findById(1));
        verify(dao, times(1)).findById(supplier.getId());
    }

    @Test
    void update_should_when(){
        Integer supplierId = 1;
        Supplier supplier = new Supplier.Builder().withFirstName("Kevin").withLastName("Eddy").build();

        given(dao.findById(supplierId)).willReturn(Optional.of(new Supplier()));
        given(dao.save(supplier)).willReturn(supplier);

        Assertions.assertEquals(supplierService.update(supplier, supplierId), supplier);
        verify(dao, times(1)).findById(supplierId);
        verify(dao, times(1)).save(any(Supplier.class));
    }
}
