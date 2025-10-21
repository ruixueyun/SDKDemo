package com.ruixue.sdk.demo.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.ruixue.sdk.demo.main.databinding.MainBtnAreaSelectBinding;

// Created by wangliang on 2024/5/16.
public class AreaSelectButton extends LinearLayout {

    private final MainBtnAreaSelectBinding binding = MainBtnAreaSelectBinding.inflate(LayoutInflater.from(getContext()), this);

    public AreaSelectButton(Context context) {
        this(context, null);
    }

    public AreaSelectButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AreaSelectButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER);
    }

    public void render(int titleResId, int englishTitleResId, int iconResId) {
        binding.areaTitleTv.setText(titleResId);
        binding.areaTitleEnglishTv.setText(englishTitleResId);
        binding.areaIconIv.setImageResource(iconResId);
    }
}
