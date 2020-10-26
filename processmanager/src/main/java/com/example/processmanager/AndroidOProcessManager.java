package com.example.processmanager;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class AndroidOProcessManager implements ProcessManager {
    private static final String MATCH_PATTERN_STRING = "^.*\\s(\\d+)K.*?\\s+([\\w\\.]+)(:.*?){0,1}$";

    @Override
    public List<String> getRunningProcessManager(Context appContext) {
        Pattern pattern = Pattern.compile(MATCH_PATTERN_STRING);
        PackageManager packageManager = appContext.getPackageManager();
        HashSet<String> listAppRunning = new HashSet<String>();
        boolean infoRangeEntered = false;
        Process p;
        String command = "top -n 1 -m 20 -s rss";
        try {
            p = Runtime.getRuntime().exec(command);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (!infoRangeEntered && line.contains("PID")) {
                    infoRangeEntered = true;
                    continue;
                }
                if (infoRangeEntered) {
                    Matcher matcher = pattern.matcher(line);
                    String pkgName = getPackageName(appContext, matcher);
                    if(pkgName != null) {
                        try {
                            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(pkgName, PackageManager.GET_META_DATA);
                            if (matcher.find() && !Util.isSystemApp(applicationInfo)) {
                                listAppRunning.add(pkgName);
                            }
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                    }

                }
                p.waitFor();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        listAppRunning.addAll(getRunningService(appContext));

        return new ArrayList<>(listAppRunning);
    }
    private String getPackageName(Context context, Matcher matcher){
        try {
            return matcher.group(2);
        } catch (Exception e) {

        }
        return null;
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
                    if(!Util.isSystemApp(applicationInfo)){
                        processMap.add(info.service.getPackageName());
                    }

                }catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }
        return processMap;
    }
}
