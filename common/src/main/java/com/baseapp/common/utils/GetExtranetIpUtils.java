package com.baseapp.common.utils;

import android.text.TextUtils;

import com.baseapp.common.base.config.BaseConfig;
import com.baseapp.common.app.Global;
import com.blankj.utilcode.util.SPUtils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Android-Dev04
 * @date 2018/1/29
 * description:获取外网ip
 */

public class GetExtranetIpUtils {

    public static void getMobileIP() {
        new Thread() {
            @Override
            public void run() {
                String ip = "";
                try {
                    String address = "https://pv.sohu.com/cityjson?ie=utf-8";
                    URL url = new URL(address);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setUseCaches(false);
                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream in = connection.getInputStream();
                        // 将流转化为字符串
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        String tmpString = "";
                        StringBuilder retJSON = new StringBuilder();
                        while ((tmpString = reader.readLine()) != null) {
                            retJSON.append(tmpString + "\n");
                        }

                        String[] split = retJSON.toString().split("=");
                        if (split.length > 1) {
                            JSONObject jsonObject = new JSONObject(split[1]);
                            ip = jsonObject.getString("cip");
                        }
                    } else {
                        ip = "";
                    }
                } catch (Exception e) {
                    ip = "";
                }
                if (TextUtils.isEmpty(ip)) {
                   SPUtils.getInstance().put(BaseConfig.BaseSPKey.EXTRANET_IP, "0.0.0.0");
                } else {
                    SPUtils.getInstance().put(BaseConfig.BaseSPKey.EXTRANET_IP, ip);
                }
            }
        }.start();
    }


}
