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
import com.ruixue.net.RXRequest;
import com.ruixue.sdk.demo.main.R;
import com.ruixue.sdk.demo.main.databinding.ActivityProDuctIdBinding;
import com.ruixue.sdk.demo.model.ProDuctIDItem;
import com.ruixue.sdk.demo.recyclerdapter.RecyclerProducidAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProDuctIdActivity extends BaseActivity {

    public final static String TAG = ProDuctIdActivity.class.getName();

    private ActivityProDuctIdBinding binding;

    private RecyclerProducidAdapter adapter;

    private int mCpid = 0;
    private String mBaseUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProDuctIdBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(mLayoutManager);

        adapter = new RecyclerProducidAdapter();
        binding.recyclerView.setAdapter(adapter);

        Intent intent = getIntent();
        if (intent != null) {
            int cpID = intent.getIntExtra("cp_id", -1);

            switch (cpID) {
                case 112:
                    init112();
                    break;
                case 114:
                    init114();
                    break;
                case 119:
                    init119();
                    break;
                case 120:
                    init120();
                    break;
            }

        }

        adapter.setOnItemClickListener((baseQuickAdapter, view, i) -> {
                Intent channelIntent = new Intent(ProDuctIdActivity.this,
                        ChannelIDActivity.class);
                channelIntent.putExtra("cp_id", mCpid);
                channelIntent.putExtra("base_url", mBaseUrl);
                channelIntent.putExtra("channel_data", baseQuickAdapter.getItems().get(i));
                startActivity(channelIntent);
        });

        binding.backBtn.setOnClickListener(v -> finish());

    }

    private void init112()
    {
        String url = "http://cn-api-demo.ruixuecloud.com/";
        List<String> list = new ArrayList<>();
        list.add(url);
        RuiXueSdk.initialize("112", "SDK", "iOSOS", list,new RXJSONCallback() {
            @Override
            public void onSuccess(@Nullable JSONObject jsonObject) {
                Log.d(TAG, "RXSDK 初始化成功");
                requestProdId(url, 112);
            }

            @Override
            public void onFailed(@NonNull JSONObject jsonObject) {
                Log.d(TAG, "RXSDK 初始化失败 - " + jsonObject);
            }
        });
    }

    private void init114()
    {
        String url = "http://cn-api-test.ruixuecloud.com/";
        List<String> list = new ArrayList<>();
        list.add(url);
        RuiXueSdk.initialize("114", "1002", "iOS", list,new RXJSONCallback() {
            @Override
            public void onSuccess(@Nullable JSONObject jsonObject) {
                Log.d(TAG, "RXSDK 初始化成功");
                requestProdId(url, 114);
            }

            @Override
            public void onFailed(@NonNull JSONObject jsonObject) {
                Log.d(TAG, "RXSDK 初始化失败 - " + jsonObject);
            }
        });
    }

    private void init119()
    {
        String url = "http://os-api-test.ruixuecloud.com/";
        List<String> list = new ArrayList<>();
        list.add(url);
        RuiXueSdk.initialize("119", "SDKOS", "iOSOS", list,new RXJSONCallback() {
            @Override
            public void onSuccess(@Nullable JSONObject jsonObject) {
                Log.d(TAG, "RXSDK 初始化成功");
                requestProdId(url, 119);
            }

            @Override
            public void onFailed(@NonNull JSONObject jsonObject) {
                Log.d(TAG, "RXSDK 初始化失败 - " + jsonObject);
            }
        });
    }

    private void init120()
    {
        String url = "http://os-api-demo.ruixuecloud.com/";
        List<String> list = new ArrayList<>();
        list.add(url);
        RuiXueSdk.initialize("120", "SDK", "iOS", list,new RXJSONCallback() {
            @Override
            public void onSuccess(@Nullable JSONObject jsonObject) {
                Log.d(TAG, "RXSDK 初始化成功");
                requestProdId(url, 120);
            }

            @Override
            public void onFailed(@NonNull JSONObject jsonObject) {
                Log.d(TAG, "RXSDK 初始化失败 - " + jsonObject);
            }
        });
    }

    private void requestProdId(String url, int cpid) {
        RXRequest.create(url + "v1/publicapi/public/apps").setRestfulData(false).setNeedLoggedIn(false).getAsync(new RXJSONCallback() {
            @Override
            public void onSuccess(@Nullable JSONObject jsonObject) {

                if (jsonObject != null) {

                    mCpid = cpid;
                    mBaseUrl = url;

                    Log.d(TAG, "productid 请求成功结果：" + jsonObject.toString());

                    Gson gson = new Gson();

                    ProDuctIDItem proDuctIDItem = gson.fromJson(jsonObject.toString(), ProDuctIDItem.class);


                    Log.d(TAG, "id 是： " + proDuctIDItem.getData().get(0).getId());


                    adapter.addAll(proDuctIDItem.getData());
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