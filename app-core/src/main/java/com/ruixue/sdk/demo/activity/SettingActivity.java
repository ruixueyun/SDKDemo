package com.ruixue.sdk.demo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.gzuliyujiang.wheelpicker.OptionPicker;
import com.github.gzuliyujiang.wheelpicker.contract.OnOptionPickedListener;
import com.github.gzuliyujiang.wheelpicker.widget.OptionWheelLayout;
import com.ruixue.RXJSONCallback;
import com.ruixue.RuiXueSdk;
import com.ruixue.leagl.PrivacyCallback;
import com.ruixue.logger.RXLogger;
import com.ruixue.net.ToastUtils;
import com.ruixue.passport.LoginMethod;
import com.ruixue.sdk.demo.helper.CommonHelper;
import com.ruixue.sdk.demo.helper.GlobalSettingHelper;
import com.ruixue.sdk.demo.main.R;
import com.ruixue.sdk.demo.main.databinding.ActivitySettingBinding;
import com.ruixue.sdk.demo.model.LanguageItem;
import com.ruixue.sdk.demo.ui.SettingItemView;
import com.ruixue.sdk.demo.utils.StatusBarUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


// Created by wangliang on 2024/5/16.
public class SettingActivity extends BaseActivity {

    private static String TAG = "SettingActivity";

    private ActivitySettingBinding binding;

//    private SinglePicker<String> languagePicker;

    private int clickCount = 0;
    private CountDownTimer clickTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySettingBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        // 注册生命周期监听
        RuiXueSdk.trackingLifecycle(this);

        // 同意隐私协议
        RuiXueSdk.setPrivacyAgree(new PrivacyCallback() {
            @Override
            public void onPrivacyAgree(boolean b) {

            }
        });

        binding.backBtn.setOnClickListener(v -> finish());
        binding.topTitleTv.setText("SDK Demo (" + (GlobalSettingHelper.getInstance().isOverseas() ? "海外版" : "国内版") + ")");
        String versionName = CommonHelper.getVersionName(this);
        binding.topVersionTv.setText("version - " + ((versionName == null) ? "unknown" : versionName));

        if (GlobalSettingHelper.getInstance().isOverseas()) {
            binding.languageSelectBtn.setOnClickListener(v -> showLanguageSelectPicker());
        } else {
            RuiXueSdk.setLanguage(this, GlobalSettingHelper.getInstance().getCurrentLanguage().getValue());
            binding.languageSelectBtn.setOnClickListener(v -> ToastUtils.showToast(this, "国内版仅支持中文"));
        }
        binding.selectedLanguageTv.setText(GlobalSettingHelper.getInstance().getCurrentLanguage().getName());

        binding.landscapeRadioBtn.setChecked(GlobalSettingHelper.getInstance().isLandscape());
        binding.portraitRadioBtn.setChecked(!GlobalSettingHelper.getInstance().isLandscape());

        binding.vcodeLoginRadioBtn.setChecked(GlobalSettingHelper.getInstance().isCaptchaLogin());
        binding.accountLoginRadioBtn.setChecked(!GlobalSettingHelper.getInstance().isCaptchaLogin());

        binding.landscapeRadioBtn.setOnClickListener(v -> doSelectLandscape());
        binding.portraitRadioBtn.setOnClickListener(v -> doSelectPortrait());

        binding.vcodeLoginRadioBtn.setOnClickListener(v -> doSelectVcodeLogin());
        binding.accountLoginRadioBtn.setOnClickListener(v -> doSelectAccountLogin());

