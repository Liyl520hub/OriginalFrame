package com.baseapp.common.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.baseapp.common.R;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;

/**
 * Created by Administrator on 2018/3/21 0021.
 * @Desc: 网络状态广播receiver
 */

public class NetworkChangeReceiver extends BroadcastReceiver{


    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){

            if (NetworkUtils.isConnected()){
                if (NetworkUtils.isWifiConnected()){
                    ToastUtils.showShort(context.getResources().getString(R.string.wifi_connected));
                }else if (NetworkUtils.isMobileData()){
                    ToastUtils.showShort(context.getResources().getString(R.string.mobile_data_connected));
                }
            }else {
                ToastUtils.showShort(context.getResources().getString(R.string.no_network));
            }
        }
    }
}
