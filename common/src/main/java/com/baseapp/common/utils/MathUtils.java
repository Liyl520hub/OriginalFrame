package com.baseapp.common.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @company: Coolbit
 * created by {Android-Dev02} on 2018/4/11 22:03
 *
 * @desc:
 */
public class MathUtils {
    public static double formatDouble(double d){
        java.text.DecimalFormat decimalFormat = new java.text.DecimalFormat("##########.##");
        return Double.parseDouble(decimalFormat.format(d));
    }
    public static double formatDouble(String d){
        java.text.DecimalFormat decimalFormat = new java.text.DecimalFormat("############.##");
        return Double.parseDouble(decimalFormat.format(d));
    }
    /**
     * Double 转string 去除科学记数法显示
     *
     * @param d
     * @return
     */
    public static String double2Str(Double d) {
        if (d == null) {
            return "";
        }
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        return (nf.format(d));
    }
}
