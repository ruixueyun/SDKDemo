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

import com.ruixue.sdk.demo.main.R;
import com.ruixue.sdk.demo.main.databinding.ActivityPayTypeSelectBinding;

public class PayTypeSelectActivity extends BaseActivity {

    private ActivityPayTypeSelectBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPayTypeSelectBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        binding.fixType.setOnClickListener(v ->
                startActivity(new Intent(PayTypeSelectActivity.this, PayTestActivity.class)));

        binding.pointType.setOnClickListener(v ->
                startActivity(new Intent(PayTypeSelectActivity.this, PointPayTestActivity.class)));

        binding.backBtn.setOnClickListener(v -> finish());

    }

}