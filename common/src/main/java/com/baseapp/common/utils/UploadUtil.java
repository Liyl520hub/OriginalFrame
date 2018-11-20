package com.baseapp.common.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/24.上传文件到服务器类
 */

public class UploadUtil {


    public static List<String> getImageUris(Context mContext, List<Uri> uris) {
        List<String> mList  = new ArrayList<>();
        //判断是拍照还是相册选择
        if (uris.get(0).toString().contains(".jpg")) {
            // 最后通知图库更新
            mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(new File(Environment.getExternalStorageDirectory() +
                            uris.get(0).getPath().replaceFirst("my_images", "Pictures")))));
            mList.add(Environment.getExternalStorageDirectory() + uris.get(0).getPath().replaceFirst("my_images", "Pictures"));


        } else {

            for (int i = 0 ;i<uris.size(); i++){

                Uri uri = uris.get(i);
                mList.add(getRealFilePath(mContext, uri));

            }

        }
        return mList;
    }


    public static String getRealFilePath(final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}
