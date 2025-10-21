package com.ruixue.sdk.demo.mumu;

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
import com.ruixue.openapi.HubActionAdapter;
import com.ruixue.passport.LoginMethod;
import com.ruixue.sdk.YofunSdkHelper;
import com.ruixue.sdk.demo.mumu.yofun.databinding.ActivityMumuDemoBinding;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MumuSplashActivity extends AppCompatActivity {
    private HubActionAdapter mAction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAction = new HubActionAdapter() {
            @Override
            public void onSplash() {
                Log.d("SplashActivity", "进入首页");
                Intent intent = new Intent(MumuSplashActivity.this, MumuDemoActivity.class);
                startActivity(intent);
            }

            @Override
            public void onQuit(boolean realQuit) {
                Log.d("SplashActivity", "退出");
            }
        };

//        YofunSdkHelper.splashOnCreate(this, mAction);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        YofunSdkHelper.splashOnDestroy(this, mAction);
    }

}
