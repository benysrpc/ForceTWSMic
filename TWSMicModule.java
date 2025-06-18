package com.beni.forcetwsmic;

import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class TWSMicModule implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("com.tencent.ig") && !lpparam.packageName.equals("com.pubg.krmobile")) {
            return;
        }

        Class<?> audioManagerClass = XposedHelpers.findClass("android.media.AudioManager", lpparam.classLoader);
        XposedHelpers.findAndHookMethod(audioManagerClass, "getDevices", int.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                AudioDeviceInfo[] original = (AudioDeviceInfo[]) param.getResult();
                List<AudioDeviceInfo> filtered = new ArrayList<>();
                for (AudioDeviceInfo device : original) {
                    if (device.getType() == AudioDeviceInfo.TYPE_BLUETOOTH_SCO) {
                        filtered.add(device);
                    }
                }
                param.setResult(filtered.toArray(new AudioDeviceInfo[0]));
            }
        });

        Class<?> audioRecordClass = XposedHelpers.findClass("android.media.AudioRecord", lpparam.classLoader);
        XposedHelpers.findAndHookConstructor(audioRecordClass,
                int.class, int.class, int.class, int.class, int.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        param.args[0] = MediaRecorder.AudioSource.VOICE_COMMUNICATION;
                    }
                });
    }
}
