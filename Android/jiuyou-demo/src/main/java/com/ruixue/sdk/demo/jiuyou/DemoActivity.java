package com.ruixue.sdk.demo.jiuyou;

import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import com.ruixue.RuiXueSdk;
import com.ruixue.sdk.demo.activity.PayType;
import com.ruixue.leagl.PrivacyCallback;
import com.ruixue.logger.RXLogger;
import com.ruixue.passport.LoginMethod;
import com.ruixue.sdk.demo.activity.BaseActivity;
import com.ruixue.sdk.demo.jiuyou.databinding.ActivityDemoBinding;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 该类为九游渠道 Demo 参考代码
// 相关配置需要配置成使用方自己的，可参考文档 https://doc.ruixuecloud.com/main/dev_doc/tripartite/ucgame/android.html
// 由于配置的原因当前项目可能有些功能跑不通，参考文档更换成自己的配置再运行
//
// Created by wangliang on 2024/5/23.
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
        String channelId = "android";
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
        params.put("screen_orientation", ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        params.put("had_req_permission", false);
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
            map.put("method", LoginMethod.M9GAME);
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
            hashMap.put("pay_type", PayType.M9GAME);
            hashMap.put("goods_tag", "test1");
            hashMap.put("env", 1);
            hashMap.put("age", 18);
            RuiXueSdk.getRXSdkApi().pay(this, hashMap,  new RXJSONCallback() {
                @Override
                public void onSuccess(@Nullable JSONObject data) {
                    Toast.makeText(DemoActivity.this, "支付订单生成成功，客户端无支付成功回调，订单成功已服务端回调为准", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailed(@NonNull JSONObject cause) {
                    Toast.makeText(DemoActivity.this, "支付失败 " + cause, Toast.LENGTH_SHORT).show();
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
