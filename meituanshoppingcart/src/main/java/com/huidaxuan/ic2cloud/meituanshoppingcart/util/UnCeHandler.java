package com.huidaxuan.ic2cloud.meituanshoppingcart.util;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.huidaxuan.ic2cloud.meituanshoppingcart.base.BaseApplication;


/**
 * @ClassName: UnCeHandler
 * @Description:程序异常退出工具类
 * @Author: dingchao
 * @Date: 2020/6/1 10:36
 */
public class UnCeHandler implements Thread.UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private static final String TAG = "UnCeHandler";
    private BaseApplication baseApplication;

    public UnCeHandler(BaseApplication baseApplication) {
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        this.baseApplication = baseApplication;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (!handleException(e) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(t, e);
        } else {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e1) {
                Log.e(TAG, "error:" + e1);
            }
            //重新启动，建议启动Logo
            Intent intent = new Intent(baseApplication.getApplicationContext(), MainActivity.class);

            @SuppressLint("WrongConstant")
            PendingIntent restartIntent = PendingIntent.getActivity(baseApplication.getApplicationContext(),
                    0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
            //退出程序
            AlarmManager mgr = (AlarmManager) baseApplication.getSystemService(Context.ALARM_SERVICE);
            //1秒钟后重启应用
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent);
            Tool.exitApp();
        }
    }


    /**
     * 自定义错误处理，收集错误信息，发送错误报告等操作均在此完成
     *
     * @param ex
     * @return true:如果处理了该异常信息；false:没有处理异常信息
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        //使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(baseApplication.getApplicationContext(), "很抱歉，程序出现异常，即将退出", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();
        return true;
    }
}
