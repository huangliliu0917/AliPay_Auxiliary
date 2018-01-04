package com.jiang.alipay_auxiliary.utils;

import java.io.File;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: jiangyao
 * @date: 2018/1/4
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 辅助操作
 */

public class Auxiliary_Util {
    private static final String TAG = "Auxiliary_Util";

    /**
     * 返回
     */
    public static boolean ClickBack() {

        if (AccessibilityOperator.getInstance().clickBackKey()) {
            LogUtil.e(TAG, "返回成功");
            return true;
        }
        LogUtil.e(TAG, "返回失败");
        return false;

    }


    /**
     * 点击
     *
     * @param click
     */
    public static boolean ClickByText(String click) {

        if (AccessibilityOperator.getInstance().clickByText(click)) {
            LogUtil.e(TAG, "点击" + click + "成功");
            return true;
        }
        LogUtil.e(TAG, "点击 " + click + " 失败");
        return false;

    }

    /**
     * 点击
     *
     * @param click
     */
    public static boolean ClickById(String click) {

        if (AccessibilityOperator.getInstance().clickById(click)) {
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
    public static boolean LongClickByText(String click) {

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
    public static boolean InputById(String click, String text) {

        if (AccessibilityOperator.getInstance().InputById(click, text)) {
            LogUtil.e(TAG, "输入 " + text + " 成功");
            return true;
        }
        LogUtil.e(TAG, "输入 " + text + " 失败");
        return false;

    }


    /**
     * 获取指定目录下所有jpg文件
     *
     * @return
     */
    public static String GetFileName(String fileurl) {
        Vector<String> vecFile = new Vector<>();

        File file = new File(fileurl);
        File[] subFile = file.listFiles();

        try {
            for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
                // 判断是否为文件夹
                if (!subFile[iFileLength].isDirectory()) {
                    String filename = subFile[iFileLength].getName();
                    LogUtil.e(TAG, "图片列表：" + filename);
                    // 判断是否为JPG结尾
                    if (filename.trim().toLowerCase().endsWith(".jpg")) {
                        if (filename.length() == 17 && match("(^\\d{13}$)", filename.substring(0, 13))) {
                            vecFile.add(filename);
                            LogUtil.e(TAG, filename);
                        }
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
            return null;
        }

        if (vecFile.size() == 1) {
            return fileurl + "/" + vecFile.get(0).toString();
        } else {
            return null;
        }
    }

    /**
     * 数据正则对比
     *
     * @param regex 正则
     * @param str   数据
     * @return
     */
    private static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }


}
