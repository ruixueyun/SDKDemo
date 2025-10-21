package com.ruixue.sdk.demo.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ruixue.RXJSONCallback;
import com.ruixue.RuiXueSdk;

import com.ruixue.net.ToastUtils;
import com.ruixue.openapi.RXSdkApi;
import com.ruixue.sdk.demo.helper.GlobalSettingHelper;
import com.ruixue.sdk.demo.main.R;
import com.ruixue.sdk.demo.main.databinding.ActivityPayTestBinding;
import com.ruixue.view.AlertTipView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

// Note: 该类中是聚合了各种支付，但由于环境参数配置原因并不能保证支付流程能跑通
//       使用方需要根据自己的参数配置好才能跑通
//       该类只做 Sample，方便使用方参考

// Created by wangliang on 2024/5/22.
public class PayTestActivity extends BaseActivity {
    private static final String TAG = "rxpay";
    private static final String hq_key = "hq_type";

    private ActivityPayTestBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RXSdkApi.getInstance().registerPlugin("com.ruixue.hq.HQSdkWrapper");
        binding = ActivityPayTestBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        binding.internalPay.setVisibility(GlobalSettingHelper.getInstance().isOverseas() ? View.GONE : View.VISIBLE);
        binding.overseasPay.setVisibility(GlobalSettingHelper.getInstance().isOverseas() ? View.VISIBLE : View.GONE);
        binding.backBtn.setOnClickListener(v -> finish());

        binding.googlePayBtn.setOnClickListener(v -> doGooglePay());
        binding.upayNetBtn.setOnClickListener(v -> doUPayNet());
        binding.upayWwwCardBtn.setOnClickListener(v -> doUPayWwwCard());
        binding.upayApiBtn.setOnClickListener(v -> doUPayApi());

        binding.payermaxBtn.setOnClickListener(v -> doPayerMaxPay());
        binding.checkoutBtn.setOnClickListener(v -> doCheckoutPay());
        binding.aptoideBtn.setOnClickListener(v -> doAptoidePay());

        // 暂时未上线 2024-5-22
                binding.unipinBtn.setVisibility(View.GONE);
        binding.unipinBtn.setOnClickListener(v -> doUnipinPay());


        binding.rxH5pay.setOnClickListener(v -> doRXH5Pay());
        // 国内支付
        binding.wxSdkPayBtn.setOnClickListener(v -> doWXSdkPay());
        binding.wxH5PayBtn.setOnClickListener(v -> doWXH5Pay());

