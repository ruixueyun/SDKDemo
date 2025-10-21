package com.ruixue.sdk.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ruixue.sdk.demo.main.R;
import com.ruixue.sdk.demo.main.databinding.ActivityChannelIdactivityBinding;
import com.ruixue.sdk.demo.helper.GlobalSettingHelper;
import com.ruixue.sdk.demo.model.ProDuctIDItem;
import com.ruixue.sdk.demo.recyclerdapter.RecyclerChannelIdAdapter;

public class ChannelIDActivity extends BaseActivity {

    private ActivityChannelIdactivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChannelIdactivityBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());


        Intent intent = getIntent();
        if (intent != null) {
            int cpid = intent.getIntExtra("cp_id", 0);
            String baseUrl = intent.getStringExtra("base_url");
            GlobalSettingHelper.debug_base_url = baseUrl;
            GlobalSettingHelper.debug_cp_id = cpid;
            ProDuctIDItem.DataDTO dataDTO =
                    (ProDuctIDItem.DataDTO) intent.getSerializableExtra("channel_data");


            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
            binding.recyclerView.setLayoutManager(mLayoutManager);

            RecyclerChannelIdAdapter adapter = new RecyclerChannelIdAdapter();
            if (dataDTO != null && dataDTO.getChannels() != null) {
                adapter.setItems(dataDTO.getChannels());
                binding.recyclerView.setAdapter(adapter);
            }

            adapter.setOnItemClickListener((baseQuickAdapter, view, i) -> {
                Intent channelIntent = new Intent(ChannelIDActivity.this,
                        SettingActivity.class);
                channelIntent.putExtra("cp_id", cpid);
                channelIntent.putExtra("productid", dataDTO.getProductId());
                channelIntent.putExtra("channelid", baseQuickAdapter.getItems().get(i).getId());
                channelIntent.putExtra("base_url", baseUrl);
                startActivity(channelIntent);
            });

            binding.backBtn.setOnClickListener(v -> finish());

        }

    }

}