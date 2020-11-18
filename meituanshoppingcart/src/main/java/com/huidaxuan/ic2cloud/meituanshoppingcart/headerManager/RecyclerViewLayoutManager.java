package com.huidaxuan.ic2cloud.meituanshoppingcart.headerManager;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearSmoothScroller;

import org.zakariya.stickyheaders.StickyHeaderLayoutManager;

/**
 * @packageName:com.huidaxuan.ic2cloud.meituanshoppingcart.headerManager
 * @className: RecyclerViewLayoutManager
 * @description:
 * @author: dingchao
 * @time: 2020-11-18 18:27
 */
public class RecyclerViewLayoutManager extends LinearSmoothScroller {

    public RecyclerViewLayoutManager(Context context) {
        super(context);
    }

    /**
     *
     * @param viewStart RecyclerView的top位置
     * @param viewEnd RecyclerView的Bottom位置
     * @param boxStart item的top位置
     * @param boxEnd  item的bottom位置
     * @param snapPreference 滑动方向的识别
     * @return
     */
    @Override
    public int calculateDtToFit(int viewStart, int viewEnd, int boxStart, int boxEnd, int snapPreference) {
        System.out.println("!###boxStart:"+boxStart+"!###viewStart:"+viewStart);
        return boxStart-viewStart+100;//返回的就是我们item置顶需要的偏移量
    }

    /**
     * 此方法返回滚动每1px需要的时间,可以用来控制滚动速度
     * 即如果返回2ms，则每滚动1000px，需要2秒钟
     * @param displayMetrics
     * @return
     */
    @Override
    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
        return super.calculateSpeedPerPixel(displayMetrics);
    }
}
