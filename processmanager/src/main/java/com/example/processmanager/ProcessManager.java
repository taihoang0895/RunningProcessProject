package com.example.processmanager;

import android.content.Context;

import java.util.List;

interface ProcessManager {
    List<String> getRunningProcessManager(Context appContext);
}
