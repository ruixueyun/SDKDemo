package com.ruixue.sdk.demo.topon;

import static com.anythink.network.admob.AdmobATConst.DEBUGGER_CONFIG.Admob_NETWORK;
import static com.anythink.network.pangle.PangleATConst.DEBUGGER_CONFIG.Pangle_NETWORK;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.anythink.core.api.ATDebuggerConfig;
import com.anythink.core.api.DeviceInfoCallback;
import com.ruixue.RXJSONCallback;
import com.ruixue.RuiXueSdk;
import com.ruixue.leagl.PrivacyCallback;
import com.ruixue.sdk.demo.activity.BaseActivity;
import com.ruixue.sdk.demo.topon.databinding.ActivityToponDemoBinding;
import com.ruixue.topon.TopOnHelper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ToponDemoActivity extends BaseActivity {
    private static String TAG = "ToponDemoActivity";

    private ActivityToponDemoBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityToponDemoBinding.inflate(LayoutInflater.from(this));
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

        TopOnHelper.setNetworkLogDebug(true);//SDK日志功能，集成测试阶段建议开启，上线前必须关闭

        Log.i("TopOnSDkWrapper", "TopOn SDK version: " + TopOnHelper.getSDKVersionName());//SDK版本

        TopOnHelper.integrationChecking(this);//检查广告平台的集成状态，提交审核时需注释此API
        //(v5.7.77新增) 打印当前设备的设备信息(IMEI、OAID、GAID、AndroidID等)

        TopOnHelper.testModeDeviceInfo(this, new DeviceInfoCallback() {
            @Override
            public void deviceInfo(String deviceInfo) {
                Log.i("TopOnSDkWrapper", "deviceInfo: " + deviceInfo);
            }
        });

        binding.initSplash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TopOnHelper.setDebuggerConfig(ToponDemoActivity.this, "1b4ac8e9-e23f-4e75-b1bc-e22d2481a44a",
                        new ATDebuggerConfig.Builder(Pangle_NETWORK).build());

                Map<String, Object> params = new HashMap<>();

                // 这里使用方填写自己的 appid 和 appkey 再测试
                params.put("topon_app_id", "a62b40f5778f3d");
                params.put("topon_app_key", "c3d0d2a9a9d451b07e62b509659f7c97");
                RuiXueSdk.getRXSdkApi().initThirdSdk(ToponDemoActivity.this, params, new RXJSONCallback() {
                    @Override
                    public void onSuccess(@Nullable JSONObject jsonObject) {
                        Log.d(TAG, "init third sdk success");
                    }

                    @Override
                    public void onFailed(@NonNull JSONObject jsonObject) {
                        Log.d(TAG, "init third sdk failed " + jsonObject);
                    }
                });
            }
        });

        binding.initOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TopOnHelper.setDebuggerConfig(ToponDemoActivity.this, "1b4ac8e9-e23f-4e75-b1bc-e22d2481a44a",
                        new ATDebuggerConfig.Builder(Admob_NETWORK).build());

                Map<String, Object> params = new HashMap<>();

                // 这里使用方填写自己的 appid 和 appkey 再测试
                params.put("topon_app_id", "a62b40f5778f3d");
                params.put("topon_app_key", "c3d0d2a9a9d451b07e62b509659f7c97");
                RuiXueSdk.getRXSdkApi().initThirdSdk(ToponDemoActivity.this, params, new RXJSONCallback() {
                    @Override
                    public void onSuccess(@Nullable JSONObject jsonObject) {
                        Log.d(TAG, "init third sdk success");
                    }

                    @Override
                    public void onFailed(@NonNull JSONObject jsonObject) {
                        Log.d(TAG, "init third sdk failed " + jsonObject);
                    }
                });
            }
        });


        binding.manuReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ToponDemoActivity.this, ManulRewardDemoActivity.class);
                startActivity(intent);
            }
        });

        binding.autoReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ToponDemoActivity.this, AutoRewardDemoActivity.class);
                startActivity(intent);
            }
        });

        binding.manuInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ToponDemoActivity.this, ManulInsertDemoActivity.class);
                startActivity(intent);
            }
        });

        binding.autoInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ToponDemoActivity.this, AutoInsertDemoActivity.class);
                startActivity(intent);
            }
        });

        binding.splash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ToponDemoActivity.this, SplashDemoActivity.class);
                startActivity(intent);
            }
        });

        binding.banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ToponDemoActivity.this, BannerDemoActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        RuiXueSdk.onActivityResult(this, requestCode, resultCode, data);
    }

}
