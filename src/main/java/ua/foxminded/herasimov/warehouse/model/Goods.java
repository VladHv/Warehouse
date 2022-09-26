package ua.foxminded.herasimov.warehouse.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
public class Goods {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Input name of goods!")
    @Size(min = 2, max = 250, message = "Length should be from 2 to 250")
    private String name;

    @NotNull(message = "Input price!")
    @Min(value = 1, message = "Price should be at least one dollar")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Goods goods = (Goods) o;
        return Objects.equals(name, goods.name) && Objects.equals(price, goods.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }

    @Override
    public String toString() {
        return "Goods{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", price=" + price +
               '}';
    }
}
