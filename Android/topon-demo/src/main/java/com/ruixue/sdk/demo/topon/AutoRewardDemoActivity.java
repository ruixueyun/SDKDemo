package com.ruixue.sdk.demo.topon;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.anythink.core.api.ATAdInfo;
import com.anythink.core.api.AdError;
import com.anythink.rewardvideo.api.ATRewardVideoAutoEventListener;
import com.anythink.rewardvideo.api.ATRewardVideoAutoLoadListener;
import com.ruixue.RuiXueSdk;
import com.ruixue.net.ToastUtils;
import com.ruixue.sdk.demo.topon.databinding.ActivityToponAutorewardBinding;
import com.ruixue.topon.adtype.RxATRewardVideoAd;
import com.ruixue.topon.adtype.RxATRewardVideoAutoAd;


public class AutoRewardDemoActivity extends AppCompatActivity {
    private static String TAG = "AutoRewardDemoActivity";

    private ActivityToponAutorewardBinding binding;

    RxATRewardVideoAd rxATRewardVideoAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityToponAutorewardBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        setStatusBarColor(getWindow(), getResources().getColor(android.R.color.white));
        setStatusBarPaddingViewHeight(binding.statusPaddingView);
        binding.backBtn.setOnClickListener(v -> finish());

        RxATRewardVideoAutoAd.rxInit(
                AutoRewardDemoActivity.this,
                new String[]{"b62b420ba3c661"},
                new ATRewardVideoAutoLoadListener() {
                    @Override
                    public void onRewardVideoAutoLoaded(String s) {
                        ToastUtils.showLongToast(AutoRewardDemoActivity.this, "广告加载成功");
                        Log.d(TAG, "onRewardVideoAutoLoaded:" + s);
                    }

                    @Override
                    public void onRewardVideoAutoLoadFail(String s, AdError adError) {
                        Log.d(TAG, "onRewardVideoAutoLoadFail: " + s);
                    }
                }
        );


        binding.showAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxATRewardVideoAd.rxEntryAdScenario("b62b420ba3c661", null);
                //需判断广告位是否准备好
                if(RxATRewardVideoAutoAd.rxIsAdReady("b62b420ba3c661")){
                    RxATRewardVideoAutoAd.rxShow(AutoRewardDemoActivity.this, "b62b420ba3c661",
                            "", new ATRewardVideoAutoEventListener() {
                                @Override
                                public void onRewardedVideoAdPlayStart(ATAdInfo atAdInfo) {
                                    Log.d(TAG, "onRewardedVideoAdPlayStart");
                                }

                                @Override
                                public void onRewardedVideoAdPlayEnd(ATAdInfo atAdInfo) {
                                    Log.d(TAG, "onRewardedVideoAdPlayEnd");
                                }

                                @Override
                                public void onRewardedVideoAdPlayFailed(AdError adError, ATAdInfo atAdInfo) {
                                    Log.d(TAG, "onRewardedVideoAdPlayFailed");
                                }

                                @Override
                                public void onRewardedVideoAdClosed(ATAdInfo atAdInfo) {
                                    Log.d(TAG, "onRewardedVideoAdClosed");
                                }

                                @Override
                                public void onRewardedVideoAdPlayClicked(ATAdInfo atAdInfo) {
                                    Log.d(TAG, "onRewardedVideoAdPlayClicked");
                                }

                                @Override
                                public void onReward(ATAdInfo atAdInfo) {
                                    Log.d(TAG, "onReward");
                                }
                            });
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        RuiXueSdk.onActivityResult(this, requestCode, resultCode, data);
    }


    private void setStatusBarColor(Window window, @ColorInt int bgColor) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(bgColor);
    }

    /**
     * 设置状态栏占位View高度，如果是不支持透明状态栏的手机，则高度为0
     */
    private void setStatusBarPaddingViewHeight(View statusPaddingView) {
        ViewGroup.LayoutParams params = statusPaddingView.getLayoutParams();
        params.height = getStatusBarHeight();
        statusPaddingView.setLayoutParams(params);
    }

    private int getStatusBarHeight() {
        int result = 72; // 1920x1280
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
