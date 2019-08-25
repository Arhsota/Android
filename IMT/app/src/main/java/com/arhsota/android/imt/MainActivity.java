package com.arhsota.android.imt;

// version 2.3
// changes in this version - second activity with correspondent features and banner on every activity
// using constraint layout
// my first real soft based on lesson 8 Skillberg
// calculating index body fat based on your weight and length both for male and female
// Sevastyanov Andrey, 2019, august
// Arkhangelsk
//

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;
//import android.support.v7.widget.ShareActionProvider;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.view.MenuItemCompat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
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
 /*   private TextView textViewW;
    private TextView textViewL;
    private TextView textViewAge;
    private TextView textViewTable3;
    private TextView textViewTable4;
    private TextView textViewTable5;
    private TextView textViewTable6;
    private TextView textViewTable7;
    private TextView textViewTable8;
    */
    private TextView textViewTable;

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


    private final String YOUR_ADMOB_APP_ID = "ca-app-pub-7279174300665421~3105181624";




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Заполнение меню; элементы (если они есть) добавляются на панель действий.
        getMenuInflater().inflate(R.menu.menu_main, menu);
      //  MenuItem menuItem = menu.findItem(R.id.action_share);
      //  shareActionProvider = (ShareActionProvider) menuItem.getActionProvider();
      //  Intent intent = new Intent(Intent.ACTION_SENDTO);
    //   Intent intent = new Intent(Intent.ACTION_SEND);
    //    intent.setData(Uri.parse("mailto:")); // only email apps should handle this
    //    intent.setType("text/plain");
     //   Toast.makeText(MainActivity.this, "111111111111111",
     //           Toast.LENGTH_LONG).show();
    //    intent.putExtra(Intent.EXTRA_TEXT, str_Date + str_IMT+"22222");
    //    intent.putExtra("IMT", str_IMT);
   //     shareActionProvider.setShareIntent(intent);
     //   setIntent("Дата: " + date + " Ваш ИМТ " + str_IMT+ " Вес: " + str_Weight + " Рост: " +str_Length + " Возраст: " + str_Age);
       // setIntent("test");

      //  return super.onCreateOptionsMenu(menu);
        return true;
    }


  /*
    private void setIntent(String text) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
      //  intent.setType("text/plain");
          intent.putExtra("IMT", str_IMT);
        //  intent.putExtra("Weight", str_Weight);
         // intent.putExtra("Length", str_Length);
      //   intent.putExtra("Age", str_Age);
         text = str_IMT + str_Weight;
        intent.putExtra(Intent.EXTRA_TEXT, str_Date + str_IMT);
        shareActionProvider.setShareIntent(intent);
    }
*/
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
             /*   //Код, выполняемый при выборе элемента Share
                Intent intent_mail = new Intent(Intent.ACTION_SENDTO);
                intent_mail.setData(Uri.parse("mailto:")); // only email apps should handle this
                //   intent_mail.setType("text/plain");
                intent_mail.putExtra("IMT", str_IMT);
                intent_mail.putExtra(Intent.EXTRA_TEXT,str_Date + " IMT  " + str_IMT + " Weight " + str_Weight);
                startActivity(intent_mail);
              */
                Intent intent_mes = new Intent(Intent.ACTION_SEND);
                intent_mes.setType("text/plain");
                intent_mes.putExtra(Intent.EXTRA_TEXT, str_Date + " Ваш ИМТ: " + str_IMT + " Вес: " + str_Weight + " Рост " + str_Length + " Возраст: " + str_Age);
//          page 143 always choose intent
                String chooserTitle = "Sharing";
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
//        test id ca-app-pub-3940256099942544/6300978111 for layout XML
//        working real banner ca-app-pub-7279174300665421/8731793267

        MobileAds.initialize(this, "ca-app-pub-7279174300665421~3105181624");
       // mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);



        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


/*
       one more banner
       mAdView2 = findViewById(R.id.adView2);
       AdRequest adRequest2 = new AdRequest.Builder().build();
        mAdView2.loadAd(adRequest2);

*/

        textView = findViewById(R.id.result_out);

        textViewTable = findViewById(R.id.text_table);
       /* textViewAge = findViewById(R.id.text_table_age);
        textViewW = findViewById(R.id.text_table_weight);
        textViewL = findViewById(R.id.text_table_length);
        textViewTable3 = findViewById(R.id.text_table3);
        textViewTable4 = findViewById(R.id.text_table4);
        textViewTable5 = findViewById(R.id.text_table5);
        textViewTable6 = findViewById(R.id.text_table6);
        textViewTable7 = findViewById(R.id.text_table7);
        textViewTable8 = findViewById(R.id.text_table8);
        */
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

          //  textViewW.setText(getString(R.string.name_text_table_detail_weight,weight));
         //   textViewL.setText(getString(R.string.name_text_table_detail_length,length));
            // highlighting age
         //   textViewAge.setText(getString(R.string.name_text_table_detail_age,age));
         //   textViewAge.setBackgroundColor(Color.GRAY);
//            highlighting age tables
        //    if (myage >=19){
//                textViewTable3.setBackgroundColor(Color.GRAY);
//            }
            if (myage < 19) {
                Toast.makeText(MainActivity.this, R.string.too_young,
                      Toast.LENGTH_LONG).show();

            }
            if ((myage >= 19) && (myage <= 24)){

                textViewTable.setText( R.string.text_table3);

                /*
                textViewTable3.setBackgroundColor(Color.GRAY);
                textViewTable4.setBackgroundColor(Color.TRANSPARENT);
                textViewTable5.setBackgroundColor(Color.TRANSPARENT);
                textViewTable6.setBackgroundColor(Color.TRANSPARENT);
                textViewTable7.setBackgroundColor(Color.TRANSPARENT);
                textViewTable8.setBackgroundColor(Color.TRANSPARENT);
                */
            }
            if ((myage > 24) && (myage <= 34)){
                textViewTable.setText(R.string.text_table4);
            }
            if ((myage > 34) && (myage <= 44)){
                textViewTable.setText(R.string.text_table5);

            }
            if ((myage > 44) && (myage <= 54)){
                textViewTable.setText(R.string.text_table6);

            }
            if ((myage > 54) && (myage <= 64)){
                textViewTable.setText(R.string.text_table7);

            }

            if (myage > 64) {
                textViewTable.setText(R.string.text_table8);

            }


//            String myresultStr = Double.toString(myresult);

            String myresultStrFormat = String.format("%.2f",myresult);
            textView.setText(getString(R.string.result_text,myresultStrFormat));

            if (myresult < 19) {
                textView.setTextColor(Color.RED);
                }
            if ((myresult >= 19) && (myresult <=24)) {
                textView.setTextColor(Color.BLACK);
            }
            if ((myresult > 25) && (myresult <= 30)) {
                textView.setTextColor(Color.BLUE);
            }
            if ((myresult > 30) && (myresult <= 33)) {
                textView.setTextColor(Color.MAGENTA);
            }
                        if ((myresult > 33)) {
                textView.setTextColor(Color.RED);
            }

        }

    };

    public void signClick(View view) {
        Intent intent_main_activity_sign = new Intent(this, MainActivitySign.class);
        startActivity(intent_main_activity_sign);
    }
}
