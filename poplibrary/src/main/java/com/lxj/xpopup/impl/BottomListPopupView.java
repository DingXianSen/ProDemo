package com.lxj.xpopup.impl;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lxj.easyadapter.EasyAdapter;
import com.lxj.easyadapter.MultiItemTypeAdapter;
import com.lxj.easyadapter.ViewHolder;
import com.lxj.xpopup.R;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.lxj.xpopup.widget.CheckView;
import com.lxj.xpopup.widget.VerticalRecyclerView;

import java.util.Arrays;

/**
 * Description: 底部的列表对话框
 * Create by dance, at 2018/12/16
 */
public class BottomListPopupView extends BottomPopupView {
    VerticalRecyclerView recyclerView;
    TextView tv_title;
    protected int bindLayoutId;
    protected int bindItemLayoutId;

    /**
     *
     * @param context
     * @param bindLayoutId layoutId 要求layoutId中必须有一个id为recyclerView的RecyclerView，如果你需要显示标题，则必须有一个id为tv_title的TextView
     * @param bindItemLayoutId itemLayoutId 条目的布局id，要求布局中必须有id为iv_image的ImageView，和id为tv_text的TextView
     */
    public BottomListPopupView(@NonNull Context context, int bindLayoutId, int bindItemLayoutId ) {
        super(context);
        this.bindLayoutId = bindLayoutId;
        this.bindItemLayoutId = bindItemLayoutId;
        addInnerContent();
    }

    @Override
    protected int getImplLayoutId() {
        return bindLayoutId == 0 ? R.layout._xpopup_bottom_impl_list : bindLayoutId;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setupDivider(popupInfo.isDarkTheme);
        tv_title = findViewById(R.id.tv_title);

        if(tv_title!=null){
            if (TextUtils.isEmpty(title)) {
                tv_title.setVisibility(GONE);
                if(findViewById(R.id.xpopup_divider)!=null)findViewById(R.id.xpopup_divider).setVisibility(GONE);
            } else {
                tv_title.setText(title);
            }
        }

        final EasyAdapter<String> adapter = new EasyAdapter<String>(Arrays.asList(data), bindItemLayoutId == 0 ? R.layout._xpopup_adapter_text_match : bindItemLayoutId) {
            @Override
            protected void bind(@NonNull ViewHolder holder, @NonNull String s, int position) {
                holder.setText(R.id.tv_text, s);
                if (iconIds != null && iconIds.length > position) {
                    holder.getView(R.id.iv_image).setVisibility(VISIBLE);
                    holder.getView(R.id.iv_image).setBackgroundResource(iconIds[position]);
                } else {
                    holder.getView(R.id.iv_image).setVisibility(GONE);
                }

                // 对勾View
                if (checkedPosition != -1) {
                    if(holder.getView(R.id.check_view)!=null){
                        holder.getView(R.id.check_view).setVisibility(position == checkedPosition ? VISIBLE : GONE);
                        holder.<CheckView>getView(R.id.check_view).setColor(XPopup.getPrimaryColor());
                    }
                    holder.<TextView>getView(R.id.tv_text).setTextColor(position == checkedPosition ?
                            XPopup.getPrimaryColor() : getResources().getColor(R.color._xpopup_title_color));
                }else {
                    if(holder.getView(R.id.check_view)!=null)holder.getView(R.id.check_view).setVisibility(GONE);
                    //如果没有选择，则文字居中
                    holder.<TextView>getView(R.id.tv_text).setGravity(Gravity.CENTER);
                }
                if(bindItemLayoutId==0 && popupInfo.isDarkTheme){
                    holder.<TextView>getView(R.id.tv_text).setTextColor(getResources().getColor(R.color._xpopup_white_color));
                }
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.SimpleOnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (selectListener != null) {
                    selectListener.onSelect(position, adapter.getData().get(position));
                }
                if (checkedPosition != -1) {
                    checkedPosition = position;
                    adapter.notifyDataSetChanged();
                }
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (popupInfo.autoDismiss) dismiss();
                    }
                }, 100);
            }
        });
        recyclerView.setAdapter(adapter);
        if (bindLayoutId==0 && popupInfo.isDarkTheme){
            applyDarkTheme();
        }
    }
    @Override
    protected void applyDarkTheme() {
        super.applyDarkTheme();
        tv_title.setTextColor(getResources().getColor(R.color._xpopup_white_color));
        ((ViewGroup)tv_title.getParent()).setBackgroundResource(R.drawable._xpopup_round3_top_dark_bg);
        findViewById(R.id.xpopup_divider).setBackgroundColor(
                getResources().getColor(R.color._xpopup_list_dark_divider)
        );
    }

    CharSequence title;
    String[] data;
    int[] iconIds;

    public BottomListPopupView setStringData(CharSequence title, String[] data, int[] iconIds) {
        this.title = title;
        this.data = data;
        this.iconIds = iconIds;
        return this;
    }

    private OnSelectListener selectListener;

    public BottomListPopupView setOnSelectListener(OnSelectListener selectListener) {
        this.selectListener = selectListener;
        return this;
    }

    int checkedPosition = -1;

    /**
     * 设置默认选中的位置
     *
     * @param position
     * @return
     */
    public BottomListPopupView setCheckedPosition(int position) {
        this.checkedPosition = position;
        return this;
    }


}
