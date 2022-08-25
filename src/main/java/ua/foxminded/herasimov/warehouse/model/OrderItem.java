package ua.foxminded.herasimov.warehouse.model;

import javax.persistence.*;

@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "goods_id")
    private Goods goods;

    public static class Builder {
        private OrderItem newOrderItem;

        public Builder(){
            newOrderItem = new OrderItem();
        }

        public Builder withId(Integer id){
            newOrderItem.id = id;
            return this;
        }

        public Builder withOrder(Order order){
            newOrderItem.order = order;
            return this;
        }

        public Builder withGoods(Goods goods) {
            newOrderItem.goods = goods;
            return this;
        }

        public OrderItem build(){
            return newOrderItem;
        }
    }

    private Integer quantity;

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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
