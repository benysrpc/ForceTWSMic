
package com.openai.forcetwsmic;

import android.media.MediaRecorder;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class ForceMicHook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedHelpers.findAndHookConstructor("android.media.AudioRecord", lpparam.classLoader,
            int.class, int.class, int.class, int.class, int.class,
            new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    int source = (int) param.args[0];
                    if (source == MediaRecorder.AudioSource.MIC ||
                        source == MediaRecorder.AudioSource.DEFAULT ||
                        source == MediaRecorder.AudioSource.CAMCORDER) {
                        param.args[0] = MediaRecorder.AudioSource.VOICE_COMMUNICATION;
                        XposedBridge.log("[ForceTWSMic] Intercepted mic source. Forced to VOICE_COMMUNICATION.");
                    }
                }
            }
        );
    }
}
