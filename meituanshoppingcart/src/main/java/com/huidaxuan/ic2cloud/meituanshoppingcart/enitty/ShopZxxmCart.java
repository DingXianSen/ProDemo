package com.huidaxuan.ic2cloud.meituanshoppingcart.enitty;

import java.util.HashMap;
import java.util.Map;

/**
 * @packageName:com.huidaxuan.ic2cloud.meituanshoppingcart.enitty
 * @className: ShopCart
 * @description:购物车实体-自选项目，数量1或0，没有更多数量
 * @author: dingchao
 * @time: 2020-11-19 08:51
 */
public class ShopZxxmCart {
    private Map<String, Integer> parentCountMap;//父保存数量
    private Map<String, ProductListEntity.ProductEntity> shoppingSingle;
    private boolean flag = false;//是否清空过  false 无 true 清空过


    public ShopZxxmCart() {
        this.shoppingSingle = new HashMap<>();
        this.parentCountMap = new HashMap<>();
        this.flag = false;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    /**
     * 添加一条数据
     *
     * @param flag   true add false reduce
     * @param mapKey f-z
     * @param dish
     * @return
     */
    public void addShoppingSingle(boolean flag, String mapKey, ProductListEntity.ProductEntity dish) {
        //如果集合中存在当前key,替换，否则
        if (flag) {//添加
            dish.setProductCount(1);
            shoppingSingle.put(mapKey, dish);
            //用来记录左侧的数字角标
            if (parentCountMap.containsKey(dish.getParentId())) {

                parentCountMap.put(dish.getParentId(), parentCountMap.get(dish.getParentId()) + 1);
            } else {//如果第一次存储
                parentCountMap.put(dish.getParentId(), 1);
            }
        } else {//移除数据
            dish.setProductCount(0);
            shoppingSingle.remove(mapKey);
            if (parentCountMap.containsKey(dish.getParentId())) {
                parentCountMap.put(dish.getParentId(), parentCountMap.get(dish.getParentId()) - 1);
            }
        }
    }


    public Map<String, ProductListEntity.ProductEntity> getShoppingSingle() {
        return shoppingSingle;
    }

    public void setShoppingSingle(Map<String, ProductListEntity.ProductEntity> shoppingSingle) {
        this.shoppingSingle = shoppingSingle;
    }

    public Map<String, Integer> getParentCountMap() {
        return parentCountMap;
    }

    public void setParentCountMap(Map<String, Integer> parentCountMap) {
        this.parentCountMap = parentCountMap;
    }

    public void clear() {
        this.flag = true;
        this.shoppingSingle.clear();
        this.parentCountMap.clear();
    }
}
