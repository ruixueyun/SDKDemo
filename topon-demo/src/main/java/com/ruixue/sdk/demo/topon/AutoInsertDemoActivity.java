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
import com.anythink.interstitial.api.ATInterstitialAutoEventListener;
import com.anythink.interstitial.api.ATInterstitialAutoLoadListener;
import com.ruixue.RuiXueSdk;
import com.ruixue.net.ToastUtils;
import com.ruixue.sdk.demo.topon.databinding.ActivityToponAutoinsertBinding;
import com.ruixue.topon.adtype.RxATInterstitial;
import com.ruixue.topon.adtype.RxATInterstitialAutoAd;


public class AutoInsertDemoActivity extends AppCompatActivity {
    private static String TAG = "AutoInsertDemoActivity";

    private ActivityToponAutoinsertBinding binding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityToponAutoinsertBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        setStatusBarColor(getWindow(), getResources().getColor(android.R.color.white));
        setStatusBarPaddingViewHeight(binding.statusPaddingView);
        binding.backBtn.setOnClickListener(v -> finish());

        RxATInterstitialAutoAd.rxInit(AutoInsertDemoActivity.this,
                new String[]{"b62b41f080cfc5"},
                new ATInterstitialAutoLoadListener() {
                    @Override
                    public void onInterstitialAutoLoaded(String s) {
                        ToastUtils.showLongToast(AutoInsertDemoActivity.this, "广告加载成功");
                        Log.d(TAG, "onInterstitialAutoLoaded");
                    }

                    @Override
                    public void onInterstitialAutoLoadFail(String s, AdError adError) {
                        Log.d(TAG, "onInterstitialAutoLoadFail");
                    }
                });

        binding.showAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxATInterstitial.rxEntryAdScenario("b62b41f080cfc5", "");
                //需判断广告位是否准备好
                if(RxATInterstitialAutoAd.rxIsAdReady("b62b41f080cfc5")){
                    RxATInterstitialAutoAd.show(
                            AutoInsertDemoActivity.this,
                            "b62b41f080cfc5",
                            "",
                            new ATInterstitialAutoEventListener() {
                                @Override
                                public void onInterstitialAdClicked(ATAdInfo atAdInfo) {
                                    Log.d(TAG, "onInterstitialAdClicked");
                                }

                                @Override
                                public void onInterstitialAdShow(ATAdInfo atAdInfo) {
                                    Log.d(TAG, "onInterstitialAdShow");
                                }

                                @Override
                                public void onInterstitialAdClose(ATAdInfo atAdInfo) {
                                    Log.d(TAG, "onInterstitialAdClose");
                                }

                                @Override
                                public void onInterstitialAdVideoStart(ATAdInfo atAdInfo) {
                                    Log.d(TAG, "onInterstitialAdVideoStart");
                                }

                                @Override
                                public void onInterstitialAdVideoEnd(ATAdInfo atAdInfo) {
                                    Log.d(TAG, "onInterstitialAdVideoEnd");
                                }

                                @Override
                                public void onInterstitialAdVideoError(AdError adError) {
                                    Log.d(TAG, "onInterstitialAdVideoError");
                                }
                            }
                    );
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
