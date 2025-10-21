package com.ruixue.sdk.demo.activity;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class PayType {
    public static final String KEY = "pay_type";
    public static final String WECHATH5 = "wechath5";
    public static final String WECHAT_PAY = "wechat";
    public static final String WECHAT = "wechat";
    public static final String BAIDUNET = "baidunet";

    public static final String MI = "mi";
    public static final String HWJOS = "hwjos";
    public static final String HONOR = "honor";
    public static final String HIHONOR = "hihonor";
    public static final String OPPO = "oppo";
    public static final String VIVO = "vivo";
    public static final String KSLY = "ksly";
    public static final String DOUYIN = "douyin";
    public static final String MIDAS = "midas";
    public static final String YSDK = "ysdk";
    public static final String AUMS = "aums";
    public static final String GOOGLE = "google";
    public static final String REALIPAY = "realipay";
    public static final String ALIPAY = "alipay";

    public static final String XSOLLA_INAPP = "xsolla_inapp";
    public static final String XSOLLA = "xsolla_inapp";
    public static final String JDJH = "jdjh";

    public static final String PAYERMAX = "payermax";

    public static final String RUIXUE_H5_TRADE = "ruixue_h5_trade";
    public static final String HT = "ht";
    public static final String ALIPAYH5 = "aph";
    public static final String BILIBILI = "bilibili";

    public static final String M4399 = "4399_mobilegame";
    public static final String UPAY = "upay";

    public static final String YEEPAY = "yeepay";
    public static final String APTOIDE = "aptoide";
    public static final String SUNING = "suning";
    public static final String QOO = "qoo";
    public static final String UNIPIN = "unipin";
    public static final String CHECKOUT = "checkout";
    public static final String MUMU = "mumu";
    public static final String M9GAME = "jiuyou";
    public static final String LEIDIAN = "leidian";

    @Retention(RetentionPolicy.SOURCE)


    @StringDef({WECHATH5, BAIDUNET, MI, HWJOS, HIHONOR, OPPO, VIVO, KSLY, DOUYIN, MIDAS, AUMS, GOOGLE, REALIPAY, ALIPAYH5, JDJH, UPAY, M9GAME})

    public @interface PayTypeDef {
    }

    public final String payType;

    public static boolean isAlipay(String payType) {
        return (payType.equals("aph")|| payType.equals(PayType.ALIPAYH5) || payType.equals(PayType.ALIPAY) || payType.equals(PayType.REALIPAY));
    }

    public PayType(@PayTypeDef String payType) {
        this.payType = payType;
    }

}
