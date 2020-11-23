package com.huidaxuan.ic2cloud.meituanshoppingcart.scrollall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.huidaxuan.ic2cloud.meituanshoppingcart.R;
import com.huidaxuan.ic2cloud.meituanshoppingcart.enitty.ProductListEntity;
import com.huidaxuan.ic2cloud.meituanshoppingcart.enitty.ShopZxxmCart;
import com.huidaxuan.ic2cloud.meituanshoppingcart.imp.ShopCartImp;

import java.util.ArrayList;

/**
 * 车主门店详情自选项目Fragment
 */
public class RightStoreOwnerZxxmProductAdapter extends RecyclerView.Adapter {
    private final int MENU_TYPE = 0;
    private final int DISH_TYPE = 1;
    private final int HEAD_TYPE = 2;

    private Context mContext;
    private ArrayList<ProductListEntity> data;

    private int mItemCount;


    private ShopZxxmCart shopCart;
    private ShopCartImp shopCartImp;


    public RightStoreOwnerZxxmProductAdapter(Context mContext, ArrayList<ProductListEntity> mMenuList, ShopZxxmCart shopCart) {
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_owner_cart_zxxm, parent, false);
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


                //判断是不是最后一个商品，防止压住购物车，所以最后一个设置边距
                if (position + 1 == getItemCount()) {//最后一个
                    dishholder.v_item_line_41.setVisibility(View.VISIBLE);
                } else {
                    dishholder.v_item_line_41.setVisibility(View.GONE);
                }
                int count = 0;
                String mapKey = dish.getParentId() + "-" + dish.getProductId();
                if (shopCart.getShoppingSingle().containsKey(mapKey)) {
                    count = shopCart.getShoppingSingle().get(mapKey).getProductCount();
                } else {
                    //这里要处理0是因为在购物车弹窗清空的时候，数据实体的count还是1选中状态不是0，但是已经执行了清空操作，所以这里处理0
                    dish.setProductCount(0);
                }

                if (count > 0) {//选中
                    dishholder.iv_item_life_product_pay_all.setImageResource(R.mipmap.iv_shopping_cart_item_sel);
                } else {//取消选中
                    dishholder.iv_item_life_product_pay_all.setImageResource(R.mipmap.iv_shopping_cart_item_unsel);
                }

                //选中点击
                dishholder.rl_item_life_product_pay_all.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //如果数量不为0，则设置可以添加，反之取消
                        //判断是不是清空过的,map为0
                        if (dish.getProductCount() != 0) {//取消选中，删除购物车数据
                            shopCart.addShoppingSingle(false, dish.getParentId() + "-" + dish.getProductId(), dish);
                            if (shopCartImp != null) {
                                shopCartImp.remove(view, position, dish);
                            }
                        } else {//小于等于0，点击的时候选中，添加数据
//                            dish.setProductCount(1);
                            shopCart.addShoppingSingle(true, dish.getParentId() + "-" + dish.getProductId(), dish);
                            if (shopCartImp != null) {
                                shopCartImp.add(view, position, dish);
                            }
                        }
                        notifyDataSetChanged();
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

        //        private ImageView iv_group_list_item_count_reduce;
//        private TextView tv_group_list_item_count_num;
//        private ImageView iv_group_list_item_count_add;
        private View v_item_line_41;
        private RelativeLayout rl_item_life_product_pay_all;
        private ImageView iv_item_life_product_pay_all;


        public DishViewHolder(View itemView) {
            super(itemView);
            tv_item_life_product_name = (TextView) itemView.findViewById(R.id.tv_item_life_product_name);
            tv_item_life_product_monty = (TextView) itemView.findViewById(R.id.tv_item_life_product_monty);
            tv_item_life_product_money = (TextView) itemView.findViewById(R.id.tv_item_life_product_money);

            right_dish_layout = (LinearLayout) itemView.findViewById(R.id.right_dish_item);

            rl_item_life_product_pay_all = itemView.findViewById(R.id.rl_item_life_product_pay_all);
            iv_item_life_product_pay_all = itemView.findViewById(R.id.iv_item_life_product_pay_all);
            v_item_line_41 = itemView.findViewById(R.id.v_item_line_41);
        }

    }
}
