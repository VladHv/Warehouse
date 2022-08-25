package ua.foxminded.herasimov.warehouse.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Goods {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Integer price;

    public static class Builder {
        private Goods newGoods;

        public Builder() {
            newGoods = new Goods();
        }

        public Builder withId(Integer id) {
            newGoods.id = id;
            return this;
        }

        public Builder withName(String name) {
            newGoods.name = name;
            return this;
        }

        public Builder withPrice(Integer price) {
            newGoods.price = price;
            return this;
        }

        public Goods build() {
            return newGoods;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
