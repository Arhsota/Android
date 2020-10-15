// my first real soft based on lesson 8 Skillberg
// calculating index body fat based on your weight and length both for male and female
// Sevastyanov Andrey, 2018, september
// Arkhangelsk


/*******************************************************************************
 *
 *  * Created by Andrey Sevastianov on 2018, september
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12.08.20 19:18
 *
 ******************************************************************************/

package com.arhsota.android.imt;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;


import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
//import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.OnFailureListener;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;


//import com.google.android.gms.ads.reward.RewardedVideoAd;



public class MainActivity extends AppCompatActivity {

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
    // str_XXX for intent
    private String str_IMT;
    private String str_IMT_mail;

    private String str_Weight = "0";
    private String str_Length = "0";
    private String str_Age = "0";
    private String str_Date = "0";

    private double mylength;
    private double myage;
    private double myresult;

    private boolean fillTextW = false;  // checking for filling all 3 input parametres
    private boolean fillTextL = false;
    private boolean fillTextA = false;

    private String str_History;

    final String LOG_TAG = "myLogs";

    final String FILENAME = "file";

    final String DIR_SD = "MyFiles";
    final String FILENAME_SD = "fileSD";

    private InterstitialAd mInterstitialAd;


    private final String YOUR_ADMOB_APP_ID = "ca-app-pub-7279174300665421~3105181624";
    ReviewInfo reviewInfo;
    ReviewManager manager;

    private String str_Main_Output;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Заполнение меню; элементы (если они есть) добавляются на панель действий.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        manager = ReviewManagerFactory.create(this);


        switch (item.getItemId()) {
            case R.id.action_create_save:
                //Код, выполняемый при выборе элемента Create Save
                Toast.makeText(MainActivity.this, R.string.saving,Toast.LENGTH_LONG).show();
                Review();
//                str_Main_Output = str_IMT;
                  str_Main_Output = getString(R.string.date) + " " + str_Date + ", " + getString(R.string.my_IMT) + " " + str_IMT + ", " +
                       getString(R.string.my_weight) + " " + str_Weight + ", " + getString(R.string.my_length) + str_Length + ", " +
                       getString(R.string.my_age) + " "+ str_Age + "|" ;
                writeFile();
//                Intent intent = new Intent(this, SecondActivity.class);
//                intent.setType("text/plain");
//                intent.putExtra("IMT", str_IMT);
//                intent.putExtra("Weight", str_Weight);
//                intent.putExtra("Length", str_Length);
//                intent.putExtra("Age", str_Age);
//                intent.putExtra("Date", str_Date);
                //   str_IMT_mail= str_Date + " Ваш ИМТ: " + str_IMT + " Вес: " + str_Weight + " Рост " + str_Length + " Возраст: " + str_Age;
                //   startActivity(intent);
                //    textViewW.setText(getString(R.string.name_text_table_detail_weight,str_weight));

                return true;
            case R.id.action_share:
                Intent intent_mes = new Intent(Intent.ACTION_SEND);
                intent_mes.setType("text/plain");
                intent_mes.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.for_subject_field) + str_Date);
                intent_mes.putExtra(Intent.EXTRA_TEXT, str_Date + " " + getString(R.string.my_IMT) + " " + str_IMT
                        + " " + getString(R.string.my_weight) + " " + str_Weight + " " + getString(R.string.my_length)
                        + " " + str_Length + " " + getString(R.string.my_age) + " " + str_Age);
//          page 143 always choose intent
                String chooserTitle = getString(R.string.share);
                Intent chosenIntent = Intent.createChooser(intent_mes, chooserTitle);
                startActivity(chosenIntent);
                return true;

            case R.id.action_share_history:
                readFile();
                Intent intent_his = new Intent(Intent.ACTION_SEND);
                intent_his.setType("text/plain");
                intent_his.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.for_subject_field) + str_Date);
                intent_his.putExtra(Intent.EXTRA_TEXT, str_History);
//          page 143 always choose intent
                String chooserTitleHis = getString(R.string.share_history);
                Intent chosenIntentHis = Intent.createChooser(intent_his, chooserTitleHis);
                startActivity(chosenIntentHis);
                return true;

            case R.id.action_history:
                Intent intent_history = new Intent(this, History.class);
                startActivity(intent_history);
                return true;

            case R.id.history_clear:
                historyClear();
                return true;

//todo
            case R.id.action_sign_up:
                // todo: NOTHING YET, waiting for next version
                // Intent intent_main_activity_sign = new Intent(this, MainActivitySign.class);
                // startActivity(intent_main_activity_sign);
                Review();

