package com.huidaxuan.ic2cloud.meituanshoppingcart.base;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.huidaxuan.ic2cloud.meituanshoppingcart.R;
import com.huidaxuan.ic2cloud.meituanshoppingcart.receiver.NetWorkStateReceiver;
import com.huidaxuan.ic2cloud.meituanshoppingcart.util.StatusBarUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @ClassName: BaseActivity
 * @Description:Activity基类-状态栏透明深色字体，可以自己设置状态栏颜色
 * @Author: dingchao
 * @Date: 2020/6/1 11:13
 */
public abstract class BaseTrDarkActivity extends AppCompatActivity {
    protected Context mContext;
    protected Activity mActivity;
    protected Unbinder mUnbinder;

    protected boolean isRegistered = false;
    protected NetWorkStateReceiver netWorkStateReceiver;

    /**
     * 视图绑定
     *
     * @param layoutResId
     */
    @Override
    public void setContentView(int layoutResId) {
        super.setContentView(layoutResId);
    }

//    @Override
//    public void setContentView(View view) {
//        super.setContentView(view);
//    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置禁止横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mContext = this;
        mActivity = this;

        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayout());

        //ButterKnife绑定,注意要在绑定试图之后写
        mUnbinder = ButterKnife.bind(this);
        savedInstanceState(savedInstanceState);
        initEvent();

        //用来设置整体下移，状态栏沉浸
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.setImmersiveStatusBar(this,true,
                getResources().getColor(R.color.color_00000000));

        //网络监听相关
        //注册网络状态监听广播
        netWorkStateReceiver = new NetWorkStateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkStateReceiver, filter);
        isRegistered = true;
        netWorkStateReceiver.netWorkListener(listener);
        initListener();
        initData();
    }

    NetWorkStateReceiver.INetWork listener = new NetWorkStateReceiver.INetWork() {
        @Override
        public void netWorkListener() {
            //说明断开网络连接了，展示统一的丢失
//            ToastUtil.showShort(mContext, "网络连接已经断开，请稍后再试");
            //可以添加统一无网络设置
            noNetWork();
        }
    };

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        //网络监听解绑
        if (isRegistered) {
            unregisterReceiver(netWorkStateReceiver);
        }
        super.onDestroy();
    }

    protected abstract int getLayout();

    protected abstract void initEvent();

    protected abstract void noNetWork();
    protected abstract void initListener();

    protected abstract void initData();
    protected abstract void savedInstanceState(Bundle savedInstanceState);
}
