package com.ruixue.sdk.overseas.demo;

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
import com.ruixue.sdk.demo.helper.GlobalSettingHelper;
//import com.ruixue.sdk.facebook.FacebookSdkWrapper;
import com.ruixue.share.PlatformType;
import com.ruixue.share.ShareMediaType;
import com.ruixue.share.ShareObject;
import com.ruixue.share.ShareScene;
import com.ruixue.utils.JSONUtil;
//import com.weile.bygame.databinding.ActivityOverseasShareTestBinding;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangliang on 2024/8/6
 */
public class OverseasShareTestActivity extends BaseActivity {
    private static final String TAG = OverseasShareTestActivity.class.getSimpleName();

//    private ActivityOverseasShareTestBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        binding = ActivityOverseasShareTestBinding.inflate(LayoutInflater.from(this));
//        setContentView(binding.getRoot());

        RuiXueSdk.trackingLifecycle(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE_PERMISSION);
            } else {
                Toast.makeText(this, "缺少文件读写权限，可能会造成无法分享文件", Toast.LENGTH_SHORT).show();
            }
        }
//
//        binding.backBtn.setOnClickListener(v -> finish());
//        binding.overseasShare.setVisibility(GlobalSettingHelper.getInstance().isOverseas() ? View.VISIBLE : View.GONE);
//        binding.systemShareImageBtn.setOnClickListener(v -> doShareSystemImage());
//        binding.systemShareTextBtn.setOnClickListener(v -> doShareSystemText());
//
//        binding.facebookShareImageBtn.setOnClickListener(v -> doShareFacebookImage());
//        binding.facebookShareLinkBtn.setOnClickListener(v -> doShareFacebookLink());
//        binding.lineShareLinkBtn.setOnClickListener(v -> doShareLineLink());
//        binding.zaloShareLinkBtn.setOnClickListener(v -> doShareZaloLink());
//        binding.tiktokShareSingleImageBtn.setOnClickListener(v -> doShareTiktokSingleImage());
//        binding.tiktokShareMoreImageBtn.setOnClickListener(v -> doShareTiktokMoreImage());
//        binding.snapchatShareImageBtn.setOnClickListener(v -> doShareSnapchatImage());
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

    private void doShareFacebookImage() {
        doShare("facebook", "sdk_chengjiu", -1);
    }

    private void doShareFacebookLink() {
        // ShareScene
//        doShare("facebook", "sunurl");

//        FacebookSdkWrapper.getInstance().shareLink(this, "http://cn-api-test.ruixuecloud.com/v1/operationapi/url/landingtest/1223", new RXJSONCallback() {
//            @Override
//            public void onSuccess(@Nullable JSONObject jsonObject) {
//                Log.d("WLTest", "Facebook 分享成功");
//            }
//
//            @Override
//            public void onFailed(@NonNull JSONObject jsonObject) {
//                Log.d("WLTest", "Facebook 分享失败");
//            }
//        });
    }

    private void doShareLineLink() {
        doShare("line", "sunurl", -1);
    }



    private void doShareZaloLink() {
        Map<String, Object> shareParams = composeCommonShareParams();
        shareParams.put("platform", "zalo");
        shareParams.put("func", "active_invitefri_zalo");
        shareParams.put("shareScene", ShareScene.TIMELINE);

        RuiXueSdk.getRXSdkApi().share(this, shareParams, new RXJSONCallback() {
            @Override
            public void onSuccess(@Nullable JSONObject jsonObject) {
                // Zalo 分享成功后如果想弹出 Toast，需要延迟一下，否则弹不出来，回调正常好用
                Log.d("WLTest", "Zalo 分享成功");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        OverseasShareTestActivity.this.runOnUiThread(() -> ToastUtils.showToast(OverseasShareTestActivity.this, "分享成功"));
                    }
                }, 300);
            }

            @Override
            public void onFailed(@NonNull JSONObject jsonObject) {
                Log.d("WLTest", "Zalo 分享失败");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        OverseasShareTestActivity.this.runOnUiThread(() -> ToastUtils.showToast(OverseasShareTestActivity.this, "分享失败"));
                    }
                }, 300);
            }
        });
    }

    // tiktok 测试目前都用捕鱼的参数，cp方用自己的参数进行测试
    private void doShareTiktokSingleImage() {
        Map<String, Object> shareParams = composeCommonShareParams();
        shareParams.put("platform", "tiktok");
        shareParams.put("func", "sdk_chengjiu");
        RuiXueSdk.getRXSdkApi().share(this, shareParams, new MyShareCallback());
    }

    private void doShareTiktokMoreImage() {
        Map<String, Object> shareParams = composeCommonShareParams();
        shareParams.put("platform", "tiktok");
        shareParams.put("func", "sdk_chengjius");
        RuiXueSdk.getRXSdkApi().share(this, shareParams, new MyShareCallback());
    }

    // 这个得用自己的包名及其参数测试
    private void doShareSnapchatImage() {
        Map<String, Object> shareParams = composeCommonShareParams();
        shareParams.put("platform", "snapchat");
        shareParams.put("func", "sdk_chengjiu");
        RuiXueSdk.getRXSdkApi().share(this, shareParams, new MyShareCallback());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        RuiXueSdk.onActivityResult(this, requestCode, resultCode, data);
    }

    private class MyShareCallback extends RXJSONCallback {
        @Override
        public void onSuccess(@Nullable JSONObject data) {
            Log.d("WLTest", "share success");

            // 如果是 zalo
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    OverseasShareTestActivity.this.runOnUiThread(() -> ToastUtils.showToast(OverseasShareTestActivity.this, "分享成功"));
                }
            }, 300);
        }

        @Override
        public void onFailed(@NonNull JSONObject cause) {
            Log.e(TAG, "share failed:" + cause);
            OverseasShareTestActivity.this.runOnUiThread(() -> ToastUtils.showToast(OverseasShareTestActivity.this, "分享失败"));
        }
    }
}
