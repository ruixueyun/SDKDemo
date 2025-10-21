package com.ruixue.sdk.demo.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ruixue.sdk.demo.utils.StatusBarUtils;

/**
 * Created by wangliang on 2024/8/7
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();
    }

    private void initStatusBar() {
        int statusBarColor = transparentStatusBar() ? getResources().getColor(android.R.color.transparent) : getResources().getColor(android.R.color.white);
        StatusBarUtils.initStatusBar(this, transparentStatusBar(), transparentNavigationBar(), statusBarColor, StatusBarUtils.MODE_LIGHT);
    }

    /**
     * 是否要绘制系统状态栏下方区域，默认为false，由基类来添加paddingView并设置与导航栏同样颜色来实现透明状态栏
     */
    boolean transparentStatusBar() {
        return false;
    }

    boolean transparentNavigationBar() {
        return false;
    }
}
