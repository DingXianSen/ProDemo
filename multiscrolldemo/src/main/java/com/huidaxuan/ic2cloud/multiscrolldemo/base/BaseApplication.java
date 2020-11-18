package com.huidaxuan.ic2cloud.multiscrolldemo.base;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.StrictMode;
import android.os.StrictMode.VmPolicy.Builder;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import java.util.ArrayList;

/**
 * @ClassName: BaseApplication
 * @Description:项目Application
 * @Author: dingchao
 * @Date: 2020/6/1 10:29
 */
public class BaseApplication extends MultiDexApplication {
    private static final String TAG = "BaseApplication";

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        //解决android 7.0系统拍照问题
        Builder builder = new Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        this.context=this;

        //程序异常退出重启
//        UnCeHandler cathExcep = new UnCeHandler(this);
//        Thread.setDefaultUncaughtExceptionHandler(cathExcep);
    }


    /**
     * 屏幕方向改变触发，打开隐藏键盘触发
     *
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * 调用时序 AppLication-->attachBaseContext();
     * ContentProvider.onCreate()
     * Application.onCreate()
     *
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    //应用程序Activity处理相关
    public static ArrayList<Activity> activities = new ArrayList<>();

    /**
     * 向Activity列表添加Activity对象
     *
     * @param activity 添加的Activity对象
     */
    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    /**
     * 从Activity列表移除Activity对象
     *
     * @param activity 要移除的Activity对象
     */
    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }


    /**
     * 关闭Activity列表中的所有Activity
     */
    public static void finishActivity() {
        for (Activity activity : activities) {
            if (null != activity) {
                activity.finish();
            }
        }
    }


    public static Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
