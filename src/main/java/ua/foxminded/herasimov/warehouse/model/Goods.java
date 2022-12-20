package ua.foxminded.herasimov.warehouse.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@ApiModel(description = "Details about the goods")
public class Goods {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "The unique id of the goods", example = "1")
    private Integer id;

    @NotNull(message = "Input name of goods!")
    @Size(min = 2, max = 250, message = "Length should be from 2 to 250")
    @ApiModelProperty(notes = "The goods' name", example = "Tomato")
    private String name;

    @NotNull(message = "Input price!")
    @Min(value = 1, message = "Price should be at least one dollar")
    @ApiModelProperty(notes = "The goods' price in dollars", example = "59")
    private Integer price;

    @JsonIgnore
    @OneToOne(mappedBy = "goods", cascade = CascadeType.ALL)
    private GoodsItem goodsItem;

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

        public Builder withGoodsItem(GoodsItem goodsItem) {
            newGoods.goodsItem = goodsItem;
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

    public GoodsItem getGoodsItem() {
        return goodsItem;
    }

    public void setGoodsItem(GoodsItem goodsItem) {
        this.goodsItem = goodsItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Goods goods = (Goods) o;

        if (!Objects.equals(name, goods.name)) return false;
        if (!Objects.equals(price, goods.price)) return false;
        return Objects.equals(goodsItem, goods.goodsItem);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (goodsItem != null ? goodsItem.hashCode() : 0);
        return result;
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
