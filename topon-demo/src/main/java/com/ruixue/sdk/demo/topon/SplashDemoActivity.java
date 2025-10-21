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
import com.anythink.splashad.api.ATSplashSkipAdListener;
import com.ruixue.RuiXueSdk;
import com.ruixue.net.ToastUtils;
import com.ruixue.sdk.demo.topon.databinding.ActivitySplashBinding;
import com.ruixue.topon.adtype.RxATSplashAd;
import com.ruixue.topon.bean.RxATSplashAdExtraInfo;
import com.ruixue.topon.bean.RxATSplashSkipInfo;
import com.ruixue.topon.listener.RxATSplashAdListener;


public class SplashDemoActivity extends AppCompatActivity {
    private static String TAG = "SplashDemoActivity";

    private ActivitySplashBinding binding;

    RxATSplashAd splashAd;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        setStatusBarColor(getWindow(), getResources().getColor(android.R.color.white));
        setStatusBarPaddingViewHeight(binding.statusPaddingView);
        binding.backBtn.setOnClickListener(v -> finish());

        load();

        binding.showAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (splashAd.isAdReady()) {
                    //container大小至少占屏幕75%
                    splashAd.rxShow(SplashDemoActivity.this, binding.splashAdContainer);
                }else{
                    Log.d(TAG, "没有 ready");
                    splashAd.loadAd();
                }
            }
        });
    }

    public void load() {
        RxATSplashAdListener listener = new RxATSplashAdListener() {
            @Override
            public void onAdLoaded(boolean isTimeout) {
                Log.d(TAG, "onAdLoaded");
                ToastUtils.showLongToast(SplashDemoActivity.this, "广告加载成功");
            }

            @Override
            public void onAdLoadTimeout() {
                Log.d(TAG, "onAdLoadTimeout");
            }

            @Override
            public void onNoAdError(AdError var1) {
                Log.d(TAG, "onNoAdError");
            }

            @Override
            public void onAdShow(ATAdInfo var1) {
                Log.d(TAG, "onAdShow");
            }

            @Override
            public void onAdClick(ATAdInfo var1) {
                Log.d(TAG, "onAdClick");
            }

            @Override
            public void onAdDismiss(ATAdInfo var1, RxATSplashAdExtraInfo rxATSplashAdExtraInfo) {
                Log.d(TAG, "onAdDismiss");
            }
        };

        String defaultConfig = "";

        //设置首次开屏广告广告源，请从TopOn后台兜底开屏广告源导出配置
        // defaultConfig = "{\"unit_id\":1442678,\"nw_firm_id\":15,\"adapter_class\":\"com.anythink.network.toutiao.TTATSplashAdapter\",\"content\":\"{\\\"button_type\\\":\\\"0\\\",\\\"dl_type\\\":\\\"0\\\",\\\"slot_id\\\":\\\"100011\\\",\\\"personalized_template\\\":\\\"0\\\",\\\"zoomoutad_sw\\\":\\\"1\\\",\\\"app_id\\\":\\\"5001121\\\"}\"}";

        defaultConfig = "{\"unit_id\":1333176,\"nw_firm_id\":8,\"adapter_class\":\"com.anythink.network.gdt.GDTATSplashAdapter\",\"content\":\"{\\\"unit_id\\\":\\\"8863364436303842593\\\",\\\"zoomoutad_sw\\\":\\\"1\\\",\\\"app_id\\\":\\\"1101152570\\\"}\"}";

        splashAd = new RxATSplashAd(this, "b62b41521d52d1", listener,
                5000, defaultConfig);

        splashAd.loadAd();

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
