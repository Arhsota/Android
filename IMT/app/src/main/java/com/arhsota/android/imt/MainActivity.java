package com.arhsota.android.imt;

// version .11
// my first real soft based on lesson 8 Skillberg
// calculating index body fat from your weight and length both for male and female
// Sevastyanov Andrew, 2018
// Sabetta

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.reward.RewardedVideoAd;



public class MainActivity extends AppCompatActivity {

    private AdView mAdView;

 //   private RewardedVideoAd mRewardedVideoAd;

    private TextView textView;
    private TextView textViewAge;
    private EditText editTextW;
    private EditText editTextL;
    private EditText editTextAge;
//    private Button button;
    private   double myweight;
    private   double mylength;
    private   double myage;
    private   double myresult;
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
                if (editable.toString().trim().length() > 0) {
                    button.setEnabled(true);
                } else {
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
                    if (editable.toString().trim().length() > 0) {
                      button.setEnabled(true);
                   } else {
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
                     if (editable.toString().trim().length() > 0) {
                         button.setEnabled(true);
                     } else {
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
            textViewAge.setText(getString(R.string.name_text_table_datail_age,age));
            textViewAge.setBackgroundColor(Color.GRAY);


//            String myresultStr = Double.toString(myresult);
            String myresultStrFormat = String.format("%.2f",myresult);


            if (myresult <= 18.50) {
                textView.setTextColor(Color.RED);
                textView.setText(getString(R.string.name_text_format_thin,myresultStrFormat));
            }
            if ((myresult > 18.50) && (myresult <=25)) {
                textView.setTextColor(Color.GREEN);
                textView.setText(getString(R.string.name_text_format_normal,myresultStrFormat));
            }
            if ((myresult > 25) && (myresult < 30)) {
                textView.setTextColor(Color.MAGENTA);
                textView.setText(getString(R.string.name_text_format_prefat,myresultStrFormat));
            }
            if ((myresult >= 30)) {
                textView.setTextColor(Color.RED);
                textView.setText(getString(R.string.name_text_format_fat,myresultStrFormat));
            }

        }

    };

}
