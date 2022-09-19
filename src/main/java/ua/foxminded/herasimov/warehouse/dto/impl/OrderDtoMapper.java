package ua.foxminded.herasimov.warehouse.dto.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.herasimov.warehouse.dao.SupplierDao;
import ua.foxminded.herasimov.warehouse.dto.DtoMapper;
import ua.foxminded.herasimov.warehouse.model.Order;

@Component
public class OrderDtoMapper implements DtoMapper<OrderDto, Order> {

    private SupplierDao supplierDao;

    @Autowired
    public OrderDtoMapper(SupplierDao supplierDao) {
        this.supplierDao = supplierDao;
    }

    @Override
    public OrderDto toDto(Order entity) {
        return new OrderDto.Builder().withId(entity.getId())
                                     .build();
    }

    @Override
    public Order toEntity(OrderDto dto) {
        return new Order.Builder().withId(dto.getId())
                                  .withSupplier(supplierDao.findById(dto.getSupplierId()).orElseThrow())
                                  .build();
    }
}
