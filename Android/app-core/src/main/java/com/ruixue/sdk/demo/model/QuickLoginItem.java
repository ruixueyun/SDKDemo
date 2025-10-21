package com.ruixue.sdk.demo.model;

import com.ruixue.passport.LoginMethod;

// Created by wangliang on 2024/5/20.
public class QuickLoginItem {
    private int iconResId;
    private int titleResId;
    private boolean isChecked;

    private LoginMethod loginMethod;

    public QuickLoginItem() {}

    public QuickLoginItem(LoginMethod method) {
        this.loginMethod = method;
    }

    public QuickLoginItem(String guest) {
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public int getTitleResId() {
        return titleResId;
    }

    public void setTitleResId(int titleResId) {
        this.titleResId = titleResId;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public LoginMethod getLoginMethod() {
        return loginMethod;
    }

    public void setLoginMethod(LoginMethod loginMethod) {
        this.loginMethod = loginMethod;
    }
}
