package com.jiang.alipay_auxiliary.utils;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

/**
 * Created by popfisher on 2017/7/11.
 */

@TargetApi(16)
public class AccessibilityOperator {
    private static final String TAG = "AccessibilityOperator";

    private Context mContext;
    private static AccessibilityOperator mInstance = new AccessibilityOperator();
    private AccessibilityEvent mAccessibilityEvent;
    private AccessibilityService mAccessibilityService;

    private AccessibilityOperator() {
    }

    public static AccessibilityOperator getInstance() {
        return mInstance;
    }

    public void init(Context context) {
        mContext = context;
    }

    public void updateEvent(AccessibilityService service, AccessibilityEvent event) {
        if (service != null && mAccessibilityService == null) {
            mAccessibilityService = service;
        }
        if (event != null) {
            mAccessibilityEvent = event;
        }
    }

    public boolean isServiceRunning() {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> services = am.getRunningServices(Short.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo info : services) {
            if (info.service.getClassName().equals(mContext.getPackageName() + ".AccessibilitySampleService")) {
                return true;
            }
        }
        return false;
    }

    private AccessibilityNodeInfo getRootNodeInfo() {
        AccessibilityNodeInfo nodeInfo = null;
        // 建议使用getRootInActiveWindow，这样不依赖当前的事件类型
        if (mAccessibilityService != null) {
            nodeInfo = mAccessibilityService.getRootInActiveWindow();
//            LogUtil.e(TAG, "nodeInfo: " + nodeInfo);
        }

        return nodeInfo;
    }

    /**
     * 根据Text搜索所有符合条件的节点, 模糊搜索方式
     */
    public List<AccessibilityNodeInfo> findNodesByText(String text) {
        AccessibilityNodeInfo nodeInfo = getRootNodeInfo();
        if (nodeInfo != null) {
            return nodeInfo.findAccessibilityNodeInfosByText(text);
        }
        return null;
    }

    /**
     * 根据View的ID搜索符合条件的节点,精确搜索方式;
     * 这个只适用于自己写的界面，因为ID可能重复
     * api要求18及以上
     *
     * @param viewId
     */
    public List<AccessibilityNodeInfo> findNodesById(String viewId) {
        AccessibilityNodeInfo nodeInfo = getRootNodeInfo();
        if (nodeInfo != null) {
            return nodeInfo.findAccessibilityNodeInfosByViewId(viewId);
        }
        return null;
    }

    /**
     * 根据 View ID 点击
     *
     * @param viewId
     * @return
     */
    public boolean clickById(String viewId) {
        return performClick(findNodesById(viewId));
    }

    /**
     * 根据 View ID 获取二维码
     *
     * @param viewId
     * @return
     */
    public void QRcodeById(String viewId) {
        performQRCode(findNodesById(viewId));
    }

    /**
     * 根据View 的文字点击
     *
     * @param text
     * @return
     */
    public boolean clickByText(String text) {
        return performClick(findNodesByText(text));
    }


    /**
     * 根据View 的文字做长按
     *
     * @param text
     * @return
     */
    public boolean LongclickByText(String text) {
        return performLongClick(findNodesByText(text));
    }

    /**
     * 根据View 的ID做长按
     *
     * @param Id
     * @return
     */
    public boolean LongclickById(String Id) {
        return performLongClick(findNodesById(Id));
    }

    /**
     * 根据 View ID 输入
     *
     * @param viewId
     * @return
     */
    public boolean InputById(String viewId, String text) {
        return performInput(findNodesById(viewId), text);
    }

