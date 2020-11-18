package com.huidaxuan.ic2cloud.multiscrolldemo.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @ClassName: BaseFragment
 * @Description:Fragment基类
 * @Author: dingchao
 * @Date: 2020/6/1 14:15
 */
public abstract class BaseFragment extends Fragment {
    protected View view;
    protected LayoutInflater inflater;
    private Unbinder unbinder;
    protected Activity mActivity;
    protected Context mContext;

    /*懒加载相关*/
    protected boolean mIsViewInitiated;
    protected boolean mIsVisibleToUser;
    protected boolean mIsDataInitiated;

    protected abstract void fetchData();

    protected abstract int getLayout();

    protected abstract void initEvent();

    protected abstract void initView();

    protected abstract void initListener();

    @Override
    public void onAttach(Context context) {
//        mActivity = getActivity();
        mActivity = (Activity) context;
        mContext = context;
        super.onAttach(context);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //判断view是否存在
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                //存在就将其删除
                parent.removeView(view);
            }
            return view;
        }

        view = inflater.inflate(getLayout(), container, false);
        //Butterknife绑定
        unbinder = ButterKnife.bind(this, view);

        initView();
        initListener();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initEvent();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inflater = onGetLayoutInflater(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroyView();
    }

    /*懒加载相关*/

    /**
     * 页面是否可见
     *
     * @param isVisibleToUser true:可见 false:相当于activity中的Pause
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsVisibleToUser = isVisibleToUser;
        initFetchData();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mIsViewInitiated = true;
        initFetchData();
    }

    protected void initFetchData() {
        if (mIsVisibleToUser && mIsViewInitiated && !mIsDataInitiated) {
            //第一次可见时加载数据
            fetchData();
            mIsDataInitiated = true;
        }
    }
}
