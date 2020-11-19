package com.huidaxuan.ic2cloud.meituanshoppingcart.imp;

import android.view.View;

import com.huidaxuan.ic2cloud.meituanshoppingcart.enitty.ProductListEntity;

/**
 *
 */
public interface ShopCartImp {
    void add(View view, int postion, ProductListEntity.ProductEntity entity);

    void remove(View view, int postion, ProductListEntity.ProductEntity entity);
}
