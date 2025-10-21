package com.ruixue.sdk.demo.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ruixue.sdk.demo.helper.CommonHelper;
import com.ruixue.sdk.demo.helper.GlobalSettingHelper;
import com.ruixue.sdk.demo.main.R;
import com.ruixue.sdk.demo.main.databinding.ActivityMainBinding;

// Created by wangliang on 2024/5/16.
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        binding.topTitleTv.setText(R.string.main_top_title);
        String versionName = CommonHelper.getVersionName(this);

        binding.topVersionTv.setText("version - " + ((versionName == null) ? "unknown" : versionName));

        binding.chinaAreaBtn.render(R.string.main_area_btn_china_title, R.string.main_area_btn_china_english_title, R.drawable.main_area_china);
        binding.overseasAreaBtn.render(R.string.main_area_btn_overseas_title, R.string.main_area_btn_overseas_english_title, R.drawable.main_area_overseas);
        binding.confirmButton.setEnabled(false);
        binding.chinaAreaBtn.setOnClickListener(v -> {
            GlobalSettingHelper.getInstance().setOverseas(false);
            binding.chinaAreaBtn.setSelected(true);
            binding.overseasAreaBtn.setSelected(false);
            binding.confirmButton.setEnabled(true);
        });
        binding.overseasAreaBtn.setOnClickListener(v -> {
            GlobalSettingHelper.getInstance().setOverseas(true);
            binding.chinaAreaBtn.setSelected(false);
            binding.overseasAreaBtn.setSelected(true);
            binding.confirmButton.setEnabled(true);
        });
    }
}
