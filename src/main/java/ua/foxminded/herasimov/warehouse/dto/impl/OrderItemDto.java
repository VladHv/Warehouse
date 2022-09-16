package ua.foxminded.herasimov.warehouse.dto.impl;

import ua.foxminded.herasimov.warehouse.dto.Dto;

import java.util.Objects;

public class OrderItemDto implements Dto {

    private Integer id;
    private Integer orderId;
    private Integer goodsId;
    private Integer amount;

    public static class Builder {
        private OrderItemDto newOrderItemDtp;

        public Builder() {
            newOrderItemDtp = new OrderItemDto();
        }

        public Builder withId(Integer id) {
            newOrderItemDtp.id = id;
            return this;
        }

        public Builder withOrderId(Integer orderId) {
            newOrderItemDtp.orderId = orderId;
            return this;
        }

        public Builder withGoodsId(Integer goodsId) {
            newOrderItemDtp.goodsId = goodsId;
            return this;
        }

        public Builder withAmount(Integer amount) {
            newOrderItemDtp.amount = amount;
            return this;
        }

        public OrderItemDto build() {
            return newOrderItemDtp;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
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
        OrderItemDto that = (OrderItemDto) o;
        return Objects.equals(orderId, that.orderId) && Objects.equals(goodsId, that.goodsId) &&
               Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, goodsId, amount);
    }

    @Override
    public String toString() {
        return "OrderItemDto{" +
               "id=" + id +
               ", orderId=" + orderId +
               ", goodsId=" + goodsId +
               ", amount=" + amount +
               '}';
    }
}
