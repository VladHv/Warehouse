package ua.foxminded.herasimov.warehouse.model;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private Set<OrderItem> orderItems;

    @Enumerated(EnumType.ORDINAL)
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    public static class Builder {
        private Order newOrder;

        public Builder() {
            newOrder = new Order();
        }

        public Builder withId(Integer id) {
            newOrder.id = id;
            return this;
        }

        public Builder withOrderItems(Set<OrderItem> orderItems) {
            newOrder.orderItems = orderItems;
            return this;
        }

        public Builder withStatus(OrderStatus status) {
            newOrder.status = status;
            return this;
        }

        public Builder withSupplier(Supplier supplier) {
            newOrder.supplier = supplier;
            return this;
        }

        public Order build() {
            return newOrder;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Set<OrderItem> getItems() {
        return orderItems;
    }

    public void setItems(Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(orderItems, order.orderItems) && status == order.status &&
               Objects.equals(supplier, order.supplier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderItems, status, supplier);
    }

    @Override
    public String toString() {
        return "Order{" +
               "id=" + id +
               ", orderItems=" + orderItems +
               ", status=" + status +
               ", supplier=" + supplier +
               '}';
    }
}
