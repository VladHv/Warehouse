package ua.foxminded.herasimov.warehouse.dto.impl;

import ua.foxminded.herasimov.warehouse.dto.Dto;

import java.util.Objects;

public class GoodsItemDto implements Dto {

    private Integer id;
    private Integer goodsId;
    private String goodsName;
    private Integer goodsPrice;
    private Integer amount;
    private Integer totalPrice;

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

        public Builder withGoodsName(String goodsName){
            newGoodsItemDto.goodsName = goodsName;
            return this;
        }

        public Builder withGoodsPrice(Integer goodsPrice){
            newGoodsItemDto.goodsPrice = goodsPrice;
            return this;
        }

        public Builder withAmount(Integer amount) {
            newGoodsItemDto.amount = amount;
            return this;
        }

        public Builder withTotalPrice(Integer totalPrice){
            newGoodsItemDto.totalPrice = totalPrice;
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

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Integer getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(Integer goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GoodsItemDto that = (GoodsItemDto) o;
        return Objects.equals(goodsId, that.goodsId) && Objects.equals(goodsName, that.goodsName) &&
               Objects.equals(goodsPrice, that.goodsPrice) && Objects.equals(amount, that.amount) &&
               Objects.equals(totalPrice, that.totalPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(goodsId, goodsName, goodsPrice, amount, totalPrice);
    }
}
