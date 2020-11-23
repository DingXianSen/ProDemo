package com.huidaxuan.ic2cloud.meituanshoppingcart.scrollall.wiget.scrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

/**
 * Author:dingchao
 * TIme:2018/5/22：16:02
 * Description:MyScrollView
 * 描述：
 */

/*ScrollView低版本适配*/
public class MyScrollView extends ScrollView {
    private OnScrollChanged mOnScrollChanged;

    public MyScrollView(Context context) {
        this(context, null);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChanged != null)
            mOnScrollChanged.onScroll(l, t, oldl, oldt);

        View view = this.getChildAt(0);
        if (this.getHeight() + this.getScrollY() == view.getHeight()) {
            _calCount++;
            if (_calCount == 1) {
                if (_listener != null) {
                    _listener.srollToBottom();

                }

            }
        } else {
            _calCount = 0;

        }
    }

    public void setOnScrollChanged(OnScrollChanged onScrollChanged) {
        this.mOnScrollChanged = onScrollChanged;
    }

    public interface OnScrollChanged {
        void onScroll(int l, int t, int oldl, int oldt);
    }


    /**
     * 接口回调
     */
    private ScrollViewListener scrollViewListener = null;

    public interface ScrollViewListener {
        void onScrollChanged(MyScrollView scrollView, int x, int y,
                             int oldx, int oldy);

    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    /**
     * 重写的onScrollChanged方法监听坐标
     * 这个监听在源码中没有写成回调的样子，
     * 只是写成了方法的样子，由于修饰这个方法的修饰符是protected，
     * （protected只能在本类，子类，同一包中调用），
     * 所以拿到ScrollView对象后在无法activity中调用此方法，
     * 所以只能重写后，子类中自动调用，
     * 所以要想在activity调用，
     * 就要写回调，
     * 上面就是我写的回调
     * 在Android源码中这种写法很多，在很多控件中都有
     */
//    @Override
//    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
//        super.onScrollChanged(x, y, oldx, oldy);
//        if (scrollViewListener != null) {
//            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
//        }
//    }


    //滑动到底部监听添加
    private OnScrollBottomListener _listener;
    private int _calCount;

    public interface OnScrollBottomListener {
        void srollToBottom();
    }

    public void registerOnScrollViewScrollToBottom(OnScrollBottomListener l) {
        _listener = l;
    }

    public void unRegisterOnScrollViewScrollToBottom() {
        _listener = null;
    }

}
