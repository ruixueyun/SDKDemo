package com.ruixue.sdk.demo.ysdk;

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
import com.ruixue.openapi.RXSdkApi;
import com.ruixue.sdk.RXYsdkLoginConfig;
import com.ruixue.sdk.demo.activity.BaseActivity;
import com.ruixue.sdk.demo.activity.MainActivity;
import com.ruixue.sdk.demo.ysdk.databinding.ActivityYsdkDemoBinding;
import com.tencent.ysdk.framework.common.ePlatform;
import com.tencent.ysdk.framework.login.YsdkLoginConfig;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 该类为应用宝渠道 Demo 参考代码
// 相关配置需要配置成使用方自己的，可参考文档 https://doc.ruixuecloud.com/main/dev_doc/tripartite/ysdk/clientAccess.html
// 由于配置的原因当前项目可能有些功能跑不通，参考文档更换成自己的配置再运行
//
// Created by wangliang on 2024/5/23.
public class YSdkDemoActivity extends BaseActivity {
    private static String TAG = "YSdkDemoActivity";

    private ActivityYsdkDemoBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityYsdkDemoBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        binding.backBtn.setOnClickListener(v -> finish());

        RuiXueSdk.setDebugEnabled(true);
        // 注册生命周期监听
        RuiXueSdk.trackingLifecycle(this);

        // 同意隐私协议
        RuiXueSdk.setPrivacyAgree(new PrivacyCallback() {
            @Override
            public void onPrivacyAgree(boolean b) {

            }
        });

        String cpid = "1000193";
        String productId = "705";
        String channelId = "210";
        List<String> hostUrls = new ArrayList<>();
        hostUrls.add("https://mdydex.hjhhsq.com/");
        RuiXueSdk.setDebugEnabled(true);
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

        // 游戏币支付
        binding.payMidas.setOnClickListener(v -> {
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("goods_tag", "bytest");
            hashMap.put("pay_type", "midas");
            hashMap.put("type", "midas");
            hashMap.put("trade_no", String.valueOf(System.currentTimeMillis()));
            RXSdkApi.getInstance().pay(this, hashMap, new RXRequestCallback() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    Log.d("ChannelDemoActivity", "米大师支付：" + jsonObject.toString());
                }
            });
        });

        // 直购支付
        binding.payYsdk.setOnClickListener(v -> {
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("goods_tag", "10009114");//ddz
            hashMap.put("env", 0);
            hashMap.put("pay_type", PayType.YSDK);
            hashMap.put("trade_no", String.valueOf(System.currentTimeMillis()));
            RuiXueSdk.getRXSdkApi().pay(this, hashMap, new RXRequestCallback() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    Log.d("ChannelDemoActivity", "米大师支付：" + jsonObject.toString());
                }
            });
        });

        // qq 登录
        binding.loginQq.setOnClickListener(v -> {
            Map<String, Object> map = new HashMap<>();
            map.put("method", "ysdk");
            map.put("platform_type", ePlatform.PLATFORM_ID_QQ);
            RXSdkApi.getInstance().login(this, map, new RXJSONCallback() {
                @Override
                public void onSuccess(@Nullable JSONObject data) {
                    if (data != null) {
                        RXLogger.i("应用宝登录成功结果: " + data.toString());
                        Toast.makeText(YSdkDemoActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailed(@NonNull JSONObject cause) {
                    RXLogger.i("应用宝登录失败结果:" + cause.toString());
                    Toast.makeText(YSdkDemoActivity.this, "登录失败:" + cause.toString(), Toast.LENGTH_LONG).show();

                }
            });
        });

        // 微信登录
        binding.loginWx.setOnClickListener(v -> {
            Map<String, Object> map = new HashMap<>();
            map.put("method", "ysdk");
            map.put("platform_type", ePlatform.PLATFORM_ID_WX);
            RXSdkApi.getInstance().login(this, map, new RXJSONCallback() {
                @Override
                public void onSuccess(@Nullable JSONObject data) {
                    if (data != null) {
                        RXLogger.i("应用宝登录成功结果: " + data.toString());
                        Toast.makeText(YSdkDemoActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailed(@NonNull JSONObject cause) {
                    RXLogger.i("应用宝登录失败结果:" + cause.toString());
                    Toast.makeText(YSdkDemoActivity.this, "登录失败:" + cause.toString(), Toast.LENGTH_LONG).show();

                }
            });
        });

        // 单机登录
        binding.loginGuest.setOnClickListener(v -> {
            Map<String, Object> map = new HashMap<>();
            map.put("method", "ysdk");
            map.put("platform_type", ePlatform.PLATFORM_ID_GUEST);
            RXSdkApi.getInstance().login(this, map, new RXJSONCallback() {
                @Override
                public void onSuccess(@Nullable JSONObject data) {
                    if (data != null) {
                        RXLogger.i("应用宝登录成功结果: " + data.toString());
                        Toast.makeText(YSdkDemoActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailed(@NonNull JSONObject cause) {
                    RXLogger.i("应用宝登录失败结果:" + cause.toString());
                    Toast.makeText(YSdkDemoActivity.this, "登录失败:" + cause.toString(), Toast.LENGTH_LONG).show();

                }
            });
        });

        binding.loginYsdkUi.setOnClickListener(v -> {
            loginWithUI();
        });
    }

    private void loginWithUI() {
        Map<String, Object> map = new HashMap<>();
        map.put("method", "ysdk");
        map.put("ysdk_login_type", "ysdk_login_ui");
        Map<String, String> privacyInfo = new HashMap<>();
        privacyInfo.put("《用户协议》", "https://anhvcpo.weilekuiming.com/static/landing/#/v1/legal/terms/100/00001");
        privacyInfo.put("《隐私协议》", "https://anhvcpo.weilekuiming.com/static/landing/#/v1/legal/terms/100/00002?lang=zh");
        RXYsdkLoginConfig config = new RXYsdkLoginConfig.Builder()
                .configPrivacyInfo(privacyInfo)
                .configLoginUiOrientation(RXYsdkLoginConfig.RXYsdkLoginUiOrientation.DEFAULT)
                .configShowCloseButton(true)
                .configPhoneLoginPlatform(true)
                .configSkipYsdkAntiAddiction(false)
                .configShowLoginFailToast(true)
                .configYsdkAutoLogin(true)
                .configYsdkAntiAddictionDialog(true)
                .create();
        map.put("ysdk_login_ui_config", config.toMap());
        RuiXueSdk.getRXSdkApi().login(this, map, new RXJSONCallback() {

            @Override
            public void onSuccess(@Nullable JSONObject data) {
                if (data != null) {
                    RXLogger.i("应用宝登录成功结果: " + data.toString());
                    Toast.makeText(YSdkDemoActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailed(@NonNull JSONObject cause) {
                RXLogger.i("应用宝登录失败结果:" + cause.toString());
                Toast.makeText(YSdkDemoActivity.this, "登录失败:" + cause.toString(), Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        RuiXueSdk.onActivityResult(this, requestCode, resultCode, data);
    }
}
