/*******************************************************************************
 *
 *  * Created by Andrey Sevastianov on 12.08.20 19:21
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 05.08.20 0:04
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

public class Help extends AppCompatActivity {
    private TextView textView;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        textView = findViewById(R.id.textHelp);
        textView.setMovementMethod(new ScrollingMovementMethod());

        MobileAds.initialize(this, "ca-app-pub-7279174300665421~3105181624");
        // mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mAdView = findViewById(R.id.adViewHelp);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}