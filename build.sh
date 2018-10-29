#!/usr/bin/env bash

# 为gradlew可执行权限
chmod +x gradlew

clean="0"
install="0"
beta="0"
release="0"

# 定义apk生成目录
debugApkPath="app/build/outputs/apk/debug/"
betaApkPath="app/build/outputs/apk/beta/"
releaseApkPath="app/build/outputs/apk/release/"

while getopts ':cibr' opt
do
    case ${opt} in
        c)
            clean="1"
        ;;
        b)
            beta="1"
        ;;
        i)
            install="1"
        ;;
        r)
            # Priority of $release is higher than $beta
            release="1"
        ;;
        *)
            echo "command not supported yet"
            exit
        ;;
    esac
done

build() {
    if [ "$release" = "1" ]; then
        ./gradlew clean assembleRelease
    elif [ "$beta" = "1" ]; then
        ./gradlew clean assembleBeta
    else
        ./gradlew assembleDebug
    fi
    return
}

# 编译debug版本apk
if [ "$clean" = "1" ]; then
    echo "Clean and build"
    ./gradlew clean
    build
else
    echo "simple build"
    build
fi

if [ "$release" = "1" ]; then
    echo "Build Release Version"
    apkPath=${releaseApkPath}
elif [ "$beta" = "1" ]; then
    echo "Build Beta Version"
    apkPath=${betaApkPath}
else
    echo "Build Debug Version"
    apkPath=${debugApkPath}
fi

# 按时间倒序查找最新的apk
apkName=($(find ${apkPath} -name *.apk|xargs ls -ta))
# 取第一个Apk,并获取文件名
apkName=${apkName[0]##*/}
echo "APK Name：$apkName"

# 签名
#echo "Signing apk..." &&
#java -Djava.library.path="security/" -jar security/signapk.jar \
#security/releasekey.x509.pem security/releasekey.pk8 \
#"$apkPath$apkName" \
#"$apkPath$apkName".tmp &&
#
#mv "$apkPath$apkName".tmp "$apkPath$apkName"
#
#echo "Signing Done" &&

if [ "$install" = "1" ]; then
    echo "Install..."
    # 安装
    adb install -r "$apkPath$apkName" &&

    # kill之前运行的camera进程并启动新的camera实例
    adb shell am force-stop "com.cloudminds.camera" &&
    adb shell am start -n "com.cloudminds.camera/com.android.camera.CameraActivity"
else
    echo "Signed Apk is generated at:$apkPath$apkName"
fi

now=$(date +%F" "%H:%M:%S) && echo "Build Time:$now"