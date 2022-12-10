package ua.foxminded.herasimov.warehouse.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.foxminded.herasimov.warehouse.dao.SupplierDao;
import ua.foxminded.herasimov.warehouse.model.Supplier;

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
    void create_should_when() {
        Supplier supplier = new Supplier.Builder().withFirstName("Bob").withLastName("Smith").build();
        given(dao.save(supplier)).willReturn(supplier);

        Assertions.assertEquals(supplierService.create(supplier), supplier);
        verify(dao, times(1)).save(supplier);

    }
}
