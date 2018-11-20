package com.baseapp.common.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.baseapp.common.base.callback.NoNetworkCallback;
import com.baseapp.common.utils.NetWorkUtils;

/**
 * Created by Administrator on 2018/3/21 0021.
 * @Desc: 网络状态广播receiver
 */

public class NetworkReceiver extends BroadcastReceiver{


    @Override
    public void onReceive(Context context, Intent intent) {
        NoNetworkCallback mCallback = (NoNetworkCallback) context;

        if (!NetWorkUtils.isNetConnected(context)){
            mCallback.showNoNetworkTipView();
        }else {
            mCallback.hideNoNetworkTipView();
        }
    }
}
