package com.huidaxuan.ic2cloud.prodemo.grouplist;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.huidaxuan.ic2cloud.prodemo.R;
import com.huidaxuan.ic2cloud.prodemo.adapter.ShoppingCartAdapter;
import com.huidaxuan.ic2cloud.prodemo.entity.EventBusEntity;
import com.huidaxuan.ic2cloud.prodemo.entity.ServiceListEntity;
import com.lxj.xpopup.impl.PartShadowPopupView;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

/**
 * Description: 自定义局部阴影弹窗
 * Create by dance, at 2018/12/21
 */
public class CustomPartShadowPopupView extends PartShadowPopupView {
    private Map<String, ServiceListEntity.childBean> stringchildBeanMap;
    private ListView lv_pop_list;
    private Context context;

    public CustomPartShadowPopupView(@NonNull Context context, Map<String, ServiceListEntity.childBean> stringchildBeanMap) {
        super(context);
        this.context = context;
        this.stringchildBeanMap = stringchildBeanMap;
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
    }


    /**
     * 初始数据绑定及操作
     */
    private void initDataViewBind() {
        //数据绑定及展示
        ShoppingCartAdapter shoppingCartAdapter = new ShoppingCartAdapter(context, stringchildBeanMap);
        lv_pop_list.setAdapter(shoppingCartAdapter);

        shoppingCartAdapter.setOnItemChrldListner((view, mapKey, type) -> {
            ServiceListEntity.childBean childBean = stringchildBeanMap.get(mapKey);
            if (type.equals("add")) {//通知ServiceListActivity增加+1
                //todo 通知更新购物车角标变化
                EventBus.getDefault().post(new EventBusEntity(mapKey, ServiceListActivity.EVENT_SHOPPING_CART_ADD));
            } else if (type.equals("reduce")) {//通知ServiceListActivity-1
                EventBus.getDefault().post(new EventBusEntity(mapKey, ServiceListActivity.EVENT_SHOPPING_CART_REDUCE));
            } else if (type.equals("zero")) {//通知ServiceListActivity 归零
                //删除数据
                stringchildBeanMap.remove(mapKey);
                //更新适配器的数据,这里需要更新数据源，否则会因为找不到对应下标报空
                shoppingCartAdapter.refreshDataSource(stringchildBeanMap);
                //如果购物车弹窗中的strMapsize为0了也就说明购物车没东西了，所以关闭弹窗
                if (stringchildBeanMap.size() == 0) {
                    this.dismiss();
                }
                EventBus.getDefault().post(new EventBusEntity(mapKey, ServiceListActivity.EVENT_SHOPPING_CART_ZERO));
            }
            //列表更新
            shoppingCartAdapter.notifyDataSetChanged();
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
}