//                Toast.makeText(MainActivity.this, R.string.in_work,Toast.LENGTH_LONG).show();
                return true;

            case R.id.action_help:
                Intent intent_help = new Intent(this, Help.class);
                startActivity(intent_help);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
    private void Review(){
        Task<ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // We can get the ReviewInfo object
                ReviewInfo reviewInfo = task.getResult();
                Task<Void> flow = manager.launchReviewFlow(this, reviewInfo);
                flow.addOnCompleteListener(task1-> {
                    // The flow has finished. The API does not indicate whether the user
                    // reviewed or not, or even whether the review dialog was shown. Thus, no
                    // matter the result, we continue our app flow.
                    // TODO: 30.09.2020 Clear Toast in further releases 
                    Toast.makeText(MainActivity.this, "Review Completed, Thank You!", Toast.LENGTH_SHORT).show();
                });
            } else {
                // There was some problem, continue regardless of the result.
                Toast.makeText(MainActivity.this, "In-App Request Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

 /*
   private void Review() {
        manager.requestReviewFlow().addOnCompleteListener(new OnCompleteListener<ReviewInfo>() {
            @Override
            public void onComplete(@NonNull Task<ReviewInfo> task) {
                if (task.isSuccessful()) {
                    reviewInfo = task.getResult();
                    manager.launchReviewFlow(MainActivity.this, reviewInfo).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(MainActivity.this, "Rating Failed", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(MainActivity.this, "Review Completed, Thank You!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(MainActivity.this, "In-App Request Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
*/

    private void readFile() {
        try {
            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    openFileInput(FILENAME)));
            String str  = "";
            // TODO: 11.08.2020 read not line by line but till the end of file
            // TODO: 11.08.2020 clean history

            // читаем содержимое
//            while ((str = br.readLine()) != null) {
            while ((str =br.readLine()) != null) {
                Log.d(LOG_TAG, str);
                str_History = str;
            }
//           str_History[10] =  "\n";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void historyClear() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.confirm);
        builder.setMessage(R.string.are_you_shure);

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                writeEmptyFile();
                Toast.makeText(MainActivity.this, "История удалена",
                        Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing



                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
        
        
    }
    

    private void writeEmptyFile() {
        try {
            // отрываем поток для записи
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    openFileOutput(FILENAME, MODE_PRIVATE)));
            // пишем данные
            bw.append(" ");
            // закрываем поток
            bw.close();
            Log.d(LOG_TAG, "Файл записан");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        AdView mAdView = findViewById(R.id.adView);
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

            //    private Button button;
            double myweight = Double.parseDouble(weight);
            mylength = Double.parseDouble(length)/100;
            myage = Double.parseDouble(age);
//            Not devide to zero
            if (mylength == 0)  {
               mylength = 1;
            }
            myresult = myweight /(mylength * mylength); //calculating IMT
            str_IMT =  String.format("%.2f",myresult); // making string format
            ProgressBar progressBar = findViewById(R.id.progressBar);
            textMinImt = findViewById(R.id.minImt);
            textMaxImt = findViewById(R.id.maxImt);

            int num19 = 19;
            if (myage < num19) {
                Toast.makeText(MainActivity.this, R.string.too_young,
                      Toast.LENGTH_LONG).show();

            }
            int num24 = 24;
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
            int num25 = 25;
            int num34 = 34;
            if ((myage > num24) && (myage <= num34)){
                textViewTable.setText(R.string.text_table4);
                progressBar.setMax(num25);
                int num20 = 20;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    progressBar.setMin(num20);
                }
                progressBar.setProgress((int) myresult);
                textMinImt.setText(String.valueOf(num20));
                textMaxImt.setText(String.valueOf(num25));
            }
            int num44 = 44;
            if ((myage > num34) && (myage <= num44)){
                textViewTable.setText(R.string.text_table5);
                int num26 = 26;
                progressBar.setMax(num26);
                int num21 = 21;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    progressBar.setMin(num21);
                }
                progressBar.setProgress((int) myresult);
                textMinImt.setText(String.valueOf(num21));
                textMaxImt.setText(String.valueOf(num26));

            }
            int num54 = 54;
            if ((myage > num44) && (myage <= num54)){
                textViewTable.setText(R.string.text_table6);
                int num27 = 27;
                progressBar.setMax(num27);
                int num22 = 22;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    progressBar.setMin(num22);
                }
                progressBar.setProgress((int) myresult);
                textMinImt.setText(String.valueOf(num22));
                textMaxImt.setText(String.valueOf(num27));

            }
            int num28 = 28;
            int num64 = 64;
            if ((myage > num54) && (myage <= num64)){
                textViewTable.setText(R.string.text_table7);
                progressBar.setMax(num28);
                int num23 = 23;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    progressBar.setMin(num23);
                }
                progressBar.setProgress((int) myresult);
                textMinImt.setText(String.valueOf(num23));
                textMaxImt.setText(String.valueOf(num28));

            }

            if (myage > num64) {
                textViewTable.setText(R.string.text_table8);
                int num29 = 59;
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
            if ((myresult >= num19) && (myresult <= num24)) {
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
