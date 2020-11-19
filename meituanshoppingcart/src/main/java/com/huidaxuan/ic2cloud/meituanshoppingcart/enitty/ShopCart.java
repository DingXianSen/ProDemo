package com.huidaxuan.ic2cloud.meituanshoppingcart.enitty;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @packageName:com.huidaxuan.ic2cloud.meituanshoppingcart.enitty
 * @className: ShopCart
 * @description:购物车实体
 * @author: dingchao
 * @time: 2020-11-19 08:51
 */
public class ShopCart {
    private int shoppingAccount;//数量
    private double shoppingTotalPrice;//购物车总价格
    private Map<ProductListEntity.ProductEntity, Integer> shoppingSingle;//保存数量
    private Map<String, Integer> parentCountMap;//父保存数量


    public ShopCart() {
        this.shoppingAccount = 0;
        this.shoppingTotalPrice = 0.0;
        this.shoppingSingle = new HashMap<>();
        this.parentCountMap = new HashMap<>();
    }

    public boolean addShoppingSingle(ProductListEntity.ProductEntity dish) {
        double remain = dish.getProductCartMoney();
//        if (remain <= 0)
//            return false;
        //商品的价格，减操作直接--
        dish.setProductCartMoney(--remain);
        int num = 0;
        if (shoppingSingle.containsKey(dish)) {
            num = shoppingSingle.get(dish);
        }
        num += 1;
        /***/
        dish.setProductCount(num);
        shoppingSingle.put(dish, num);

        //如果这个map存在这个父ID的值
        int parentNum = 0;
        if (parentCountMap.containsKey(dish.getParentId())) {
            parentNum = parentCountMap.get(dish.getParentId());
            parentNum += 1;
        } else {//如果第一次存储
            parentNum = 1;
        }
        parentCountMap.put(dish.getParentId(), parentNum);

        Log.e("TAG", "addShoppingSingle: " + shoppingSingle.get(dish));
        shoppingTotalPrice += dish.getProductMoney();//加商品的正常价格
        shoppingAccount += num;
        return true;
    }

    public boolean subShoppingSingle(ProductListEntity.ProductEntity dish) {
        int num = 0;
        if (shoppingSingle.containsKey(dish)) {
            num = shoppingSingle.get(dish);
        }
        if (num <= 0) return false;
        num--;
        double remain = dish.getProductCartMoney();
        dish.setProductCartMoney(++remain);
        dish.setProductCount(num);
        shoppingSingle.put(dish, num);
        if (num == 0) {
            shoppingSingle.remove(dish);
        }

        //如果这个map存在这个父ID的值
        int parentNum = 0;
        if (parentCountMap.containsKey(dish.getParentId())) {
            parentNum = parentCountMap.get(dish.getParentId());
            parentNum -= 1;
            parentCountMap.put(dish.getParentId(), parentNum);
        }
        shoppingTotalPrice -= dish.getProductMoney();
        shoppingAccount -= num;
        return true;
    }


    public int getShoppingAccount() {
        return shoppingSingle.size();
    }

    public void setShoppingAccount(int shoppingAccount) {
        this.shoppingAccount = shoppingAccount;
    }

    public double getShoppingTotalPrice() {
        return shoppingTotalPrice;
    }

    public void setShoppingTotalPrice(double shoppingTotalPrice) {
        this.shoppingTotalPrice = shoppingTotalPrice;
    }

    public Map<ProductListEntity.ProductEntity, Integer> getShoppingSingle() {
        return shoppingSingle;
    }

    public void setShoppingSingle(Map<ProductListEntity.ProductEntity, Integer> shoppingSingle) {
        this.shoppingSingle = shoppingSingle;
    }

    public Map<String, Integer> getParentCountMap() {
        return parentCountMap;
    }

    public void setParentCountMap(Map<String, Integer> parentCountMap) {
        this.parentCountMap = parentCountMap;
    }

    public void clear() {
        this.shoppingAccount = 0;
        this.shoppingTotalPrice = 0;
        this.shoppingSingle.clear();
    }
}
