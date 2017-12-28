package com.jiang.alipay_auxiliary;

import android.accessibilityservice.AccessibilityService;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.jiang.alipay_auxiliary.utils.AccessibilityOperator;
import com.jiang.alipay_auxiliary.utils.LogUtil;

import java.util.List;

/**
 * @author: jiangyao
 * @date: 2017/12/19
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO:
 */

public class MyAccessibilityService extends AccessibilityService {
    private static final String TAG = "MyAccessibilityService";

    public static String money, message;

    //流程记录
    boolean 收钱, 设置金额, 添加收款理由, 金额输入, 理由输入, 输入完成, 返回收款页面, 获取二维码;

    /**
     * 查询前台应用的包名
     *
     * @param context
     * @return
     */
    public static String getTopAppPackageName(Context context) {

        String packageName = "";
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        try {
            List<ActivityManager.RunningAppProcessInfo> processes = activityManager.getRunningAppProcesses();
            if (processes.size() == 0) {
                return packageName;
            }
            for (ActivityManager.RunningAppProcessInfo process : processes) {

                if (process.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return process.processName;
                }
            }
        } catch (Exception ignored) {
            LogUtil.e(TAG, "没查到");
        }
        return packageName;
    }

    /**
     * 系统会在成功连接上你的服务的时候调用这个方法，
     * 在这个方法里你可以做一下初始化工作，
     * 例如设备的声音震动管理，
     * 也可以调用setServiceInfo()进行配置工作。
     */
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        LogUtil.e(TAG, "服务已开启");

        LogUtil.e(TAG, "金额：" + money);
        LogUtil.e(TAG, "理由：" + message);

        //如果前台是支付宝
        if (getTopAppPackageName(MyApplication.context).equals("com.eg.android.AlipayGphone")) {

        } else {
            startActivity(getPackageManager().getLaunchIntentForPackage("com.eg.android.AlipayGphone"));
        }

    }

    /**
     * 通过这个函数可以接收系统发送来的AccessibilityEvent，
     * 接收来的AccessibilityEvent是经过过滤的，
     * 过滤是在配置工作时设置的。
     *
     * @param event
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        AccessibilityOperator.getInstance().updateEvent(this, event);

        String classname = event.getClassName().toString();

        switch (event.getEventType()) {
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                LogUtil.e(TAG, "点击了: " + classname);
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                LogUtil.e(TAG, "到: " + classname);

                //进入支付宝主页面
                if ("com.eg.android.AlipayGphone.AlipayLogin".equals(classname) && !收钱) {
                    Click("收钱");
                    收钱 = true;
                }

                //进入支付宝收钱页面
                if ("com.alipay.mobile.payee.ui.PayeeQRActivity".equals(classname) && !设置金额) {
                    Click("设置金额");
                    设置金额 = true;
                }

                //进入支付宝设置金额页面
                if ("com.alipay.mobile.payee.ui.PayeeQRSetMoneyActivity".equals(classname) && !添加收款理由) {
                    Click("添加收款理由");
                    添加收款理由 = true;
                }

                //进入支付宝设置金额页面
                if ("com.alipay.mobile.payee.ui.PayeeQRSetMoneyActivity".equals(classname) && !金额输入) {
                    LogUtil.e(TAG, "金额：" + money);
                    Input("com.alipay.mobile.ui:id/content", money);
                    金额输入 = true;
                }

                //进入支付宝设置金额页面
                if ("com.alipay.mobile.payee.ui.PayeeQRSetMoneyActivity".equals(classname) && !理由输入) {
                    Input("com.alipay.mobile.ui:id/content", message);

                    理由输入 = true;
                }

                //进入支付宝设置金额页面
                if ("com.alipay.mobile.payee.ui.PayeeQRSetMoneyActivity".equals(classname) && !输入完成) {
                    Click("确定");
                    输入完成 = true;
                }

                //进入支付宝设置金额页面
                if ("com.alipay.mobile.payee.ui.PayeeQRSetMoneyActivity".equals(classname) && !返回收款页面) {
                    AccessibilityOperator.getInstance().clickBackKey();
                    返回收款页面 = true;
                }

                //进入支付宝收钱页面
                if ("com.alipay.mobile.payee.ui.PayeeQRActivity".equals(classname) && !获取二维码) {

                    获取二维码 = true;
                    LogUtil.e(TAG, "获取到二维码");
                }


//
////                com.alipay.mobile.payee:id/payee_QRCodeImageView  二维码
//
//                //进入支付宝收钱页面
//                if ("com.alipay.mobile.payee.ui.PayeeQRActivity".equals(classname)) {
//                    Click("设置金额");
//                }


                break;

            default:
                break;

        }
    }

    /**
     * 点击
     *
     * @param click
     */
    public boolean Click(String click) {

        if (AccessibilityOperator.getInstance().clickByText(click)) {
            LogUtil.e(TAG, "点击" + click + "成功");
            return true;
        }
        LogUtil.e(TAG, "点击 " + click + " 失败");
        return false;

    }

    /**
     * 赋值
     *
     * @param click
     * @param text
     */
    public boolean Input(String click, String text) {

        if (AccessibilityOperator.getInstance().InputById(click, text)) {
            LogUtil.e(TAG, "输入 " + text + " 成功");
            return true;
        }
        LogUtil.e(TAG, "输入 " + text + " 失败");
        return false;

    }

    /**
     * 这个在系统想要中断AccessibilityService返给的响应时会调用。
     * 在整个生命周期里会被调用多次。
     */
    @Override
    public void onInterrupt() {
        Log.e(TAG, "onInterrupt: ");
    }

    /**
     * 在系统将要关闭这个AccessibilityService会被调用。
     * 在这个方法中进行一些释放资源的工作。
     *
     * @param intent
     * @return
     */
    @Override
    public boolean onUnbind(Intent intent) {
        LogUtil.e(TAG, "onUnbind: ");
        return super.onUnbind(intent);
    }

}
