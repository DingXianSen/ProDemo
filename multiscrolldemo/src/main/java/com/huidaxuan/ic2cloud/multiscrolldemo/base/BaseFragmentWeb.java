package com.huidaxuan.ic2cloud.multiscrolldemo.base;

import androidx.fragment.app.Fragment;

/**
 * @packageName:com.huidaxuan.ic2cloud.app2b.base
 * @className: BaseFragmentWeb
 * @description:
 * @author: dingchao
 * @time: 2020-07-23 10:45
 */
public abstract class BaseFragmentWeb extends Fragment {
    /** Fragment当前状态是否可见 */
    protected boolean isVisible;

    //setUserVisibleHint  adapter中的每个fragment切换的时候都会被调用，如果是切换到当前页，那么isVisibleToUser==true，否则为false
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }


    /**
     * 可见
     */
    protected void onVisible() {
        lazyLoad();
    }


    /**
     * 不可见
     */
    protected void onInvisible() {


    }

    /**
     * 延迟加载
     * 子类必须重写此方法
     */
    protected abstract void lazyLoad();

}
