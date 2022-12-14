package ua.foxminded.herasimov.warehouse.model;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "The unique id of the order item", example = "1")
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "goods_id")
    private Goods goods;

    @NotNull(message = "Input amount!")
    @Min(value = 1, message = "Amount should be at least one")
    @ApiModelProperty(notes = "The goods' amount in order", example = "66")
    private Integer amount;

    public static class Builder {
        private OrderItem newOrderItem;

        public Builder() {
            newOrderItem = new OrderItem();
        }

        public Builder withId(Integer id) {
            newOrderItem.id = id;
            return this;
        }

        public Builder withOrder(Order order) {
            newOrderItem.order = order;
            return this;
        }

        public Builder withGoods(Goods goods) {
            newOrderItem.goods = goods;
            return this;
        }

        public Builder withAmount(Integer amount) {
            newOrderItem.amount = amount;
            return this;
        }

        public OrderItem build() {
            return newOrderItem;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
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
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(order, orderItem.order) && Objects.equals(goods, orderItem.goods) &&
               Objects.equals(amount, orderItem.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, goods, amount);
    }

    @Override
    public String toString() {
        return "OrderItem{" +
               "id=" + id +
               ", order=" + order +
               ", goods=" + goods +
               ", amount=" + amount +
               '}';
    }
}
