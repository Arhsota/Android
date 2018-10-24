package com.arhsota.android.imt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SecondActivity extends Activity {

    private TextView tvView;
    private AdView mAdViewSecond;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
      protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        MobileAds.initialize(this, "ca-app-pub-7279174300665421~3105181624");
     //   mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);



        mAdViewSecond = findViewById(R.id.adViewSecond);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdViewSecond.loadAd(adRequest);

        tvView = findViewById(R.id.result_save_out);


        String str_IMT = getIntent().getStringExtra("IMT");
        String str_Weight = getIntent().getStringExtra("Weight");
        String str_Length = getIntent().getStringExtra("Length");
        String str_Age = getIntent().getStringExtra("Age");
        String date = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(Calendar.getInstance().getTime());


        tvView.setText(date + " Ваш ИМТ: " + str_IMT + " Вес: " + str_Weight + " Рост " + str_Length + " Возраст: " + str_Age);

        Toast.makeText(SecondActivity.this, "Saving...",
                Toast.LENGTH_LONG).show();
    }
}
