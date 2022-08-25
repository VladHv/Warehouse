package ua.foxminded.herasimov.warehouse.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
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

}
