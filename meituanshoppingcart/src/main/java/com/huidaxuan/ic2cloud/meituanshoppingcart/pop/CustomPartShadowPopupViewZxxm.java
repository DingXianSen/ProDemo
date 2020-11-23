package com.huidaxuan.ic2cloud.meituanshoppingcart.pop;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.huidaxuan.ic2cloud.meituanshoppingcart.R;
import com.huidaxuan.ic2cloud.meituanshoppingcart.enitty.EventBusShoppingEntity;
import com.huidaxuan.ic2cloud.meituanshoppingcart.enitty.ProductListEntity;
import com.huidaxuan.ic2cloud.meituanshoppingcart.enitty.ShopZxxmCart;
import com.huidaxuan.ic2cloud.meituanshoppingcart.scrollall.adapter.ShoppingCartZxxmAdapter;
import com.lxj.xpopup.impl.PartShadowPopupView;

import org.greenrobot.eventbus.EventBus;

/**
 * @className: CustomPartShadowPopupView
 * @description:自选项目弹窗
 * @author: dingchao
 * @time: 2020-11-19 15:13
 */
public class CustomPartShadowPopupViewZxxm extends PartShadowPopupView implements View.OnClickListener {
    private ListView lv_pop_list;
    private Context context;
    private ShopZxxmCart shopCart;
    private TextView tv_shopping_cart_clear_all;
    private TextView tv_shopping_cart_top_key;
    private TextView tv_shopping_cart_top_key_v;
    ShoppingCartZxxmAdapter shoppingCartAdapter;

    public CustomPartShadowPopupViewZxxm(@NonNull Context context, ShopZxxmCart shopCart) {
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
        tv_shopping_cart_top_key = findViewById(R.id.tv_shopping_cart_top_key);
        tv_shopping_cart_clear_all.setOnClickListener(this);
        tv_shopping_cart_top_key_v.setVisibility(INVISIBLE);
    }


    /**
     * 初始数据绑定及操作
     */
    private void initDataViewBind() {
        //数据绑定及展示
        shoppingCartAdapter = new ShoppingCartZxxmAdapter(context, shopCart);
        lv_pop_list.setAdapter(shoppingCartAdapter);
        updateShoppingCartNum();

        //数据删除，通知更新
        shoppingCartAdapter.setOnItemChrldListner(new ShoppingCartZxxmAdapter.OnItemChrldListner() {
            @Override
            public void onCall(View view, ProductListEntity.ProductEntity entity, String type) {
                if (type.equals("del")) {
                    updateShoppingCartNum();
//                    shopCart.addShoppingSingle(false, entity.getParentId() + "-" + entity.getProductId(), entity);
                    if (shopCart != null && shopCart.getShoppingSingle().size() == 0) {
                        dismiss();
                    }
                    //通知跟新UI
                    EventBus.getDefault().post(new EventBusShoppingEntity(entity, "delZxxm"));
                }
            }
        });
    }

    @Override
    protected void onShow() {
        super.onShow();
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();

    }

    //      updateShoppingCartNum();
//        EventBus.getDefault().post(new EventBusShoppingEntity(entity, "add"));
//    updateShoppingCartNum();
//        if (shopCart != null && shopCart.getParentCountMap().size() == 0) {
//        this.dismiss();
//}

    /**
     * 更新数字
     */
    private void updateShoppingCartNum() {
        if (shopCart != null) {
            int textCount = 0;
            for (ProductListEntity.ProductEntity m : shopCart.getShoppingSingle().values()) {
                Log.e("btn_shopping_cart_pay", "map集合中存储的数据---->" + m.getProductCount());
                textCount += m.getProductCount();
            }
            tv_shopping_cart_top_key.setText("已选项目（" + textCount + ")");
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
                EventBus.getDefault().post(new EventBusShoppingEntity(null, "clearZxxmAll"));
                break;
            default:
                break;
        }
    }
}