    /**
     * 点击
     *
     * @param nodeInfos
     * @retur
     */
    private boolean performClick(List<AccessibilityNodeInfo> nodeInfos) {
        if (nodeInfos != null && !nodeInfos.isEmpty()) {
            AccessibilityNodeInfo node;
            for (int i = 0; i < nodeInfos.size(); i++) {
                node = nodeInfos.get(i);
                //逐层判断

                //传入层
                if (node.isEnabled()) {
                    LogUtil.e(TAG, "准备点击：" + node.getClassName());
                    if (node.performAction(AccessibilityNodeInfo.ACTION_CLICK)) {
                        LogUtil.e(TAG, "点击： " + node.getClassName() + " 成功");
                        return true;
                    }
                }

                //上一层
                if (node.getParent().isEnabled()) {
                    LogUtil.e(TAG, "准备点击：" + node.getParent().getClassName());
                    if (node.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK)) {
                        LogUtil.e(TAG, "点击： " + node.getParent().getClassName() + " 成功");
                        return true;
                    }
                }

                //上上一层
                if (node.getParent().getParent().isEnabled()) {
                    LogUtil.e(TAG, "准备点击：" + node.getParent().getParent().getClassName());
                    if (node.getParent().getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK)) {
                        LogUtil.e(TAG, "点击： " + node.getParent().getParent().getClassName() + " 成功");
                        return true;
                    }
                }

                //表层
                if (node.getParent().getParent().getParent().isEnabled()) {
                    LogUtil.e(TAG, "准备点击：" + node.getParent().getParent().getParent().getClassName());
                    if (node.getParent().getParent().getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK)) {
                        LogUtil.e(TAG, "点击： " + node.getParent().getParent().getParent().getClassName() + " 成功");
                        return true;
                    }
                }


            }
        }

        return false;
    }

    /**
     * 长点击
     *
     * @param nodeInfos
     * @return
     */
    private boolean performLongClick(List<AccessibilityNodeInfo> nodeInfos) {
        if (nodeInfos != null && !nodeInfos.isEmpty()) {
            AccessibilityNodeInfo node;
            for (int i = 0; i < nodeInfos.size(); i++) {
                node = nodeInfos.get(i);
                //传入层
                if (node != null && node.isEnabled()) {
                    LogUtil.e(TAG, "准备长按：" + node.getClassName());
                    if (node.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK)) {
                        LogUtil.e(TAG, "长按： " + node.getClassName() + " 成功");
                        return true;
                    }
                }

                //上一层
                if (node.getParent() != null && node.getParent().isEnabled()) {
                    LogUtil.e(TAG, "准备长按：" + node.getParent().getClassName());
                    if (node.getParent().performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK)) {
                        LogUtil.e(TAG, "长按： " + node.getParent().getClassName() + " 成功");
                        return true;
                    }
                }

                //上上一层
                if (node.getParent().getParent() != null && node.getParent().getParent().isEnabled()) {
                    LogUtil.e(TAG, "准备长按：" + node.getParent().getParent().getClassName());
                    if (node.getParent().getParent().performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK)) {
                        LogUtil.e(TAG, "长按： " + node.getParent().getParent().getClassName() + " 成功");
                        return true;
                    }
                }

                //表层
                if (node.getParent().getParent().getParent() != null && node.getParent().getParent().getParent().isEnabled()) {
                    LogUtil.e(TAG, "准备长按：" + node.getParent().getParent().getParent().getClassName());
                    if (node.getParent().getParent().getParent().performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK)) {
                        LogUtil.e(TAG, "长按： " + node.getParent().getParent().getParent().getClassName() + " 成功");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 输入
     * 针对支付宝定制话
     *
     * @param nodeInfos
     * @return
     */
    private boolean performInput(List<AccessibilityNodeInfo> nodeInfos, String text) {
        if (nodeInfos != null && !nodeInfos.isEmpty()) {
            AccessibilityNodeInfo node;

            LogUtil.e(TAG, "搜索到:" + nodeInfos.size() + "个");

            //支付金额输入有两个输入框
            //上面一个为收款金额
            //下一个为收款理由
            if (nodeInfos.size() == 2) {
                try {
                    int i = Integer.valueOf(text);
                    node = nodeInfos.get(0);
                    Bundle bundle = new Bundle();
                    bundle.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text);
                    node.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, bundle);
                    return true;
                } catch (Exception e) {
                    node = nodeInfos.get(1);
                    Bundle bundle = new Bundle();
                    bundle.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text);
                    node.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, bundle);
                    return true;
                }
            }

//            for (int i = 0; i < nodeInfos.size(); i++) {
//                node = nodeInfos.get(i);
//
//                LogUtil.e(TAG, "View类型：" + node.getClassName());
//
//                if (node.getClassName().toString().contains("EditText")) {
//                    Bundle bundle = new Bundle();
//                    bundle.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text);
//                    node.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, bundle);
//                    return true;
//                }
//            }
        }
        return false;
    }

    /**
     * 获取二维码
     * 针对支付宝定制话
     *
     * @param nodeInfos
     * @return
     */
    private boolean performQRCode(List<AccessibilityNodeInfo> nodeInfos) {
        if (nodeInfos != null && !nodeInfos.isEmpty()) {
            AccessibilityNodeInfo node;

            for (int i = 0; i < nodeInfos.size(); i++) {
                node = nodeInfos.get(i);
                LogUtil.e(TAG, "View类型：" + node.getClassName());

            }
        }
        return false;
    }


    public boolean clickBackKey() {
        return performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
    }

    private boolean performGlobalAction(int action) {
        return mAccessibilityService.performGlobalAction(action);
    }
}
