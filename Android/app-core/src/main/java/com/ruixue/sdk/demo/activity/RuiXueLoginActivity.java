package com.ruixue.sdk.demo.activity;

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

import com.ruixue.RXJSONCallback;
import com.ruixue.net.RXRequest;
import com.ruixue.net.ToastUtils;
import com.ruixue.sdk.demo.helper.GlobalSettingHelper;
import com.ruixue.sdk.demo.main.R;
import com.ruixue.sdk.demo.main.databinding.ActivityRuiXueLoginBinding;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class RuiXueLoginActivity extends BaseActivity {

    public final static String TAG = RuiXueLoginActivity.class.getName();

    private ActivityRuiXueLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRuiXueLoginBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        binding.backBtn.setOnClickListener(v -> finish());

        binding.lgoinBtn.setOnClickListener(v -> {


            String name = binding.userName.getText().toString().trim();
            String passWord = binding.passWord.getText().toString().trim();


            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] inputBytes = passWord.getBytes();
                md.update(inputBytes);
                byte[] digestBytes = md.digest();
                StringBuilder sb = new StringBuilder();
                for (byte b : digestBytes) {
                    sb.append(String.format("%02x", b & 0xff));
                }

                Log.d(TAG, "md5值：" + sb.toString().toUpperCase());

                requestLogin(name, sb.toString());

            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }

        });

    }

    private void requestLogin(String account, String password) {
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("account", account);
        bodyMap.put("password", password.toUpperCase());
        bodyMap.put("language", "zh");
        RXRequest.create("http://haiqi-test.ruixuecloud.com/api/v1/gwapi/login/pwd")
                .setRestfulData(false).setNeedLoggedIn(false)
                .setBody(bodyMap)
                .postAsync(new RXJSONCallback() {
                    @Override
                    public void onSuccess(@Nullable JSONObject jsonObject) {
                        GlobalSettingHelper.ruixueLogin = true;
                        ToastUtils.showToast(RuiXueLoginActivity.this, "登录成功");
                        startActivity(new Intent(RuiXueLoginActivity.this, SelectCpIdActivity.class));
                    }

                    @Override
                    public void onFailed(@NonNull JSONObject jsonObject) {
                        ToastUtils.showToast(RuiXueLoginActivity.this, "登录失败");
                    }
                });
    }

}