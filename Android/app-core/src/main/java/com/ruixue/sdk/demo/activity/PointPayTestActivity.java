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

import com.google.gson.Gson;
import com.ruixue.RXJSONCallback;
import com.ruixue.sdk.demo.activity.PayType;
import com.ruixue.net.RXRequest;
import com.ruixue.net.ToastUtils;
import com.ruixue.sdk.demo.helper.GlobalSettingHelper;
import com.ruixue.sdk.demo.main.R;
import com.ruixue.sdk.demo.main.databinding.ActivityPointPayTestBinding;
import com.ruixue.sdk.demo.model.PayPointBean;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PointPayTestActivity extends BaseActivity {

    private final static String TAG = PointPayTestActivity.class.getName();

    private ActivityPointPayTestBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPointPayTestBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        binding.internalPay.setVisibility(GlobalSettingHelper.getInstance().isOverseas() ? View.GONE : View.VISIBLE);
        binding.overseasPay.setVisibility(GlobalSettingHelper.getInstance().isOverseas() ? View.VISIBLE : View.GONE);
        binding.backBtn.setOnClickListener(v -> finish());

        binding.wxSdkPayBtn.setOnClickListener(v -> {
            Map<String, Object> pay = new HashMap<>();
            Map<String, Object> extmap = new HashMap<>();
            pay.put("ext", extmap);
            pay.put("pay_type", PayType.WECHAT_PAY);
            jumpToPay(pay);
        });

        binding.wxH5PayBtn.setOnClickListener(v -> {
            Map<String, Object> pay = new HashMap<>();
            Map<String, Object> extmap = new HashMap<>();
            pay.put("ext", extmap);
            pay.put("pay_type", PayType.WECHATH5);
            jumpToPay(pay);
        });

        binding.alipaySdkPayBtn.setOnClickListener(v -> {
            Map<String, Object> pay = new HashMap<>();
            Map<String, Object> extmap = new HashMap<>();
            pay.put("ext", extmap);
            pay.put("pay_type", PayType.ALIPAY);
            jumpToPay(pay);
        });

        binding.alipayH5PayBtn.setOnClickListener(v -> {
            Map<String, Object> pay = new HashMap<>();
            Map<String, Object> extmap = new HashMap<>();
            pay.put("ext", extmap);
            pay.put("pay_type", PayType.ALIPAYH5);
            jumpToPay(pay);
        });

        binding.yeePayBtn.setOnClickListener(v -> {
            Map<String, Object> pay = new HashMap<>();
            Map<String, Object> extmap = new HashMap<>();
            extmap.put("pay_type", "wechat");
            extmap.put("pay_way", "H5_PAY");
            pay.put("ext", extmap);
            pay.put("pay_type", PayType.YEEPAY);
            jumpToPay(pay);
        });

        binding.jdPayBtn.setOnClickListener(v -> {
            Map<String, Object> pay = new HashMap<>();
            Map<String, Object> extmap = new HashMap<>();
            extmap.put("callback_url", "jixiang433://pay?code=0");
            pay.put("ext", extmap);
            pay.put("pay_type", PayType.JDJH);
            jumpToPay(pay);
        });

        binding.snWxPayBtn.setOnClickListener(v -> {
            Map<String, Object> pay = new HashMap<>();
            Map<String, Object> extmap = new HashMap<>();
            extmap.put("appid", GlobalSettingHelper.WEIXIN_APPID);
            extmap.put("pay_type", PayType.WECHAT_PAY);
            pay.put("ext", extmap);
            pay.put("pay_type", PayType.SUNING);
            jumpToPay(pay);
        });

        binding.ylWxPayBtn.setOnClickListener(v -> {
            Map<String, Object> pay = new HashMap<>();
            Map<String, Object> extmap = new HashMap<>();
            extmap.put("subAppId", GlobalSettingHelper.WEIXIN_APPID); // 吉祥捕鱼
            extmap.put("pay_type", PayType.WECHAT_PAY);
            pay.put("ext", extmap);
            pay.put("pay_type", PayType.AUMS);
            jumpToPay(pay);
        });

        binding.ylAliPayBtn.setOnClickListener(v -> {
            Map<String, Object> pay = new HashMap<>();
            Map<String, Object> extmap = new HashMap<>();
            extmap.put("pay_type", PayType.ALIPAY);
            pay.put("ext", extmap);
            pay.put("pay_type", PayType.AUMS);
            jumpToPay(pay);
        });

        binding.ylMiniGameBtn.setOnClickListener(v -> {
            Map<String, Object> pay = new HashMap<>();
            Map<String, Object> extmap = new HashMap<>();
            extmap.put("pay_type", "minigame");
            extmap.put("pre_mini", "Y");
            extmap.put("subAppId", GlobalSettingHelper.WEIXIN_APPID);
            pay.put("ext", extmap);
            pay.put("pay_type", PayType.AUMS);
            jumpToPay(pay);
        });

        binding.ylYsfBtn.setOnClickListener(v -> {
            Map<String, Object> pay = new HashMap<>();
            Map<String, Object> extmap = new HashMap<>();
            extmap.put("pay_type", "uac");
            extmap.put("supportBank", "CCB");//目前支持：CCB（中国建设银行）、CNCB(中信银行手机银行)、CEB(光大银行手机银行)、PAB（平安口袋银行）
            extmap.put("invokeType", "APP"); //APP_H5：APP+H5 APP：APP H5：H5 默认取值APP_H5。
            pay.put("ext", extmap);
            pay.put("pay_type", PayType.AUMS);
            jumpToPay(pay);
        });

        binding.ylWxPluginBtn.setOnClickListener(v -> {
            Map<String, Object> pay = new HashMap<>();
            Map<String, Object> extmap = new HashMap<>();
            extmap.put("pay_type", PayType.WECHAT_PAY);
            extmap.put("subAppId", "wx0dd648911d21b454");//支付插件
            pay.put("ext", extmap);
            pay.put("pay_type", PayType.AUMS);
            jumpToPay(pay);
        });

        binding.ylAliPayPluginBtn.setOnClickListener(v -> {
            Map<String, Object> pay = new HashMap<>();
            Map<String, Object> extmap = new HashMap<>();
            extmap.put("pay_type", PayType.ALIPAY);
            pay.put("ext", extmap);
            pay.put("pay_type", PayType.AUMS);
            jumpToPay(pay);
        });

        binding.ylYsfPluginBtn.setOnClickListener(v -> {
            Map<String, Object> pay = new HashMap<>();
            Map<String, Object> extmap = new HashMap<>();
            extmap.put("pay_type", "uac");
            pay.put("ext", extmap);
            pay.put("pay_type", PayType.AUMS);
            jumpToPay(pay);
        });

        binding.googlePayBtn.setOnClickListener(v -> {
            Map<String, Object> pay = new HashMap<>();
            Map<String, Object> extmap = new HashMap<>();
            pay.put("ext", extmap);
            pay.put("pay_type", PayType.GOOGLE);
            jumpToPay(pay);
        });

        binding.upayNetBtn.setOnClickListener(v -> {
            Map<String, Object> pay = new HashMap<>();
            Map<String, Object> extmap = new HashMap<>();
            extmap.put("pay_type", "net");
            extmap.put("type_id", "13");
            extmap.put("vendor", "");
            pay.put("ext", extmap);
            pay.put("pay_type", PayType.UPAY);
            jumpToPay(pay);
        });

        binding.upayWwwCardBtn.setOnClickListener(v -> {
            Map<String, Object> pay = new HashMap<>();
            Map<String, Object> extmap = new HashMap<>();
            extmap.put("pay_type", "ww_card");
            extmap.put("equipment_type", "2");
            extmap.put("country_name", "vietnam");
            extmap.put("vendor", "");
            pay.put("ext", extmap);
            pay.put("pay_type", "upay");
            jumpToPay(pay);
        });

        binding.upayApiBtn.setOnClickListener(v -> {
            Map<String, Object> pay = new HashMap<>();
            Map<String, Object> extmap = new HashMap<>();
            extmap.put("pay_type", "api");
            extmap.put("equipment_type", "2");
            extmap.put("vendor", "2");
            extmap.put("card_id", "2");
            extmap.put("card_num", "2");
            pay.put("ext", extmap);
            pay.put("pay_type", "upay");
            jumpToPay(pay);
        });

        binding.payermaxBtn.setOnClickListener(v -> {
            Map<String, Object> pay = new HashMap<>();
            Map<String, Object> extmap = new HashMap<>();
            extmap.put("language", "zh");
            extmap.put("frontCallbackUrl", "jixiang433://");
            pay.put("ext", extmap);
            pay.put("pay_type", PayType.PAYERMAX);
            pay.put("currency", "MYR");//TWD,MYR
            jumpToPay(pay);
        });

        binding.xsollaBtn.setOnClickListener(v -> {
            Map<String, Object> pay = new HashMap<>();
            Map<String, Object> extmap = new HashMap<>();
            extmap.put("user_name", "test");
            pay.put("ext", extmap);
            pay.put("pay_type", PayType.XSOLLA_INAPP);
            jumpToPay(pay);
        });

        binding.aptoideBtn.setOnClickListener(v -> {
            Map<String, Object> pay = new HashMap<>();
            Map<String, Object> extmap = new HashMap<>();
            pay.put("ext", extmap);
            pay.put("pay_type", PayType.APTOIDE);
            pay.put("currency", "USD");
            jumpToPay(pay);
        });

        binding.checkoutBtn.setOnClickListener(v -> {
            Map<String, Object> pay = new HashMap<>();
            Map<String, Object> extmap = new HashMap<>();
            extmap.put("user_name", "test");
            pay.put("ext", extmap);
            pay.put("pay_type", PayType.CHECKOUT);
            pay.put("currency", "USD");
            jumpToPay(pay);
        });

        binding.unipinBtn.setOnClickListener(v -> {
            Map<String, Object> pay = new HashMap<>();
            Map<String, Object> extmap = new HashMap<>();
            pay.put("ext", extmap);
            pay.put("pay_type", PayType.UNIPIN);
            pay.put("currency", "IDR");
            jumpToPay(pay);
        });

    }

    private void jumpToPay(Map<String, Object> payMap) {
        requestPayPoint(payMap);
    }

    private void requestPayPoint(Map<String, Object> payMap) {
        String url = "";
        switch (GlobalSettingHelper.debug_cp_id) {
            case 112:
                url = "https://cn-admin-demo.ruixuecloud.com/";
                break;
            case 114:
                url = "https://cn-admin-test.ruixuecloud.com/";
                break;
            case 119:
                url = "https://os-admin-test.ruixuecloud.com/";
                break;
            case 120:
                url = "https://os-admin-demo.ruixuecloud.com/";
                break;
        }
        RXRequest.create(url + "api/v1/operationtools/third_goods/all").setRestfulData(false).setNeedLoggedIn(false).getAsync(new RXJSONCallback() {
            @Override
            public void onSuccess(@Nullable JSONObject jsonObject) {

                if (jsonObject != null) {

                    Log.d(TAG, "productid 请求成功结果：" + jsonObject.toString());

                    Gson gson = new Gson();
                    PayPointBean payPointBean = gson.fromJson(jsonObject.toString(), PayPointBean.class);
                    String payType = (String) payMap.get("pay_type");

                    List<PayPointBean.DataDTO.PayGoodsDTO.ThirdGoodsDTO> thirdGoodsDTOList =
                            payPointBean.getData().getPayGoods().getThirdGoods();

                    List<PayPointBean.DataDTO.PayGoodsDTO.PublicDTO> publicDTOList =
                            payPointBean.getData().getPayGoods().getPublicX();

                    Intent intent = new Intent(
                            PointPayTestActivity.this,
                            PayPointListActivity.class
                    );

                    intent.putExtra("pay_map", (Serializable) payMap);


                    if (thirdGoodsDTOList != null && !thirdGoodsDTOList.isEmpty()) {
                        for (int i = 0; i < thirdGoodsDTOList.size(); i++) {
                            if (payType.equals(thirdGoodsDTOList.get(i).getType())) {
                                intent.putExtra("third_good", (Serializable) thirdGoodsDTOList.get(i));
                                startActivity(intent);
                                return;
                            }
                        }
                    }

                    if (publicDTOList != null && !publicDTOList.isEmpty()) {
                        intent.putExtra("public_good_list", (Serializable) publicDTOList);
                        startActivity(intent);
                    }else {
                        ToastUtils.showToast(PointPayTestActivity.this, "当前支付方式没有对应计费点");
                    }

                }

            }



            @Override
            public void onFailed(@NonNull JSONObject jsonObject) {
                Log.d(TAG, "productid 请求失败结果：" + jsonObject.toString());
            }
        });
    }

}