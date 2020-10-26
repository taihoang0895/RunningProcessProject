package com.example.processmanager;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

class AboveAndroidOProcessManager implements ProcessManager {
    @Override
    public List<String> getRunningProcessManager(Context appContext) {
        HashSet<String> listAppRunning = new HashSet<String>();
        PackageManager packageManager = appContext.getPackageManager();
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(PackageManager.GET_META_DATA);
        for (PackageInfo packetInfo :
                packageInfoList) {
            if ((packetInfo.applicationInfo.flags & ApplicationInfo.FLAG_STOPPED) == 0 && Util.isLaunchable(packageManager, packetInfo.packageName) && !Util.isSystemApp(packetInfo.applicationInfo)) {
                listAppRunning.add(packetInfo.packageName);
            }
        }
        return new ArrayList<String>(listAppRunning);
    }
}
