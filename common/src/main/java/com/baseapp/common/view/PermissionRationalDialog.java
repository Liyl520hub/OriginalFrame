package com.baseapp.common.view;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.baseapp.common.R;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;

import java.util.List;

/**
 * @company: Coolbit
 * created by {Android-Dev01} on 2018/4/8 0008 下午 6:26
 * @desc: AndPermission申请权限的rational dailog
 */

public class PermissionRationalDialog implements Rationale<List<String>> {

    @Override
    public void showRationale(Context context, List<String> permissions, final RequestExecutor executor) {
        List<String> permissionNames = Permission.transformText(context, permissions);
        String message = context.getString(R.string.permission_rational_message, TextUtils.join("\n", permissionNames));

        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);
        mDialogBuilder.
                setCancelable(false).
                setTitle(R.string.permission_rational_title).
                setMessage(message).
                setPositiveButton(R.string.permission_resume, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        executor.execute();
                    }
                }).
                setNegativeButton(R.string.permission_cancle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        executor.cancel();
                    }
                }).
                show();

    }


}
