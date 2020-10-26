package com.example.processmanager;

import android.content.Context;
import android.os.Build;

import java.util.List;

public class CoreProcessManager implements ProcessManager{
    private ProcessManager processManager;
    public CoreProcessManager() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            processManager = new AboveAndroidOProcessManager();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                processManager = new AndroidOProcessManager();
            } else {
                processManager = new AndroidNProcessManager();
            }
        }
    }

    @Override
    public List<String> getRunningProcessManager(Context appContext) {
        return processManager.getRunningProcessManager(appContext);
    }
}
