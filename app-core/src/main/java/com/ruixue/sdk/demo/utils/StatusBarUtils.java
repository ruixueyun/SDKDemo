package com.ruixue.sdk.demo.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.annotation.RequiresApi;

/**
 * Created by wangliang on 2024/8/7
 */
public class StatusBarUtils {

    public static final int MODE_LIGHT = 1;
    public static final int MODE_DARK = 2;

    public static void initStatusBar(
            Activity activity,
            boolean transparentStatusBar,
            boolean transparentNavigationBar,
            @ColorInt int statusBarColor,
            int statusBarMode
    ) {
        if (activity == null || activity.getWindow() == null) {
            return;
        }
        if (transparentStatusBar) {
            setTransparentStatusBar(activity.getWindow());
        }
        setStatusBarColor(activity.getWindow(), statusBarColor);

        if (transparentNavigationBar) {
            activity.getWindow().setNavigationBarColor(statusBarColor);
            activity.getWindow().getDecorView().setSystemUiVisibility(activity.getWindow().getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }

        setStatusBarMode(activity.getWindow(), statusBarMode);
    }

    /**
     * 设置透明状态栏，6.0以上为全透明，6.0以下5.0及以上为半透明，小米魅族5.0及以上为全透
     */
    @SuppressLint("InlinedApi")
    private static boolean setTransparentStatusBar(Window window) {
        if (window == null) {
            return false;
        }

        setStatusBarColor(window, Color.TRANSPARENT);

        window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        return true;
    }

    static boolean setStatusBarColor(Window window, @ColorInt int bgColor) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(bgColor);
        return true;
    }

    /**
     * 设置状态栏模式，状态栏图标和文字将变为深色/浅色
     */
    static boolean setStatusBarMode(Window window, int statusBarMode) {
        if (window == null) {
            return false;
        }
        return setAboveMashMallowStatusBarMode(window, statusBarMode == MODE_LIGHT);
    }

    private static boolean setAboveMashMallowStatusBarMode(Window window, boolean darkIcon) {
        Log.d("WLTest", "setAboveMashMallowStatusBarMode");
        View decor = window.getDecorView();
        if (darkIcon) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.d("WLTest", "setAboveMashMallowStatusBarMode darkIcon");
                decor.setSystemUiVisibility(decor.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.d("WLTest", "setAboveMashMallowStatusBarMode lightIcon");
                decor.setSystemUiVisibility(decor.getSystemUiVisibility() & View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
        return true;
    }


}

