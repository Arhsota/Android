package com.arhsota.android.imt;

// version 2.7 some improvements such as normal input and translation in English version
// version 2.8 adding progress bar
// version 2.8.1 move button calculate a little bit higher
// and a little bit in text in main string with data
// my first real soft based on lesson 8 Skillberg
// calculating index body fat based on your weight and length both for male and female
// Sevastyanov Andrey, 2019, 2020
// Arkhangelsk, Sabetta
// Copyright (c) all rights reserved


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;


import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;
//import android.support.v7.widget.ShareActionProvider;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.view.MenuItemCompat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
//import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.text.SimpleDateFormat;
import java.util.Calendar;


//import com.google.android.gms.ads.reward.RewardedVideoAd;



public class MainActivity extends AppCompatActivity {

    private AdView mAdView;
//    private FirebaseAnalytics mFirebaseAnalytics;
    private ShareActionProvider shareActionProvider;

 //   private RewardedVideoAd mRewardedVideoAd;

    private TextView textView;
    private TextView textViewTable;
    private TextView textMinImt;
    private TextView textMaxImt;

    private EditText editTextW;
    private EditText editTextL;
    private EditText editTextAge;
//    private Button button;
    private   double myweight;
    // str_XXX for intent
    private   String str_IMT ;
    private   String str_IMT_mail ;

    private   String str_Weight= "0";
    private   String str_Length = "0";
    private   String str_Age = "0";
    private   String str_Date = "0";

    private   double mylength;
    private   double myage;
    private   double myresult;

    private   boolean fillTextW = false;  // checking for filling all 3 input parametres
    private   boolean fillTextL = false;
    private   boolean fillTextA = false;

    final String LOG_TAG = "myLogs";

    final String FILENAME = "file";

    final String DIR_SD = "MyFiles";
    final String FILENAME_SD = "fileSD";

    private InterstitialAd mInterstitialAd;


    private final String YOUR_ADMOB_APP_ID = "ca-app-pub-7279174300665421~3105181624";

    private final int num19 = 19;
    private final int num20 = 20;
    private final int num21 = 21;
    private final int num22 = 22;
    private final int num23 = 23;
    private final int num24 = 24;
    private final int num25 = 25;
    private final int num26 = 26;
    private final int num27 = 27;
    private final int num28 = 28;
    private final int num29 = 59;
    private final int num34 = 34;
    private final int num44 = 44;
    private final int num54 = 54;
    private final int num64 = 64;



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Заполнение меню; элементы (если они есть) добавляются на панель действий.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }


   @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.action_create_save:
                //Код, выполняемый при выборе элемента Create Save
                Intent intent = new Intent(this, SecondActivity.class);
                intent.setType("text/plain");
                intent.putExtra("IMT", str_IMT);
                intent.putExtra("Weight", str_Weight);
                intent.putExtra("Length", str_Length);
                intent.putExtra("Age", str_Age);
                intent.putExtra("Date", str_Date);
             //   str_IMT_mail= str_Date + " Ваш ИМТ: " + str_IMT + " Вес: " + str_Weight + " Рост " + str_Length + " Возраст: " + str_Age;
                startActivity(intent);
            //    textViewW.setText(getString(R.string.name_text_table_detail_weight,str_weight));

                return true;
            case R.id.action_share:
                Intent intent_mes = new Intent(Intent.ACTION_SEND);
                intent_mes.setType("text/plain");
                intent_mes.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.for_subject_field) + str_Date);
                intent_mes.putExtra(Intent.EXTRA_TEXT, str_Date + " "+ getString(R.string.my_IMT) + " " + str_IMT
                        + " " + getString(R.string.my_weight) + " " +str_Weight + " " + getString(R.string.my_length)
                        + " " + str_Length + " " + getString(R.string.my_age) + " " +str_Age);
//          page 143 always choose intent
                String chooserTitle = getString(R.string.share);
                Intent chosenIntent = Intent.createChooser(intent_mes, chooserTitle);
                startActivity(chosenIntent);
                return true;

            case R.id.action_history:
               Intent intent_history = new Intent(this, History.class);
                startActivity(intent_history);
                return true;

//todo
            case R.id.action_sign_up:
                // todo: NOTHING YET, waiting for next version
               // Intent intent_main_activity_sign = new Intent(this, MainActivitySign.class);
               // startActivity(intent_main_activity_sign);
                Toast.makeText(MainActivity.this, R.string.in_work,
                        Toast.LENGTH_LONG).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

     //   Toolbar toolbar = findViewById(R.id.toolbar);

     //  setSupportActionBar(toolbar);



        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713 for advertisment 58fd171274ed00c90079860acbfcfda3
//        test id for banner ca-app-pub-3940256099942544/6300978111 for layout XML
//        working real banner ca-app-pub-7279174300665421/8731793267
//        test id for interpage ca-app-pub-3940256099942544/1033173712 for layout XML
//        working real page ca-app-pub-7279174300665421/5898016751

        MobileAds.initialize(this, "ca-app-pub-7279174300665421~3105181624");
       // mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7279174300665421/5898016751");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });


        textView = findViewById(R.id.result_out);

        textViewTable = findViewById(R.id.text_table);

        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(0);

        textMinImt = findViewById(R.id.minImt);
        textMaxImt = findViewById(R.id.maxImt);

        final Button button = findViewById(R.id.calculate_btn);
        editTextW = findViewById(R.id.weight);
        editTextL = findViewById(R.id.length);
        editTextAge = findViewById(R.id.age);


