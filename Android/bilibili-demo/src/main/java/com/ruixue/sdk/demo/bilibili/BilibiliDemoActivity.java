package com.ruixue.sdk.demo.bilibili;

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
import com.ruixue.sdk.demo.bilibili.databinding.ActivityBilibiliDemoBinding;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 该类为 Bilibili 渠道 Demo 参考代码
// 相关配置需要配置成使用方自己的，可参考文档 https://doc.ruixuecloud.com/main/dev_doc/tripartite/bilibili/clientAccess.html
// 由于配置的原因当前项目可能有些功能跑不通，参考文档更换成自己的配置再运行
//
// Created by wangliang on 2024/5/23.
public class BilibiliDemoActivity extends BaseActivity {
    private static String TAG = "BaiduDemoActivity";

    private ActivityBilibiliDemoBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBilibiliDemoBinding.inflate(LayoutInflater.from(this));
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

        // 这里使用方填写自己的 appid 和 appkey 再测试
        params.put("appid", "122323232");
        params.put("appkey", "fdfdafdfasdfdfdfdfdf");
        params.put("service_id", "2121212");
        params.put("server_name", "test");
        params.put("merchant_id", "11221");
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
            map.put("method", LoginMethod.BILIBILI);
            RuiXueSdk.getRXSdkApi().login(this, map, new RXJSONCallback() {
                @Override
                public void onSuccess(@Nullable JSONObject data) {
                    if (data != null) {
                        RXLogger.i("登录成功结果: " + data.toString());
                        Toast.makeText(BilibiliDemoActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailed(@NonNull JSONObject cause) {
                    RXLogger.i("登录失败结果:" + cause.toString());
                    Toast.makeText(BilibiliDemoActivity.this, "登录失败:" + cause.toString(), Toast.LENGTH_LONG).show();

                }
            });
        });

        // 支付
        binding.pay.setOnClickListener(v -> {
            Map<String, Object> hashMap = new HashMap<>();
            Map<String, Object> extMap = new HashMap<>();
            extMap.put("game_money", 100);
            extMap.put("bili_uid", "22112");
            extMap.put("bili_role", "");
            extMap.put("bili_server_id", "");
            extMap.put("bili_username", "");
            hashMap.put("ext", extMap);
            hashMap.put("goods_tag", "bytest");
            hashMap.put("pay_type", PayType.BILIBILI);
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
