package com.jiang.alipay_auxiliary;

import android.accessibilityservice.AccessibilityService;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.jiang.alipay_auxiliary.utils.AccessibilityOperator;
import com.jiang.alipay_auxiliary.utils.LogUtil;
import com.jiang.alipay_auxiliary.utils.QRCodeUtils;

import java.io.File;
import java.util.List;
import java.util.Vector;

/**
 * @author: jiangyao
 * @date: 2017/12/19
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO:
 */

public class MyAccessibilityService extends AccessibilityService {
    private static final String TAG = "MyAccessibilityService";

    //
    String AliPay_QRCode_url = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)) + "/Camera";

    public static String money, message;

    //流程记录
    boolean 收钱, 设置金额, 添加收款理由, 金额输入, 理由输入, 输入完成, 返回收款页面, 获取二维码;

    private boolean 我, 钱包, 收付款, 二维码收款, 设金额, 输入金额, 添加备注, 输入备注, 返回二维码收款, 保存收款码, 识别收款码;

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

//        //如果前台是支付宝
//        if (getTopAppPackageName(MyApplication.context).equals("com.eg.android.AlipayGphone")) {
//
//        } else {
//            startActivity(getPackageManager().getLaunchIntentForPackage("com.eg.android.AlipayGphone"));
//        }


        //如果前台是微信
        if (getTopAppPackageName(MyApplication.context).equals("com.tencent.mm")) {

        } else {
            startActivity(getPackageManager().getLaunchIntentForPackage("com.tencent.mm"));
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

                //进入微信主页面
                if ("com.tencent.mm.ui.LauncherUI".equals(classname) && !我) {
                    Click("我");
                    我 = true;
                }

                //进入钱包主页面
                if ("com.tencent.mm.ui.LauncherUI".equals(classname) && !钱包) {
                    Click("钱包");
                    钱包 = true;
                }

                //进入微信收付款页面
                if ("com.tencent.mm.plugin.mall.ui.MallIndexUI".equals(classname) && !收付款) {
                    Click("收付款");
                    收付款 = true;
                }

                //进入微信二维码收款页面
                if ("com.tencent.mm.plugin.offline.ui.WalletOfflineCoinPurseUI".equals(classname) && !二维码收款) {
                    Click("二维码收款");
                    二维码收款 = true;
                }

                //进入微信二维码收款页面
                if ("com.tencent.mm.plugin.collect.ui.CollectMainUI".equals(classname) && !设金额) {
                    Click("设置金额");
                    设金额 = true;
                }

                //进入微信二维码收款页面
                if ("com.tencent.mm.plugin.collect.ui.CollectCreateQRCodeUI".equals(classname) && !二维码收款) {


//                    Click("确定");
                    二维码收款 = true;
                }


                //点击"保存收款码"
                if ("com.tencent.mm.plugin.collect.ui.CollectMainUI".equals(classname) && !保存收款码) {
                    Click("保存收款码");
                    保存收款码 = true;
                }


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
                    Input("com.alipay.mobile.ui:id/content", "10");
                    金额输入 = true;
                }

                //进入支付宝设置金额页面
                if ("com.alipay.mobile.payee.ui.PayeeQRSetMoneyActivity".equals(classname) && !理由输入) {
                    Input("com.alipay.mobile.ui:id/content", "没有理由");

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
                if ("com.alipay.mobile.payee.ui.PayeeQRActivity".equals(classname) && !获取二维码 && 金额输入) {

                    //保存图片
                    if (Click("保存图片")) {

                        try {
                            LogUtil.e(TAG, "稍等一下");
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        //获得图片路径
                        String Img_File = GetVideoFileName();

                        LogUtil.e(TAG, "图片路径：" + Img_File);

                        //转换
                        Drawable drawable = Drawable.createFromPath(Img_File);

                        //识别
                        String AliPay_QRcode = QRCodeUtils.getStringFromQRCode(drawable);

                        LogUtil.e(TAG, "收款二维码:" + AliPay_QRcode);

                        //删除文件
                        if (new File(Img_File).delete()) {
                            LogUtil.e(TAG, "删除成功");
                        }

                    }

                    获取二维码 = true;
                    LogUtil.e(TAG, "获取到二维码");

                }


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
     * 长按
     *
     * @param click
     */
    public boolean LongClick(String click) {

        if (AccessibilityOperator.getInstance().LongclickByText(click)) {
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


    /**
     * 获取支付宝付款二维码
     *
     * @param money
     * @param message
     */
    public void AliPayQRcode(String money, String message) {

    }


    /**
     * 获取指定目录下所有jpg文件
     *
     * @return
     */
    public String GetVideoFileName() {
        Vector<String> vecFile = new Vector<>();

        File file = new File(AliPay_QRCode_url);
        File[] subFile = file.listFiles();

        try {
            for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
                // 判断是否为文件夹
                if (!subFile[iFileLength].isDirectory()) {
                    String filename = subFile[iFileLength].getName();
                    // 判断是否为MP4结尾
                    if (filename.trim().toLowerCase().endsWith(".jpg")) {
                        vecFile.add(filename);
                        LogUtil.e(TAG, filename);
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
            return null;
        }

        if (vecFile.size() == 1) {
            return AliPay_QRCode_url + "/" + vecFile.get(0).toString();
        } else {
            return null;
        }
    }
}
