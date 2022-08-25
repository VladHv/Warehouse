package ua.foxminded.herasimov.warehouse.model;

import javax.persistence.*;

@Entity
public class GoodsItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "goods_id")
    private Goods goods;
    private Integer amount;

    public static class Builder {
        private GoodsItem newGoodsItem;

        public Builder() {
            newGoodsItem = new GoodsItem();
        }

        public Builder withId(Integer id) {
            newGoodsItem.id = id;
            return this;
        }

        public Builder withGoods(Goods goods) {
            newGoodsItem.goods = goods;
            return this;
        }

        public Builder withAmount(Integer amount) {
            newGoodsItem.amount = amount;
            return this;
        }

        public GoodsItem build() {
            return newGoodsItem;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
}
