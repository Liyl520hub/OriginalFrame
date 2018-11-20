package com.baseapp.common.utils;

import java.util.List;

/**
 * @company: Coolbit
 * created by {Android-Dev01} on 2018/5/22 0022 下午 4:02
 *
 * @desc:
 */

public class PermissionTipUtils {

    public static String getTipMessage(List<String> permissionNames){

        StringBuilder sb = new StringBuilder();
        sb.append("CoolBit需要获取");

        if (permissionNames != null && permissionNames.size() > 0){
            if (permissionNames.size() > 2){
                for (int i = 0 ; i < permissionNames.size() ; i++){
                    if (i < permissionNames.size() - 2){
                        sb.
                                append("\"").
                                append(permissionNames.get(i)).
                                append("\"").
                                append("、");
                    }else if (i == permissionNames.size() - 2){
                        sb.append("\"").append(permissionNames.get(i)).append("\"和");
                    }else {
                        sb.append("\"").append(permissionNames.get(i)).append("\"权限才能继续，请前往设置中开启。");
                    }
                }
            }else if (permissionNames.size() == 2){
                sb.append("\"").append(permissionNames.get(0)).append("\"和\"").append(permissionNames.get(1)).append("\"权限才能继续，请前往设置中开启。");
            }else {
                sb.append("\"").append(permissionNames.get(0)).append("\"权限才能继续，请前往设置中开启。");
            }
        }else {
            sb.append("\"\"权限才能继续，请前往设置中开启。");
        }

        return sb.toString();
    }
}
