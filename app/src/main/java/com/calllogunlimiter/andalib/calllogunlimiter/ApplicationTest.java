package com.calllogunlimiter.andalib.calllogunlimiter;

import android.net.Uri;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContentValues;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

class ApplicationTest implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
        if (lpparam.packageName.equals("android.provider.CallLog")) {
            XposedBridge.log("We are in Call Log ");
            ClassLoader classLoader = lpparam.classLoader;

            XC_MethodReplacement CacheUtils_getCacheDirectory = new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                    Context context = ((Context) param.args[0]);
                    Uri uri = ((Uri) param.args[1]);
                    ContentValues values = ((ContentValues) param.args[2]);

                    final ContentResolver resolver = context.getContentResolver();
                    Uri result = resolver.insert(uri, values);

                    return result;

                }
            };

            XposedHelpers.findAndHookMethod(
                    "android.provider.CallLog",
                    classLoader,
                    "addEntryAndRemoveExpiredEntries",
                    Context.class,
                    String.class,
                    CacheUtils_getCacheDirectory);

            XposedHelpers.findAndHookMethod(
                    "android.provider.CallLog",
                    classLoader,
                    "addEntryAndRemoveExpiredEntries",
                    Context.class,
                    String.class,
                    CacheUtils_getCacheDirectory);
        }
    }

}
