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

import com.ruixue.sdk.demo.helper.GlobalSettingHelper;
import com.ruixue.sdk.demo.main.R;
import com.ruixue.sdk.demo.main.databinding.ActivityPointShareTestBinding;

public class PointShareTestActivity extends BaseActivity {

    private ActivityPointShareTestBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPointShareTestBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        binding.internalShare.setVisibility(GlobalSettingHelper.getInstance().isOverseas() ? View.GONE : View.VISIBLE);
        binding.overseasShare.setVisibility(GlobalSettingHelper.getInstance().isOverseas() ? View.VISIBLE : View.GONE);


        binding.systemShareBtn.setOnClickListener(v -> {
            jumpToPointList("system");
        });

        binding.weixinShareBtn.setOnClickListener(v -> {
            jumpToPointList("wechat");
        });

        binding.facebookShareBtn.setOnClickListener(v -> {
            jumpToPointList("facebook");
        });

        binding.lineShareBtn.setOnClickListener(v-> {
            jumpToPointList("line");
        });

        binding.zaloShareBtn.setOnClickListener(v -> {
            jumpToPointList("zalo");
        });

        binding.tiktokShareBtn.setOnClickListener(v -> {
            jumpToPointList("tiktok");
        });

        binding.snapchatBtn.setOnClickListener(v -> {
            jumpToPointList("snapchat");
        });

        binding.backBtn.setOnClickListener(v -> finish());


    }

    private void jumpToPointList(String platform) {
        Intent intent = new Intent(this, SharePointListActivity.class);
        intent.putExtra("platform", platform);
        startActivity(intent);
    }

}