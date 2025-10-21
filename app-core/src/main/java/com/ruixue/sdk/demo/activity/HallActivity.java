package com.ruixue.sdk.demo.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
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

import com.bumptech.glide.Glide;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.ruixue.RuiXueSdk;
import com.ruixue.callback.RXUICallback;
import com.ruixue.logger.RXLogger;
import com.ruixue.openapi.IRXView;
import com.ruixue.openapi.OnViewCloseListener;
import com.ruixue.openapi.RXSdkUI;
import com.ruixue.sdk.demo.eventbus.BackToSettingEvent;
import com.ruixue.sdk.demo.helper.CommonHelper;
import com.ruixue.sdk.demo.helper.GlobalSettingHelper;
import com.ruixue.sdk.demo.image.GlideEngine;
import com.ruixue.sdk.demo.main.R;
import com.ruixue.sdk.demo.main.databinding.ActivityHallBinding;
import com.ruixue.view.RXWebView;
import com.ruixue.view.UserCenterView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// Created by wangliang on 2024/5/21.
public class HallActivity extends BaseActivity {

    private static final int ALBUM_REQ_CODE = 102;

    private ActivityHallBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHallBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

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

        setRequestedOrientation(GlobalSettingHelper.getInstance().isLandscape() ? ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);

        if (GlobalSettingHelper.getInstance().isLandscape()) {
            binding.backgroundIv.setBackgroundResource(R.drawable.global_default_bg_landscape);
            binding.changeBgTipIv.setImageResource(R.drawable.global_change_bg_tip_landscape);
            binding.landscapeServiceBtn.setVisibility(View.VISIBLE);
            binding.landscapeUserCenterBtn.setVisibility(View.VISIBLE);
            binding.landscapeShareBtn.setVisibility(View.VISIBLE);
            binding.landscapePayBtn.setVisibility(View.VISIBLE);
            binding.portraitServiceBtn.setVisibility(View.GONE);
            binding.portraitUserCenterBtn.setVisibility(View.GONE);
            binding.portraitShareBtn.setVisibility(View.GONE);
            binding.portraitPayBtn.setVisibility(View.GONE);
            ViewGroup.LayoutParams lp = binding.backBtn.getLayoutParams();
            if (lp instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) lp;
                marginLayoutParams.rightMargin = dpToPx(this, 30);
                marginLayoutParams.topMargin = dpToPx(this, 50);
                binding.backBtn.setLayoutParams(marginLayoutParams);
            }
        } else {
            binding.backgroundIv.setBackgroundResource(R.drawable.global_default_bg_portrait);
            binding.changeBgTipIv.setImageResource(R.drawable.global_change_bg_tip_portrait);
            binding.landscapeServiceBtn.setVisibility(View.GONE);
            binding.landscapeUserCenterBtn.setVisibility(View.GONE);
            binding.landscapeShareBtn.setVisibility(View.GONE);
            binding.landscapePayBtn.setVisibility(View.GONE);
            binding.portraitServiceBtn.setVisibility(View.VISIBLE);
            binding.portraitUserCenterBtn.setVisibility(View.VISIBLE);
            binding.portraitShareBtn.setVisibility(View.VISIBLE);
            binding.portraitPayBtn.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams lp = binding.backBtn.getLayoutParams();
            if (lp instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) lp;
                marginLayoutParams.rightMargin = dpToPx(this, 30);
                marginLayoutParams.topMargin = dpToPx(this, 114);
                binding.backBtn.setLayoutParams(marginLayoutParams);
            }
        }

        binding.changeBgTipIv.setOnClickListener(v -> launchAlbum());
        binding.backBtn.setOnClickListener(v -> finish());
        binding.backSettingBtn.setOnClickListener(v -> backToSetting());
        binding.portraitServiceBtn.setOnClickListener(v -> launchService());
        binding.landscapeServiceBtn.setOnClickListener(v -> launchService());
        binding.portraitUserCenterBtn.setOnClickListener(v -> launchUserCenter());
        binding.landscapeUserCenterBtn.setOnClickListener(v -> launchUserCenter());
        binding.landscapeShareBtn.setOnClickListener(v -> launchShareTestPage());
        binding.portraitShareBtn.setOnClickListener(v -> launchShareTestPage());
        binding.landscapePayBtn.setOnClickListener(v -> launchPayTestPage());
        binding.portraitPayBtn.setOnClickListener(v -> launchPayTestPage());
    }

    @Override
    boolean transparentStatusBar() {
        return true;
    }

    @Override
    boolean transparentNavigationBar() {
        return true;
    }

    private void launchShareTestPage() {
        if (GlobalSettingHelper.ruixueLogin) {
            startActivity(new Intent(this, ShareTypeSelectActivity.class));
        } else {
            if (GlobalSettingHelper.getInstance().isOverseas()) {
                CommonHelper.startActivityByClass(this, "com.ruixue.sdk.overseas.demo.OverseasShareTestActivity", null);
            } else {
                CommonHelper.startActivityByClass(this, "com.ruixue.sdk.demo.ShareTestActivity", null);
            }
        }
    }

    private void launchPayTestPage() {
        if (GlobalSettingHelper.ruixueLogin) {
            startActivity(new Intent(this, PayTypeSelectActivity.class));
        } else {
            startActivity(new Intent(this, PayTestActivity.class));
        }
    }

    private void launchService() {
        // 具体参数 cp 方自己设置
        // 文档连接 https://doc.ruixuecloud.com/main/dev_doc/customer/clientAccess.html#%E5%B8%AE%E5%8A%A9%E4%B8%AD%E5%BF%83
        Map<String, Object> custom = new HashMap<>();
        custom.put("transmit_args", "透传参数");
        custom.put("game_user_id", 1000);
        custom.put("nickname", "用户昵称");
        custom.put("head_img_url", "用户头像");
        custom.put("queue_name", "default");
        RXSdkUI.getInstance().helperCenterUI(this, custom, new RXUICallback() {
            @Override
            public void onSuccess(@Nullable JSONObject jsonObject) {

            }

            @Override
            public void onFailed(@NonNull JSONObject jsonObject) {

            }
        }).show();
    }

    private void launchUserCenter() {
        userCenterTest(RuiXueSdk.getFirstBaseUrl() + "static/passport/#/usercenter");
    }

    private void userCenterTest(String url) {
        // 具体参数 cp 方自己设置
        Map<String, Object> custom = new HashMap<>();
        custom.put("transmit_args", "透传参数");
        custom.put("game_user_id", 1000);
        custom.put("nickname", "用户昵称");
        custom.put("head_img_url", "用户头像");
        custom.put("queue_name", "default");
        UserCenterView userCenterView = (UserCenterView) RXSdkUI.getInstance().userCenterUI(this, custom, new RXUICallback() {
            @Override
            public Map<String, Object> onClickHandle(Map<String, Object> params) {
                return null;
            }

            @Override
            public void onSuccess(@Nullable JSONObject data) {
                RXLogger.i("UserCenterView:" + data);
                if (data != null && data.optString("type", "").equals("switch_user")) {
                    finish();
                }
            }

            @Override
            public void onFailed(@NonNull JSONObject cause) {
                RXLogger.i("UserCenterView:" + cause);
            }
        });

        userCenterView.setDebugEnable(true);
        userCenterView.setJsDisable(true);
        userCenterView.setSyncInfoEnable(true);

        Map<String, Object> user_center = new HashMap<>();
        //设置用户中心模块 开关
//        user_center.put("btns", new String[]{"change_pwd", "acount_cancel", "privacy_policy", "real_name", "phone_management"});
//        userCenterView.setConfigParams(user_center);

        Map<String, Object> sync = new HashMap<>();
        userCenterView.setSyncParams(sync);
        userCenterView.setWebViewCloseListener(new OnViewCloseListener() {
            @Override
            public void onClosed(IRXView v) {
            }
        });
        if (!TextUtils.isEmpty(url))
            userCenterView.setCustomUrl(url);
        userCenterView.show();
    }

    private void backToSetting() {
        finish();
        EventBus.getDefault().post(new BackToSettingEvent());
    }

    private void launchAlbum() {
        if (CommonHelper.isSupportEasyPhoto(this)) {
            EasyPhotos.createAlbum(this, false, false, GlideEngine.getInstance()).start(ALBUM_REQ_CODE);
        } else {
            openGallery();
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, ALBUM_REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && requestCode == ALBUM_REQ_CODE) {
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

    public static int dpToPx(Context context, int dp) {
        Resources r = context.getApplicationContext().getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return (int) px;
    }
}
