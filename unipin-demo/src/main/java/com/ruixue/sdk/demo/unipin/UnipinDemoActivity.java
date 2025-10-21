package com.ruixue.sdk.demo.unipin;

import android.content.Intent;
import android.os.Bundle;
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

import com.ruixue.RXJSONCallback;
import com.ruixue.RXRequestCallback;
import com.ruixue.RuiXueSdk;
 import com.ruixue.leagl.PrivacyCallback;
import com.ruixue.logger.RXLogger;
import com.ruixue.passport.LoginMethod;
import com.ruixue.sdk.demo.activity.BaseActivity;
import com.ruixue.sdk.demo.activity.PayType;
import com.ruixue.sdk.demo.unipin.databinding.ActivityUnipinDemoBinding;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UnipinDemoActivity extends BaseActivity {
    private static String TAG = "BaiduDemoActivity";

    private ActivityUnipinDemoBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUnipinDemoBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        binding.backBtn.setOnClickListener(v -> finish());

        // 注册生命周期监听
        RuiXueSdk.trackingLifecycle(this);

        // 同意隐私协议
        RuiXueSdk.setPrivacyAgree(new PrivacyCallback() {
            @Override
            public void onPrivacyAgree(boolean b) {

            }
        });

        String cpid = "119";
        String productId = "SDKOS";
        String channelId = "AndroidOS";
        List<String> hostUrls = new ArrayList<>();
        hostUrls.add("https://os-api-test.ruixuecloud.com/");
        RuiXueSdk.initialize(cpid, productId, channelId, hostUrls, new RXJSONCallback() {
            @Override
            public void onSuccess(@Nullable JSONObject jsonObject) {
                Log.d(TAG, "RXSDK 初始化成功");
            }

            @Override
            public void onFailed(@NonNull JSONObject jsonObject) {
                Log.d(TAG, "RXSDK 初始化失败 - " + jsonObject);
            }
        });
        RuiXueSdk.getRXSdkApi().initThirdSdk(this, new HashMap<>(), new RXJSONCallback() {
            @Override
            public void onSuccess(@Nullable JSONObject jsonObject) {
                Log.d(TAG, "init third sdk success");
            }

            @Override
            public void onFailed(@NonNull JSONObject jsonObject) {
                Log.d(TAG, "init third sdk failed " + jsonObject);
            }
        });

        binding.login.setOnClickListener(v -> {
            Map<String, Object> map = new HashMap<>();
            map.put("method", LoginMethod.GUEST);
            RuiXueSdk.getRXSdkApi().login(this, map, new RXJSONCallback() {
                @Override
                public void onSuccess(@Nullable JSONObject data) {
                    if (data != null) {
                        RXLogger.i("登录成功结果: " + data.toString());
                        Toast.makeText(UnipinDemoActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailed(@NonNull JSONObject cause) {
                    RXLogger.i("登录失败结果:" + cause.toString());
                    Toast.makeText(UnipinDemoActivity.this, "登录失败:" + cause.toString(), Toast.LENGTH_LONG).show();

                }
            });
        });

        // 支付
        binding.pay.setOnClickListener(v -> {
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("goods_tag", "unipin");
            hashMap.put("pay_type", PayType.UNIPIN);
            hashMap.put("trade_no", String.valueOf(System.currentTimeMillis()));
            RuiXueSdk.getRXSdkApi().pay(this, hashMap, new RXRequestCallback() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    Log.d(TAG, "Unipin 支付：" + jsonObject.toString());
                }
            });
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        RuiXueSdk.onActivityResult(this, requestCode, resultCode, data);
    }

}
