package com.huidaxuan.ic2cloud.multiscrolldemo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

/**
 * @ClassName: NetWorkStateReceiver
 * @Description:网络监听,监听网络状态变化
 * @Author: dingchao
 * @Date: 2020/6/1 14:54
 */
public class NetWorkStateReceiver extends BroadcastReceiver {
    private static final String TAG = "NetWorkStateReceiver";
    private INetWork iNetWork;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "网络状态发生了变化");
        //检测API是不是小于23，因为到了API23之后getNetworkInfo(int networkType)方法被弃用
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            //获得ConnectivityManager对象
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //获取ConnectivityManager对象对应的NetworkInfo对象
            //获取WIFI连接的信息
            NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            //获取移动数据连接的信息
            NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
//                Toast.makeText(context, "WIFI已连接,移动数据已连接", Toast.LENGTH_SHORT).show();
            } else if (wifiNetworkInfo.isConnected() && !dataNetworkInfo.isConnected()) {
//                Toast.makeText(context, "WIFI已连接,移动数据已断开", Toast.LENGTH_SHORT).show();
            } else if (!wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                Toast.makeText(context, "WIFI已断开,移动数据已连接", Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(context, "WIFI已断开,移动数据已断开", Toast.LENGTH_SHORT).show();
//                Toast.makeText(context, "暂无网络连接", Toast.LENGTH_SHORT).show();
                iNetWork.netWorkListener();
            }
            //API大于23时使用下面的方式进行网络监听
        } else {
            Log.e(TAG, "API level 大于23");
            //获得ConnectivityManager对象
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            //获取所有网络连接的信息
            Network[] networks = connMgr.getAllNetworks();
            //用于存放网络连接信息
            StringBuilder sb = new StringBuilder();
            //判断networks是否有值存在
            if (networks.length <= 0) {
//                Toast.makeText(context, "暂无网络连接", Toast.LENGTH_SHORT).show();
                iNetWork.netWorkListener();
            } else {
                //通过循环将网络信息逐个取出来
                for (int i = 0; i < networks.length; i++) {
                    //获取ConnectivityManager对象对应的NetworkInfo对象
                    NetworkInfo networkInfo = connMgr.getNetworkInfo(networks[i]);
                    sb.append(networkInfo.getTypeName() + " connect is " + networkInfo.isConnected());
                    Log.e(TAG, "for:---->" + networkInfo.getTypeName() + "-zhuantai:" + networkInfo.isConnected());
                }
                Log.e(TAG, "sb.toString():---->" + sb.toString());
                if (sb.toString().equals("WIFI_P2P connect is false")) {
                    iNetWork.netWorkListener();
                }
            }
            Log.e(TAG, "networks.length:---->" + networks.length);
        }
    }

    public interface INetWork {
        void netWorkListener();
    }

    public void netWorkListener(@NonNull INetWork netWork) {
        iNetWork = netWork;
    }
}
