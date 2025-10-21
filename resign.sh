#!/bin/bash

# ==== 参数配置 ====
ORIGINAL_APK="/Users/apple_lee/Documents/ruixue/rxsdk-public-android-demo/app_overseas/build/intermediates/apk/release/app_overseas-release.apk"                     # 原始 APK 文件路径
MODIFIED_APK="/Users/apple_lee/Documents/ruixue/rxsdk-public-android-demo/app_overseas/build/intermediates/apk/release/app_overseas-release1.apk"                # 去掉 testOnly 后重新打包的 APK
SIGNED_APK="/Users/apple_lee/Documents/ruixue/rxsdk-public-android-demo/app_overseas/build/intermediates/apk/release/app_overseas-release2.apk"           # 最终签名后的 APK

KEYSTORE_PATH="./keystore/weileHall.keystore"
KEY_ALIAS="weilegame"
KEYSTORE_PASS="577588599"
KEY_PASS="577588599"

APKTOOL_PATH="apktool"                     # 确保 apktool 命令可用
APKSIGNER_PATH="apksigner"                 # 确保 apksigner 命令可用

WORK_DIR="tmp_apk"

# ==== 步骤开始 ====

echo "🛠️ 开始反编译 APK..."
$APKTOOL_PATH d "$ORIGINAL_APK" -o "$WORK_DIR" -f

echo "✂️ 修改 AndroidManifest.xml 去除 testOnly..."
MANIFEST_FILE="$WORK_DIR/AndroidManifest.xml"
if grep -q 'android:testOnly="true"' "$MANIFEST_FILE"; then
    sed -i '' 's/android:testOnly="true"//g' "$MANIFEST_FILE"
    echo "✅ 已删除 testOnly 标记"
else
    echo "ℹ️ 未发现 testOnly，无需修改"
fi

echo "📦 重新打包 APK..."
$APKTOOL_PATH b "$WORK_DIR" -o "$MODIFIED_APK"

echo "🔐 使用 apksigner 对 APK 进行 v1 + v2 签名..."
$APKSIGNER_PATH sign \
  --ks "$KEYSTORE_PATH" \
  --ks-key-alias "$KEY_ALIAS" \
  --ks-pass pass:"$KEYSTORE_PASS" \
  --key-pass pass:"$KEY_PASS" \
  --out "$SIGNED_APK" \
  "$MODIFIED_APK"

echo "🧪 验证签名结果..."
$APKSIGNER_PATH verify "$SIGNED_APK"

if [ $? -eq 0 ]; then
    echo "✅ 签名成功：$SIGNED_APK 可用于安装"
else
    echo "❌ 签名失败，请检查签名信息"
    exit 1
fi

# ==== 可选清理 ====
# rm -rf "$WORK_DIR" "$MODIFIED_APK"

echo "🎉 处理完成"