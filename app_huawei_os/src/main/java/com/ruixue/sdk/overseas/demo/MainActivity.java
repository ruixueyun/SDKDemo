package com.ruixue.sdk.overseas.demo;

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

//import com.facebook.applinks.AppLinkData;
import com.ruixue.RXJSONCallback;
import com.ruixue.RuiXueSdk;
import com.ruixue.leagl.PrivacyCallback;
import com.ruixue.net.ToastUtils;
import com.ruixue.passport.LoginMethod;
import com.ruixue.sdk.demo.activity.LoginActivity;
import com.ruixue.sdk.demo.activity.RuiXueLoginActivity;
import com.ruixue.sdk.demo.activity.SettingActivity;
import com.ruixue.sdk.demo.helper.CommonHelper;
import com.ruixue.sdk.demo.helper.GlobalSettingHelper;
import com.ruixue.sdk.demo.main.databinding.ActivitySettingBinding;
import com.ruixue.sdk.demo.model.LanguageItem;
import com.ruixue.sdk.demo.ui.SettingItemView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by wangliang on 2024/8/5
 */
public class MainActivity extends AppCompatActivity {

    private ActivitySettingBinding binding;


    private int clickCount = 0;
    private CountDownTimer clickTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getIntent() != null) {
//            Uri uri = getIntent().getData();
//            if (uri != null) {
//                try {
//                    String appLinkData = uri.getQueryParameter("al_applink_data");
//                    Log.d("WLTest", "facebook data > " + appLinkData);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            AppLinkData data = AppLinkData.createFromActivity(MainActivity.this);
//            if (data != null) {
//                Log.d("WLTest", "facebook applink data" + data.getAppLinkData());
//            }
//        }
//
//        Intent intent = getIntent();
//        AppLinkData data = AppLinkData.createFromActivity(MainActivity.this);
//
//        // 延迟深度链接
//        Log.d("WLTest", "fetchDeferredAppLinkData >>>");
//        AppLinkData.fetchDeferredAppLinkData(MainActivity.this, appLinkData -> {
//            if (appLinkData != null) {
//                Log.d("WLTest", "facebook applink data" + appLinkData.getAppLinkData());
//            }
//        });
    }
}
