package com.ruixue.sdk.demo.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.ruixue.sdk.demo.main.R;
import com.ruixue.sdk.demo.main.databinding.ItemSettingBinding;

// Created by wangliang on 2024/5/20.
public class SettingItemView extends LinearLayout {

    private final ItemSettingBinding binding = ItemSettingBinding.inflate(LayoutInflater.from(getContext()), this);

    public SettingItemView(Context context) {
        this(context, null);
    }

    public SettingItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void init() {
        setOrientation(LinearLayout.HORIZONTAL);
        setBackground(getContext().getDrawable(R.drawable.common_white_card_bg));
    }

    public void render(int iconResId, int titleResId) {
        binding.settingItemIv.setImageResource(iconResId);
        binding.settingItemTitleTv.setText(titleResId);
    }

    public void render(int iconResId, String title) {
        binding.settingItemIv.setImageResource(iconResId);
        binding.settingItemTitleTv.setText(title);
    }

    public boolean isSelectedItem() {
        return binding.settingItemSelectCb.isChecked();
    }



}
