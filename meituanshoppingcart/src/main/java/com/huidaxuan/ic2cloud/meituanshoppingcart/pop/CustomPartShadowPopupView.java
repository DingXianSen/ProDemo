package com.huidaxuan.ic2cloud.meituanshoppingcart.pop;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.huidaxuan.ic2cloud.meituanshoppingcart.R;
import com.huidaxuan.ic2cloud.meituanshoppingcart.adapter.ShoppingCartAdapter;
import com.huidaxuan.ic2cloud.meituanshoppingcart.enitty.EventBusShoppingEntity;
import com.huidaxuan.ic2cloud.meituanshoppingcart.enitty.ProductListEntity;
import com.huidaxuan.ic2cloud.meituanshoppingcart.enitty.ShopCart;
import com.huidaxuan.ic2cloud.meituanshoppingcart.imp.ShopCartImp;
import com.lxj.xpopup.impl.PartShadowPopupView;

import org.greenrobot.eventbus.EventBus;

/**
 * @packageName:com.huidaxuan.ic2cloud.meituanshoppingcart.pop
 * @className: CustomPartShadowPopupView
 * @description:
 * @author: dingchao
 * @time: 2020-11-19 15:13
 */
public class CustomPartShadowPopupView extends PartShadowPopupView implements ShopCartImp, View.OnClickListener {
    private ListView lv_pop_list;
    private Context context;
    private ShopCart shopCart;
    private TextView tv_shopping_cart_clear_all;
    private TextView tv_shopping_cart_top_key_v;
    ShoppingCartAdapter shoppingCartAdapter;

    public CustomPartShadowPopupView(@NonNull Context context, ShopCart shopCart) {
        super(context);
        this.context = context;
        this.shopCart = shopCart;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.pop_shopping_cart;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initListener();
        initDataViewBind();
    }

    /**
     * 控件初始绑定
     */
    private void initListener() {
        lv_pop_list = findViewById(R.id.lv_pop_list);
        tv_shopping_cart_clear_all = findViewById(R.id.tv_shopping_cart_clear_all);
        tv_shopping_cart_top_key_v = findViewById(R.id.tv_shopping_cart_top_key_v);
        tv_shopping_cart_clear_all.setOnClickListener(this);
    }


    /**
     * 初始数据绑定及操作
     */
    private void initDataViewBind() {
        //数据绑定及展示
        shoppingCartAdapter = new ShoppingCartAdapter(context, shopCart);
        lv_pop_list.setAdapter(shoppingCartAdapter);
        shoppingCartAdapter.setShopCartImp(this);
        updateShoppingCartNum();
    }

    @Override
    protected void onShow() {
        super.onShow();
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
    }

    @Override
    public void add(View view, int postion, ProductListEntity.ProductEntity entity) {
        updateShoppingCartNum();
        EventBus.getDefault().post(new EventBusShoppingEntity(entity, "add"));
    }

    /**
     * 更新数字
     */
    private void updateShoppingCartNum() {
        if (shopCart != null) {
            int textCount = 0;
            for (ProductListEntity.ProductEntity m : shopCart.getShoppingSingle().keySet()) {
                Log.e("btn_shopping_cart_pay", "map集合中存储的数据---->" + m.getProductCount());
                textCount += m.getProductCount();
            }
            tv_shopping_cart_top_key_v.setText("(共" + textCount + "件商品)");
        }
    }

    @Override
    public void remove(View view, int postion, ProductListEntity.ProductEntity entity) {
        //判读count是不是到0了，到0说明没数据了，如果购物车弹窗开着，则关闭
        updateShoppingCartNum();
        EventBus.getDefault().post(new EventBusShoppingEntity(entity, "reduce"));
        if (shopCart != null && shopCart.getShoppingAccount() == 0) {
            this.dismiss();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_shopping_cart_clear_all:
                //清空
                shopCart.clear();
                this.dismiss();
                updateShoppingCartNum();
                EventBus.getDefault().post(new EventBusShoppingEntity(null, "clearAll"));
                break;
            default:
                break;
        }
    }
}
