package com.ruixue.sdk.demo.huawei;

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
import com.ruixue.sdk.demo.activity.PayType;
import com.ruixue.leagl.PrivacyCallback;
import com.ruixue.logger.RXLogger;
import com.ruixue.passport.LoginMethod;
import com.ruixue.sdk.demo.activity.BaseActivity;
import com.ruixue.sdk.demo.huawei.databinding.ActivityDemoBinding;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 该类为华为渠道 Demo 参考代码
// 相关配置需要配置成使用方自己的，可参考文档 https://doc.ruixuecloud.com/main/dev_doc/tripartite/huawei/clientAccess.html
// 由于配置的原因当前项目可能有些功能跑不通，参考文档更换成自己的配置再运行
//
// Created by wangliang on 2024/5/24.
public class DemoActivity extends BaseActivity {
    private static String TAG = "DemoActivity";

    private ActivityDemoBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDemoBinding.inflate(LayoutInflater.from(this));
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

        String cpid = "114";
        String productId = "1002";
        String channelId = "100";
        List<String> hostUrls = new ArrayList<>();
        hostUrls.add("http://cn-api-test.ruixuecloud.com/");
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
        Map<String, Object> params = new HashMap<>();

        RuiXueSdk.getRXSdkApi().initThirdSdk(this, params, new RXJSONCallback() {
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
            map.put("method", LoginMethod.HUAWEI);
            RuiXueSdk.getRXSdkApi().login(this, map, new RXJSONCallback() {
                @Override
                public void onSuccess(@Nullable JSONObject data) {
                    if (data != null) {
                        RXLogger.i("登录成功结果: " + data.toString());
                        Toast.makeText(DemoActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailed(@NonNull JSONObject cause) {
                    RXLogger.i("登录失败结果:" + cause.toString());
                    Toast.makeText(DemoActivity.this, "登录失败:" + cause.toString(), Toast.LENGTH_LONG).show();

                }
            });
        });

        // 支付
        binding.pay.setOnClickListener(v -> {
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("goods_tag", "bytest");
            hashMap.put("pay_type", PayType.HWJOS);
            hashMap.put("trade_no", String.valueOf(System.currentTimeMillis()));
            RuiXueSdk.getRXSdkApi().pay(this, hashMap, new RXRequestCallback() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    Log.d(TAG, "支付：" + jsonObject.toString());
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
