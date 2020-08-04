package com.huidaxuan.ic2cloud.prodemo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.huidaxuan.ic2cloud.prodemo.R;
import com.huidaxuan.ic2cloud.prodemo.entity.ServiceListEntity;

import java.util.Map;

/**
 * @packageName:com.huidaxuan.ic2cloud.app2b.adapter
 * @className: ShoppingCartAdapter
 * @description:购物车适配器Map数据源
 * @author: dingchao
 * @time: 2020-08-04 13:59
 */
public class ShoppingCartAdapter extends BaseAdapter {
    private Context context;
    private Map<String, ServiceListEntity.childBean> stringchildBeanMap;
    private String[] mKeys;

    public ShoppingCartAdapter(Context context, Map<String, ServiceListEntity.childBean> stringchildBeanMap) {
        this.context = context;
        this.stringchildBeanMap = stringchildBeanMap;
        mKeys = stringchildBeanMap.keySet().toArray(new String[stringchildBeanMap.size()]);
    }

    @Override
    public int getCount() {
        return stringchildBeanMap != null ? stringchildBeanMap.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return stringchildBeanMap.get(mKeys[position]);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ShoppingCartAdapter.ViewHolder holder = null;
        if (convertView == null) {
            holder = new ShoppingCartAdapter.ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_service_list, parent, false);
            holder.iv_group_list_item = convertView.findViewById(R.id.iv_group_list_item);
            holder.tv_group_list_item_title = convertView.findViewById(R.id.tv_group_list_item_title);
            holder.tv_group_list_item_msg = convertView.findViewById(R.id.tv_group_list_item_msg);
            holder.tv_group_list_item_money = convertView.findViewById(R.id.tv_group_list_item_money);
            holder.tv_group_list_item_count_reduce = convertView.findViewById(R.id.tv_group_list_item_count_reduce);
            holder.tv_group_list_item_count_num = convertView.findViewById(R.id.tv_group_list_item_count_num);
            holder.tv_group_list_item_count_add = convertView.findViewById(R.id.tv_group_list_item_count_add);

            holder.tv_group_list_item_count_reduce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShoppingCartAdapter.this.onUpdateNum(v, position, false);
                }
            });
            holder.tv_group_list_item_count_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShoppingCartAdapter.this.onUpdateNum(v, position, true);
                }
            });
            convertView.setTag(holder);
        } else {
            holder = (ShoppingCartAdapter.ViewHolder) convertView.getTag();
        }

        Log.e("mapKey", "mapkey------------->" + mKeys.length);
        //信息绑定
        holder.tv_group_list_item_title.setText(stringchildBeanMap.get(mKeys[position]).getTitle());
        holder.tv_group_list_item_msg.setText(stringchildBeanMap.get(mKeys[position]).getMessage());
        holder.tv_group_list_item_money.setText("¥" + stringchildBeanMap.get(mKeys[position]).getMoney());
        holder.tv_group_list_item_count_num.setText(stringchildBeanMap.get(mKeys[position]).getCount() + "");


        return convertView;

    }

    public void refreshDataSource(Map<String, ServiceListEntity.childBean> stringchildBeanMap) {
        this.stringchildBeanMap = stringchildBeanMap;
        mKeys = stringchildBeanMap.keySet().toArray(new String[stringchildBeanMap.size()]);
    }


    private class ViewHolder {
        ImageView iv_group_list_item;
        TextView tv_group_list_item_title;
        TextView tv_group_list_item_msg;
        TextView tv_group_list_item_money;
        TextView tv_group_list_item_count_reduce;
        TextView tv_group_list_item_count_num;
        TextView tv_group_list_item_count_add;
    }


    //要定义一个按钮监听抽象接口和时间
    public interface OnItemChrldListner {
        void onCall(View view, String position, String type);
    }

    //定义一个监听 再activity中调用
    private ShoppingCartAdapter.OnItemChrldListner onItemChrldListner;

    public void setOnItemChrldListner(ShoppingCartAdapter.OnItemChrldListner onItemChrldListner) {
        this.onItemChrldListner = onItemChrldListner;
    }


    /**
     * 控制加减方法
     *
     * @param position 每组下子项的下标
     * @param flag     标识加还是减
     */
    void onUpdateNum(View view, int position, boolean flag) {
        int initCount = stringchildBeanMap.get(mKeys[position]).getCount();
        if (flag) {//加
            //设置num+1
            onItemChrldListner.onCall(view, mKeys[position], "add");
        } else {//减
            //判断是不是0，如果是减到0了，就通知不能在减了
            if (initCount - 1 > 0) {
                onItemChrldListner.onCall(view, mKeys[position], "reduce");
            } else {
                //todo 不能在减了,如果当前为0了，则在activity中的购物车删除这条为0的数据
                initCount = 0;
//                stringchildBeanMap.remove(mKeys[position]);
                onItemChrldListner.onCall(view, mKeys[position], "zero");
            }
        }
    }

}
