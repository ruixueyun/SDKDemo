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

import com.anythink.core.api.ATAdConst;
import com.anythink.core.api.ATAdInfo;
import com.anythink.core.api.AdError;
import com.anythink.rewardvideo.api.ATRewardVideoListener;
import com.ruixue.RuiXueSdk;
import com.ruixue.net.ToastUtils;
import com.ruixue.sdk.demo.topon.databinding.ActivityToponManulrewardBinding;
import com.ruixue.topon.adtype.RxATRewardVideoAd;

import java.util.HashMap;
import java.util.Map;


public class ManulRewardDemoActivity extends AppCompatActivity {
    private static String TAG = "ManulRewardDemoActivity";

    private ActivityToponManulrewardBinding binding;

    RxATRewardVideoAd rxATRewardVideoAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityToponManulrewardBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        setStatusBarColor(getWindow(), getResources().getColor(android.R.color.white));
        setStatusBarPaddingViewHeight(binding.statusPaddingView);
        binding.backBtn.setOnClickListener(v -> finish());

        rewardLoadAd();
        binding.showAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rewardShowAsd();
            }
        });
    }

    private void rewardShowAsd() {
    /*
     为了统计场景到达率，相关信息可查阅 "https://docs.toponad.com/#/zh-cn/android/NetworkAccess/scenario/scenario"
     在满足广告触发条件时调用“进入广告场景”方法，比如：
     ** 广告场景是在清理结束后弹出广告，则在清理结束时调用；
     * 1、先调用 "entryAdScenario"
     * 2、在调用 "isAdReady" 是否可展示
     * 3、最后调用 "show" 展示
     */
        RxATRewardVideoAd.entryAdScenario("b62b420ba3c661", null);
        if (rxATRewardVideoAd.isAdReady()) {
            rxATRewardVideoAd.show(this);
        }
    }

    private void rewardLoadAd() {
        if (rxATRewardVideoAd == null) {
            rxATRewardVideoAd = new RxATRewardVideoAd(this, "b62b420ba3c661");
            rxATRewardVideoAd.setAdListener(new ATRewardVideoListener() {
                @Override
                public void onRewardedVideoAdLoaded() {
                    Log.d(TAG, "onRewardedVideoAdLoaded");
                    ToastUtils.showLongToast(ManulRewardDemoActivity.this, "广告加载成功");
                }
                @Override
                public void onRewardedVideoAdFailed(AdError adError) {
                    //注意：禁止在此回调中执行广告的加载方法进行重试，否则会引起很多无用请求且可能会导致应用卡顿
                    //AdError，请参考 https://docs.toponad.com/#/zh-cn/android/android_doc/android_test?id=aderror
                    Log.e(TAG, "onRewardedVideoAdFailed:" + adError.getFullErrorInfo());
                }
                @Override
                public void onRewardedVideoAdPlayStart(ATAdInfo adInfo) {
                    //ATAdInfo可区分广告平台以及获取广告平台的广告位ID等
                    //请参考 https://docs.toponad.com/#/zh-cn/android/android_doc/android_sdk_callback_access?id=callback_info

                    //建议在此回调中调用load进行广告的加载，方便下一次广告的展示（不需要调用isAdReady()）
                    rxATRewardVideoAd.load();
                    Log.d(TAG, "onRewardedVideoAdPlayStart");
                }
                @Override
                public void onRewardedVideoAdPlayEnd(ATAdInfo atAdInfo) {
                    Log.d(TAG, "onRewardedVideoAdPlayEnd");
                }
                @Override
                public void onRewardedVideoAdPlayFailed(AdError adError, ATAdInfo atAdInfo) {
                    //AdError，请参考 https://docs.toponad.com/#/zh-cn/android/android_doc/android_test?id=aderror
                    Log.d(TAG, "onRewardedVideoAdPlayFailed:" + adError.getFullErrorInfo());
                }
                @Override
                public void onRewardedVideoAdClosed(ATAdInfo atAdInfo) {
                    Log.d(TAG, "onRewardedVideoAdClosed");
                }
                @Override
                public void onReward(ATAdInfo atAdInfo) {
                    //建议在此回调中下发奖励，一般在onRewardedVideoAdClosed之前回调
                    Log.d(TAG, "onReward");
                }
                @Override
                public void onRewardedVideoAdPlayClicked(ATAdInfo atAdInfo) {
                    Log.d(TAG, "onRewardedVideoAdPlayClicked");
                }
            });
        }
        String userid = "test_userid_001";
        String userdata = "test_userdata_001";
        Map<String, Object> localMap = new HashMap<>();
        localMap.put(ATAdConst.KEY.USER_ID, userid);
        localMap.put(ATAdConst.KEY.USER_CUSTOM_DATA, userdata);
        //Load时传自定义参数，在广告源填充之后自定义参数会保存起来，在展示时回传。适用场景:传入会话信息做防作弊类似
        rxATRewardVideoAd.setLocalExtra(localMap);
        rxATRewardVideoAd.load();
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
