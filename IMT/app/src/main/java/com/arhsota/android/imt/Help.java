/*******************************************************************************
 *
 *  * Created by Andrey Sevastianov on Septenber 2018
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 25.12.2021, 15:21
 *
 ******************************************************************************/

package com.arhsota.android.imt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class Help extends AppCompatActivity {
    private TextView textView;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        textView = findViewById(R.id.textHelp);
        textView.setMovementMethod(new ScrollingMovementMethod());

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }

        });

        mAdView = findViewById(R.id.adViewHelp);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}