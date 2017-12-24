package com.jiang.alipay_auxiliary.utils;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
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
            LogUtil.e(TAG, "nodeInfo: " + nodeInfo);
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

    public boolean clickByText(String text) {
        return performClick(findNodesByText(text));
    }

    /**
     * 根据View的ID搜索符合条件的节点,精确搜索方式;
     * 这个只适用于自己写的界面，因为ID可能重复
     * api要求18及以上
     *
     * @param viewId
     * @return 是否点击成功
     */
    public boolean clickById(String viewId) {
        return performClick(findNodesById(viewId));
    }


    /**
     * 根据View的ID搜索符合条件的节点,精确搜索方式;
     * 这个只适用于自己写的界面，因为ID可能重复
     * api要求18及以上
     *
     * @param viewId
     * @return 是否点击成功
     */
    /**
     * 根据View的ID搜索符合条件的节点，精确搜索方式；
     *
     * @param viewId  View ID
     * @param content 辅助内容
     */
    public void InputById(String viewId, String content) {
        performInput(findNodesById(viewId), content);
    }

    /**
     * 点击
     *
     * @param nodeInfos
     * @return
     */
    private boolean performClick(List<AccessibilityNodeInfo> nodeInfos) {
        if (nodeInfos != null && !nodeInfos.isEmpty()) {
            AccessibilityNodeInfo node;
            for (int i = 0; i < nodeInfos.size(); i++) {
                node = nodeInfos.get(i);
                // 获得点击View的类型
                LogUtil.e(TAG, "View类型：" + node.getClassName());
                // 进行模拟点击
                if (node.isEnabled()) {
                    //点击本视图
                    boolean b0 = node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    if (b0) {
                        return true;
                    }

                    //点击父视图
                    boolean b1 = node.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    if (b1) {
                        return true;
                    }

                    //点击祖视图
                    boolean b2 = node.getParent().getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    if (b2) {
                        return true;
                    }

                    //点击外视图
                    boolean b3 = node.getParent().getParent().getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    if (b3) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 赋值
     *
     * @param nodeInfos
     * @param content   内容
     * @return
     */
    private void performInput(List<AccessibilityNodeInfo> nodeInfos, String content) {
        if (nodeInfos != null && !nodeInfos.isEmpty()) {
            AccessibilityNodeInfo node;
            for (int i = 0; i < nodeInfos.size(); i++) {
                node = nodeInfos.get(i);
                // 获得点击View的类型
                LogUtil.e(TAG, "View类型：" + node.getClassName());
                node.setText(content);
            }
        }
    }


    public boolean clickBackKey() {
        return performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
    }

    private boolean performGlobalAction(int action) {
        return mAccessibilityService.performGlobalAction(action);
    }
}
