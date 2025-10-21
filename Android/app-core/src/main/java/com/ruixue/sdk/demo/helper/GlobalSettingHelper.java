package com.ruixue.sdk.demo.helper;

import androidx.annotation.Nullable;

import com.ruixue.RXJSONCallback;
import com.ruixue.RuiXueSdk;
import com.ruixue.sdk.demo.main.BuildConfig;
import com.ruixue.sdk.demo.model.LanguageItem;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

// 演示用来保存配置的全局 Helper 工具类
// Created by wangliang on 2024/5/16.
public class GlobalSettingHelper {


    public static final String OVERSEAS_CPID = "119";
    public static final String OVERSEAS_PRODUCT_ID = "SDKOS";
    public static final String OVERSEAS_CHANNEL_ID = "iOSOS";
    public static final String OVERSEAS_BASE_URL = "http://os-api-test.ruixueyun.com/";

    public static final String CPID = "114";
    public static final String PRODUCT_ID = "1002";
    public static final String CHANNEL_ID = "100";
    public static final String BASE_URL = "http://cn-api-test.ruixueyun.com/";

    // SDK 支持的多语言
    private static final String ENGLISH = "en"; // 英语
    private static final String CHINESE = "zh"; // 简体中文
    private static final String JAPAN = "ja"; // 日本语
    private static final String TRADITIONAL_CHINESE = "tc"; // 繁体中文
    private static final String PHILIPPINES = "tl"; // 菲律宾语
    private static final String THAI = "th"; // 泰语
    private static final String VIETNAMESE = "vi"; // 越南文
    private static final String INDONESIAN = "id"; // 印尼语

    // 三方自己的测试参数，这里为了方便查看，都放在这里
    public static final String ALIKEY = "rPTnFy5eAxZTuLVBuur5JXDluwkT7B9jyypi1NselETj6YyYHJuhhKz5kkzIVH1FXa3SB6yMkKPK4vQkOH/9pCeGL/smhwzEwRnp9osz2CXighagGFJvtNyKLs0Wf78ZM6a9ntvzBJ4Fw3WqguPPIzQGPKkIVAfovaJZV9rTBb8hwmgiyAW8x6O0zfZ+exScIX6zJ+UVZTxmTxYl3gSyY41/4U5nTP2W0nljFqLasLiCSluWZNWQ2bfBgf1BkqC6tExfNAnvFq5be2MVnUXStsp6jTOMOXBKZdnSjMfGeOI=";
    //    public static final String ALIKEY = "rPTnFy5eAxZTuLVBuur5JXDluwkT7B9jyypi1NselETj6YyYHJuhhKjBtqm05q2t83HyqD2ybRT8U9qGU1eb2MRsQuWsEor1H2Z/K7ha9j+v3UF//lQnZfksSFZoSrRi80jIEgYeqUSK0RhCkkPA3i4j+UR1WR6P7sndv20K6fw/HU66L7s4u2ry3dcb+GDnPylZ9ixx2QWnYXN4ReDxZLlMSpzZEpaj7/nZ1LwEzZ8JRyV1PjzT+N6UwcgnvQyOaq9IskxjQYdLqeZOCMLlM4Lemo32iwtT/2bZzUzkZYQRf8XHH90YYw==";
//    public static final String WEIXIN_APPID = "wx0e8330405b5fab6b";
    public static final String WEIXIN_APPID = "wxd9cba83a0a1ef20d";

    public static int debug_cp_id;
    public static String debug_base_url = "";
    public static boolean ruixueLogin = false;

    static class Single {
        final static GlobalSettingHelper INSTANCE = new GlobalSettingHelper();
    }

    private GlobalSettingHelper() {
        languageItems.clear();
        languageItems.add(new LanguageItem("简体中文", CHINESE));
        languageItems.add(new LanguageItem("繁体中文", TRADITIONAL_CHINESE));
        languageItems.add(new LanguageItem("英语", ENGLISH));
        languageItems.add(new LanguageItem("日语", JAPAN));
        languageItems.add(new LanguageItem("印尼语", INDONESIAN));
        languageItems.add(new LanguageItem("菲律宾语", PHILIPPINES));
        languageItems.add(new LanguageItem("泰语", THAI));
        languageItems.add(new LanguageItem("越南语", VIETNAMESE));

        currentLanguage = languageItems.get(0);
    }

    public static GlobalSettingHelper getInstance() {
        return Single.INSTANCE;
    }

    private String channelName = "ruixue";

    private List<LanguageItem> languageItems = new ArrayList<>();
    private LanguageItem currentLanguage;

    private boolean isOverseas = false;

    private boolean landscape = true; //是否为横屏

    private boolean isCaptchaLogin = true;  //是否为验证码登录

    private List<String> quickLoginMethods = new ArrayList<>();

    public List<String> getQuickLoginMethods() {
        return quickLoginMethods;
    }

    public void setQuickLoginMethods(List<String> quickLoginMethods) {
        this.quickLoginMethods.clear();
        this.quickLoginMethods.addAll(quickLoginMethods);
    }

    public List<LanguageItem> getLanguageItems() {
        return languageItems;
    }

    public LanguageItem getCurrentLanguage() {
        return currentLanguage;
    }

    public void setCurrentLanguage(LanguageItem item) {
        this.currentLanguage = item;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    // 海外默认渠道
    public boolean isOverseas() {
        return channelName.equals("overseas");
    }

    // 国内默认渠道
    public boolean isRuixueChannel() {
        return channelName.equals("ruixue");
    }

    public void setOverseas(boolean overseas) {
        isOverseas = overseas;
    }

    public boolean isLandscape() {
        return landscape;
    }

    public void setLandscape(boolean landscape) {
        this.landscape = landscape;
    }

    public boolean isCaptchaLogin() {
        return isCaptchaLogin;
    }

    public void setIsCaptchaLogin(boolean isCaptchaLogin) {
        this.isCaptchaLogin = isCaptchaLogin;
    }


    public void initialize(boolean isOverseas, RXJSONCallback callback) {
        String cpid;
        String productId;
        String channelId;
        List<String> hostUrls = new ArrayList<>();

        if (isOverseas) {
            cpid = /*"1000040"; */GlobalSettingHelper.OVERSEAS_CPID;
            productId = /*"265"; */GlobalSettingHelper.OVERSEAS_PRODUCT_ID;
            channelId = /*"2002"; */ GlobalSettingHelper.OVERSEAS_CHANNEL_ID;
//                hostUrls.add("https://rxapi.fishinggamezone.com/");
            hostUrls.add(GlobalSettingHelper.OVERSEAS_BASE_URL);
        } else {
            cpid = GlobalSettingHelper.CPID;
            productId = GlobalSettingHelper.PRODUCT_ID;
            channelId = GlobalSettingHelper.CHANNEL_ID;
            hostUrls.add(GlobalSettingHelper.BASE_URL);
        }
        this.isOverseas = isOverseas;
        RuiXueSdk.initialize(cpid, productId, channelId, hostUrls, callback);
    }

}

