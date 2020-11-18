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

import com.huidaxuan.ic2cloud.meituanshoppingcart.receiver.NetWorkStateReceiver;
import com.huidaxuan.ic2cloud.meituanshoppingcart.util.StatusBarUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @ClassName: BaseActivity
 * @Description:Activity-基类状态栏透明浅色字体，有半透明效果
 * @Author: dingchao
 * @Date: 2020/6/1 11:13
 */
public abstract class BaseTrLightActivity extends AppCompatActivity {
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

        initEvent();

        //用来设置整体下移，状态栏沉浸
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
//        if (!StatusBarUtil.setStatusBarDarkTheme(this, false)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
//            StatusBarUtil.setStatusBarColor(this, 0x55000000);
//        }
//        StatusBarUtil.setStatusBarDarkTheme(mActivity, true);
//        StatusBarUtil.setStatusBarColor(mActivity, mContext.getResources().getColor(R.color.color_ffffff));//设置背景颜色
        StatusBarUtil.setStatusBarDarkTheme(this, false);//白色
        StatusBarUtil.setStatusBarColor(this, 0x55000000);

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

}
