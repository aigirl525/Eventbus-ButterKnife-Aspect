package com.example.aspectlibrary;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckNetUtil {
    /**
     * 检查当前网络是否可用
     */
    public static boolean isNetworkAvailable(Context context){
        //获取手机所有连接管理对象（包括对wifi，net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager != null){
            //获取NetworkIndo对象
            NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
            if (networkInfos != null && networkInfos.length > 0){
                for (int i = 0; i < networkInfos.length; i++){
                    //判断当前网络状态是否为连接状态
                    if (networkInfos[i].getState() == NetworkInfo.State.CONNECTED){
                        return true;
                    }
                }

            }
        }
        return false;
    }
}
