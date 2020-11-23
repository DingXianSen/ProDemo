package com.huidaxuan.ic2cloud.meituanshoppingcart.enitty;

/**
 * @packageName:com.huidaxuan.ic2cloud.meituanshoppingcart.enitty
 * @className: EventBusShoppingEntity
 * @description：车主服务-装饰用品通知activity数据更新使用
 * @author: dingchao
 * @time: 2020-11-19 15:49
 */
public class EventBusShoppingZxypEntity {
    private ShopCart shopCart;
    private String key;
    private ShopZxxmCart shopZxxmCart;

    public EventBusShoppingZxypEntity(ShopCart shopCart, String key) {
        this.shopCart = shopCart;
        this.key = key;
    }

    public EventBusShoppingZxypEntity(String key, ShopZxxmCart shopZxxmCart) {
        this.key = key;
        this.shopZxxmCart = shopZxxmCart;
    }

    public ShopZxxmCart getShopZxxmCart() {
        return shopZxxmCart;
    }

    public void setShopZxxmCart(ShopZxxmCart shopZxxmCart) {
        this.shopZxxmCart = shopZxxmCart;
    }

    public ShopCart getShopCart() {
        return shopCart;
    }

    public void setShopCart(ShopCart shopCart) {
        this.shopCart = shopCart;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
