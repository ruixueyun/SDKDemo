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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.ruixue.RXJSONCallback;
import com.ruixue.RuiXueSdk;
import com.ruixue.callback.RXCallback;
import com.ruixue.error.RXException;
import com.ruixue.net.RXRequest;
import com.ruixue.net.ToastUtils;
import com.ruixue.openapi.RXShareConfig;
import com.ruixue.sdk.demo.helper.GlobalSettingHelper;
import com.ruixue.sdk.demo.main.R;
import com.ruixue.sdk.demo.main.databinding.ActivitySharePointListBinding;
import com.ruixue.sdk.demo.model.SharePointBean;
import com.ruixue.sdk.demo.recyclerdapter.RecyclerSharePointAdapter;
import com.ruixue.share.ShareDataResult;

import org.json.JSONObject;

import java.util.HashMap;

public class SharePointListActivity extends BaseActivity {

    public final static String TAG = SharePointListActivity.class.getName();

    private ActivitySharePointListBinding binding;
    private RecyclerSharePointAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySharePointListBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(mLayoutManager);

        adapter = new RecyclerSharePointAdapter();
        binding.recyclerView.setAdapter(adapter);

        requestSharePoint();

        Intent intent = getIntent();
        String platform = intent.getStringExtra("platform");

        adapter.setOnItemClickListener((baseQuickAdapter, view, i) -> {

            HashMap<String, Object> shareMap = new HashMap<>();
            shareMap.put("func",  baseQuickAdapter.getItems().get(i).getTag());
            shareMap.put("platform",  platform);
            RuiXueSdk.getApi().getShareData(shareMap, new RXCallback<ShareDataResult>() {
                @Override
                public void onSuccess(@Nullable ShareDataResult shareDataResult) {
                    if (shareDataResult != null && shareDataResult.getData() != null
                            && shareDataResult.getData().getStrategy() != null
                            && shareDataResult.getData().getStrategy().getId() > 0) {

                        RXShareConfig shareConfig = new RXShareConfig();
                        shareConfig.setFunc(baseQuickAdapter.getItems().get(i).getTag());
                        shareConfig.setPlatform(platform);
                        shareConfig.setAndroidScheme("//com.example.share");
                        shareConfig.setiOSScheme("//com.example.share");

                        RuiXueSdk.getApi().share(SharePointListActivity.this, shareConfig, new RXJSONCallback() {
                            @Override
                            public void onSuccess(@Nullable JSONObject jsonObject) {
                                Log.d(TAG, "分享成功");
                                ToastUtils.showToast(SharePointListActivity.this, "分享成功");
                            }

                            @Override
                            public void onFailed(@NonNull JSONObject jsonObject) {
                                ToastUtils.showToast(SharePointListActivity.this, "分享失败");
                            }
                        });
                    }else {
                        ToastUtils.showToast(SharePointListActivity.this, "此埋点未配置分享策略");
                    }
                }

                @Override
                public void onFailed(@NonNull ShareDataResult shareDataResult) {
                    ToastUtils.showToast(SharePointListActivity.this, "此埋点未配置分享策略");
                }

                @Override
                public void onError(RXException e) {
                    ToastUtils.showToast(SharePointListActivity.this, "此埋点未配置分享策略");
                }
            });
        });

        binding.backBtn.setOnClickListener(v -> finish());
    }

    private void requestSharePoint() {
        RXRequest.create(GlobalSettingHelper.debug_base_url + "v1/operationapi/trigger/list")
                .setRestfulData(false).setNeedLoggedIn(false)
                .getAsync(new RXJSONCallback() {
            @Override
            public void onSuccess(@Nullable JSONObject jsonObject) {

                if (jsonObject != null) {
                    Log.d(TAG, "productid 请求成功结果：" + jsonObject.toString());

                    Gson gson = new Gson();

                    SharePointBean sharePointBean = gson.fromJson(jsonObject.toString(), SharePointBean.class);

                    adapter.addAll(sharePointBean.getData());
                    adapter.notifyDataSetChanged();

                }

            }



            @Override
            public void onFailed(@NonNull JSONObject jsonObject) {
                Log.d(TAG, "productid 请求失败结果：" + jsonObject.toString());
            }
        });
    }

}