        binding.debugView.setOnClickListener(v -> {
            if (clickCount < 5) {
                clickCount++;
                Log.d("adsfsadf", "点击次数：" + clickCount);
                if (clickCount == 5) {
                    // 取消计时器以防止多次触发
                    if (clickTimer != null) {
                        clickTimer.cancel();
                    }
                    // 点击次数达到5次时的操作
                    startActivity(new Intent(SettingActivity.this, RuiXueLoginActivity.class));
                }
            } else {
                // 如果已经是第五次点击，则不增加计数
                clickCount = 0;
            }
            clickTimer = new CountDownTimer(3000, 3000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    // 这里可以放置重置后的操作，例如更新UI
                }

                @Override
                public void onFinish() {
                    clickCount = 0;
                    // 重置后的操作
                }
            }.start();
        });

        initQuickLoginItems();

        Intent intent = getIntent();

        String cpid;
        String productId;
        String channelId;
        List<String> hostUrls = new ArrayList<>();

        if (intent != null && intent.getIntExtra("cp_id", 0) > 0) {
            cpid = intent.getIntExtra("cp_id", 0) + "";
            productId = intent.getStringExtra("productid");
            channelId = intent.getStringExtra("channelid");
            String baseUrl = intent.getStringExtra("base_url");

            hostUrls.add(baseUrl);
        } else {
            if (GlobalSettingHelper.getInstance().isOverseas()) {
                cpid = /*"1000040"; */GlobalSettingHelper.OVERSEAS_CPID;
                productId = /*"265"; */GlobalSettingHelper.OVERSEAS_PRODUCT_ID;
                channelId = /*"2002"; */ GlobalSettingHelper.OVERSEAS_CHANNEL_ID;
//                hostUrls.add("https://rxapi.fishinggamezone.com/");
                hostUrls.add(GlobalSettingHelper.OVERSEAS_BASE_URL);
            } else {
                cpid = GlobalSettingHelper.CPID;
                productId = GlobalSettingHelper.PRODUCT_ID;
                channelId = GlobalSettingHelper.CHANNEL_ID;
                hostUrls.add(GlobalSettingHelper.BASE_URL);
            }
        }

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

        Map<String, Object> map = new HashMap<>();

        // google 测试 clientId 使用方换成自己的 clientId
        // 海外捕鱼 google clientId
        map.put("clientId", "302429863905-5c237rh19tecnmu08p6e8p4jj6uhfsvv.apps.googleusercontent.com");
        map.put("line_channel_id", "1660717706");

        // reddit
        map.put("reddit_clientid", "MjsG77lLx0ndS4u8JPjqCw");
        map.put("reddit_redirecturi", "http://localhost");
        map.put("alikey", "rPTnFy5eAxZTuLVBuur5JXDluwkT7B9jyypi1NselETj6YyYHJuhhKz5kkzIVH1FXa3SB6yMkKPK4vQkOH/9pCeGL/smhwzEwRnp9osz2CXighagGFJvtNyKLs0Wf78ZM6a9ntvzBJ4Fw3WqguPPIzQGPKkIVAfovaJZV9rTBb8hwmgiyAW8x6O0zfZ+exScIX6zJ+UVZTxmTxYl3gSyY41/4U5nTP2W0nljFqLasLiCSluWZNWQ2bfBgf1BkqC6tExfNAnvFq5be2MVnUXStsp6jTOMOXBKZdnSjMfGeOI=");
        map.put("catappult_public_key", "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApfgVe4/f8ax7eu7HawiWaQ1Y0PtMc7Z12l4ZNBV8jI2CgUtGPOPiFa8xKDzlsSM3/GNWNx493oN9fU34Q+lnV3nzKulTP8BjQRGXlhe55yoZEWzM99DsHrAywf3DTlYxzoaXKyu5by3E5+h0m0tN45+Iudp3xNXRDj6sfEm6qiwI25w+ahNQpVYEsA36MQSrYIDAwhl63yntu1OAuneKg+C4PErvWeY+2YaNciS0Yhx1608MOpOqYaZ8fsjw10BVvZhCsp3sCNZfjF7uxJalr3kMmnGt7mA1soUEbWDT2vAwL3B/ezdyVCW/kok+moNPSd1ywlSUK0TRTJ2TtVypvwIDAQAB");

        RuiXueSdk.getRXSdkApi().initThirdSdk(this, map, new RXJSONCallback() {
            @Override
            public void onSuccess(@Nullable JSONObject jsonObject) {
                Log.d(TAG, "init third sdk success");
                Intent intent = getIntent();
            }

            @Override
            public void onFailed(@NonNull JSONObject jsonObject) {
                Log.d(TAG, "init third sdk failed " + jsonObject);
            }
        });

        binding.confirmButton.setOnClickListener(v -> {
            saveQuickLoginMethods();
            startActivity(new Intent(this, LoginActivity.class));
        });
    }

    @Override
    boolean transparentStatusBar() {
        return true;
    }

    private void saveQuickLoginMethods() {
        List<String> selectedQuickLoginItems = new ArrayList<>();
        if (!GlobalSettingHelper.getInstance().isOverseas()) {
            selectedQuickLoginItems.add(LoginMethod.QUICKPHONE);
        }
        RXLogger.d("WLTest");
        if (binding.quickLoginContainer.getChildCount() == quickLoginItems.size()) {
            for (int i = 0; i < quickLoginItems.size(); i++) {
                View itemView = binding.quickLoginContainer.getChildAt(i);
                if (itemView instanceof SettingItemView) {
                    SettingItemView settingItemView = (SettingItemView) itemView;
                    if (settingItemView.isSelectedItem()) {
                        selectedQuickLoginItems.add(quickLoginItems.get(i).getMethod());
                    }
                }
            }
        } else {
            Log.e(TAG, "quick view and data inconsistent");
        }

        GlobalSettingHelper.getInstance().setQuickLoginMethods(selectedQuickLoginItems);
    }

    private OptionPicker languagePicker;

    private void showLanguageSelectPicker() {
        if (languagePicker == null) {
            languagePicker = new OptionPicker(this);
            languagePicker.setData(GlobalSettingHelper.getInstance().getLanguageItems());
            languagePicker.setOnOptionPickedListener(new OnOptionPickedListener() {
                @Override
                public void onOptionPicked(int position, Object data) {
                    if (data instanceof LanguageItem) {
                        LanguageItem item = (LanguageItem) data;
                        Log.d("WLTest", "onOptionPicked position:" + position + ", item:" + item.getValue());
                        GlobalSettingHelper.getInstance().setCurrentLanguage(item);
                        binding.selectedLanguageTv.setText(item.getName());
                    }
                }
            });
            OptionWheelLayout wheelLayout = languagePicker.getWheelLayout();
            wheelLayout.setIndicatorEnabled(true);
            wheelLayout.setTextColor(Color.parseColor("#99171A1D"));
            wheelLayout.setSelectedTextColor(Color.parseColor("#171A1D"));
        }
        languagePicker.show();
    }

    private List<LoginMethod> quickLoginItems = new ArrayList<>();

    // quickLoginItems 与 binding.quickLoginContainer 一一对应
    private void initQuickLoginItems() {
        quickLoginItems.add(LoginMethod.create(LoginMethod.GUEST));
        if (GlobalSettingHelper.getInstance().isOverseas()) {
            quickLoginItems.add(LoginMethod.create(LoginMethod.GOOGLE));
            quickLoginItems.add(LoginMethod.create(LoginMethod.FACEBOOK));
            quickLoginItems.add(LoginMethod.create(LoginMethod.LINE));
            quickLoginItems.add(LoginMethod.create(LoginMethod.ZALO));
            quickLoginItems.add(LoginMethod.create(LoginMethod.INSTAGRAM));
            quickLoginItems.add(LoginMethod.create(LoginMethod.TIKTOK));
            quickLoginItems.add(LoginMethod.create(LoginMethod.REDDIT));
        } else {

            quickLoginItems.add(LoginMethod.create(LoginMethod.USERNAME));
            quickLoginItems.add(LoginMethod.create(LoginMethod.CAPTCHACODE));
            quickLoginItems.add(LoginMethod.create(LoginMethod.WECHAT));
        }
        binding.quickLoginContainer.removeAllViews();
        for (LoginMethod item : quickLoginItems) {
            SettingItemView itemView = new SettingItemView(this);
            itemView.render(item.getIcon(), item.getName());
            binding.quickLoginContainer.addView(itemView);
            setSettingItemMargin(itemView);
        }
    }

    private void setSettingItemMargin(View itemView) {
        ViewGroup.LayoutParams lp = itemView.getLayoutParams();
        if (lp instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) lp;

            int margin = (int) (4 * getResources().getDisplayMetrics().density);
            marginLayoutParams.topMargin = margin;
            marginLayoutParams.bottomMargin = margin;
            itemView.setLayoutParams(marginLayoutParams);
        }
    }

    private void doSelectLandscape() {
        GlobalSettingHelper.getInstance().setLandscape(true);
        binding.landscapeRadioBtn.setChecked(true);
        binding.portraitRadioBtn.setChecked(false);
    }

    private void doSelectPortrait() {
        GlobalSettingHelper.getInstance().setLandscape(false);
        binding.landscapeRadioBtn.setChecked(false);
        binding.portraitRadioBtn.setChecked(true);
    }

    private void doSelectVcodeLogin() {
        GlobalSettingHelper.getInstance().setIsCaptchaLogin(true);
        binding.vcodeLoginRadioBtn.setChecked(true);
        binding.accountLoginRadioBtn.setChecked(false);
    }

    private void doSelectAccountLogin() {
        GlobalSettingHelper.getInstance().setIsCaptchaLogin(false);
        binding.accountLoginRadioBtn.setChecked(true);
        binding.vcodeLoginRadioBtn.setChecked(false);
    }

    private Long lastBackPressedTime = 0L;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - lastBackPressedTime < 3000L) {
            super.onBackPressed();
        } else {
            lastBackPressedTime = System.currentTimeMillis();
            Toast.makeText(this, R.string.setting_exit_app_tip, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clickTimer = null;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("WLTest", "onNewIntent");
    }
}
