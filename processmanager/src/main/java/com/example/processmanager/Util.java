package com.example.processmanager;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Method;

public class Util {
    public static Boolean isSystemApp(ApplicationInfo applicationInfo) {
        try {
            return (Build.DEVICE.contains("huawei") || Build.DEVICE.contains("HUAWEI")) ? p(applicationInfo) : d(applicationInfo);
        } catch (Exception e) {
            return false;
        }

    }

    public static boolean isLaunchable(PackageManager packageManager, String pkgName) {
        return packageManager.getLaunchIntentForPackage(pkgName) != null;
    }

    private static boolean d(ApplicationInfo applicationInfo) {
        return (applicationInfo.flags & 1) > 0;
    }

    private static boolean p(ApplicationInfo applicationInfo) {
        boolean z = false;
        if (applicationInfo == null) {
            return true;
        }
        try {
            for (Method method : applicationInfo.getClass().getMethods()) {
                method.setAccessible(true);
                if (method.getReturnType() == String.class) {
                    try {
                        Log.d("libDevice", method.getName() + ":" + ((String) method.invoke(applicationInfo, new Object[0])));
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("libDevice", "e:" + e.getMessage());
                    }
                }
            }
            Method method2 = applicationInfo.getClass().getMethod("getResourcePath", new Class[0]);
            try {
                method2.setAccessible(true);
                String str = (String) method2.invoke(applicationInfo, new Object[0]);
                if (str.startsWith("/system")) {
                    z = true;
                } else {
                    if (str.startsWith("/data")) {
                        z = false;
                    }
                    z = true;
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                Log.d("libDevice", "err:" + e2.getMessage() + " cause:" + e2.getCause());
            }
            return z;
        } catch (NoSuchMethodException e3) {
            Log.d("libDevice", "e:" + e3.getMessage() + " " + e3.getCause());
            e3.printStackTrace();
            return true;
        }
    }


}
