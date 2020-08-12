/*******************************************************************************
 *
 *  * Created by Andrey Sevastianov on 12.08.20 19:21
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12.08.20 18:25
 *
 ******************************************************************************/

package com.arhsota.android.imt;

import android.app.Activity;
import android.os.Bundle;
//import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
// import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class SecondActivity extends Activity {

    private TextView tvView;
    private AdView mAdViewSecond;

    final String LOG_TAG = "myLogs";

    final String FILENAME = "file";

    final String DIR_SD = "MyFiles";
    final String FILENAME_SD = "fileSD";
    private String str_Date;
    private String str_IMT;
    private String str_Weight;
    private String str_Length;
    private String str_Age;
    private String str_Main_Output;
 //   private FirebaseAnalytics mFirebaseAnalytics;


    @Override
      protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

      //  MobileAds.initialize(this, "ca-app-pub-7279174300665421~3105181624");
     //   mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

//        String eol = System.getProperty("line.separator");

        mAdViewSecond = findViewById(R.id.adViewSecond);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdViewSecond.loadAd(adRequest);

        tvView = findViewById(R.id.result_save_out);


        str_IMT = getIntent().getStringExtra("IMT");
        str_Weight = getIntent().getStringExtra("Weight");
        str_Length = getIntent().getStringExtra("Length");
        str_Age = getIntent().getStringExtra("Age");
        str_Date = getIntent().getStringExtra("Date");
        str_Main_Output = getString(R.string.date) + " " + str_Date + ", " + getString(R.string.my_IMT) + " " + str_IMT + ", " +
                getString(R.string.my_weight) + " " + str_Weight + ", " + getString(R.string.my_length) + str_Length + ", " +
                getString(R.string.my_age) + " "+ str_Age + " | " ;
        writeFile();
      //  String date = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(Calendar.getInstance().getTime());


        tvView.setText(str_Main_Output);

        Toast.makeText(SecondActivity.this, R.string.saving,
                Toast.LENGTH_LONG).show();
    }
    void writeFile() {
        try {

            // отрываем поток для записи

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    openFileOutput(FILENAME, MODE_APPEND)));
            // пишем данные
            bw.append(str_Main_Output);


            // закрываем поток
            bw.close();
            Log.d(LOG_TAG, "Файл записан");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
