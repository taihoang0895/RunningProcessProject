package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.processmanager.CoreProcessManager;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CoreProcessManager coreProcessManager = new CoreProcessManager();
        List<String> result =  coreProcessManager.getRunningProcessManager(this);
        for (String pkg : result){
            Log.d("taih", "pkg " + pkg);
        }
    }
}