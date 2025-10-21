package com.ruixue.sdk.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter4.BaseQuickAdapter;
import com.ruixue.sdk.demo.main.R;
import com.ruixue.sdk.demo.main.databinding.ActivitySelectCpIdBinding;
import com.ruixue.sdk.demo.model.CpNameItem;
import com.ruixue.sdk.demo.recyclerdapter.RecyclerCpidAdapter;

import java.util.ArrayList;
import java.util.List;

public class SelectCpIdActivity extends BaseActivity {

    private ActivitySelectCpIdBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectCpIdBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        List<CpNameItem> cpIdList = new ArrayList<>();

        CpNameItem cpNameItem112 = new CpNameItem();
        cpNameItem112.cpid = 112;
        cpNameItem112.Name = "国内-瑞雪测试112";
        cpIdList.add(cpNameItem112);

        CpNameItem cpNameItem114 = new CpNameItem();
        cpNameItem114.cpid = 114;
        cpNameItem114.Name = "国内-瑞雪测试114";
        cpIdList.add(cpNameItem114);

        CpNameItem cpNameItem119 = new CpNameItem();
        cpNameItem119.cpid = 119;
        cpNameItem119.Name = "海外-瑞雪测试119";
        cpIdList.add(cpNameItem119);

        CpNameItem cpNameItem120 = new CpNameItem();
        cpNameItem120.cpid = 120;
        cpNameItem120.Name = "海外-瑞雪测试120";
        cpIdList.add(cpNameItem120);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(mLayoutManager);

        RecyclerCpidAdapter adapter = new RecyclerCpidAdapter();

        adapter.addAll(cpIdList);

        binding.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener<CpNameItem>() {
            @Override
            public void onClick(@NonNull BaseQuickAdapter<CpNameItem, ?> baseQuickAdapter,
                                @NonNull View view, int i) {
                CpNameItem cpNameItem = baseQuickAdapter.getItems().get(i);
                Intent intent = new Intent(SelectCpIdActivity.this,
                        ProDuctIdActivity.class);
                intent.putExtra("cp_id", cpNameItem.cpid);
                startActivity(intent);
            }
        });

        binding.backBtn.setOnClickListener(v -> finish());


    }

}