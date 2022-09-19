package ua.foxminded.herasimov.warehouse.dto.impl;

import ua.foxminded.herasimov.warehouse.dto.Dto;

import javax.validation.constraints.NotNull;

public class OrderDto implements Dto {

    private Integer id;

    @NotNull(message = "Choose supplier!")
    private Integer supplierId;

    public static class Builder {
        private OrderDto newOrderDto;

        public Builder() {
            newOrderDto = new OrderDto();
        }

        public Builder withId(Integer id) {
            newOrderDto.id = id;
            return this;
        }

        public Builder withSupplierId(Integer supplierId) {
            newOrderDto.supplierId = supplierId;
            return this;
        }

        public OrderDto build() {
            return newOrderDto;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    @Override
    public String toString() {
        return "OrderDto{" +
               "id=" + id +
               ", supplierId=" + supplierId +
               '}';
    }
}
