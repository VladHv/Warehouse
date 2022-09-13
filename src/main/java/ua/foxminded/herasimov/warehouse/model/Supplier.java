package ua.foxminded.herasimov.warehouse.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.Objects;
import java.util.Set;

@Entity
public class Supplier extends Person {

    @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY)
    private Set<Order> orders;

    public static final class Builder extends Person.ABuilder<Supplier, Builder> {

        @Override
        protected Supplier getObject() {
            return new Supplier();
        }

        @Override
        protected Builder thisObject() {
            return this;
        }

        public Builder withOrders(Set<Order> orders) {
            object.orders = orders;
            return this;
        }
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Supplier supplier = (Supplier) o;
        return Objects.equals(orders, supplier.orders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), orders);
    }

    @Override
    public String toString() {
        return "Supplier{" +
               "id=" + id +
               ", firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               '}';
    }
}
