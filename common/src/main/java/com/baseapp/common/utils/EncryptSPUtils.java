package com.baseapp.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.baseapp.common.base.BaseApplication;
import com.baseapp.common.utils.SecuritySharedPreference;
import com.blankj.utilcode.util.SPUtils;

import java.util.Iterator;
import java.util.Map;


/**
 * 对SharedPreference文件中的各种类型的数据进行存取操作
 *
 * @author Administrator
 */
public class EncryptSPUtils {

    public static void setSharedStringData(Context context, String key, String value) {

        //单独对token 做加密处理
        if ("token".equals(key) || "userPhone".equals(key)) {
            SPUtils.getInstance().put(encryptPreference(key), encryptPreference(value));
        } else {
            SPUtils.getInstance().put(key, value);
        }
    }

    public static String getSharedStringData(Context context, String key) {

        if ("token".equals(key) || "userPhone".equals(key)) {
            String encryptValue = SPUtils.getInstance().getString(encryptPreference(key), "");
            return "".equals(encryptValue) ? "" : decryptPreference(encryptValue);
        }
        return SPUtils.getInstance().getString(key, "");
    }

    /**
     * encrypt function
     *
     * @return cipherText base64 加密
     */
    private static String encryptPreference(String plainText) {
        return EncryptUtil.getInstance(BaseApplication.getAppContext()).encrypt(plainText);
    }

    /**
     * decrypt function
     *
     * @return plainText 解密
     */
    private static String decryptPreference(String cipherText) {
        return EncryptUtil.getInstance(BaseApplication.getAppContext()).decrypt(cipherText);
    }

}
