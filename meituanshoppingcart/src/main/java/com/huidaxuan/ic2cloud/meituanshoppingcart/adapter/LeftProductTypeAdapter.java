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
    private List<onItemSelectedListener> mSelectedListenerList;

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
        } else {
            viewHolder.menuLayout.setSelected(false);
        }
    }

    @Override
    public int getItemCount() {
        return mMenuList.size();
    }

    public void setSelectedNum(int selectedNum) {
        if (selectedNum < getItemCount() && selectedNum >= 0) {
            this.mSelectedNum = selectedNum;
            notifyDataSetChanged();
        }
    }

    public int getSelectedNum() {
        return mSelectedNum;
    }

    private class LeftMenuViewHolder extends RecyclerView.ViewHolder {

        TextView menuName;
        LinearLayout menuLayout;

        public LeftMenuViewHolder(final View itemView) {
            super(itemView);
            menuName = (TextView) itemView.findViewById(R.id.left_menu_textview);
            menuLayout = (LinearLayout) itemView.findViewById(R.id.left_menu_item);
            menuLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemChrldListner.onCall(view, getAdapterPosition());
                    int clickPosition = getAdapterPosition();
                    setSelectedNum(clickPosition);
//                    notifyItemSelected(clickPosition);
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
