package com.ruixue.sdk.demo.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.DisplayCutout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.ruixue.RuiXueSdk;
import com.ruixue.callback.RXUICallback;
import com.ruixue.net.ToastUtils;
import com.ruixue.openapi.Constants;
import com.ruixue.openapi.LoginUIConfig;
import com.ruixue.openapi.RXGlobalData;
import com.ruixue.openapi.RXLoginUIModel;
import com.ruixue.openapi.RXSdkUI;
import com.ruixue.passport.LoginMethod;
import com.ruixue.sdk.demo.eventbus.BackToSettingEvent;
import com.ruixue.sdk.demo.helper.CommonHelper;
import com.ruixue.sdk.demo.helper.GlobalSettingHelper;
import com.ruixue.sdk.demo.image.GlideEngine;
import com.ruixue.sdk.demo.main.R;
import com.ruixue.sdk.demo.main.databinding.ActivityLoginBinding;
import com.ruixue.sdk.demo.utils.StatusBarUtils;
import com.ruixue.utils.JSONUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

// Note: 该类中是聚合了各种分享，但由于环境参数配置原因并不能保证流程能跑通
//       使用方需要根据自己的参数配置好才能跑通
//       该类只做 Sample，方便使用方参考

// Created by wangliang on 2024/5/20.
public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";

    private static final int LOGIN_ALBUM_REQ_CODE = 101;

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        EventBus.getDefault().register(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            final View decorView = getWindow().getDecorView();
            decorView.post(new Runnable() {
                @Override
                public void run() {
                    WindowInsets insets = getWindow().getDecorView().getRootWindowInsets();
                    if (insets != null && insets.getDisplayCutout() != null) {
                        DisplayCutout displayCutout = getWindow().getDecorView().getRootWindowInsets().getDisplayCutout();
                        Log.e("TAG", "安全区域距离屏幕左边的距离 SafeInsetLeft:" + displayCutout.getSafeInsetLeft());
                        Log.e("TAG", "安全区域距离屏幕右部的距离 SafeInsetRight:" + displayCutout.getSafeInsetRight());
                        Log.e("TAG", "安全区域距离屏幕顶部的距离 SafeInsetTop:" + displayCutout.getSafeInsetTop());
                        Log.e("TAG", "安全区域距离屏幕底部的距离 SafeInsetBottom:" + displayCutout.getSafeInsetBottom());
                    }
                }
            });
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        RuiXueSdk.setLanguage(this, GlobalSettingHelper.getInstance().getCurrentLanguage().getValue());

        setRequestedOrientation(GlobalSettingHelper.getInstance().isLandscape() ? ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);

        if (GlobalSettingHelper.getInstance().isLandscape()) {
            binding.backgroundIv.setBackgroundResource(R.drawable.global_default_bg_landscape);
            binding.changeBgTipIv.setImageResource(R.drawable.global_change_bg_tip_landscape);
            binding.landscapeLoginBtn.setVisibility(View.VISIBLE);
            binding.portraitLoginBtn.setVisibility(View.GONE);
            ViewGroup.LayoutParams lp = binding.backSettingBtn.getLayoutParams();
            if (lp instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) lp;
                marginLayoutParams.rightMargin = dpToPx(this, 30);
                marginLayoutParams.topMargin = dpToPx(this, 50);
                binding.backSettingBtn.setLayoutParams(marginLayoutParams);
            }
        } else {
            binding.backgroundIv.setBackgroundResource(R.drawable.global_default_bg_portrait);
            binding.changeBgTipIv.setImageResource(R.drawable.global_change_bg_tip_portrait);
            binding.landscapeLoginBtn.setVisibility(View.GONE);
            binding.portraitLoginBtn.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams lp = binding.backSettingBtn.getLayoutParams();
            if (lp instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) lp;
                marginLayoutParams.rightMargin = dpToPx(this, 30);
                marginLayoutParams.topMargin = dpToPx(this, 114);
                binding.backSettingBtn.setLayoutParams(marginLayoutParams);
            }
        }

        binding.changeBgTipIv.setOnClickListener(v -> launchAlbum());
        binding.backSettingBtn.setOnClickListener(v -> finish());
        binding.landscapeLoginBtn.setOnClickListener(v -> doLogin());
        binding.portraitLoginBtn.setOnClickListener(v -> doLogin());
    }

    @Override
    boolean transparentStatusBar() {
        return true;
    }

    @Override
    boolean transparentNavigationBar() {
        return true;
    }

    private void doLogin() {
        if(GlobalSettingHelper.getInstance().isOverseas()) {
            RXSdkUI.getInstance().loginUIOS(this, getLoginUIConfig(), null, new RXUICallback() {
                @Override
                public void onSuccess(@Nullable JSONObject jsonObject) {
                    Log.d(TAG, "login success");
                    ToastUtils.showToast(LoginActivity.this, "登录成功");
                    startActivity(new Intent(LoginActivity.this, HallActivity.class));
                }

                @Override
                public void onFailed(@NonNull JSONObject jsonObject) {
                    Log.d(TAG, "login failed " + jsonObject);
                    ToastUtils.showToast(LoginActivity.this, "登录失败 " + jsonObject);
                }
            }).show();
        } else {
//            RXSdkUI.getInstance().showLoginUI(this, getLoginUIConfig(false, false,false, false),
            RXSdkUI.getInstance().showLoginUI(this, getLoginUIModel(), new RXUICallback() {
                @Override
                public void onSuccess(@Nullable JSONObject jsonObject) {
                    Log.d(TAG, "login success");
                    ToastUtils.showToast(LoginActivity.this, "登录成功");
                    startActivity(new Intent(LoginActivity.this, HallActivity.class));
                }

                @Override
                public void onFailed(@NonNull JSONObject jsonObject) {
                    Log.d(TAG, "login failed " + jsonObject);
                    ToastUtils.showToast(LoginActivity.this, "登录失败 " + jsonObject);
                }

                @Override
                public Map<String, Object> onClickHandle(Map<String, Object> params) {
                    String method = (String) params.get("method");
                    if (LoginMethod.QUICKPHONE.equals(method)) {
                        Map<String, Object> aliMobileMap = new HashMap<>();
                        //阿里一键登录使用的appkey 从这里注入
                        aliMobileMap.put("alikey", GlobalSettingHelper.ALIKEY);
                        try {
                            if (params.containsKey("ext")) {
                                HashMap<String, Object> pa = (HashMap<String, Object>) params.get("ext");
                                if (pa != null) {
                                    pa.putAll(aliMobileMap);
                                }
                            } else {
                                params.put("ext", aliMobileMap);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (LoginMethod.WECHAT.equals(method)) {
                        Map<String, Object> wechatMap = new HashMap<>();
                        //微信登录使用的微信appid
                        wechatMap.put("appid", GlobalSettingHelper.WEIXIN_APPID);
                        try {
                            if (params.containsKey("ext")) {
                                HashMap<String, Object> pa = (HashMap<String, Object>) params.get("ext");
                                if (pa != null) {
                                    pa.putAll(wechatMap);
                                }
                            } else {
                                params.put("ext", wechatMap);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Log.d("WLTest", JSONUtil.toJSONObject(params).toString());
                    return params;
                }
            });
        }
    }

    private static RXLoginUIModel getLoginUIModel() {
        RXLoginUIModel loginUIConfig = new RXLoginUIModel();
//        if (withOther) {
//            List<String> loginMethod = Arrays.asList(LoginMethod.CAPTCHACODE, LoginMethod.USERNAME, LoginMethod.GUEST, LoginMethod.WECHAT, "quickphone");
//            loginUIConfig.setLoginMethods(loginMethod);
//        }
//        loginUIConfig.setIndulgeAuth(0);

         loginUIConfig.setForgotUrl(GlobalSettingHelper.BASE_URL + "static/passport/#/user/forgetpassword");
         loginUIConfig.setLoginMethods(GlobalSettingHelper.getInstance().getQuickLoginMethods());


        loginUIConfig.setTitleResId(R.drawable.logo);

        loginUIConfig.setPrivacyOne("用户协议", "https://anhvcpo.weilekuiming.com/static/landing/#/v1/legal/terms/100/00001");
        loginUIConfig.setPrivacyTwo("隐私协议", "https://anhvcpo.weilekuiming.com/static/landing/#/v1/legal/terms/100/00002?lang=zh");
//        loginUIConfig.setPrivacyTwo("隐私协议", "file://android_asset/xieyi.html");
//        loginUIConfig.setPrivacyThree("儿童隐私", "https://anhvcpo.weilekuiming.com/static/landing/#/v1/legal/terms/100/00002");

        LinkedHashMap<String, Object> d = new LinkedHashMap<>();
//        https://anhvcpo.weilekuiming.com/static/passport/#/helpcenter/questioncatalogue
//        Map<String,String> dz=new HashMap<>();
//        dz.put("zh","用户协议");
//        d.put("https://anhvcpo.weilekuiming.com/static/landing/#/v1/legal/terms/100/00001", dz);
//        d.put("https://anhvcpo.weilekuiming.com/static/landing/#/v1/legal/terms/100/00002?lang=zh", "隐私协议");
//        d.put("https://anhvcpo.weilekuiming.com/static/landing/#/v1/legal/terms/100/00003", "儿童隐私");
//        loginUIConfig.setPrivacies(d);
        return loginUIConfig;
    }

    private static LoginUIConfig getLoginUIConfig() {
        LoginUIConfig loginUIConfig = RXGlobalData.getPassportCfg();
        loginUIConfig.setLoginMethods(GlobalSettingHelper.getInstance().getQuickLoginMethods());

//        loginUIConfig.setCaptchaLogin(GlobalSettingHelper.getInstance().isCaptchaLogin());
        loginUIConfig.setTitleResId(R.drawable.logo);
//        loginUIConfig.setFirstNeedSetPassword(true);
//        Map<String, Object> loginMap = new HashMap<>();
//        loginUIConfig.setLoginMethods(loginMap);

        Map<String, Object> cmap = new HashMap<>();
        cmap.put("sign_fields", new String[]{"openid", "age"});
        loginUIConfig.setCustomParams(cmap);

        return loginUIConfig;
    }

    private void launchAlbum() {
        if (!CommonHelper.isSupportEasyPhoto(this)) {
            openGallery();
        } else {
            EasyPhotos.createAlbum(this, true, false, GlideEngine.getInstance()).start(LOGIN_ALBUM_REQ_CODE);
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, LOGIN_ALBUM_REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("WLTest", "onActivityResult " + requestCode + "--" + resultCode);
        RuiXueSdk.onActivityResult(this, requestCode, resultCode, data);
        if (RESULT_OK == resultCode && requestCode == LOGIN_ALBUM_REQ_CODE) {
            if (!CommonHelper.isSupportEasyPhoto(this)) {
                if (data != null) {
                    Uri uri = data.getData();
                    Log.d("WLTest", "uri : " + uri);
                    Glide.with(this).load(uri).into(binding.backgroundIv);
                }
            } else {
                //返回对象集合：如果你需要了解图片的宽、高、大小、用户是否选中原图选项等信息，可以用这个
                ArrayList<Photo> resultPhotos = data.getParcelableArrayListExtra(EasyPhotos.RESULT_PHOTOS);
                if (resultPhotos != null && !resultPhotos.isEmpty()) {
                    Photo photo = resultPhotos.get(0);
                    File file = new File(photo.path);
                    Glide.with(this).load(Uri.fromFile(file)).into(binding.backgroundIv);
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBackToSettingEvent(BackToSettingEvent event) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public static int dpToPx(Context context, int dp) {
        Resources r = context.getApplicationContext().getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return (int) px;
    }
}
