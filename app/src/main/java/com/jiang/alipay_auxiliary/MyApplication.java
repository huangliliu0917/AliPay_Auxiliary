package com.jiang.alipay_auxiliary;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

/**
 * @author: jiangyao
 * @date: 2017/12/27
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO:
 */

public class MyApplication extends Application {

    public static Activity activity;
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
}
