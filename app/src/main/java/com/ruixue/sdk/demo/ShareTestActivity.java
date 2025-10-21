package com.ruixue.sdk.demo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ruixue.RXJSONCallback;
import com.ruixue.RuiXueSdk;
import com.ruixue.logger.RXLogger;
import com.ruixue.net.ToastUtils;
import com.ruixue.openapi.RXSdkApi;
import com.ruixue.sdk.demo.activity.BaseActivity;
import com.ruixue.sdk.demo.databinding.ActivityShareTestBinding;
import com.ruixue.sdk.demo.helper.GlobalSettingHelper;
import com.ruixue.sdk.demo.main.R;
import com.ruixue.share.PlatformType;
import com.ruixue.share.ShareMediaType;
import com.ruixue.share.ShareObject;
import com.ruixue.share.ShareScene;
import com.ruixue.utils.JSONUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

// Note: 该类中是聚合了各种分享，但由于环境参数配置原因并不能保证流程能跑通
//       使用方需要根据自己的参数配置好才能跑通
//       该类只做 Sample，方便使用方参考

// Created by wangliang on 2024/5/22.
public class ShareTestActivity extends BaseActivity {
    private static final String TAG = "ShareTestActivity";

    private ActivityShareTestBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShareTestBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        RuiXueSdk.trackingLifecycle(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE_PERMISSION);
            } else {
                Toast.makeText(this, "缺少文件读写权限，可能会造成无法分享文件", Toast.LENGTH_SHORT).show();
            }
        }

        binding.backBtn.setOnClickListener(v -> finish());
        binding.internalShare.setVisibility(GlobalSettingHelper.getInstance().isOverseas() ? View.GONE : View.VISIBLE);
        binding.systemShareImageBtn.setOnClickListener(v -> doShareSystemImage());
        binding.systemShareTextBtn.setOnClickListener(v -> doShareSystemText());
        binding.wxShareImageTimelineBtn.setOnClickListener(v -> doShareWeixinImage(ShareScene.TIMELINE));
        binding.wxShareLinkTimelineBtn.setOnClickListener(v -> doShareWeixinLink(ShareScene.TIMELINE));
        binding.wxShareImageSessionBtn.setOnClickListener(v -> doShareWeixinImage(ShareScene.SESSION));
        binding.wxShareLinkSessionBtn.setOnClickListener(v -> doShareWeixinLink(ShareScene.SESSION));
    }

    private void doShareSystemImage() {
        String func = GlobalSettingHelper.getInstance().isOverseas() ? "sdk_chengjius" : "111";
        doShare("system", func, ShareScene.SELECT);
    }

    private void doShareSystemText() {
        ShareObject shareObject = new ShareObject();
        shareObject.setPlatform(PlatformType.SYSTEM.getKeyword());
        shareObject.setTitle("分享文本标题");
        shareObject.setDescription("分享文本描述");
        shareObject.setShareScene(ShareScene.SELECT);
        shareObject.setType(ShareMediaType.TEXT);
        String jsonStr = new Gson().toJson(shareObject);
        RXLogger.i(jsonStr);
        Map<String, Object> map1 = JSONUtil.fromJson(jsonStr, new TypeToken<Map<String, Object>>() {
        }.getType());

        RXSdkApi.getInstance().share(this, map1, new MyShareCallback());
    }

    private void doShareWeixinImage(int scene) {
        doShare("wechat", "sdk_chengjiushare", scene);
    }

    private void doShareWeixinLink(int scene) {
        // 缺 weixin appid
        doShare("wechat", "sunshare2", scene);
    }

    private void doShare(String platform, String func, int shareScene) {
        Map<String, Object> shareParams = composeCommonShareParams();
        shareParams.put("platform", platform);
        shareParams.put("func", func);
        shareParams.put("appid", GlobalSettingHelper.WEIXIN_APPID);
        shareParams.put("shareScene", shareScene);
        RuiXueSdk.getRXSdkApi().share(this, shareParams, new MyShareCallback());
    }

    private Map<String, Object> composeCommonShareParams() {
        Map<String, Object> shareParams = new HashMap<>();
        shareParams.put("protocol_android", "jixiang433://");
        shareParams.put("protocol_ios", "jixiang433://");
        shareParams.put("auto_share", true);
        shareParams.put("auto_report", true);
        return shareParams;
    }

    private class MyShareCallback extends RXJSONCallback {
        @Override
        public void onSuccess(@Nullable JSONObject data) {
            Log.d("WLTest", "share success");

            // 如果是 zalo
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ShareTestActivity.this.runOnUiThread(() -> ToastUtils.showToast(ShareTestActivity.this, "分享成功"));
                }
            }, 300);
        }

        @Override
        public void onFailed(@NonNull JSONObject cause) {
            Log.e(TAG, "share failed:" + cause);
            ShareTestActivity.this.runOnUiThread(() -> ToastUtils.showToast(ShareTestActivity.this, "分享失败"));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        RuiXueSdk.onActivityResult(this, requestCode, resultCode, data);
    }

    private static final int REQUEST_WRITE_STORAGE_PERMISSION = 121;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_STORAGE_PERMISSION) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "缺少文件读写权限，可能会造成无法分享文件", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
