package ua.foxminded.herasimov.warehouse.dto.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.herasimov.warehouse.dao.GoodsDao;
import ua.foxminded.herasimov.warehouse.dto.DtoMapper;
import ua.foxminded.herasimov.warehouse.model.GoodsItem;

@Component
public class GoodsItemDtoMapper implements DtoMapper<GoodsItemDto, GoodsItem> {

    private GoodsDao dao;

    @Autowired
    public GoodsItemDtoMapper(GoodsDao dao) {
        this.dao = dao;
    }

    @Override
    public GoodsItemDto toDto(GoodsItem entity) {
        return new GoodsItemDto.Builder().withId(entity.getId())
                                         .withGoodsId(entity.getGoods().getId())
                                         .withGoodsName(entity.getGoods().getName())
                                         .withGoodsPrice(entity.getGoods().getPrice())
                                         .withAmount(entity.getAmount())
                                         .withTotalPrice(entity.getAmount() * entity.getGoods().getPrice())
                                         .build();
    }

    @Override
    public GoodsItem toEntity(GoodsItemDto dto) {
        return new GoodsItem.Builder().withId(dto.getId())
                                      .withGoods(dao.findById(dto.getGoodsId()).orElseThrow())
                                      .withAmount(dto.getAmount())
                                      .build();
    }
}
