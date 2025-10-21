package com.ruixue.sdk.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import com.ruixue.sdk.demo.helper.CommonHelper;
import com.ruixue.sdk.demo.helper.GlobalSettingHelper;
import com.ruixue.sdk.demo.main.R;
import com.ruixue.sdk.demo.main.databinding.ActivityShareTypeSelectBinding;

public class ShareTypeSelectActivity extends AppCompatActivity {

    private ActivityShareTypeSelectBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShareTypeSelectBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        binding.fixType.setOnClickListener(v -> {
            if (GlobalSettingHelper.getInstance().isOverseas()) {
                CommonHelper.startActivityByClass(this, "com.ruixue.sdk.overseas.demo.OverseasShareTestActivity", null);
            } else {
                CommonHelper.startActivityByClass(this, "com.ruixue.sdk.demo.ShareTestActivity", null);
            }
        });

        binding.pointType.setOnClickListener(v -> {
            startActivity(new Intent(this, PointShareTestActivity.class));
        });

        binding.backBtn.setOnClickListener(v -> finish());

    }

}