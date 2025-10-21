package com.ruixue.sdk.demo.topon;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.anythink.banner.api.ATBannerExListener;
import com.anythink.core.api.ATAdInfo;
import com.anythink.core.api.ATNetworkConfirmInfo;
import com.anythink.core.api.AdError;
import com.ruixue.RuiXueSdk;
import com.ruixue.net.ToastUtils;
import com.ruixue.sdk.demo.topon.databinding.ActivityBannerBinding;
import com.ruixue.topon.adtype.RxATBannerView;


public class BannerDemoActivity extends AppCompatActivity {
    private static String TAG = "BannerDemoActivity";

    private ActivityBannerBinding binding;

    RxATBannerView mBannerView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBannerBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        setStatusBarColor(getWindow(), getResources().getColor(android.R.color.white));
        setStatusBarPaddingViewHeight(binding.statusPaddingView);
        binding.backBtn.setOnClickListener(v -> finish());

        initBannerView();
        addBannerViewToContainer();

        binding.showAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAd();
            }
        });
    }

    private void initBannerView() {
        mBannerView = new RxATBannerView(this);
        mBannerView.setPlacementId("b62b420ae05bb4");
        //Loading and displaying ads should keep the container and BannerView visible all the time
        mBannerView.setVisibility(View.VISIBLE);
        mBannerView.setBannerAdListener(new ATBannerExListener() {

            @Override
            public void onDeeplinkCallback(boolean isRefresh, ATAdInfo adInfo, boolean isSuccess) {
                Log.d(TAG, "onDeeplinkCallback:" + adInfo.toString() + "--status:" + isSuccess);
            }

            @Override
            public void onDownloadConfirm(Context context, ATAdInfo adInfo, ATNetworkConfirmInfo networkConfirmInfo) {
                Log.d(TAG, "onDownloadConfirm:" + adInfo.toString() + " networkConfirmInfo:" + networkConfirmInfo);
            }

            @Override
            public void onBannerLoaded() {
                Log.d(TAG, "onBannerLoaded");
                ToastUtils.showLongToast(BannerDemoActivity.this, "广告加载成功");

            }

            @Override
            public void onBannerFailed(AdError adError) {
                Log.d(TAG, "onBannerFailed: " + adError.getFullErrorInfo());
            }

            @Override
            public void onBannerClicked(ATAdInfo entity) {
                Log.d(TAG, "onBannerClicked:" + entity.toString());
            }

            @Override
            public void onBannerShow(ATAdInfo entity) {
                Log.d(TAG, "onBannerShow:" + entity.toString());
            }

            @Override
            public void onBannerClose(ATAdInfo entity) {
                mBannerView.setVisibility(View.GONE);
                Log.d(TAG, "onBannerClose:" + entity.toString());
            }

            @Override
            public void onBannerAutoRefreshed(ATAdInfo entity) {
                Log.d(TAG, "onBannerAutoRefreshed:" + entity.toString());
            }

            @Override
            public void onBannerAutoRefreshFail(AdError adError) {
                Log.d(TAG, "onBannerAutoRefreshFail: " + adError.getFullErrorInfo());
            }
        });

    }

    private void addBannerViewToContainer() {
        if (mBannerView != null) {
            binding.adviewContainer.addView(mBannerView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,  binding.adviewContainer.getLayoutParams().height));
        }
    }

    private void loadAd() {
        //Loading and displaying ads should keep the container and BannerView visible all the time
        mBannerView.setVisibility(View.VISIBLE);
        binding.adviewContainer.setVisibility(View.VISIBLE);

        mBannerView.loadAd();
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
