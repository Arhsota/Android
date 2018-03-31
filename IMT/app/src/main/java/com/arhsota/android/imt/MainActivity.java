package com.arhsota.android.imt;

// version 1.3
// my first real soft based on lesson 8 Skillberg
// calculating index body fat based on your weight and length both for male and female
// Sevastyanov Andrey, 2018, march
// Sabetta
// Special thanks to A. Bakulina for testing this app

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.reward.RewardedVideoAd;



public class MainActivity extends AppCompatActivity {

    private AdView mAdView;

 //   private RewardedVideoAd mRewardedVideoAd;

    private TextView textView;
    private TextView textViewW;
    private TextView textViewL;
    private TextView textViewAge;
    private TextView textViewTable3;
    private TextView textViewTable4;
    private TextView textViewTable5;
    private TextView textViewTable6;
    private TextView textViewTable7;
    private TextView textViewTable8;
    private EditText editTextW;
    private EditText editTextL;
    private EditText editTextAge;
//    private Button button;
    private   double myweight;
    private   double mylength;
    private   double myage;
    private   double myresult;

    private   boolean fillTextW = false;  // checking for filling all 3 input parametres
    private   boolean fillTextL = false;
    private   boolean fillTextA = false;

    private final String YOUR_ADMOB_APP_ID = "ca-app-pub-7279174300665421~3105181624";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713 for advertisment 58fd171274ed00c90079860acbfcfda3
//        test id ca-app-pub-3940256099942544/6300978111 for layout XML

        MobileAds.initialize(this, "ca-app-pub-7279174300665421~3105181624");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


/*
       mAdView2 = findViewById(R.id.adView2);
       AdRequest adRequest2 = new AdRequest.Builder().build();
        mAdView2.loadAd(adRequest2);

*/

        textView = findViewById(R.id.result_out);
        textViewAge = findViewById(R.id.text_table_age);
        textViewW = findViewById(R.id.text_table_weight);
        textViewL = findViewById(R.id.text_table_length);
        textViewTable3 = findViewById(R.id.text_table3);
        textViewTable4 = findViewById(R.id.text_table4);
        textViewTable5 = findViewById(R.id.text_table5);
        textViewTable6 = findViewById(R.id.text_table6);
        textViewTable7 = findViewById(R.id.text_table7);
        textViewTable8 = findViewById(R.id.text_table8);
        final Button button = findViewById(R.id.calculate_btn);
        editTextW = findViewById(R.id.weight);
        editTextL = findViewById(R.id.length);
        editTextAge = findViewById(R.id.age);




//        reading weight
        editTextW.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

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
        @Override
        public void onClick(View view) {

            String weight = editTextW.getText().toString();
            String length = editTextL.getText().toString();
            String age = editTextAge.getText().toString();


            myweight = Double.parseDouble(weight);
            mylength = Double.parseDouble(length)/100;
            myage = Double.parseDouble(age);
            myresult = myweight/(mylength * mylength); //calculatin IMT

            textViewW.setText(getString(R.string.name_text_table_detail_weight,weight));
            textViewL.setText(getString(R.string.name_text_table_detail_length,length));
            // highlighting age
            textViewAge.setText(getString(R.string.name_text_table_detail_age,age));
            textViewAge.setBackgroundColor(Color.GRAY);
//            highlighting age tables
        //    if (myage >=19){
//                textViewTable3.setBackgroundColor(Color.GRAY);
//            }
            if (myage < 19) {
                Toast.makeText(MainActivity.this, "Too young, to be in table of age",
                      Toast.LENGTH_LONG).show();
                textViewTable3.setBackgroundColor(Color.TRANSPARENT);
                textViewTable4.setBackgroundColor(Color.TRANSPARENT);
                textViewTable5.setBackgroundColor(Color.TRANSPARENT);
                textViewTable6.setBackgroundColor(Color.TRANSPARENT);
                textViewTable7.setBackgroundColor(Color.TRANSPARENT);
                textViewTable8.setBackgroundColor(Color.TRANSPARENT);

            }
            if ((myage >= 19) && (myage <= 24)){
                textViewTable3.setBackgroundColor(Color.GRAY);
                textViewTable4.setBackgroundColor(Color.TRANSPARENT);
                textViewTable5.setBackgroundColor(Color.TRANSPARENT);
                textViewTable6.setBackgroundColor(Color.TRANSPARENT);
                textViewTable7.setBackgroundColor(Color.TRANSPARENT);
                textViewTable8.setBackgroundColor(Color.TRANSPARENT);

            }
            if ((myage > 24) && (myage <= 34)){
                textViewTable4.setBackgroundColor(Color.GRAY);
                textViewTable3.setBackgroundColor(Color.TRANSPARENT);
                textViewTable5.setBackgroundColor(Color.TRANSPARENT);
                textViewTable6.setBackgroundColor(Color.TRANSPARENT);
                textViewTable7.setBackgroundColor(Color.TRANSPARENT);
                textViewTable8.setBackgroundColor(Color.TRANSPARENT);

            }
            if ((myage > 34) && (myage <= 44)){
                textViewTable5.setBackgroundColor(Color.GRAY);
                textViewTable4.setBackgroundColor(Color.TRANSPARENT);
                textViewTable3.setBackgroundColor(Color.TRANSPARENT);
                textViewTable6.setBackgroundColor(Color.TRANSPARENT);
                textViewTable7.setBackgroundColor(Color.TRANSPARENT);
                textViewTable8.setBackgroundColor(Color.TRANSPARENT);
            }
            if ((myage > 44) && (myage <= 54)){
                textViewTable6.setBackgroundColor(Color.GRAY);
                textViewTable4.setBackgroundColor(Color.TRANSPARENT);
                textViewTable5.setBackgroundColor(Color.TRANSPARENT);
                textViewTable3.setBackgroundColor(Color.TRANSPARENT);
                textViewTable7.setBackgroundColor(Color.TRANSPARENT);
                textViewTable8.setBackgroundColor(Color.TRANSPARENT);
            }
            if ((myage > 54) && (myage <= 64)){
                textViewTable7.setBackgroundColor(Color.GRAY);
                textViewTable4.setBackgroundColor(Color.TRANSPARENT);
                textViewTable5.setBackgroundColor(Color.TRANSPARENT);
                textViewTable6.setBackgroundColor(Color.TRANSPARENT);
                textViewTable3.setBackgroundColor(Color.TRANSPARENT);
                textViewTable8.setBackgroundColor(Color.TRANSPARENT);
            }

            if (myage > 64) {
                textViewTable8.setBackgroundColor(Color.GRAY);
                textViewTable4.setBackgroundColor(Color.TRANSPARENT);
                textViewTable5.setBackgroundColor(Color.TRANSPARENT);
                textViewTable6.setBackgroundColor(Color.TRANSPARENT);
                textViewTable7.setBackgroundColor(Color.TRANSPARENT);
                textViewTable3.setBackgroundColor(Color.TRANSPARENT);
            }


//            String myresultStr = Double.toString(myresult);
            String myresultStrFormat = String.format("%.2f",myresult);
            textView.setText(getString(R.string.result_text,myresultStrFormat));

            if (myresult < 19) {
                textView.setTextColor(Color.RED);
                }
            if ((myresult >= 19) && (myresult <=24)) {
                textView.setTextColor(Color.WHITE);
            }
            if ((myresult > 25) && (myresult <= 34)) {
                textView.setTextColor(Color.MAGENTA);
            }
                        if ((myresult > 34)) {
                textView.setTextColor(Color.RED);
            }

        }

    };

}
