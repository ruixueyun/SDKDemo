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
import com.anythink.interstitial.api.ATInterstitialListener;
import com.ruixue.RuiXueSdk;
import com.ruixue.net.ToastUtils;
import com.ruixue.sdk.demo.topon.databinding.ActivityToponManulinsertBinding;
import com.ruixue.topon.adtype.RxATInterstitial;


public class ManulInsertDemoActivity extends AppCompatActivity {
    private static String TAG = "ManulInsertDemoActivity";

    private ActivityToponManulinsertBinding binding;

    private RxATInterstitial mInterstitialAd;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityToponManulinsertBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        setStatusBarColor(getWindow(), getResources().getColor(android.R.color.white));
        setStatusBarPaddingViewHeight(binding.statusPaddingView);
        binding.backBtn.setOnClickListener(v -> finish());

        interLoadAd();

        binding.showAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InnerShowAd();
            }
        });
    }

    private void interLoadAd() {
        if (mInterstitialAd == null) {
            mInterstitialAd = new RxATInterstitial(this, "b62b41f080cfc5");
            mInterstitialAd.setAdListener(new ATInterstitialListener() {
                @Override
                public void onInterstitialAdLoaded() {

                    ToastUtils.showLongToast(ManulInsertDemoActivity.this, "广告加载成功");


                    Log.d(TAG, "onInterstitialAdLoaded : 我正在运行。。。");

                }

                @Override
                public void onInterstitialAdLoadFail(AdError adError) {
                    //注意：禁止在此回调中执行广告的加载方法进行重试，否则会引起很多无用请求且可能会导致应用卡顿
                    //AdError，请参考 https://docs.toponad.com/#/zh-cn/android/android_doc/android_test?id=aderror
                    Log.e(TAG, "onInterstitialAdLoadFail:" + adError.getFullErrorInfo());
                }

                @Override
                public void onInterstitialAdClicked(ATAdInfo atAdInfo) {
                    Log.d(TAG, "onInterstitialAdClicked : 我正在运行。。。");
                }

                @Override
                public void onInterstitialAdShow(ATAdInfo atAdInfo) {
                    //ATAdInfo可区分广告平台以及获取广告平台的广告位ID等
                    //请参考 https://docs.toponad.com/#/zh-cn/android/android_doc/android_sdk_callback_access?id=callback_info
                    //建议在此回调中调用load进行广告的加载，方便下一次广告的展示（不需要调用isAdReady()）
                    mInterstitialAd.load();
                    Log.d(TAG, "onInterstitialAdShow : 我正在运行。。。");
                }

                @Override
                public void onInterstitialAdClose(ATAdInfo atAdInfo) {
                    Log.d(TAG, "onInterstitialAdClose : 我正在运行。。。");
                }

                @Override
                public void onInterstitialAdVideoStart(ATAdInfo atAdInfo) {
                    Log.d(TAG, "onInterstitialAdVideoStart : 我正在运行。。。");
                }

                @Override
                public void onInterstitialAdVideoEnd(ATAdInfo atAdInfo) {
                    Log.d(TAG, "onInterstitialAdVideoEnd : 我正在运行。。。");
                }

                @Override
                public void onInterstitialAdVideoError(AdError adError) {
                    //AdError，请参考 https://docs.toponad.com/#/zh-cn/android/android_doc/android_test?id=aderror
                    Log.e(TAG, "onInterstitialAdVideoError:" + adError.getFullErrorInfo());
                }
            });
            mInterstitialAd.load();
        }
    }

    private void InnerShowAd() {
   /*
     为了统计场景到达率，相关信息可查阅 "https://docs.toponad.com/#/zh-cn/android/NetworkAccess/scenario/scenario"
     在满足广告触发条件时调用“进入广告场景”方法，比如：
     ** 广告场景是在清理结束后弹出广告，则在清理结束时调用；
     * 1、先调用 "entryAdScenario"
     * 2、在调用 "isAdReady" 是否可展示
     * 3、最后调用 "show" 展示
     */
        RxATInterstitial.entryAdScenario("b62b41f080cfc5", "");
        if (mInterstitialAd.isAdReady()) {
            mInterstitialAd.show(this,"");
        }
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
