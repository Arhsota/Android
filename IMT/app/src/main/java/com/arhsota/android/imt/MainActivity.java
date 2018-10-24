package com.arhsota.android.imt;

// version 1.7
// changes in this version - make different size of text due to resolution fix
// ordinary background, intent to new activity and theme to bar (not working properly yet)
// action choice due to action bar
// my first real soft based on lesson 8 Skillberg
// calculating index body fat based on your weight and length both for male and female
// Sevastyanov Andrey, 2018, september
// Arkhangelsk
//

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.inputmethodservice.Keyboard;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.text.SimpleDateFormat;
import java.util.Calendar;
//import com.google.android.gms.ads.reward.RewardedVideoAd;



public class MainActivity extends Activity {

    private AdView mAdView;
    private FirebaseAnalytics mFirebaseAnalytics;
    private ShareActionProvider shareActionProvider;

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
    // str_XXX for intent
    private   String str_IMT ;

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

    private final String YOUR_ADMOB_APP_ID = "ca-app-pub-7279174300665421~3105181624";


 /*   @Override    public boolean onCreateOptionsMenu(Menu menu) {
        // Заполнение меню; элементы (если они есть) добавляются на панель действий.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider) menuItem.getActionProvider();
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_TEXT, str_Date + str_IMT);
        shareActionProvider.setShareIntent(intent);
     //   setIntent("Дата: " + date + " Ваш ИМТ " + str_IMT+ " Вес: " + str_Weight + " Рост: " +str_Length + " Возраст: " + str_Age);
       // setIntent("test");
        return super.onCreateOptionsMenu(menu);
    }
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
 /*   @Override
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
                startActivity(intent);
            //    textViewW.setText(getString(R.string.name_text_table_detail_weight,str_weight));

                return true;
            case R.id.action_history:
                //Код, выполняемый при выборе элемента History
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

         //   String date = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(Calendar.getInstance().getTime());
         //   str_Date = date;

            myweight = Double.parseDouble(weight);
            mylength = Double.parseDouble(length)/100;
            myage = Double.parseDouble(age);
//            Not devide to zero
            if (mylength == 0)  {
               mylength = 1;
            }
            myresult = myweight/(mylength * mylength); //calculating IMT
            str_IMT =  String.format("%.2f",myresult); // making string format

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
                textView.setTextColor(Color.BLUE);
            }
            if ((myresult > 25) && (myresult <= 34)) {
                textView.setTextColor(Color.BLUE);
            }
                        if ((myresult > 34)) {
                textView.setTextColor(Color.RED);
            }

        }

    };

}
