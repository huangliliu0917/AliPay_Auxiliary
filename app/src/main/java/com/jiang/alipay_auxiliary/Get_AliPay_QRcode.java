package com.jiang.alipay_auxiliary;

import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.view.accessibility.AccessibilityEvent;

import com.jiang.alipay_auxiliary.utils.AccessibilityOperator;
import com.jiang.alipay_auxiliary.utils.Auxiliary_Util;
import com.jiang.alipay_auxiliary.utils.LogUtil;
import com.jiang.alipay_auxiliary.utils.QRCodeUtils;

import java.io.File;

/**
 * @author: jiangyao
 * @date: 2017/12/29
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO:  获取支付宝二维码 处理工具
 */

public class Get_AliPay_QRcode {
    private static final String TAG = "Get_AliPay_QRcode";

    String AliPay_QRCode_url = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)) + "/Camera";

    //流程记录
    private static boolean 收钱, 设置金额, 添加收款理由, 金额输入, 理由输入, 输入完成, 返回收款页面, 获取二维码;

    AccessibilityEvent event;

    AliPay_Entity.itemEntity itemEntity;

    AliPay_Entity entity;

    private static int i = 0;

    public Get_AliPay_QRcode(AccessibilityEvent event, AliPay_Entity entity) {
        this.event = event;
        this.entity = entity;
    }

    public AliPay_Entity start() {

        String classname = event.getClassName().toString();

        LogUtil.e(TAG, "到：" + classname);
        LogUtil.e(TAG, "收钱：" + 收钱);
        LogUtil.e(TAG, "设置金额：" + 设置金额);
        LogUtil.e(TAG, "添加收款理由：" + 添加收款理由);
        LogUtil.e(TAG, "金额输入：" + 金额输入);
        LogUtil.e(TAG, "理由输入：" + 理由输入);
        LogUtil.e(TAG, "输入完成：" + 输入完成);
        LogUtil.e(TAG, "返回收款页面：" + 返回收款页面);
        LogUtil.e(TAG, "获取二维码：" + 获取二维码);

        //进入支付宝主页面
        if ("com.eg.android.AlipayGphone.AlipayLogin".equals(classname) && !收钱) {
            Auxiliary_Util.ClickByText("收钱");
            收钱 = true;
            设置金额 = false;
        }

        //进入支付宝收钱页面
        if ("com.alipay.mobile.payee.ui.PayeeQRActivity".equals(classname) && !设置金额) {
            Auxiliary_Util.ClickByText("设置金额");
            设置金额 = true;
        }

        //进入支付宝设置金额页面
        if ("com.alipay.mobile.payee.ui.PayeeQRSetMoneyActivity".equals(classname) && !添加收款理由) {
            Auxiliary_Util.ClickByText("添加收款理由");
            添加收款理由 = true;
        }

        //进入支付宝设置金额页面
        if ("com.alipay.mobile.payee.ui.PayeeQRSetMoneyActivity".equals(classname) && !金额输入) {
            itemEntity = entity.getItemEntities().get(i);
            Auxiliary_Util.InputById("com.alipay.mobile.ui:id/content", itemEntity.getMoney());
            金额输入 = true;
        }

        //进入支付宝设置金额页面
        if ("com.alipay.mobile.payee.ui.PayeeQRSetMoneyActivity".equals(classname) && !理由输入) {
            Auxiliary_Util.InputById("com.alipay.mobile.ui:id/content", itemEntity.getMessage());
            理由输入 = true;
        }

        //进入支付宝设置金额页面
        if ("com.alipay.mobile.payee.ui.PayeeQRSetMoneyActivity".equals(classname) && !输入完成) {
            Auxiliary_Util.ClickByText("确定");
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
            if (Auxiliary_Util.ClickByText("保存图片")) {

                try {
                    LogUtil.e(TAG, "稍等一下");
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //获得图片路径
                String Img_File = Auxiliary_Util.GetFileName(AliPay_QRCode_url);

                LogUtil.e(TAG, "图片路径：" + Img_File);

                if (Img_File != null) {
                    //转换
                    Drawable drawable = Drawable.createFromPath(Img_File);

                    //识别
                    String AliPay_QRcode = QRCodeUtils.getStringFromQRCode(drawable);

                    LogUtil.e(TAG, "收款二维码:" + AliPay_QRcode);

                    entity.getItemEntities().get(i).setQrcode(AliPay_QRcode);

                    //删除文件
                    if (new File(Img_File).delete()) {
                        LogUtil.e(TAG, "删除成功");
                    }

                    获取二维码 = true;
                    LogUtil.e(TAG, "获取到二维码");
                }

                收钱 = false;
                设置金额 = false;
                添加收款理由 = false;
                金额输入 = false;
                理由输入 = false;
                输入完成 = false;
                返回收款页面 = false;
                获取二维码 = false;

                if (i < entity.getItemEntities().size()-1) {
                    i++;
                    start();
                } else {
                    return entity;
                }

                Auxiliary_Util.ClickBack();

            }
        }

        return null;
    }
}
