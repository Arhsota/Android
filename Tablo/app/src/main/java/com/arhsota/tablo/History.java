/*******************************************************************************
 *
 *  * Created by Andrey Sevastianov on 10 january 2019
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 25.01.2022, 19:53
 *
 ******************************************************************************/

package com.arhsota.tablo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
// import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class History extends Activity {



    final String LOG_TAG = "myLogs";

    final String FILESAVE = "filesave";

    final String DIR_SD = "MyFiles";
    final String FILENAME_SD = "fileSD";
    private String str_Date;
    private String str_IMT;
    private String str_Weight;
    private String str_Length;
    private String str_Age;
    private String str_History;
    private String line;
    String[] arraystrIMT;
    //   private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        TextView tvHistory; /* local var */
        AdView mAdViewSecond; /* local var */

        //  MobileAds.initialize(this, "ca-app-pub-7279174300665421~3105181624");
        //   mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);




        mAdViewSecond = findViewById(R.id.adViewSecond);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdViewSecond.loadAd(adRequest);

//        tvHistory = findViewById(R.id.result_history);
//        tvHistory.setMovementMethod(new ScrollingMovementMethod());


      /*  str_IMT = getIntent().getStringExtra("IMT");
        str_Weight = getIntent().getStringExtra("Weight");
        str_Length = getIntent().getStringExtra("Length");
        str_Age = getIntent().getStringExtra("Age");
        str_Date = getIntent().getStringExtra("Date");
*/

        readFile();

        //  String date = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(Calendar.getInstance().getTime());
//        tvHistory.setText (str_History );


        //   tvView.setText(str_Date + " Ваш ИМТ: " + str_IMT + " Вес: " + str_Weight + " Рост " + str_Length + " Возраст: " + str_Age);
//        If you click History when you installed app for the first time
        if (arraystrIMT == null) {
            Toast.makeText(History.this, R.string.nohistory,
                    Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(History.this, R.string.reading,
                    Toast.LENGTH_LONG).show();

            // находим список
            ListView lvMain = (ListView) findViewById(R.id.lvMain);

            // создаем адаптер
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, arraystrIMT);

            // присваиваем адаптер списку
            lvMain.setAdapter(adapter);
        }
    }
    void readFile() {

        try {
            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    openFileInput(FILESAVE)));
            String str  = "";

            // читаем содержимое
//            while ((str = br.readLine()) != null) {
            while ((str =br.readLine()) != null) {
                arraystrIMT = str.split(",");
                Log.d(LOG_TAG, str);
                str_History = str;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}

