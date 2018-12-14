package com.arhsota.android.imt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
// import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class History extends Activity {

    private TextView tvHistory;
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
    private String str_History;
    private String line;

    //   private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        //  MobileAds.initialize(this, "ca-app-pub-7279174300665421~3105181624");
        //   mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);



        mAdViewSecond = findViewById(R.id.adViewSecond);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdViewSecond.loadAd(adRequest);

        tvHistory = findViewById(R.id.result_history);


      /*  str_IMT = getIntent().getStringExtra("IMT");
        str_Weight = getIntent().getStringExtra("Weight");
        str_Length = getIntent().getStringExtra("Length");
        str_Age = getIntent().getStringExtra("Age");
        str_Date = getIntent().getStringExtra("Date");
*/
        readFile();
        //  String date = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(Calendar.getInstance().getTime());
        tvHistory.setText (str_History + "History" + line);


     //   tvView.setText(str_Date + " Ваш ИМТ: " + str_IMT + " Вес: " + str_Weight + " Рост " + str_Length + " Возраст: " + str_Age);

        Toast.makeText(History.this, "Reading...",
                Toast.LENGTH_LONG).show();
    }
    void readFile() {
    /*    try {
            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    openFileInput(FILENAME)));
            String str  = "";
            // читаем содержимое
            while ((str = br.readLine()) != null) {
                Log.d(LOG_TAG, str);
            }
            str_History = str;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        try {
            InputStream inputStream = openFileInput(FILENAME);

            if (inputStream != null) {
                InputStreamReader isr = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(isr);
             //   String line;
                StringBuilder builder = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    builder.append(line + "\n");
                 //   Log.d ("History","IMT");
                }

                inputStream.close();
              //  mEditText.setText(builder.toString());
            }
        } catch (Throwable t) {
            Toast.makeText(getApplicationContext(),
                    "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
        }
    }


}

