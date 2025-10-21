package com.ruixue.sdk.demo.helper;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

// Created by wangliang on 2024/5/16.
public class CommonHelper {

    public static String getVersionName(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isSupportEasyPhoto(Context context) {
        return (Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE && getTargetSdkVersion(context) < Build.VERSION_CODES.UPSIDE_DOWN_CAKE);
    }

    public static int getTargetSdkVersion(Context context) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
            int targetSdkVersion = applicationInfo.targetSdkVersion;
            Log.d("WLTest", "targetSdkVersion:" + targetSdkVersion);
            return targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static boolean startActivityByClass(Context context, String className, Bundle bundle) {
        try {
            Class<?> clz = Class.forName(className);
            Intent it = new Intent(context, clz);
            if (null != bundle) {
                it.putExtras(bundle);
            }
            context.startActivity(it);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("error", e.toString());
            Toast.makeText(context, "打开失败，请查看日志!", Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
