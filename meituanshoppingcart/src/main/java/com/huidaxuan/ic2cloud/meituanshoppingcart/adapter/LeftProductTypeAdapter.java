package com.huidaxuan.ic2cloud.meituanshoppingcart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.huidaxuan.ic2cloud.meituanshoppingcart.R;
import com.huidaxuan.ic2cloud.meituanshoppingcart.enitty.ProductListEntity;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class LeftProductTypeAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private ArrayList<ProductListEntity> mMenuList;
    private int mSelectedNum;
    private String mUpdateParentId;
    private int mUpdateParentCount;
    private List<onItemSelectedListener> mSelectedListenerList;
    private boolean clearCount = false;

    public interface onItemSelectedListener {
        public void onLeftItemSelected(int postion, ProductListEntity menu);
    }

    public void addItemSelectedListener(onItemSelectedListener listener) {
        if (mSelectedListenerList != null)
            mSelectedListenerList.add(listener);
    }

    public void removeItemSelectedListener(onItemSelectedListener listener) {
        if (mSelectedListenerList != null && !mSelectedListenerList.isEmpty())
            mSelectedListenerList.remove(listener);
    }

    public LeftProductTypeAdapter(Context mContext, ArrayList<ProductListEntity> mMenuList) {
        this.mContext = mContext;
        this.mMenuList = mMenuList;
        this.mSelectedNum = -1;
        this.mUpdateParentId = "-1";
        this.mUpdateParentCount = 0;
        this.mSelectedListenerList = new ArrayList<>();
        if (mMenuList.size() > 0)
            mSelectedNum = 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.left_menu_item, parent, false);
        LeftMenuViewHolder viewHolder = new LeftMenuViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ProductListEntity dishMenu = mMenuList.get(position);
        LeftMenuViewHolder viewHolder = (LeftMenuViewHolder) holder;
        viewHolder.menuName.setText(dishMenu.getTypeName());
        if (mSelectedNum == position) {
            viewHolder.menuLayout.setSelected(true);
            viewHolder.v_left_menu_item_line.setVisibility(View.VISIBLE);
        } else {
            viewHolder.menuLayout.setSelected(false);
            viewHolder.v_left_menu_item_line.setVisibility(View.INVISIBLE);
        }

        if (dishMenu.getTypeId().equals(mUpdateParentId)) {//选中的ID
            //更改数据
            dishMenu.setTypeCount(mUpdateParentCount);
        }
        if (clearCount) {//隐藏所有数据，设置count都为0
            viewHolder.tv_left_menu_count.setVisibility(View.GONE);
            dishMenu.setTypeCount(0);
        } else {
            viewHolder.tv_left_menu_count.setVisibility(View.VISIBLE);
            viewHolder.tv_left_menu_count.setText(dishMenu.getTypeCount() + "");
            dishMenu.setTypeCount(dishMenu.getTypeCount());
        }
        if (dishMenu.getTypeCount() > 0) {//展示
            viewHolder.tv_left_menu_count.setVisibility(View.VISIBLE);
            viewHolder.tv_left_menu_count.setText(dishMenu.getTypeCount() + "");
        } else {//隐藏
            viewHolder.tv_left_menu_count.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return mMenuList.size();
    }

    /**
     * 选中左侧区域
     *
     * @param selectedNum
     */
    public void setSelectedNum(int selectedNum) {
        if (selectedNum < getItemCount() && selectedNum >= 0) {
            this.mSelectedNum = selectedNum;
            notifyDataSetChanged();
        }
    }

    /**
     * 更新左侧角标，需要知道那个对象
     *
     * @param
     */
    public void setUpdateMenuCount(String parentId, int mUpdateParentCount) {
        //需要实体数据保存更新
        this.mUpdateParentId = parentId;
        this.mUpdateParentCount = mUpdateParentCount;
        notifyDataSetChanged();
        this.clearCount = false;

    }


    /**
     * 清空购物车所有数据
     */
    public void setClearCount() {
        this.clearCount = true;
        notifyDataSetChanged();
    }


    public int getSelectedNum() {
        return mSelectedNum;
    }

    private class LeftMenuViewHolder extends RecyclerView.ViewHolder {

        TextView menuName;
        TextView tv_left_menu_count;
        LinearLayout menuLayout;
        View v_left_menu_item_line;

        public LeftMenuViewHolder(final View itemView) {
            super(itemView);
            menuName = (TextView) itemView.findViewById(R.id.left_menu_textview);
            tv_left_menu_count = (TextView) itemView.findViewById(R.id.tv_left_menu_count);
            menuLayout = (LinearLayout) itemView.findViewById(R.id.left_menu_item);
            v_left_menu_item_line = itemView.findViewById(R.id.v_left_menu_item_line);
            menuLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    onItemChrldListner.onCall(view, getAdapterPosition());
                    int clickPosition = getAdapterPosition();
                    setSelectedNum(clickPosition);
                    notifyItemSelected(clickPosition);
                }
            });
        }
    }

    private void notifyItemSelected(int position) {
        if (mSelectedListenerList != null && !mSelectedListenerList.isEmpty()) {
            for (onItemSelectedListener listener : mSelectedListenerList) {
                listener.onLeftItemSelected(position, mMenuList.get(position));
            }
        }
    }


    //要定义一个按钮监听抽象接口和时间
    public interface OnItemChrldListner {
        void onCall(View view, int sectionIndex);
    }

    //定义一个监听 再activity中调用
    private LeftProductTypeAdapter.OnItemChrldListner onItemChrldListner;

    public void setOnItemChrldListner(LeftProductTypeAdapter.OnItemChrldListner onItemChrldListner) {
        this.onItemChrldListner = onItemChrldListner;
    }
}
