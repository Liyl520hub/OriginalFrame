package com.baseapp.common.utils;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;


import com.baseapp.common.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Android-Dev04
 * @date 2018/1/7
 * description:Edittext工具类 一行代码设置表情过滤器
 */

public class EditTextUtils {

    private static Pattern EMOJI_PATTERN = Pattern.compile("[^a-zA-Z0-9.，,。?! \u4E00-\\u9FA5_\\-*/\\{\\}$()（）;/！\\[\\]+\\&？@#￥$\"\"“”；《》<>]’‘'");

    /**
     * @param mcontext  上下文
     * @param mEditText 要设置过滤器的Edittext
     * @param isShow    控制是否吐司
     */
    public static void setEditTextFilters(final Context mcontext, EditText mEditText, final boolean isShow) {

        mEditText.setFilters(new InputFilter[]{new InputFilter() {

            @Override
            public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
                Matcher matcher = EMOJI_PATTERN.matcher(charSequence);
                if (!matcher.find()) {
                    return null;
                } else {

                    if (isShow) {
                        ToastUitl.show(mcontext, R.mipmap.wenti, "不支持输入Emoji表情符号", 700);
                    }

                    return "";
                }

            }
        }});
    }

    public static void setEditTextFiltersLength(final Context mcontext, EditText mEditText, final boolean isShow, int textLength) {

        mEditText.setFilters(new InputFilter[]{new InputFilter() {

            @Override
            public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
                Matcher matcher = EMOJI_PATTERN.matcher(charSequence);
                if (!matcher.find()) {
                    return null;
                } else {

                    if (isShow) {
                        ToastUitl.show(mcontext, R.mipmap.wenti, "不支持输入Emoji表情符号", 700);
                    }

                    return "";
                }

            }
        }, new InputFilter.LengthFilter(textLength)});
    }

}
