package ua.foxminded.herasimov.warehouse.dto.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.herasimov.warehouse.dao.GoodsDao;
import ua.foxminded.herasimov.warehouse.dao.OrderDao;
import ua.foxminded.herasimov.warehouse.dto.DtoMapper;
import ua.foxminded.herasimov.warehouse.model.OrderItem;

@Component
public class OrderItemDtoMapper implements DtoMapper<OrderItemDto, OrderItem> {

    private GoodsDao goodsDao;
    private OrderDao orderDao;

    @Autowired
    public OrderItemDtoMapper(GoodsDao goodsDao, OrderDao orderDao) {
        this.goodsDao = goodsDao;
        this.orderDao = orderDao;
    }

    @Override
    public OrderItemDto toDto(OrderItem entity) {
        return new OrderItemDto.Builder().withId(entity.getId())
                                         .withOrderId(entity.getOrder().getId())
                                         .withGoodsId(entity.getGoods().getId())
                                         .withAmount(entity.getAmount())
                                         .build();
    }

    @Override
    public OrderItem toEntity(OrderItemDto dto) {
        return new OrderItem.Builder().withId(dto.getId())
                                      .withOrder(orderDao.findById(dto.getOrderId()).orElseThrow())
                                      .withGoods(goodsDao.findById(dto.getGoodsId()).orElseThrow())
                                      .withAmount(dto.getAmount())
                                      .build();
    }
}
