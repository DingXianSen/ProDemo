package com.huidaxuan.ic2cloud.selectlistdata.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.huidaxuan.ic2cloud.selectlistdata.R;
import com.huidaxuan.ic2cloud.selectlistdata.entity.DateEntity;

import java.util.HashMap;
import java.util.List;

/**
 * @packageName:com.huidaxuan.ic2cloud.app2b.adapter
 * @className: RvMenuAdapter
 * @description:日期适配器
 * @author: dingchao
 * @time: 2020-09-27 11:50
 */
public class RvDateListAdapter extends RecyclerView.Adapter<RvDateListAdapter.ViewHolder> {
    private Context context;
    private List<DateEntity> data;
    private int checkItemPosition = 0;



    public RvDateListAdapter(Context context, List<DateEntity> oftenMenuList) {
        this.context = context;
        this.data = oftenMenuList;
    }

    public void setCheckItem(int position) {
        checkItemPosition = position;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hor_date_list,
                parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tv_item_date_date.setText(data.get(position).getDateName());
        holder.tv_item_date_week.setText(data.get(position).getWeekName());
        //信息绑定
        fillValue(position, holder);
        holder.ll_date_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemChrldListner.onCall(v, position);
            }
        });
    }

    private void fillValue(int position, ViewHolder viewHolder) {
        if (checkItemPosition != -1) {
            if (checkItemPosition == position) {
                viewHolder.tv_item_date_week.setTextColor(context.getResources().getColor(R.color.color_ffffff));
                viewHolder.tv_item_date_week.setBackground(context.getResources().getDrawable(R.drawable.shape_date_week_bg_sel));
            } else {
                viewHolder.tv_item_date_week.setTextColor(context.getResources().getColor(R.color.color_333333));
                viewHolder.tv_item_date_week.setBackground(context.getResources().getDrawable(R.drawable.shape_date_week_bg_unsel));
            }
        } else {
            viewHolder.tv_item_date_week.setTextColor(context.getResources().getColor(R.color.color_333333));
            viewHolder.tv_item_date_week.setBackground(context.getResources().getDrawable(R.drawable.shape_date_week_bg_unsel));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_item_date_date;
        TextView tv_item_date_week;
        LinearLayout ll_date_all;

        public ViewHolder(View view) {
            super(view);
            tv_item_date_date = view.findViewById(R.id.tv_item_date_date);
            tv_item_date_week = view.findViewById(R.id.tv_item_date_week);
            ll_date_all = view.findViewById(R.id.ll_date_all);
        }
    }


    //要定义一个按钮监听抽象接口和时间
    public interface OnItemChrldListner {
        void onCall(View view, int position);
    }

    //定义一个监听 再activity中调用
    private RvDateListAdapter.OnItemChrldListner onItemChrldListner;

    public void setOnItemChrldListner(RvDateListAdapter.OnItemChrldListner onItemChrldListner) {
        this.onItemChrldListner = onItemChrldListner;
    }
}
