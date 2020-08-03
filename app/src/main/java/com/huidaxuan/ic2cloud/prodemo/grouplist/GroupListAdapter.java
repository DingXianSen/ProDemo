package com.huidaxuan.ic2cloud.prodemo.grouplist;

import android.content.Context;
import android.net.wifi.aware.PublishConfig;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huidaxuan.ic2cloud.prodemo.R;

import org.zakariya.stickyheaders.SectioningAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @packageName:com.huidaxuan.ic2cloud.prodemo
 * @className: GroupListAdapter
 * @description:分组列表适配器
 * @author: dingchao
 * @time: 2020-08-03 09:29
 */
public class GroupListAdapter extends SectioningAdapter {
    private Context context;
    private List<GroupListEntity> data = new ArrayList<>();
//    private int sectionClickIndex;
//    private int itemClickIndex;

    public GroupListAdapter(Context context, List<GroupListEntity> data) {
        this.context = context;
        this.data.addAll(data);
    }


    @Override
    public int getNumberOfSections() {
        return data.size();
    }

    @Override
    public int getNumberOfItemsInSection(int sectionIndex) {
        return data.get(sectionIndex).getChildBeans().size();
    }

    @Override
    public boolean doesSectionHaveHeader(int sectionIndex) {
        return !TextUtils.isEmpty(data.get(sectionIndex).getGroupTitle());
    }


    //设置头布局
    public class HeaderViewHolder extends SectioningAdapter.HeaderViewHolder {
        TextView tv_group_list_header_title;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            tv_group_list_header_title = itemView.findViewById(R.id.tv_group_list_header_title);
        }
    }

    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int headerUserType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.group_list_header, parent, false);
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(SectioningAdapter.HeaderViewHolder viewHolder, int sectionIndex, int headerUserType) {
        //绑定头布局的值
        HeaderViewHolder hvh = (HeaderViewHolder) viewHolder;
        hvh.tv_group_list_header_title.setText(data.get(sectionIndex).getGroupTitle());
    }

    //设置单项内容
    public class ItemViewHolder extends SectioningAdapter.ItemViewHolder implements View.OnClickListener {
        ImageView iv_group_list_item;
        TextView tv_group_list_item_title;
        TextView tv_group_list_item_msg;
        TextView tv_group_list_item_money;
        TextView tv_group_list_item_count_reduce;
        TextView tv_group_list_item_count_num;
        TextView tv_group_list_item_count_add;

        public ItemViewHolder(View itemView) {
            super(itemView);
            iv_group_list_item = itemView.findViewById(R.id.iv_group_list_item);
            tv_group_list_item_title = itemView.findViewById(R.id.tv_group_list_item_title);
            tv_group_list_item_msg = itemView.findViewById(R.id.tv_group_list_item_msg);
            tv_group_list_item_money = itemView.findViewById(R.id.tv_group_list_item_money);
            tv_group_list_item_count_reduce = itemView.findViewById(R.id.tv_group_list_item_count_reduce);
            tv_group_list_item_count_num = itemView.findViewById(R.id.tv_group_list_item_count_num);
            tv_group_list_item_count_add = itemView.findViewById(R.id.tv_group_list_item_count_add);

            tv_group_list_item_count_reduce.setOnClickListener(this);
            tv_group_list_item_count_add.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            int section = GroupListAdapter.this.getSectionForAdapterPosition(adapterPosition);
            int item = GroupListAdapter.this.getPositionOfItemInSection(section, adapterPosition);
            switch (v.getId()) {
                case R.id.tv_group_list_item_count_reduce:
                    GroupListAdapter.this.onUpdateNum(v,section, item, false);
                    break;
                case R.id.tv_group_list_item_count_add:
                    GroupListAdapter.this.onUpdateNum(v,section, item, true);
                    Log.e("groupListEntities", "adapter-add-sectionClickIndex-->" + section + "---itemClickIndex-->" + item);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public SectioningAdapter.ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int itemUserType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.group_list_item, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindItemViewHolder(SectioningAdapter.ItemViewHolder viewHolder, int sectionIndex, int itemIndex, int itemUserType) {
        //绑定单项布局的值
        List<GroupListEntity.childBean> childBeans = data.get(sectionIndex).getChildBeans();
        ItemViewHolder ivh = (ItemViewHolder) viewHolder;
        ivh.tv_group_list_item_title.setText(childBeans.get(itemIndex).getTitle());
        ivh.tv_group_list_item_msg.setText(childBeans.get(itemIndex).getMessage());
        ivh.tv_group_list_item_money.setText("¥" + childBeans.get(itemIndex).getMoney() + "");
        ivh.tv_group_list_item_count_num.setText(childBeans.get(itemIndex).getCount() + "");
    }

    //要定义一个按钮监听抽象接口和时间
    public interface OnItemChrldListner {
        void onCall(View view, int sectionIndex, int itemIndex, String type);
    }

    //定义一个监听 再activity中调用
    private GroupListAdapter.OnItemChrldListner onItemChrldListner;

    public void setOnItemChrldListner(GroupListAdapter.OnItemChrldListner onItemChrldListner) {
        this.onItemChrldListner = onItemChrldListner;
    }

    /**
     * 控制加减方法
     *
     * @param sectionIndex 那一组的下标
     * @param itemIndex    每组下子项的下标
     * @param flag         标识加还是减
     */
    void onUpdateNum(View view,int sectionIndex, int itemIndex, boolean flag) {
        int initCount = data.get(sectionIndex).getChildBeans().get(itemIndex).getCount();
        if (flag) {//加
            //设置num+1
//            initCount = initCount + 1;
            initCount += 1;
            onItemChrldListner.onCall(view,sectionIndex, itemIndex, "add");
        } else {//减
            //判断是不是0，如果是减到0了，就通知不能在减了
            if (initCount - 1 > 0) {
//                initCount = initCount - 1;
                initCount -= 1;
                onItemChrldListner.onCall(view,sectionIndex, itemIndex, "reduce");
            } else {
                //todo 不能在减了,如果当前为0了，则在activity中的购物车删除这条为0的数据
                initCount = 0;
                onItemChrldListner.onCall(view,sectionIndex, itemIndex, "zero");
            }
        }
        //修改实体
        data.get(sectionIndex).getChildBeans().get(itemIndex).setCount(initCount);
        //当前页的数字+1，刷新页面
        notifyDataSetChanged();
    }


}
