package com.huidaxuan.ic2cloud.meituanshoppingcart.scrollall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.huidaxuan.ic2cloud.meituanshoppingcart.R;
import com.huidaxuan.ic2cloud.meituanshoppingcart.enitty.ProductListEntity;
import com.huidaxuan.ic2cloud.meituanshoppingcart.enitty.ShopZxxmCart;

import java.util.ArrayList;

/**
 * @packageName:com.huidaxuan.ic2cloud.app2b.adapter
 * @className: ShoppingCartAdapter
 * @description:购物车适配器Map数据源
 * @author: dingchao
 * @time: 2020-08-04 13:59
 */
public class ShoppingCartZxxmAdapter extends BaseAdapter {
    private Context context;
    private ShopZxxmCart shopCart;
    private ArrayList<ProductListEntity.ProductEntity> dishList;
    private int itemCount;

    public ShoppingCartZxxmAdapter(Context context, ShopZxxmCart shopCart) {
        this.context = context;
        this.shopCart = shopCart;
        this.itemCount = shopCart.getShoppingSingle().size();
        this.dishList = new ArrayList<>();
        dishList.addAll(shopCart.getShoppingSingle().values());
    }

    @Override
    public int getCount() {
        return this.itemCount;
    }

    @Override
    public Object getItem(int position) {
        return "";
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ShoppingCartZxxmAdapter.ViewHolder holder = null;
        if (convertView == null) {
            holder = new ShoppingCartZxxmAdapter.ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_shopping_cart_zxxm, parent, false);

            holder.tv_item_life_product_name = convertView.findViewById(R.id.tv_item_life_product_name);
            holder.tv_item_life_product_monty = convertView.findViewById(R.id.tv_item_life_product_monty);
            holder.tv_item_life_product_money = convertView.findViewById(R.id.tv_item_life_product_money);
            holder.rl_item_life_product_pay_all = convertView.findViewById(R.id.rl_item_life_product_pay_all);

            convertView.setTag(holder);
        } else {
            holder = (ShoppingCartZxxmAdapter.ViewHolder) convertView.getTag();
        }

        final ProductListEntity.ProductEntity dish = getDishByPosition(position);
        if (dish != null) {
            //信息绑定
            holder.tv_item_life_product_name.setText(dish.getProductName());
            holder.tv_item_life_product_monty.setText("描述 描述 描述 描述 描述 描述 描述 描述 描述 描述 描述 ");
            holder.tv_item_life_product_money.setText("" + dish.getProductMoney());

            //这里只删不加减
            holder.rl_item_life_product_pay_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mapKey = dish.getParentId() + "-" + dish.getProductId();
                    if (shopCart.getShoppingSingle().containsKey(mapKey)) {
                        //删除数据
                        onItemChrldListner.onCall(v, dish, "del");
                        shopCart.addShoppingSingle(false, mapKey, dish);
                        dishList.clear();
                        dishList.addAll(shopCart.getShoppingSingle().values());
                        itemCount = shopCart.getShoppingSingle().size();
                    }
                    notifyDataSetChanged();
                    onItemChrldListner.onCall(v, dish, "del");
                }
            });
        }


        return convertView;

    }


    public ProductListEntity.ProductEntity getDishByPosition(int position) {
        return dishList.get(position);
    }


    private class ViewHolder {
        private TextView tv_item_life_product_name;
        private TextView tv_item_life_product_monty;
        private TextView tv_item_life_product_money;

        private RelativeLayout rl_item_life_product_pay_all;

    }


    //要定义一个按钮监听抽象接口和时间
    public interface OnItemChrldListner {
        void onCall(View view, ProductListEntity.ProductEntity entity, String type);
    }

    //定义一个监听 再activity中调用
    private ShoppingCartZxxmAdapter.OnItemChrldListner onItemChrldListner;

    public void setOnItemChrldListner(ShoppingCartZxxmAdapter.OnItemChrldListner onItemChrldListner) {
        this.onItemChrldListner = onItemChrldListner;
    }


}