//        reading weight
        editTextW.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textViewTable.setText("");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                fillTextW = editable.toString().trim().length() > 0;
                if ((fillTextW == true) && (fillTextL == true) && (fillTextA == true)) {
                    button.setEnabled(true);
                }
                else {
                    button.setEnabled(false);
                }
            }


          }
        );

        button.setOnClickListener(onClickListener);
// reading length
        editTextL.addTextChangedListener(new TextWatcher() {
            @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                 }

            @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                 }

            @Override
                 public void afterTextChanged(Editable editable) {
                fillTextL = editable.toString().trim().length() > 0;

                if ((fillTextW == true) && (fillTextL == true) && (fillTextA == true) ) {
                    button.setEnabled(true);
                }
                else {
                    button.setEnabled(false);
                }

              }
        }
        );

        button.setOnClickListener(onClickListener);
// reading age
        editTextAge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
              }

            @Override
             public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               }

             @Override
              public void afterTextChanged(Editable editable) {
                 fillTextA = editable.toString().trim().length() > 0;
                 if ((fillTextW == true) && (fillTextL == true) && (fillTextA == true)) {
                     button.setEnabled(true);
                 }
                 else {
                     button.setEnabled(false);
                 }
              }
         }
        );

        button.setOnClickListener(onClickListener);
    }


    private final View.OnClickListener onClickListener = new View.OnClickListener() {

        public void onClick(View view) {


            KeyboardHide.hide(view); // Hide keyboard after click on button CALCULATE
            // interpage adds
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }
            String weight = editTextW.getText().toString();
            String length = editTextL.getText().toString();
            String age = editTextAge.getText().toString();
            str_Weight = weight;
            str_Length = length;
            str_Age = age;

            String date = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(Calendar.getInstance().getTime());
            str_Date = date;
           // str_Date = "MyDate";

            myweight = Double.parseDouble(weight);
            mylength = Double.parseDouble(length)/100;
            myage = Double.parseDouble(age);
//            Not devide to zero
            if (mylength == 0)  {
               mylength = 1;
            }
            myresult = myweight/(mylength * mylength); //calculating IMT
            str_IMT =  String.format("%.2f",myresult); // making string format
            ProgressBar progressBar = findViewById(R.id.progressBar);
            textMinImt = findViewById(R.id.minImt);
            textMaxImt = findViewById(R.id.maxImt);

            if (myage < num19) {
                Toast.makeText(MainActivity.this, R.string.too_young,
                      Toast.LENGTH_LONG).show();

            }
            if ((myage >= num19) && (myage <= num24)){

                textViewTable.setText( R.string.text_table3);

                progressBar.setMax(num24);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    progressBar.setMin(num19);
                }
                progressBar.setProgress((int) myresult);
                textMinImt.setText(String.valueOf(num19));
                textMaxImt.setText(String.valueOf(num24));

            }
            if ((myage > num24) && (myage <= num34)){
                textViewTable.setText(R.string.text_table4);
                progressBar.setMax(num25);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    progressBar.setMin(num20);
                }
                progressBar.setProgress((int) myresult);
                textMinImt.setText(String.valueOf(num20));
                textMaxImt.setText(String.valueOf(num25));
            }
            if ((myage > num34) && (myage <= num44)){
                textViewTable.setText(R.string.text_table5);
                progressBar.setMax(num26);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    progressBar.setMin(num21);
                }
                progressBar.setProgress((int) myresult);
                textMinImt.setText(String.valueOf(num21));
                textMaxImt.setText(String.valueOf(num26));

            }
            if ((myage > num44) && (myage <= num54)){
                textViewTable.setText(R.string.text_table6);
                progressBar.setMax(num27);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    progressBar.setMin(num22);
                }
                progressBar.setProgress((int) myresult);
                textMinImt.setText(String.valueOf(num22));
                textMaxImt.setText(String.valueOf(num27));

            }
            if ((myage > num54) && (myage <= num64)){
                textViewTable.setText(R.string.text_table7);
                progressBar.setMax(num28);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    progressBar.setMin(num23);
                }
                progressBar.setProgress((int) myresult);
                textMinImt.setText(String.valueOf(num23));
                textMaxImt.setText(String.valueOf(num28));

            }

            if (myage > num64) {
                textViewTable.setText(R.string.text_table8);
                progressBar.setMax(num29);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    progressBar.setMin(num24);
                }
                progressBar.setProgress((int) myresult);
                textMinImt.setText(String.valueOf(num24));
                textMaxImt.setText(String.valueOf(num29));

            }


//            String myresultStr = Double.toString(myresult);

            String myresultStrFormat = String.format("%.2f",myresult);
            textView.setText(getString(R.string.result_text,myresultStrFormat));

            if (myresult < num19) {
                textView.setTextColor(Color.RED);
                }
            if ((myresult >= num19) && (myresult <=num24)) {
                textView.setTextColor(Color.BLACK);
            }
            if ((myresult > num25) && (myresult <= num28)) {
                textView.setTextColor(Color.BLUE);
            }
            if ((myresult > num28) && (myresult <= 30)) {
                textView.setTextColor(Color.MAGENTA);
            }
                        if ((myresult > 30)) {
                textView.setTextColor(Color.RED);
            }

        }

    };

    public void signClick(View view) {
        Intent intent_main_activity_sign = new Intent(this, MainActivitySign.class);
        startActivity(intent_main_activity_sign);
    }
}
