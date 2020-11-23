package com.huidaxuan.ic2cloud.meituanshoppingcart.scrollall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.huidaxuan.ic2cloud.meituanshoppingcart.R;
import com.huidaxuan.ic2cloud.meituanshoppingcart.enitty.ProductListEntity;
import com.huidaxuan.ic2cloud.meituanshoppingcart.enitty.ShopCart;
import com.huidaxuan.ic2cloud.meituanshoppingcart.imp.ShopCartImp;

import java.util.ArrayList;

/**
 * 车主门店详情装饰用品Fragment
 */
public class RightStoreOwnerZsypProductAdapter extends RecyclerView.Adapter {
    private final int MENU_TYPE = 0;
    private final int DISH_TYPE = 1;
    private final int HEAD_TYPE = 2;

    private Context mContext;
    private ArrayList<ProductListEntity> data;

    private int mItemCount;


    private ShopCart shopCart;
    private ShopCartImp shopCartImp;


    public RightStoreOwnerZsypProductAdapter(Context mContext, ArrayList<ProductListEntity> mMenuList, ShopCart shopCart) {
        this.mContext = mContext;
        this.data = mMenuList;
        this.mItemCount = mMenuList.size();
        this.shopCart = shopCart;
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_owner_cart_zsyp, parent, false);
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
                //根据当前的下标获取父ID
                final int posss = getDishPositionByOnePosition(position);
                dishholder.tv_item_life_product_name.setText(dish.getProductName());
                dishholder.tv_item_life_product_monty.setText("月售 " + dish.getProductMonth());
                dishholder.tv_item_life_product_money.setText("" + dish.getProductMoney());
                dishholder.right_dish_layout.setContentDescription(position + "");
                dishholder.tv_group_list_item_count_num.setText(dish.getProductCount() + "");


                //判断是不是最后一个商品，防止压住购物车，所以最后一个设置边距
                if (position + 1 == getItemCount()) {//最后一个
                    dishholder.v_item_line_41.setVisibility(View.VISIBLE);
                } else {
                    dishholder.v_item_line_41.setVisibility(View.GONE);
                }
                int count = 0;
                if (shopCart.getShoppingSingle().containsKey(dish)) {
                    count = shopCart.getShoppingSingle().get(dish);
                }

                if (count <= 0) {
                    dishholder.tv_group_list_item_count_num.setVisibility(View.INVISIBLE);
                    dishholder.iv_group_list_item_count_reduce.setVisibility(View.INVISIBLE);
                } else {
                    dishholder.tv_group_list_item_count_num.setVisibility(View.VISIBLE);
                    dishholder.iv_group_list_item_count_reduce.setVisibility(View.VISIBLE);
                    dishholder.tv_group_list_item_count_num.setText(count + "");
                }

                //加减点击时间
                dishholder.iv_group_list_item_count_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (shopCart.addShoppingSingle(dish)) {
//                            notifyItemChanged(position);
                            //当前数字变化刷新
                            notifyDataSetChanged();
                            if (shopCartImp != null) {
                                shopCartImp.add(view, position, dish);

                            }
                        }
                    }
                });

                dishholder.iv_group_list_item_count_reduce.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (shopCart.subShoppingSingle(dish)) {
//                            notifyItemChanged(position);
                            //当前数字变化刷新
                            notifyDataSetChanged();
                            if (shopCartImp != null)
                                shopCartImp.remove(view, position, dish);

                        }
                    }
                });
            }
        }
    }

    public ShopCartImp getShopCartImp() {
        return shopCartImp;
    }

    public void setShopCartImp(ShopCartImp shopCartImp) {
        this.shopCartImp = shopCartImp;
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
        private LinearLayout right_dish_layout;

        private ImageView iv_group_list_item_count_reduce;
        private TextView tv_group_list_item_count_num;
        private ImageView iv_group_list_item_count_add;
        private View v_item_line_41;


        public DishViewHolder(View itemView) {
            super(itemView);
            tv_item_life_product_name = (TextView) itemView.findViewById(R.id.tv_item_life_product_name);
            tv_item_life_product_monty = (TextView) itemView.findViewById(R.id.tv_item_life_product_monty);
            tv_item_life_product_money = (TextView) itemView.findViewById(R.id.tv_item_life_product_money);

            right_dish_layout = (LinearLayout) itemView.findViewById(R.id.right_dish_item);

            iv_group_list_item_count_reduce = (ImageView) itemView.findViewById(R.id.iv_group_list_item_count_reduce);
            tv_group_list_item_count_num = (TextView) itemView.findViewById(R.id.tv_group_list_item_count_num);
            iv_group_list_item_count_add = (ImageView) itemView.findViewById(R.id.iv_group_list_item_count_add);
            v_item_line_41 = itemView.findViewById(R.id.v_item_line_41);
        }

    }
}