        binding.alipaySdkPayBtn.setOnClickListener(v -> doAlipaySdkPay());
        binding.alipayH5PayBtn.setOnClickListener(v -> doAlipayH5Pay());
        binding.yeePayBtn.setOnClickListener(v -> doYeePay());
        binding.jdPayBtn.setOnClickListener(v -> doJDPay());
        binding.snWxPayBtn.setOnClickListener(v -> doSNWXPay());
        binding.ylWxPayBtn.setOnClickListener(v -> doYLWXPay());
        binding.ylAliPayBtn.setOnClickListener(v -> doYLAlipayPay());
        binding.ylMiniGameBtn.setOnClickListener(v -> doYLMiniGamePay());
        binding.ylYsfBtn.setOnClickListener(v -> doYLYSFPay());
        binding.ylWxPluginBtn.setOnClickListener(v -> doYLWXPluginPay());
        binding.ylAliPayPluginBtn.setOnClickListener(v -> doYLAlipayPluginPay());
        binding.ylYsfPluginBtn.setOnClickListener(v -> doYLYSFPluginPay());
        binding.xsollaBtn.setOnClickListener(v -> doXsollaPay());
    }

    private void doPay(Map<String, Object> pay, RXJSONCallback callback) {
        String payType = (String) pay.get("hq_type");
        if (TextUtils.isEmpty(payType)) {
            payType = (String) pay.get(hq_key);
        }

        if (payType != null && PayType.isAlipay(payType)) {
            RuiXueSdk.getApi().checkQuickAp(new RXJSONCallback() {
                //    "agreement_no_encrypt": "string",
//    "quick_text": "string",
//    "pay_cfgid": "string"
                @Override
                public void onSuccess(@Nullable JSONObject data) {
                    if (data != null) {
                        Map<String, Object> ext = pay.containsKey("ext") ? (Map<String, Object>) pay.get("ext") : new HashMap<>();
                        if (ext == null) {
                            ext = new HashMap<>();
                        }
                        ext.put("pay_cfgid", data.optString("pay_cfgid", ""));
                        pay.put("ext", ext);
                        if (!TextUtils.isEmpty(data.optString("agreement_no_encrypt"))) {
                            Map<String, Object> finalExt = ext;
                            AlertTipView.create(PayTestActivity.this, "提示", data.optString("quick_text", "是否使用免密支付"), new RXJSONCallback() {
                                @Override
                                public void onSuccess(@Nullable JSONObject data) {
                                    finalExt.put("agreement_no_encrypt", data.optString("agreement_no_encrypt"));
                                    pay.put("ext", finalExt);
                                    RuiXueSdk.getApi().pay(PayTestActivity.this, pay, callback);
                                }

                                @Override
                                public void onFailed(@NonNull JSONObject cause) {
                                    RuiXueSdk.getApi().pay(PayTestActivity.this, pay, callback);
                                }
                            }).show();
                        } else {
                            RuiXueSdk.getApi().pay(PayTestActivity.this, pay, callback);
                        }
                    } else {
                        RuiXueSdk.getApi().pay(PayTestActivity.this, pay, callback);
                    }
                }

                @Override
                public void onFailed(@NonNull JSONObject cause) {
                    ToastUtils.showToast(PayTestActivity.this, "查询免密支付失败：" + cause.toString());
                }
            });

        } else {
            RuiXueSdk.getApi().pay(this, pay, callback);
        }

    }


    /**
     * 微信 支付
     * <p>
     * 参数请根据自己的环境配置，详见文档 https://doc.ruixuecloud.com/main/dev_doc/payment/android.html
     */
    private void doWXSdkPay() {
        Map<String, Object> pay = new HashMap<>();
        Map<String, Object> extmap = new HashMap<>();
        pay.put(hq_key, PayType.WECHAT_PAY);
        pay.put("env", 1);// 测试: 1 生产: 0
        pay.put("ext", extmap);
        pay.put("age", 18);
        pay.put("trade_no", String.valueOf(System.currentTimeMillis()));
        pay.put("notify_url", "http://game.pay.result.callback");

        this.doPay(pay, new RXJSONCallback() {
            @Override
            public void onSuccess(@Nullable JSONObject jsonObject) {
                ToastUtils.showToast(PayTestActivity.this, "微信 SDK 支付成功");
            }

            @Override
            public void onFailed(@NonNull JSONObject jsonObject) {
                Log.d(TAG, "pay failed:" + jsonObject);
                ToastUtils.showToast(PayTestActivity.this, "微信 SDK 支付失败:" + jsonObject);
            }
        });
    }

    /**
     * 微信H5 支付
     * <p>
     * 参数请根据自己的环境配置，详见文档 https://doc.ruixuecloud.com/main/dev_doc/payment/android.html
     */
    private void doWXH5Pay() {
        Map<String, Object> pay = new HashMap<>();
        Map<String, Object> extmap = new HashMap<>();
        pay.put(hq_key, PayType.WECHATH5);
        pay.put("env", 1);// 测试: 1 生产: 0
        pay.put("ext", extmap);
        pay.put("age", 18);
        pay.put("goods_tag", "bytest");
        pay.put("trade_no", String.valueOf(System.currentTimeMillis()));
        pay.put("notify_url", "http://game.pay.result.callback");

        this.doPay(pay, new RXJSONCallback() {
            @Override
            public void onSuccess(@Nullable JSONObject jsonObject) {
                ToastUtils.showToast(PayTestActivity.this, "微信 H5 支付成功");
            }

            @Override
            public void onFailed(@NonNull JSONObject jsonObject) {
                Log.d(TAG, "pay failed:" + jsonObject);
                ToastUtils.showToast(PayTestActivity.this, "微信 H5 支付失败:" + jsonObject);
            }
        });
    }

    /**
     * 支付宝 支付
     * <p>
     * 参数请根据自己的环境配置，详见文档 https://doc.ruixuecloud.com/main/dev_doc/payment/android.html
     */
    private void doAlipaySdkPay() {
        Map<String, Object> pay = new HashMap<>();
        Map<String, Object> extmap = new HashMap<>();
        pay.put(hq_key, PayType.ALIPAY);
        pay.put("env", 1);// 测试: 1 生产: 0
        pay.put("ext", extmap);
        pay.put("age", 18);
        pay.put("goods_tag", "842000099");
        pay.put("trade_no", String.valueOf(System.currentTimeMillis()));
        pay.put("notify_url", "http://game.pay.result.callback");

        this.doPay(pay, new RXJSONCallback() {
            @Override
            public void onSuccess(@Nullable JSONObject jsonObject) {
                ToastUtils.showToast(PayTestActivity.this, "支付宝 SDK 支付成功");
            }

            @Override
            public void onFailed(@NonNull JSONObject jsonObject) {
                Log.d(TAG, "pay failed:" + jsonObject);
                ToastUtils.showToast(PayTestActivity.this, "支付宝 SDK 支付失败:" + jsonObject);
            }
        });
    }

    /**
     * 支付宝H5 支付
     * <p>
     * 参数请根据自己的环境配置，详见文档 https://doc.ruixuecloud.com/main/dev_doc/payment/android.html
     */
    private void doAlipayH5Pay() {
        Map<String, Object> pay = new HashMap<>();
        Map<String, Object> extmap = new HashMap<>();
        pay.put(hq_key, PayType.ALIPAYH5);
        pay.put("env", 1);// 测试: 1 生产: 0
        pay.put("ext", extmap);
        pay.put("age", 18);
        pay.put("trade_no", String.valueOf(System.currentTimeMillis()));
        pay.put("notify_url", "http://game.pay.result.callback");

        this.doPay(pay, new RXJSONCallback() {
            @Override
            public void onSuccess(@Nullable JSONObject jsonObject) {
                ToastUtils.showToast(PayTestActivity.this, "支付宝 H5 支付成功");
            }

            @Override
            public void onFailed(@NonNull JSONObject jsonObject) {
                Log.d(TAG, "pay failed:" + jsonObject);
                ToastUtils.showToast(PayTestActivity.this, "支付宝 H5 支付失败:" + jsonObject);
            }
        });
    }

    /**
     * 易宝 支付
     * <p>
     * 参数请根据自己的环境配置，详见文档 https://doc.ruixuecloud.com/main/dev_doc/payment/android.html
     */
    private void doYeePay() {
        Map<String, Object> pay = new HashMap<>();
        Map<String, Object> extmap = new HashMap<>();
        extmap.put(hq_key, "wechat");
        extmap.put("pay_way", "H5_PAY");
        pay.put(hq_key, PayType.YEEPAY);
        pay.put("env", 1);// 测试: 1 生产: 0
        pay.put("ext", extmap);
        pay.put("age", 18);
        pay.put("trade_no", String.valueOf(System.currentTimeMillis()));
        pay.put("notify_url", "http://game.pay.result.callback");

        this.doPay(pay, new RXJSONCallback() {
            @Override
            public void onSuccess(@Nullable JSONObject jsonObject) {
                ToastUtils.showToast(PayTestActivity.this, "易宝支付成功");
            }

            @Override
            public void onFailed(@NonNull JSONObject jsonObject) {
                Log.d(TAG, "pay failed:" + jsonObject);
                ToastUtils.showToast(PayTestActivity.this, "易宝支付失败:" + jsonObject);
            }
        });
    }

    /**
     * 京东 支付
     * <p>
     * 参数请根据自己的环境配置，详见文档 https://doc.ruixuecloud.com/main/dev_doc/payment/android.html
     */
    private void doJDPay() {
        Map<String, Object> pay = new HashMap<>();
        Map<String, Object> extmap = new HashMap<>();
        extmap.put("callback_url", "jixiang433://pay?code=0");
        pay.put(hq_key, PayType.JDJH);
        pay.put("env", 1);// 测试: 1 生产: 0
        pay.put("ext", extmap);
        pay.put("age", 18);
        pay.put("trade_no", String.valueOf(System.currentTimeMillis()));
        pay.put("notify_url", "http://game.pay.result.callback");

        this.doPay(pay, new RXJSONCallback() {
            @Override
            public void onSuccess(@Nullable JSONObject jsonObject) {
                ToastUtils.showToast(PayTestActivity.this, "京东支付成功");
            }

            @Override
            public void onFailed(@NonNull JSONObject jsonObject) {
                Log.d(TAG, "pay failed:" + jsonObject);
                ToastUtils.showToast(PayTestActivity.this, "京东支付失败:" + jsonObject);
            }
        });
    }

    /**
     * 苏宁微信 支付
     * <p>
     * 参数请根据自己的环境配置，详见文档 https://doc.ruixuecloud.com/main/dev_doc/payment/android.html
     */
    private void doSNWXPay() {
        Map<String, Object> pay = new HashMap<>();
        Map<String, Object> extmap = new HashMap<>();
        extmap.put("appid", GlobalSettingHelper.WEIXIN_APPID);
        extmap.put(hq_key, PayType.WECHAT_PAY);
        pay.put(hq_key, PayType.SUNING);
        pay.put("env", 1);// 测试: 1 生产: 0
        pay.put("ext", extmap);
        pay.put("age", 18);
        pay.put("trade_no", String.valueOf(System.currentTimeMillis()));
        pay.put("notify_url", "http://game.pay.result.callback");

        this.doPay(pay, new RXJSONCallback() {
            @Override
            public void onSuccess(@Nullable JSONObject jsonObject) {
                ToastUtils.showToast(PayTestActivity.this, "苏宁微信支付成功");
            }

            @Override
            public void onFailed(@NonNull JSONObject jsonObject) {
                Log.d(TAG, "pay failed:" + jsonObject);
                ToastUtils.showToast(PayTestActivity.this, "苏宁微信支付失败:" + jsonObject);
            }
        });
    }

    /**
     * 银联微信 支付
     * <p>
     * 参数请根据自己的环境配置，详见文档 https://doc.ruixuecloud.com/main/dev_doc/payment/android.html
     */
    private void doYLWXPay() {
        Map<String, Object> pay = new HashMap<>();
        Map<String, Object> extmap = new HashMap<>();
        extmap.put("subAppId", GlobalSettingHelper.WEIXIN_APPID); // 吉祥捕鱼
        extmap.put(hq_key, PayType.WECHAT_PAY);
        pay.put(hq_key, PayType.AUMS);
        pay.put("env", 1);// 测试: 1 生产: 0
        pay.put("ext", extmap);
        pay.put("age", 18);
        pay.put("trade_no", String.valueOf(System.currentTimeMillis()));
        pay.put("notify_url", "http://game.pay.result.callback");

        this.doPay(pay, new RXJSONCallback() {
            @Override
            public void onSuccess(@Nullable JSONObject jsonObject) {
                ToastUtils.showToast(PayTestActivity.this, "银联微信支付成功");
            }

            @Override
            public void onFailed(@NonNull JSONObject jsonObject) {
                Log.d(TAG, "pay failed:" + jsonObject);
                ToastUtils.showToast(PayTestActivity.this, "银联微信支付失败:" + jsonObject);
            }
        });
    }

    /**
     * 银联支付宝 支付
     * <p>
     * 参数请根据自己的环境配置，详见文档 https://doc.ruixuecloud.com/main/dev_doc/payment/android.html
     */
    private void doYLAlipayPay() {
        Map<String, Object> pay = new HashMap<>();
        Map<String, Object> extmap = new HashMap<>();
        extmap.put(hq_key, PayType.ALIPAY);
        pay.put(hq_key, PayType.AUMS);
        pay.put("env", 1);// 测试: 1 生产: 0
        pay.put("ext", extmap);
        pay.put("age", 18);
        pay.put("trade_no", String.valueOf(System.currentTimeMillis()));
        pay.put("notify_url", "http://game.pay.result.callback");

        this.doPay(pay, new RXJSONCallback() {
            @Override
            public void onSuccess(@Nullable JSONObject jsonObject) {
                ToastUtils.showToast(PayTestActivity.this, "银联支付宝支付成功");
            }

            @Override
            public void onFailed(@NonNull JSONObject jsonObject) {
                Log.d(TAG, "pay failed:" + jsonObject);
                ToastUtils.showToast(PayTestActivity.this, "银联支付宝支付失败:" + jsonObject);
            }
        });
    }

    /**
     * 银联小游戏 支付
     * <p>
     * 参数请根据自己的环境配置，详见文档 https://doc.ruixuecloud.com/main/dev_doc/payment/android.html
     */
    private void doYLMiniGamePay() {
        Map<String, Object> pay = new HashMap<>();
        Map<String, Object> extmap = new HashMap<>();
        extmap.put(hq_key, "minigame");
        extmap.put("pre_mini", "Y");
        extmap.put("subAppId", GlobalSettingHelper.WEIXIN_APPID);
        pay.put(hq_key, PayType.AUMS);
        pay.put("env", 1);// 测试: 1 生产: 0
        pay.put("ext", extmap);
        pay.put("age", 18);
        pay.put("trade_no", String.valueOf(System.currentTimeMillis()));
        pay.put("notify_url", "http://game.pay.result.callback");

        this.doPay(pay, new RXJSONCallback() {
            @Override
            public void onSuccess(@Nullable JSONObject jsonObject) {
                ToastUtils.showToast(PayTestActivity.this, "银联小游戏支付成功");
            }

            @Override
            public void onFailed(@NonNull JSONObject jsonObject) {
                Log.d(TAG, "pay failed:" + jsonObject);
                ToastUtils.showToast(PayTestActivity.this, "银联小游戏支付失败:" + jsonObject);
            }
        });
    }

    /**
     * 银联云闪付 支付
     * <p>
     * 参数请根据自己的环境配置，详见文档 https://doc.ruixuecloud.com/main/dev_doc/payment/android.html
     */
    private void doYLYSFPay() {
        Map<String, Object> pay = new HashMap<>();
        Map<String, Object> extmap = new HashMap<>();
        extmap.put(hq_key, "uac");
        extmap.put("supportBank", "CCB");//目前支持：CCB（中国建设银行）、CNCB(中信银行手机银行)、CEB(光大银行手机银行)、PAB（平安口袋银行）
        extmap.put("invokeType", "APP"); //APP_H5：APP+H5 APP：APP H5：H5 默认取值APP_H5。
        pay.put(hq_key, PayType.AUMS);
        pay.put("env", 1);// 测试: 1 生产: 0
        pay.put("ext", extmap);
        pay.put("age", 18);
        pay.put("trade_no", String.valueOf(System.currentTimeMillis()));
        pay.put("notify_url", "http://game.pay.result.callback");

        this.doPay(pay, new RXJSONCallback() {
            @Override
            public void onSuccess(@Nullable JSONObject jsonObject) {
                ToastUtils.showToast(PayTestActivity.this, "银联云闪付支付成功");
            }

            @Override
            public void onFailed(@NonNull JSONObject jsonObject) {
                Log.d(TAG, "pay failed:" + jsonObject);
                ToastUtils.showToast(PayTestActivity.this, "银联云闪付支付失败:" + jsonObject);
            }
        });
    }

    /**
     * 银联微信插件 支付
     * <p>
     * 参数请根据自己的环境配置，详见文档 https://doc.ruixuecloud.com/main/dev_doc/payment/android.html
     */
    private void doYLWXPluginPay() {
        Map<String, Object> pay = new HashMap<>();
        Map<String, Object> extmap = new HashMap<>();
        extmap.put(hq_key, PayType.WECHAT_PAY);
        extmap.put("subAppId", "wx0dd648911d21b454");//支付插件
        pay.put("plugin_name", "com.jixiang.game.qipai");
        pay.put(hq_key, PayType.AUMS);
        pay.put("env", 1);// 测试: 1 生产: 0
        pay.put("ext", extmap);
        pay.put("age", 18);
        pay.put("trade_no", String.valueOf(System.currentTimeMillis()));
        pay.put("notify_url", "http://game.pay.result.callback");

        this.doPay(pay, new RXJSONCallback() {
            @Override
            public void onSuccess(@Nullable JSONObject jsonObject) {
                ToastUtils.showToast(PayTestActivity.this, "银联微信插件支付成功");
            }

            @Override
            public void onFailed(@NonNull JSONObject jsonObject) {
                Log.d(TAG, "pay failed:" + jsonObject);
                ToastUtils.showToast(PayTestActivity.this, "银联微信插件支付失败:" + jsonObject);
            }
        });
    }

    /**
     * 银联支付宝插件 支付
     * <p>
     * 参数请根据自己的环境配置，详见文档 https://doc.ruixuecloud.com/main/dev_doc/payment/android.html
     */
    private void doYLAlipayPluginPay() {
        Map<String, Object> pay = new HashMap<>();
        Map<String, Object> extmap = new HashMap<>();
        extmap.put(hq_key, PayType.ALIPAY);
        pay.put("plugin_name", "com.jixiang.game.qipai");
        pay.put(hq_key, PayType.AUMS);
        pay.put("env", 1);// 测试: 1 生产: 0
        pay.put("ext", extmap);
        pay.put("age", 18);
        pay.put("trade_no", String.valueOf(System.currentTimeMillis()));
        pay.put("notify_url", "http://game.pay.result.callback");

        this.doPay(pay, new RXJSONCallback() {
            @Override
            public void onSuccess(@Nullable JSONObject jsonObject) {
                ToastUtils.showToast(PayTestActivity.this, "银联支付宝插件支付成功");
            }

            @Override
            public void onFailed(@NonNull JSONObject jsonObject) {
                Log.d(TAG, "pay failed:" + jsonObject);
                ToastUtils.showToast(PayTestActivity.this, "银联支付宝插件支付失败:" + jsonObject);
            }
        });
    }

    private void doXsollaPay() {
        Map<String, Object> pay = new HashMap<>();
        Map<String, Object> extmap = new HashMap<>();
        extmap.put("user_name", "test");
        pay.put(hq_key, PayType.XSOLLA_INAPP);
        pay.put("env", 1);// 测试: 1 生产: 0
        pay.put("ext", extmap);
        pay.put("age", 18);
        pay.put("trade_no", String.valueOf(System.currentTimeMillis()));
        pay.put("notify_url", "http://game.pay.result.callback");

        this.doPay(pay, new RXJSONCallback() {
            @Override
            public void onSuccess(@Nullable JSONObject jsonObject) {
                ToastUtils.showToast(PayTestActivity.this, "Xsolla 支付成功");
            }

            @Override
            public void onFailed(@NonNull JSONObject jsonObject) {
                Log.d(TAG, "pay failed:" + jsonObject);
                ToastUtils.showToast(PayTestActivity.this, "Xsolla 支付失败:" + jsonObject);
            }
        });
    }

    /**
     * 银联云闪付插件 支付
     * <p>
     * 参数请根据自己的环境配置，详见文档 https://doc.ruixuecloud.com/main/dev_doc/payment/android.html
     */
    private void doYLYSFPluginPay() {
        Map<String, Object> pay = new HashMap<>();
        Map<String, Object> extmap = new HashMap<>();
        extmap.put(hq_key, "uac");
        pay.put("plugin_name", "com.jixiang.game.qipai");
        pay.put(hq_key, PayType.AUMS);
        pay.put("env", 1);// 测试: 1 生产: 0
        pay.put("ext", extmap);
        pay.put("age", 18);
        pay.put("trade_no", String.valueOf(System.currentTimeMillis()));
        pay.put("notify_url", "http://game.pay.result.callback");

        this.doPay(pay, new RXJSONCallback() {
            @Override
            public void onSuccess(@Nullable JSONObject jsonObject) {
                ToastUtils.showToast(PayTestActivity.this, "银联云闪付插件支付成功");
            }

            @Override
            public void onFailed(@NonNull JSONObject jsonObject) {
                Log.d(TAG, "pay failed:" + jsonObject);
                ToastUtils.showToast(PayTestActivity.this, "银联云闪付插件支付失败:" + jsonObject);
            }
        });
    }

    /**
     * Google 支付
     * <p>
     * 参数请根据自己的环境配置，详见文档 https://doc.ruixuecloud.com/main/dev_doc/payment/android.html
     */
    private void doGooglePay() {
        Map<String, Object> pay = new HashMap<>();
        Map<String, Object> extmap = new HashMap<>();
        pay.put(hq_key, PayType.GOOGLE);
        pay.put("env", 1);// 测试: 1 生产: 0
        pay.put("ext", extmap);
        pay.put("age", 18);
        pay.put("trade_no", String.valueOf(System.currentTimeMillis()));
        pay.put("notify_url", "http://game.pay.result.callback");

        this.doPay(pay, new RXJSONCallback() {
            @Override
            public void onSuccess(@Nullable JSONObject jsonObject) {
                ToastUtils.showToast(PayTestActivity.this, "Google Pay 支付成功");
            }

            @Override
            public void onFailed(@NonNull JSONObject jsonObject) {
                Log.d(TAG, "pay failed:" + jsonObject);
                ToastUtils.showToast(PayTestActivity.this, "Google Pay 支付失败:" + jsonObject);
            }
        });
    }

    /**
     * UPayNet 支付
     * <p>
     * 参数请根据自己的环境配置，详见文档 https://doc.ruixuecloud.com/main/dev_doc/payment/android.html
     */
    private void doUPayNet() {
        Map<String, Object> pay = new HashMap<>();
        Map<String, Object> extmap = new HashMap<>();
        extmap.put(hq_key, "net");
        extmap.put("type_id", "13");
        extmap.put("vendor", "");
        pay.put(hq_key, "upay");
        pay.put("env", 1);// 测试: 1 生产: 0
        pay.put("ext", extmap);
        pay.put("age", 18);

        pay.put("trade_no", String.valueOf(System.currentTimeMillis()));
        pay.put("notify_url", "http://game.pay.result.callback");

        this.doPay(pay, new RXJSONCallback() {
            @Override
            public void onSuccess(@Nullable JSONObject jsonObject) {
                ToastUtils.showToast(PayTestActivity.this, "UPay Net 支付成功");
            }

            @Override
            public void onFailed(@NonNull JSONObject jsonObject) {
                Log.d(TAG, "pay failed:" + jsonObject);
                ToastUtils.showToast(PayTestActivity.this, "UPay Net 支付失败:" + jsonObject);
            }
        });
    }

    /**
     * UPay WWW Card 支付
     * <p>
     * 参数请根据自己的环境配置，详见文档 https://doc.ruixuecloud.com/main/dev_doc/payment/android.html
     */
    private void doUPayWwwCard() {
        Map<String, Object> pay = new HashMap<>();
        Map<String, Object> extmap = new HashMap<>();
        extmap.put(hq_key, "ww_card");
        extmap.put("equipment_type", "2");
        extmap.put("country_name", "vietnam");
        extmap.put("vendor", "");
        pay.put("goods_tag", "upay_wwcard"); //1元吉祥捕鱼

        pay.put(hq_key, "upay");
        pay.put("env", 1);// 测试: 1 生产: 0
        pay.put("ext", extmap);
        pay.put("age", 18);

        pay.put("trade_no", String.valueOf(System.currentTimeMillis()));
        pay.put("notify_url", "http://game.pay.result.callback");

        this.doPay(pay, new RXJSONCallback() {
            @Override
            public void onSuccess(@Nullable JSONObject jsonObject) {
                ToastUtils.showToast(PayTestActivity.this, "UPay www Card 支付成功");
            }

            @Override
            public void onFailed(@NonNull JSONObject jsonObject) {
                Log.d(TAG, "pay failed:" + jsonObject);
                ToastUtils.showToast(PayTestActivity.this, "UPay www Card 支付失败:" + jsonObject);
            }
        });
    }

    /**
     * UPay Api 支付
     * <p>
     * 参数请根据自己的环境配置，详见文档 https://doc.ruixuecloud.com/main/dev_doc/payment/android.html
     */
    private void doUPayApi() {
        Log.d("WLTest", "doUPayApi");
        Map<String, Object> pay = new HashMap<>();
        Map<String, Object> extmap = new HashMap<>();
        extmap.put(hq_key, "api");
        extmap.put("equipment_type", "2");
        extmap.put("vendor", "2");
        extmap.put("card_id", "2");
        extmap.put("card_num", "2");
        pay.put(hq_key, "upay");

        pay.put("env", 1);
        pay.put("ext", extmap);
        pay.put("age", 18);

        pay.put("trade_no", String.valueOf(System.currentTimeMillis()));
        pay.put("notify_url", "http://game.pay.result.callback");

        this.doPay(pay, new RXJSONCallback() {
            @Override
            public void onSuccess(@Nullable JSONObject jsonObject) {
                ToastUtils.showToast(PayTestActivity.this, "UPay Api 支付成功");
            }

            @Override
            public void onFailed(@NonNull JSONObject jsonObject) {
                Log.d(TAG, "pay failed:" + jsonObject);
                ToastUtils.showToast(PayTestActivity.this, "UPay Api 支付失败:" + jsonObject);
            }
        });
    }

    /**
     * PayerMax 支付
     * <p>
     * 参数请根据自己的环境配置，详见文档 https://doc.ruixuecloud.com/main/dev_doc/payment/android.html
     */
    private void doPayerMaxPay() {
        Map<String, Object> pay = new HashMap<>();
        Map<String, Object> extmap = new HashMap<>();
        extmap.put("language", "zh");
        extmap.put("frontCallbackUrl", "jixiang433://");
        pay.put(hq_key, PayType.PAYERMAX);
        pay.put("currency", "MYR");//TWD,MYR
        pay.put("env", 1);// 测试: 1 生产: 0
        pay.put("ext", extmap);
        pay.put("age", 18);

        pay.put("trade_no", String.valueOf(System.currentTimeMillis()));
        pay.put("notify_url", "http://game.pay.result.callback");

        this.doPay(pay, new RXJSONCallback() {
            @Override
            public void onSuccess(@Nullable JSONObject jsonObject) {
                ToastUtils.showToast(PayTestActivity.this, "PayerMax 支付成功");
            }

            @Override
            public void onFailed(@NonNull JSONObject jsonObject) {
                Log.d(TAG, "pay failed:" + jsonObject);
                ToastUtils.showToast(PayTestActivity.this, "PayerMax 支付失败:" + jsonObject);
            }
        });
    }

    /**
     * Checkout 支付
     * <p>
     * 参数请根据自己的环境配置，详见文档 https://doc.ruixuecloud.com/main/dev_doc/payment/android.html
     */
    private void doCheckoutPay() {
        Map<String, Object> pay = new HashMap<>();
        Map<String, Object> extmap = new HashMap<>();
//        extmap.put("user_name", "test");
        extmap.put("country_code", "HK");
        extmap.put("return_url", "https://www.baidu.com?d=3&d13s=3131sjkaskdjkjasdkasdjk");
        pay.put(hq_key, PayType.CHECKOUT);
        pay.put("env", 1); // 测试: 1 生产: 0
        pay.put("is_debug", 1); // 测试: 1 生产: 0
        pay.put("goods_tag", "ios_tag");
        pay.put("currency", "HKD");
        pay.put("ext", extmap);
        pay.put("age", 18);

        pay.put("openid", "rxu7yMR4b-K6ZeqwdwytZYi1oixM0QlBGPYkW1pE");
        pay.put("h5_setting_id", 22);
        pay.put("h5_platform_id", "1716366554976new_create");

        pay.put("trade_no", String.valueOf(System.currentTimeMillis()));
        pay.put("notify_url", "http://game.pay.result.callback");

        this.doPay(pay, new RXJSONCallback() {
            @Override
            public void onSuccess(@Nullable JSONObject jsonObject) {
                ToastUtils.showToast(PayTestActivity.this, "Checkout 支付成功");
            }

            @Override
            public void onFailed(@NonNull JSONObject jsonObject) {
                Log.d(TAG, "pay failed:" + jsonObject);
                ToastUtils.showToast(PayTestActivity.this, "Checkout 支付失败:" + jsonObject);
            }
        });
    }

    /**
     * Aptoide 支付
     * <p>
     * 参数请根据自己的环境配置，详见文档 https://doc.ruixuecloud.com/main/dev_doc/payment/android.html
     * NOTE: 测试时需要手机安装 AppCoins Wallet , productId 1002 channelId 1000
     */
    private void doAptoidePay() {
        Map<String, Object> pay = new HashMap<>();
        Map<String, Object> extmap = new HashMap<>();
        pay.put(hq_key, PayType.APTOIDE);
        pay.put("ext", extmap);
        pay.put("env", 1);
        pay.put("goods_tag", "bytest");
        pay.put("currency", "USD");
        pay.put("age", 18);
        pay.put("trade_no", String.valueOf(System.currentTimeMillis()));
        pay.put("notify_url", "http://game.pay.result.callback");

        this.doPay(pay, new RXJSONCallback() {
            @Override
            public void onSuccess(@Nullable JSONObject jsonObject) {
                PayTestActivity.this.runOnUiThread(() -> ToastUtils.showToast(PayTestActivity.this, "Aptoide 支付成功"));
            }

            @Override
            public void onFailed(@NonNull JSONObject jsonObject) {
                Log.d(TAG, "pay failed:" + jsonObject);
                PayTestActivity.this.runOnUiThread(() -> ToastUtils.showToast(PayTestActivity.this, "Aptoide 支付失败:" + jsonObject));
            }
        });
    }

    private void doRXH5Pay() {
        Map<String, Object> pay = new HashMap<>();
        Map<String, Object> extmap = new HashMap<>();


        pay.put("ext", extmap);
        pay.put("hq_type", "ht");
//            pay.put("goods_tag", "843030099");
        pay.put("goods_tag", "842000099");
//            pay.put("goods_tag", "bytest");
//            pay.put("env", 1);
        pay.put("country_code", "CN");
        pay.put("auto_close", true);
        pay.put("indulge_auth", 0);
        pay.put("currency_symbol", "￥");
//            pay.put("goods_name", "测试商品");
        pay.put("return_url", "https://www.baidu.com");
        this.doPay(pay, new RXJSONCallback() {
            @Override
            public void onSuccess(@Nullable JSONObject jsonObject) {
                PayTestActivity.this.runOnUiThread(() -> ToastUtils.showToast(PayTestActivity.this, "  支付成功"));
            }

            @Override
            public void onFailed(@NonNull JSONObject jsonObject) {
                Log.d(TAG, "pay failed:" + jsonObject);
                PayTestActivity.this.runOnUiThread(() -> ToastUtils.showToast(PayTestActivity.this, "  支付失败:" + jsonObject));
            }
        });
    }

    /**
     * Unipin 支付
     * <p>
     * 参数请根据自己的环境配置，详见文档 https://doc.ruixuecloud.com/main/dev_doc/payment/android.html
     */
    private void doUnipinPay() {
        Map<String, Object> pay = new HashMap<>();
        Map<String, Object> extmap = new HashMap<>();
        pay.put(hq_key, PayType.UNIPIN);
        pay.put("ext", extmap);
        pay.put("env", 1);
        pay.put("goods_tag", "paytest");
        pay.put("currency", "IDR");
        pay.put("age", 18);
        pay.put("trade_no", String.valueOf(System.currentTimeMillis()));
        pay.put("notify_url", "http://game.pay.result.callback");

        this.doPay(pay, new RXJSONCallback() {
            @Override
            public void onSuccess(@Nullable JSONObject jsonObject) {
                PayTestActivity.this.runOnUiThread(() -> ToastUtils.showToast(PayTestActivity.this, "Unipay 支付成功"));
            }

            @Override
            public void onFailed(@NonNull JSONObject jsonObject) {
                Log.d(TAG, "pay failed:" + jsonObject);
                PayTestActivity.this.runOnUiThread(() -> ToastUtils.showToast(PayTestActivity.this, "Unipay 支付失败:" + jsonObject));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        RuiXueSdk.onActivityResult(this, requestCode, resultCode, data);
    }
}
