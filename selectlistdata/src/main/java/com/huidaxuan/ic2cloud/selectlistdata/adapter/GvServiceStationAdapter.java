package com.huidaxuan.ic2cloud.selectlistdata.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huidaxuan.ic2cloud.selectlistdata.R;
import com.huidaxuan.ic2cloud.selectlistdata.entity.ServiceStationEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: BalanceBankCardAdapter
 * @Description: GridView选择时间工位适配器
 * @Author: dingchao
 * @Date: 2018/09/27 16:26
 */
public class GvServiceStationAdapter extends BaseAdapter {
    // 填充数据的list
    private List<ServiceStationEntity> list;
    // 上下文
    private Context context;
    // 用来导入布局
    private LayoutInflater inflater = null;
    // 用来控制CheckBox的选中状况
    private static HashMap<Integer, Boolean> isSelected;


    // 构造器
    public GvServiceStationAdapter(List<ServiceStationEntity> list, Context context) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        isSelected = new HashMap<Integer, Boolean>();
        initDate();
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
//        return Long.parseLong(list.get(position).getFid());
        return Long.parseLong("0");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            // 获得ViewHolder对象
            holder = new ViewHolder();
            // 导入布局并赋值给convertview
            convertView = inflater.inflate(R.layout.item_service_station, null);
            holder.rl_item_service_station_all = convertView.findViewById(R.id.rl_item_service_station_all);
            holder.tv_item_service_station_time = convertView.findViewById(R.id.tv_item_service_station_time);
            holder.v_item_service_station_line = convertView.findViewById(R.id.v_item_service_station_line);
            holder.tv_item_service_station_number = convertView.findViewById(R.id.tv_item_service_station_number);
            holder.iv_item_service_station_sel = convertView.findViewById(R.id.iv_item_service_station_sel);
            // 为view设置标签
            convertView.setTag(holder);
        } else {
            // 取出holder
            holder = (ViewHolder) convertView.getTag();
        }

        // 设置list中TextView的显示
        holder.tv_item_service_station_time.setText(list.get(position).getTime());
        holder.tv_item_service_station_number.setText(list.get(position).getStationNowNum() + "/" + list.get(position).getStationAllNum());
        fillValue(position, holder, list.get(position).isSelFlag());
        return convertView;
    }

    private void fillValue(int position, ViewHolder viewHolder, boolean selFlag) {
        if (selFlag) {//没有过期，可以选择
            //选中的改变字体颜色
            if (getIsSelected().get(position)) {
                selUpdateColor(viewHolder, 2);
            } else {
                selUpdateColor(viewHolder, 3);
            }
        } else {//过期失效，不可以选择
            selUpdateColor(viewHolder, 1);
        }
    }

    private void selUpdateColor(ViewHolder holder, int num) {
        if (num == 1) {//灰色
            holder.rl_item_service_station_all.setBackground(context.getResources().getDrawable(R.drawable.shape_service_station_bg_gray));
            holder.tv_item_service_station_number.setTextColor(context.getResources().getColor(R.color.color_999999));
            holder.tv_item_service_station_time.setTextColor(context.getResources().getColor(R.color.color_999999));
            holder.v_item_service_station_line.setBackgroundColor(context.getResources().getColor(R.color.color_999999));
            holder.iv_item_service_station_sel.setVisibility(View.INVISIBLE);
        } else if (num == 2) {//选中
            holder.rl_item_service_station_all.setBackground(context.getResources().getDrawable(R.drawable.shape_service_station_bg_red));
            holder.tv_item_service_station_number.setTextColor(context.getResources().getColor(R.color.color_fb233d));
            holder.tv_item_service_station_time.setTextColor(context.getResources().getColor(R.color.color_fb233d));
            holder.v_item_service_station_line.setBackgroundColor(context.getResources().getColor(R.color.color_fb233d));
            holder.iv_item_service_station_sel.setVisibility(View.VISIBLE);
        } else {//黑色
            holder.rl_item_service_station_all.setBackground(context.getResources().getDrawable(R.drawable.shape_service_station_bg_black));
            holder.tv_item_service_station_number.setTextColor(context.getResources().getColor(R.color.color_333333));
            holder.tv_item_service_station_time.setTextColor(context.getResources().getColor(R.color.color_333333));
            holder.v_item_service_station_line.setBackgroundColor(context.getResources().getColor(R.color.color_333333));
            holder.iv_item_service_station_sel.setVisibility(View.INVISIBLE);
        }
    }


    // 初始化isSelected的数据
    public void initDate() {
        for (int i = 0; i < list.size(); i++) {
            getIsSelected().put(i, false);
        }
    }

    public static void cancelSelect() {
        //遍历map，全部设置为false
        for (Map.Entry<Integer, Boolean> mm : getIsSelected().entrySet()) {
            getIsSelected().put(mm.getKey(), false);
        }
    }

    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        GvServiceStationAdapter.isSelected = isSelected;
    }

    public class ViewHolder {
        public RelativeLayout rl_item_service_station_all;
        private TextView tv_item_service_station_time;
        private View v_item_service_station_line;
        private TextView tv_item_service_station_number;
        private ImageView iv_item_service_station_sel;

    }

}
