package com.baseapp.common.base;


import com.baseapp.common.http.error.ErrorType;

public interface BaseView {
    void showErrorTip(ErrorType errorType, int errorCode, String message);
}
