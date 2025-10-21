package com.ruixue.sdk.demo.model;

import com.github.gzuliyujiang.wheelview.contract.TextProvider;

// Created by wangliang on 2024/5/20.
public class LanguageItem implements TextProvider {
    private String name; // 语言显示名称
    private String value; // 语言 value

    public LanguageItem() {}

    public LanguageItem(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String provideText() {
        return name;
    }
}
