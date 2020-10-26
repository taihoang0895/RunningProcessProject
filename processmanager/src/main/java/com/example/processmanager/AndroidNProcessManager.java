package com.example.processmanager;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.example.processmanager.androidprocess.AndroidProcesses;
import com.example.processmanager.androidprocess.models.AndroidAppProcess;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

class AndroidNProcessManager implements ProcessManager {
    @Override
    public List<String> getRunningProcessManager(Context appContext) {
        HashSet<String> listAppRunning = new HashSet<String>();
        List<AndroidAppProcess> androidAppProcessesList = AndroidProcesses.getRunningAppProcesses();
        for (AndroidAppProcess process :
                androidAppProcessesList) {

            listAppRunning.add(process.getPackageName());
        }
        listAppRunning.addAll(getRunningService(appContext));
        return new ArrayList<>(listAppRunning);
    }

    private static HashSet<String> getRunningService(Context context) {
        PackageManager packageManager = context.getPackageManager();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        HashSet<String> processMap = new HashSet<>();
        if (activityManager != null) {
            List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(15);
            for (ActivityManager.RunningServiceInfo info :
                    serviceList) {
                try {
                    ApplicationInfo applicationInfo = packageManager.getApplicationInfo(info.service.getPackageName(), PackageManager.GET_META_DATA);
                    if (!Util.isSystemApp(applicationInfo)) {
                        processMap.add(info.service.getPackageName());
                    }

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return processMap;
    }

}
