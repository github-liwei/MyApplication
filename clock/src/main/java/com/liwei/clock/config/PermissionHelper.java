package com.liwei.clock.config;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LIWEI on 2017/10/24.
 */
public class PermissionHelper {

    private Object mObject; // 请求权限的对象（Activity,fragment）
    private Dialog mPermissionDialog;

    private static Map<String, String> mAllPermissonMap; // 应用所需的所以权限
    private List<String> mLacksPermissionsList; // 当前mObject缺少的权限

    private static final int PERMISSION_REQUEST_CODE = 13; // 系统权限管理页面的参数
    private boolean isHasCheck; // 是否已经拉起过系统权限检测, 防止和系统提示框重叠

    private OnPermissionListener mPermissionListener;

    /**
     * 构造权限管理hepler
     *
     * @param object 当前要使用的activity或者fragment
     */
    public PermissionHelper(Object object) {
        mObject = object;
        isHasCheck = false;
    }

    // Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    public void checkPermisson(OnPermissionListener listener, String... permissons) {
        if (Build.VERSION.SDK_INT >= 23 && !isHasCheck) {
            mPermissionListener = listener;
            initMap();
            checkSelfPermission(permissons);
            isHasCheck = true;
        } else {
            if (null != listener) {
                listener.onAgreePermission();
            }
        }
    }

    // 如果一定需要权限，就请在activity或者fragment重载方法onresume调用本api
    public void onResume() {
        if (null != mPermissionDialog && !mPermissionDialog.isShowing()) {
            if (null != mLacksPermissionsList && !mLacksPermissionsList.isEmpty()) {
                String[] permissions = mLacksPermissionsList.toArray(new String[mLacksPermissionsList.size()]);
                checkSelfPermission(permissions);
            }
        }
    }

    private void initMap() {
        if (null == mAllPermissonMap) {
            mAllPermissonMap = new HashMap<String, String>();
            mAllPermissonMap.put(Manifest.permission.SEND_SMS, "发短信");
            mAllPermissonMap.put(Manifest.permission.CALL_PHONE, "拨打电话");
            mAllPermissonMap.put(Manifest.permission.READ_CALL_LOG, "查看通讯记录");
            mAllPermissonMap.put(Manifest.permission.RECORD_AUDIO, "录音");
            mAllPermissonMap.put(Manifest.permission.ACCESS_COARSE_LOCATION, "ACCESS_COARSE_LOCATION");
            mAllPermissonMap.put(Manifest.permission.ACCESS_FINE_LOCATION, "ACCESS_FINE_LOCATION");
            mAllPermissonMap.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, "文件读写");


        }
    }

    // 检查权限集合
    private void checkSelfPermission(String... perrmissons) {
        if (!lacksPermissions(perrmissons)) {
            if (null != mPermissionDialog) {
                mPermissionDialog.dismiss();
            }
        }
    }

    // 判断权限集合
    public boolean lacksPermissions(String... permissions) {
        boolean isLacksPermission = false;

        if (null != mLacksPermissionsList) {
            mLacksPermissionsList.clear();
        }

        for (String permission : permissions) {
            if (lacksPermission(permission)) {
                if (null == mLacksPermissionsList) {
                    mLacksPermissionsList = new ArrayList<String>();
                }
                mLacksPermissionsList.add(permission);
                isLacksPermission = true;
            }
        }

        if (null != mLacksPermissionsList && !mLacksPermissionsList.isEmpty()) {
            requestPermissions(mLacksPermissionsList.toArray(new String[mLacksPermissionsList.size()]));
        } else {
            if (null != mPermissionListener) {
                mPermissionListener.onAgreePermission();
            }
        }
        return isLacksPermission;
    }

    // 请求权限
    @TargetApi(23)
    private void requestPermissions(String... permissions) {
        if (mObject instanceof Activity) {
            ActivityCompat.requestPermissions((Activity) mObject, permissions, PERMISSION_REQUEST_CODE);
        } else if (mObject instanceof android.app.Fragment) {
            ((android.app.Fragment) mObject).requestPermissions(permissions, PERMISSION_REQUEST_CODE);
        } else if (mObject instanceof android.support.v4.app.Fragment) {
            ((android.support.v4.app.Fragment) mObject).requestPermissions(permissions, PERMISSION_REQUEST_CODE);
        }
    }

    // 判断是否缺少权限
    private boolean lacksPermission(String permission) {
        Activity activity = getActivity();
        if (null == activity) {
            return false;
        }
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_DENIED;
    }

    // ActivityCompat.requestPermissions 返回结果
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE && null != mLacksPermissionsList
                && !mLacksPermissionsList.isEmpty()) {
            for (int i = 0; i < permissions.length; i++) {
                if (mLacksPermissionsList.contains(permissions[i])
                        && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    mLacksPermissionsList.remove(permissions[i]); // 已经授权
                }
            }

            if (mLacksPermissionsList.isEmpty()) {
                if (null != mPermissionListener) {
                    mPermissionListener.onAgreePermission();
                }
            } else { // 还有权限未授
                showMessage(mLacksPermissionsList.toArray(new String[mLacksPermissionsList.size()]));
            }
        }
    }

    private void showMessage(String... perrmissons) {
        final Activity activity = getActivity();
        if (null == activity) {
            return;
        }

        if (null != mPermissionDialog) {
            mPermissionDialog.dismiss();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false)
                .setTitle("请授予应用权限")
                .setMessage(getMsg(perrmissons))
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings(activity);
                        mPermissionDialog.dismiss();
                        isHasCheck = false;
                    }
                });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (null != mPermissionListener) {
                    mPermissionListener.onDeniedPermission();
                }
            }
        });
        // mPermissionDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        mPermissionDialog = builder.create();
        mPermissionDialog.show();
    }

    private String getMsg(String... perrmissons) {
        StringBuffer sb = new StringBuffer("应用缺少：");
        for (String string : perrmissons) {
            if (mAllPermissonMap.containsKey(string)) {
                sb.append(mAllPermissonMap.get(string) + "、");
            } else {
                sb.append("(未在PermissionHepler类里initMap方法配置说明)、");
            }
        }
        sb.delete(sb.length() - 1, sb.length());
        sb.append("权限");
        return sb.toString();
    }

    private Activity getActivity() {
        if (mObject instanceof Activity) {
            return ((Activity) mObject);
        } else if (mObject instanceof android.app.Fragment) {
            return ((android.app.Fragment) mObject).getActivity();
        } else if (mObject instanceof android.support.v4.app.Fragment) {
            return ((android.support.v4.app.Fragment) mObject).getActivity();
        } else {
            return null;
        }
    }

    // 启动应用的设置
    private void startAppSettings(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
//      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public interface OnPermissionListener {
        /**
         * sdk<23或>=23时所需权限已经获取到了权限执行api
         */
        void onAgreePermission();

        /**
         * sdk>=23时所需权限未获取到了权限执行api
         */
        void onDeniedPermission();
    }
}
