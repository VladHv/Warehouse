package ua.foxminded.herasimov.warehouse.dto.impl;

import ua.foxminded.herasimov.warehouse.dto.Dto;

import java.util.Objects;

public class GoodsItemDto implements Dto {

    private Integer id;
    private Integer goodsId;
    private Integer amount;

    public static class Builder {
        private GoodsItemDto newGoodsItemDto;

        public Builder() {
            newGoodsItemDto = new GoodsItemDto();
        }

        public Builder withId(Integer id) {
            newGoodsItemDto.id = id;
            return this;
        }

        public Builder withGoodsId(Integer goodsId) {
            newGoodsItemDto.goodsId = goodsId;
            return this;
        }

        public Builder withAmount(Integer amount) {
            newGoodsItemDto.amount = amount;
            return this;
        }

        public GoodsItemDto build() {
            return newGoodsItemDto;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GoodsItemDto that = (GoodsItemDto) o;
        return Objects.equals(goodsId, that.goodsId) && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(goodsId, amount);
    }

    @Override
    public String toString() {
        return "GoodsItemDto{" +
               "id=" + id +
               ", goodsId=" + goodsId +
               ", amount=" + amount +
               '}';
    }
}
