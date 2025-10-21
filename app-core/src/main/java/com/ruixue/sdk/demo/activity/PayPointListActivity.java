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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ruixue.RXJSONCallback;
import com.ruixue.RuiXueSdk;
import com.ruixue.net.ToastUtils;
import com.ruixue.sdk.demo.main.R;
import com.ruixue.sdk.demo.main.databinding.ActivityPayPointListBinding;
import com.ruixue.sdk.demo.model.PayPointBean;
import com.ruixue.sdk.demo.recyclerdapter.PayPointListAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PayPointListActivity extends BaseActivity {

    private ActivityPayPointListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPayPointListBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        binding.backBtn.setOnClickListener(v -> finish());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(mLayoutManager);

        PayPointListAdapter adapter = new PayPointListAdapter();
        binding.recyclerView.setAdapter(adapter);


        Intent intent = getIntent();
        @SuppressWarnings("unchecked") Map<String, Object> payMap =
                (Map<String, Object>) intent.getSerializableExtra("pay_map");
        PayPointBean.DataDTO.PayGoodsDTO.ThirdGoodsDTO thirdGoodsDTO =
                (PayPointBean.DataDTO.PayGoodsDTO.ThirdGoodsDTO) intent.getSerializableExtra("third_good");

        adapter.setOnItemClickListener((baseQuickAdapter, view, i) -> {
            payMap.put("goods_tag", baseQuickAdapter.getItems().get(i));
            payMap.put("env", 1);// 测试: 1 生产: 0
            payMap.put("age", 18);
            payMap.put("trade_no", String.valueOf(System.currentTimeMillis()));
            payMap.put("notify_url", "http://game.pay.result.callback");
            RuiXueSdk.getRXSdkApi().pay(this, payMap, new RXJSONCallback() {
                @Override
                public void onSuccess(@Nullable JSONObject jsonObject) {
                    ToastUtils.showToast(PayPointListActivity.this, "支付成功");
                }

                @Override
                public void onFailed(@NonNull JSONObject jsonObject) {
                    ToastUtils.showToast(PayPointListActivity.this, "支付失败:" + jsonObject);
                }
            });
        });


        if (thirdGoodsDTO != null && thirdGoodsDTO.getTag() != null && !thirdGoodsDTO.getTag().isEmpty()) {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < thirdGoodsDTO.getTag().size(); i++) {
                list.add(thirdGoodsDTO.getTag().get(i).getRuixueTag());
            }
            adapter.addAll(list);
            adapter.notifyDataSetChanged();
        }else {
            @SuppressWarnings("unchecked") List<PayPointBean.DataDTO.PayGoodsDTO.PublicDTO> publicDTOList =
                    (List<PayPointBean.DataDTO.PayGoodsDTO.PublicDTO>) intent.getSerializableExtra("public_good_list");

            if (publicDTOList != null && !publicDTOList.isEmpty()) {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < publicDTOList.size(); i++) {
                    list.add(publicDTOList.get(i).getTag());
                }
                adapter.addAll(list);
                adapter.notifyDataSetChanged();
            }
        }

    }

}