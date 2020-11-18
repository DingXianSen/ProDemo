package com.huidaxuan.ic2cloud.meituanshoppingcart.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.huidaxuan.ic2cloud.meituanshoppingcart.R;
import com.huidaxuan.ic2cloud.meituanshoppingcart.enitty.ProductListEntity;

import org.zakariya.stickyheaders.SectioningAdapter;

import java.util.ArrayList;

/**
 *
 */
public class RightProductAdapter extends SectioningAdapter {

    private Context mContext;
    private ArrayList<ProductListEntity> data;
    private int mSelectedNum;

    public RightProductAdapter(Context mContext, ArrayList<ProductListEntity> mMenuList) {
        this.mContext = mContext;
        this.data = mMenuList;
        this.mSelectedNum = -1;
    }

    public int getSelectedNum() {
        return mSelectedNum;
    }

    @Override
    public int getNumberOfSections() {
        return data.size();
    }

    @Override
    public int getNumberOfItemsInSection(int sectionIndex) {
        return data.get(sectionIndex).getProductEntities().size();
    }

    @Override
    public boolean doesSectionHaveHeader(int sectionIndex) {
        return !TextUtils.isEmpty(data.get(sectionIndex).getTypeName());
    }

    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int headerUserType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.include_right_item_title, parent, false);
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(SectioningAdapter.HeaderViewHolder viewHolder, int sectionIndex, int headerUserType) {
        //绑定头布局的值
        HeaderViewHolder hvh = (HeaderViewHolder) viewHolder;
        hvh.right_menu_title.setText(data.get(sectionIndex).getTypeName());
        hvh.right_menu_layout.setContentDescription(sectionIndex + "");
    }

    @Override
    public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int itemUserType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.item_life_product, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindItemViewHolder(SectioningAdapter.ItemViewHolder viewHolder, int sectionIndex, int itemIndex, int itemUserType) {
        //根据下标取商品
//        final ProductListEntity.ProductEntity dish = getDishByPosition(sectionIndex);
        final ProductListEntity.ProductEntity dish = data.get(sectionIndex).getProductEntities().get(itemIndex);
        //根据下标获取当前商品的下标
//        int productPosition = getDishPositionByOnePosition(position);
        int productPosition = itemIndex;
        ItemViewHolder dishholder = (ItemViewHolder) viewHolder;
        dishholder.tv_item_life_product_name.setText(dish.getProductName());
        dishholder.tv_item_life_product_monty.setText("月售 " + dish.getProductMonth());
        dishholder.tv_item_life_product_money.setText("" + dish.getProductMoney());

//        dishholder.right_dish_layout.setContentDescription(sectionIndex + "");
        dishholder.right_dish_layout.setContentDescription(itemIndex + "");


        dishholder.tv_group_list_item_count_num.setText(dish.getProductCount() + "");
        int count = dish.getProductCount();

        if (count <= 0) {
            dishholder.tv_group_list_item_count_num.setVisibility(View.INVISIBLE);
            dishholder.tv_group_list_item_count_reduce.setVisibility(View.INVISIBLE);
        } else {
            dishholder.tv_group_list_item_count_num.setVisibility(View.VISIBLE);
            dishholder.tv_group_list_item_count_reduce.setVisibility(View.VISIBLE);
            dishholder.tv_group_list_item_count_num.setText(count + "");
        }


        dishholder.tv_group_list_item_count_reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RightProductAdapter.this.OnUpdateNum(view, sectionIndex, productPosition, dish, false);
            }
        });
    }


    /**
     * 加减操作，这里要两个下标是需要组装成Map的key-》父-子（例：1-1）,用来区分等
     *
     * @param view
     * @param typePosition    总列表所在下标（当前数据所在（所有分类2级的和）的真实下标）
     * @param productPosition 当前分类下的下标
     * @param entity          子项实体
     * @param flag            flag true:+ false:-
     */
    private void OnUpdateNum(View view, int typePosition, int productPosition, ProductListEntity.ProductEntity entity, boolean flag) {

        Log.e("OnUpdateNum", "------------typePosition:" + typePosition + "-----productPosition:" + productPosition);
        int initCount = entity.getProductCount();
        if (flag) {//加操作
            initCount += 1;
            onItemChrldListner.onCall(view, typePosition, productPosition, "add", entity);
        } else {//减操作，减需要注意到0怎样
            if (initCount - 1 > 0) {
                initCount -= 1;
                onItemChrldListner.onCall(view, typePosition, productPosition, "reduce", entity);
            } else {
                //到0了，不能在减，如果当前为0了，在activity中的购物车中删除对应的数据
                initCount = 0;
                onItemChrldListner.onCall(view, typePosition, productPosition, "zero", entity);
            }
        }
        //对应的实体替换
        entity.setProductCount(initCount);
        //当前数字变化刷新
        notifyDataSetChanged();
    }

    //要定义一个按钮监听抽象接口和时间
    public interface OnItemChrldListner {
        void onCall(View view, int sectionIndex, int itemIndex, String type, ProductListEntity.ProductEntity entity);
    }

    //定义一个监听 再activity中调用
    private RightProductAdapter.OnItemChrldListner onItemChrldListner;

    public void setOnItemChrldListner(RightProductAdapter.OnItemChrldListner onItemChrldListner) {
        this.onItemChrldListner = onItemChrldListner;
    }


    /**
     * 刷新数据，展示对应的父下标的数据
     *
     * @param selectedNum
     */
    public void setSelectedNum(int selectedNum) {
        if (selectedNum < getItemCount() && selectedNum >= 0) {
            this.mSelectedNum = selectedNum;
            notifyDataSetChanged();
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


    private class HeaderViewHolder extends SectioningAdapter.HeaderViewHolder {
        private LinearLayout right_menu_layout;
        private TextView right_menu_title;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            right_menu_layout = (LinearLayout) itemView.findViewById(R.id.right_menu_item);
            right_menu_title = (TextView) itemView.findViewById(R.id.right_menu_tv);
        }
    }

    private class ItemViewHolder extends SectioningAdapter.ItemViewHolder implements View.OnClickListener {
        private TextView tv_item_life_product_name;
        private TextView tv_item_life_product_monty;
        private TextView tv_item_life_product_money;
        private LinearLayout right_dish_layout;

        private TextView tv_group_list_item_count_reduce;
        private TextView tv_group_list_item_count_num;
        private TextView tv_group_list_item_count_add;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tv_item_life_product_name = (TextView) itemView.findViewById(R.id.tv_item_life_product_name);
            tv_item_life_product_monty = (TextView) itemView.findViewById(R.id.tv_item_life_product_monty);
            tv_item_life_product_money = (TextView) itemView.findViewById(R.id.tv_item_life_product_money);

            right_dish_layout = (LinearLayout) itemView.findViewById(R.id.right_dish_item);

            tv_group_list_item_count_reduce = (TextView) itemView.findViewById(R.id.tv_group_list_item_count_reduce);
            tv_group_list_item_count_num = (TextView) itemView.findViewById(R.id.tv_group_list_item_count_num);
            tv_group_list_item_count_add = (TextView) itemView.findViewById(R.id.tv_group_list_item_count_add);

            tv_group_list_item_count_reduce.setOnClickListener(this);
            tv_group_list_item_count_add.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            int section = RightProductAdapter.this.getSectionForAdapterPosition(adapterPosition);
            int item = RightProductAdapter.this.getPositionOfItemInSection(section, adapterPosition);
            switch (view.getId()) {
                case R.id.tv_group_list_item_count_add:
                    Log.e("OnUpdateNum", "----------position1:" + section + "-------position2:" + item);
//                    RightProductAdapter.this.OnUpdateNum(view, section, item, dish, true);
                    break;
                default:
                    break;
            }
        }
    }
}
