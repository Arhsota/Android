package com.test.packages;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private AppManager appManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MainActivity", "Debug!");
        Log.w("MainActivity", "Warning!");
        Log.v("MainActivity", "Verbose!");
        Log.i("MainActivity", "Info!");
        Log.e("MainActivity", "ERROR!");
        Log.wtf("MainActivity", "WTF???!");
    }
}
