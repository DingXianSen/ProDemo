package com.huidaxuan.ic2cloud.meituanshoppingcart.scrollall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.huidaxuan.ic2cloud.meituanshoppingcart.R;
import com.huidaxuan.ic2cloud.meituanshoppingcart.enitty.ProductListEntity;

import java.util.ArrayList;

/**
 * 门店详情-无购物车服务
 */
public class RightServiceNoCartAdapter extends RecyclerView.Adapter {
    private final int MENU_TYPE = 0;
    private final int DISH_TYPE = 1;
    private final int HEAD_TYPE = 2;

    private Context mContext;
    private ArrayList<ProductListEntity> data;

    private int mItemCount;

    public RightServiceNoCartAdapter(Context mContext, ArrayList<ProductListEntity> mMenuList) {
        this.mContext = mContext;
        this.data = mMenuList;
        this.mItemCount = mMenuList.size();
        for (ProductListEntity menu : mMenuList) {
            mItemCount += menu.getProductEntities().size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        int sum = 0;
        for (ProductListEntity menu : data) {
            if (position == sum) {
                return MENU_TYPE;
            }
            sum += menu.getProductEntities().size() + 1;
        }
        return DISH_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == MENU_TYPE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.include_right_item_title, parent, false);
            MenuViewHolder viewHolder = new MenuViewHolder(view);
            return viewHolder;
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_owner_all_tc, parent, false);
            DishViewHolder viewHolder = new DishViewHolder(view);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == MENU_TYPE) {
            MenuViewHolder menuholder = (MenuViewHolder) holder;
            if (menuholder != null) {
                menuholder.right_menu_title.setText(getMenuByPosition(position).getTypeName());
                menuholder.right_menu_layout.setContentDescription(position + "");
            }
        } else {
            final DishViewHolder dishholder = (DishViewHolder) holder;
            if (dishholder != null) {
                //根据下标取商品,当前这个position是右侧（所有展示数据）的下标
                final ProductListEntity.ProductEntity dish = getDishByPosition(position);
                dishholder.tv_item_life_product_name.setText(dish.getProductName());
                dishholder.tv_item_life_product_monty.setText("月售 " + dish.getProductMonth());
                dishholder.tv_item_life_product_money.setText("" + dish.getProductMoney());
                dishholder.right_dish_layout.setContentDescription(position + "");
                //todo 套餐描述
//                dishholder.tv_item_life_product_msg.setText("" + dish.getProductMoney());


                //加减点击时间
                dishholder.btn_item_life_product_pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //todo 门店详情-全部套餐-购买时把当前对象传递过去
                        onItemChrldListner.onCall(view, dish, "pay");
                    }
                });
            }
        }
    }


    public ProductListEntity getMenuByPosition(int position) {
        int sum = 0;
        for (ProductListEntity menu : data) {
            if (position == sum) {
                return menu;
            }
            sum += menu.getProductEntities().size() + 1;
        }
        return null;
    }

    public ProductListEntity.ProductEntity getDishByPosition(int position) {
        for (ProductListEntity menu : data) {
            if (position > 0 && position <= menu.getProductEntities().size()) {
                return menu.getProductEntities().get(position - 1);
            } else {
                position -= menu.getProductEntities().size() + 1;
            }
        }
        return null;
    }

    /**
     * 根据夫下标获取当前具体商品的下标
     *
     * @param position 父下标
     * @return
     */
    public int getDishPositionByOnePosition(int position) {
        for (ProductListEntity menu : data) {
            if (position > 0 && position <= menu.getProductEntities().size()) {
                return position - 1;
            } else {
                position -= menu.getProductEntities().size() + 1;
            }
        }
        return 0;
    }

    public ProductListEntity getMenuOfMenuByPosition(int position) {
        for (ProductListEntity menu : data) {
            if (position == 0) return menu;
            if (position > 0 && position <= menu.getProductEntities().size()) {
                return menu;
            } else {
                position -= menu.getProductEntities().size() + 1;
            }
        }
        return null;
    }


    @Override
    public int getItemCount() {
        return mItemCount;
    }


    private class MenuViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout right_menu_layout;
        private TextView right_menu_title;

        public MenuViewHolder(View itemView) {
            super(itemView);
            right_menu_layout = (LinearLayout) itemView.findViewById(R.id.right_menu_item);
            right_menu_title = (TextView) itemView.findViewById(R.id.right_menu_tv);
        }
    }

    private class DishViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_item_life_product_name;
        private TextView tv_item_life_product_monty;
        private TextView tv_item_life_product_money;
        private TextView tv_item_life_product_msg;
        private LinearLayout right_dish_layout;
        private Button btn_item_life_product_pay;

        public DishViewHolder(View itemView) {
            super(itemView);
            tv_item_life_product_name = (TextView) itemView.findViewById(R.id.tv_item_life_product_name);
            tv_item_life_product_monty = (TextView) itemView.findViewById(R.id.tv_item_life_product_monty);
            tv_item_life_product_money = (TextView) itemView.findViewById(R.id.tv_item_life_product_money);
            tv_item_life_product_msg = (TextView) itemView.findViewById(R.id.tv_item_life_product_msg);
            right_dish_layout = (LinearLayout) itemView.findViewById(R.id.right_dish_item);
            btn_item_life_product_pay = (Button) itemView.findViewById(R.id.btn_item_life_product_pay);
        }

    }


    //要定义一个按钮监听抽象接口和时间
    public interface OnItemChrldListner {
        void onCall(View view, ProductListEntity.ProductEntity entity, String type);
    }

    //定义一个监听 再activity中调用
    private RightServiceNoCartAdapter.OnItemChrldListner onItemChrldListner;

    public void setOnItemChrldListner(RightServiceNoCartAdapter.OnItemChrldListner onItemChrldListner) {
        this.onItemChrldListner = onItemChrldListner;
    }
}
