package com.arhsota.alcome;

// Info of hour much time remains alcohol in your body
// Idea from DS
// 2019 September, October, 10 november
// ver 1.0

import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private boolean isClick100;
    private boolean isClick300;
    private boolean isClick500;
    private TextView textViewRemainTime;


    private RadioButton radioButton100;
    private RadioButton radioButton300;
    private RadioButton radioButton500;

    private int seconds;
    private String timeStr;

    //Секундомер работает?
    private boolean running;
    private boolean wasRunning = true;

    private AdView mAdView;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this,
                "ca-app-pub-7279174300665421~4445221287");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7279174300665421/4010107019");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });
       /* Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        */

  /*      FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

   */
        radioButton100 = (RadioButton) findViewById(R.id.hundred);
        radioButton300 = (RadioButton) findViewById(R.id.threehundred);
        radioButton500 = (RadioButton) findViewById(R.id.fivehundred);

      /*  Spinner spinnerAlcoType = (Spinner) findViewById(R.id.alcotype);
        String selected = spinnerAlcoType.getSelectedItem().toString();
        posAlcoType = spinnerAlcoType.getSelectedItemPosition();



        Spinner spinnerWeight = (Spinner) findViewById(R.id.weight);
        String selectedweight = spinnerWeight.getSelectedItem().toString();
        posWeight = spinnerWeight.getSelectedItemPosition();
*/
        textViewRemainTime = findViewById(R.id.remainTime);

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState); // этой строки нет в книге

        savedInstanceState.putInt("seconds", seconds);
        savedInstanceState.putString("timeStr",timeStr);
        savedInstanceState.putBoolean("running", running);
        savedInstanceState.putBoolean("wasRunning", wasRunning);
        //  savedInstanceState.putBoolean("player1", player1);
      }

    @Override
    protected void onStop() {
        super.onStop();
        wasRunning = running;
       // running = false;
    }
    //    page 179
    @Override
    protected void onStart() {
        super.onStart();
        if (wasRunning) {
            running = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick100(View view) {
        isClick100 = true;
        isClick300 = false;
        isClick500 = false;


    }

    public void onClick300(View view) {
        isClick100 = false;
        isClick300 = true;
        isClick500 = false;
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }

    }

    public void onClick500(View view) {
        isClick100 = false;
        isClick300 = false;
        isClick500 = true;
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
    }

    public void onClickCalculate(View view) {

        Spinner spinnerAlcoType = (Spinner) findViewById(R.id.alcotype);
        String selected = spinnerAlcoType.getSelectedItem().toString();
        int posAlcoType = spinnerAlcoType.getSelectedItemPosition();

        Spinner spinnerWeight = (Spinner) findViewById(R.id.weight);
        String selectedweight = spinnerWeight.getSelectedItem().toString();
        int posWeight = spinnerWeight.getSelectedItemPosition();

        String strRemain = getString((R.string.remain)) + ": ";
        String strHours  = " " +getString((R.string.hours)) ;



//        timeStr = getResources().getStringArray(R.array.remain_array)[26];
  //      seconds = Integer.parseInt(testStr);
  //      Float sec_fl = Float.parseFloat(testStr);


//        Toasts are only for checking output, further should be commented
//        Toast.makeText(getApplicationContext(), selected, Toast.LENGTH_SHORT).show();

 //       Toast.makeText(getApplicationContext(), String.format("%1d", posWeight), Toast.LENGTH_SHORT).show();


         if ((radioButton100.isChecked()) || (radioButton300.isChecked()) || (radioButton500.isChecked())){
            switch (posAlcoType) {
                case 0:
//                    4%
                    if ((isClick100) && (posWeight == 0)) {
                        timeStr = getString(R.string.remain_0_35);
//                        timeStr = getResources().getStringArray(R.array.remain_array)[26];
                        textViewRemainTime.setText(strRemain + timeStr + strHours); }
                    if ((isClick100) && (posWeight == 1)) {
                        timeStr = getString(R.string.remain_0_30);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick100) && (posWeight == 2)) {
                        timeStr = getString(R.string.remain_0_25);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick100) && (posWeight == 3)) {
                        timeStr = getString(R.string.remain_0_23);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick100) && (posWeight == 4)) {
                        timeStr = getString(R.string.remain_0_21);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick300) && (posWeight == 0)) {
                        timeStr = getString(R.string.remain_1_44);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick300) && (posWeight == 1)) {
                        timeStr = getString(R.string.remain_1_29);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick300) && (posWeight == 2)) {
                        timeStr = getString(R.string.remain_1_18);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick300) && (posWeight == 3)) {
                        timeStr = getString(R.string.remain_1_10);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick300) && (posWeight == 4)) {
                        timeStr = getString(R.string.remain_1_03);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick500) && (posWeight == 0)) {
                        timeStr = getString(R.string.remain_2_54);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick500) && (posWeight == 1)) {
                        timeStr = getString(R.string.remain_2_29);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick500) && (posWeight == 2)) {
                        timeStr = getString(R.string.remain_2_11);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick500) && (posWeight == 3)) {
                        timeStr = getString(R.string.remain_1_56);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick500) && (posWeight == 4)) {
                        timeStr = getString(R.string.remain_1_44);
                        textViewRemainTime.setText("Remain 1,44");
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    break;

                case 1:
//                    6%
                    if ((isClick100) && (posWeight == 0)) {
                        timeStr = getString(R.string.remain_0_52);
//                        timeStr = getResources().getStringArray(R.array.remain_array)[26];
                        textViewRemainTime.setText(strRemain + timeStr + strHours); }
                    if ((isClick100) && (posWeight == 1)) {
                        timeStr = getString(R.string.remain_0_45);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick100) && (posWeight == 2)) {
                        timeStr = getString(R.string.remain_0_39);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick100) && (posWeight == 3)) {
                        timeStr = getString(R.string.remain_0_35);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick100) && (posWeight == 4)) {
                        timeStr = getString(R.string.remain_0_31);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick300) && (posWeight == 0)) {
                        timeStr = getString(R.string.remain_2_37);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick300) && (posWeight == 1)) {
                        timeStr = getString(R.string.remain_2_14);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick300) && (posWeight == 2)) {
                        timeStr = getString(R.string.remain_1_57);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick300) && (posWeight == 3)) {
                        timeStr = getString(R.string.remain_1_44);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick300) && (posWeight == 4)) {
                        timeStr = getString(R.string.remain_1_34);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick500) && (posWeight == 0)) {
                        timeStr = getString(R.string.remain_4_21);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick500) && (posWeight == 1)) {
                        timeStr = getString(R.string.remain_3_44);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick500) && (posWeight == 2)) {
                        timeStr = getString(R.string.remain_3_16);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick500) && (posWeight == 3)) {
                        timeStr = getString(R.string.remain_2_54);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick500) && (posWeight == 4)) {
                        timeStr = getString(R.string.remain_2_37);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}

                    break;

                case 2:
//                    9%
                    if ((isClick100) && (posWeight == 0)) {
                        timeStr = getString(R.string.remain_1_18);
//                        timeStr = getResources().getStringArray(R.array.remain_array)[26];
                        textViewRemainTime.setText(strRemain + timeStr + strHours); }
                    if ((isClick100) && (posWeight == 1)) {
                        timeStr = getString(R.string.remain_1_07);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick100) && (posWeight == 2)) {
                        timeStr = getString(R.string.remain_0_59);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick100) && (posWeight == 3)) {
                        timeStr = getString(R.string.remain_0_52);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick100) && (posWeight == 4)) {
                        timeStr = getString(R.string.remain_0_47);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick300) && (posWeight == 0)) {
                        timeStr = getString(R.string.remain_3_55);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick300) && (posWeight == 1)) {
                        timeStr = getString(R.string.remain_3_21);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick300) && (posWeight == 2)) {
                        timeStr = getString(R.string.remain_2_56);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick300) && (posWeight == 3)) {
                        timeStr = getString(R.string.remain_2_37);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick300) && (posWeight == 4)) {
                        timeStr = getString(R.string.remain_2_21);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick500) && (posWeight == 0)) {
                        timeStr = getString(R.string.remain_6_32);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick500) && (posWeight == 1)) {
                        timeStr = getString(R.string.remain_5_36);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick500) && (posWeight == 2)) {
                        timeStr = getString(R.string.remain_4_54);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick500) && (posWeight == 3)) {
                        timeStr = getString(R.string.remain_4_21);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick500) && (posWeight == 4)) {
                        timeStr = getString(R.string.remain_3_55);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    break;


                case 3:
//                    11 %
                    if ((isClick100) && (posWeight == 0)) {
                        timeStr = getString(R.string.remain_1_36);
//                        timeStr = getResources().getStringArray(R.array.remain_array)[26];
                        textViewRemainTime.setText(strRemain + timeStr + strHours); }
                    if ((isClick100) && (posWeight == 1)) {
                        timeStr = getString(R.string.remain_1_22);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick100) && (posWeight == 2)) {
                        timeStr = getString(R.string.remain_1_12);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick100) && (posWeight == 3)) {
                        timeStr = getString(R.string.remain_1_04);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick100) && (posWeight == 4)) {
                        timeStr = getString(R.string.remain_0_57);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick300) && (posWeight == 0)) {
                        timeStr = getString(R.string.remain_4_47);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick300) && (posWeight == 1)) {
                        timeStr = getString(R.string.remain_4_06);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick300) && (posWeight == 2)) {
                        timeStr = getString(R.string.remain_3_35);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick300) && (posWeight == 3)) {
                        timeStr = getString(R.string.remain_3_11);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick300) && (posWeight == 4)) {
                        timeStr = getString(R.string.remain_2_52);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick500) && (posWeight == 0)) {
                        timeStr = getString(R.string.remain_7_59);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick500) && (posWeight == 1)) {
                        timeStr = getString(R.string.remain_6_50);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick500) && (posWeight == 2)) {
                        timeStr = getString(R.string.remain_5_59);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick500) && (posWeight == 3)) {
                        timeStr = getString(R.string.remain_5_19);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick500) && (posWeight == 4)) {
                        timeStr = getString(R.string.remain_4_47);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    break;

                case 4:
//                    18%
                    if ((isClick100) && (posWeight == 0)) {
                        timeStr = getString(R.string.remain_2_37);
//                        timeStr = getResources().getStringArray(R.array.remain_array)[26];
                        textViewRemainTime.setText(strRemain + timeStr + strHours); }
                    if ((isClick100) && (posWeight == 1)) {
                        timeStr = getString(R.string.remain_2_14);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick100) && (posWeight == 2)) {
                        timeStr = getString(R.string.remain_1_57);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick100) && (posWeight == 3)) {
                        timeStr = getString(R.string.remain_1_44);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick100) && (posWeight == 4)) {
                        timeStr = getString(R.string.remain_1_34);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick300) && (posWeight == 0)) {
                        timeStr = getString(R.string.remain_7_50);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick300) && (posWeight == 1)) {
                        timeStr = getString(R.string.remain_6_43);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick300) && (posWeight == 2)) {
                        timeStr = getString(R.string.remain_5_52);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick300) && (posWeight == 3)) {
                        timeStr = getString(R.string.remain_5_13);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick300) && (posWeight == 4)) {
                        timeStr = getString(R.string.remain_4_42);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick500) && (posWeight == 0)) {
                        timeStr = getString(R.string.remain_13_03);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick500) && (posWeight == 1)) {
                        timeStr = getString(R.string.remain_11_11);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick500) && (posWeight == 2)) {
                        timeStr = getString(R.string.remain_9_47);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick500) && (posWeight == 3)) {
                        timeStr = getString(R.string.remain_8_42);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick500) && (posWeight == 4)) {
                        timeStr = getString(R.string.remain_7_50);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    break;
                case 5:
//                    24%
                    if ((isClick100) && (posWeight == 0)) {
                        timeStr = getString(R.string.remain_3_29);
//                        timeStr = getResources().getStringArray(R.array.remain_array)[26];
                        textViewRemainTime.setText(strRemain + timeStr + strHours); }
                    if ((isClick100) && (posWeight == 1)) {
                        timeStr = getString(R.string.remain_2_59);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick100) && (posWeight == 2)) {
                        timeStr = getString(R.string.remain_2_37);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick100) && (posWeight == 3)) {
                        timeStr = getString(R.string.remain_2_19);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick100) && (posWeight == 4)) {
                        timeStr = getString(R.string.remain_2_05);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick300) && (posWeight == 0)) {
                        timeStr = getString(R.string.remain_10_26);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick300) && (posWeight == 1)) {
                        timeStr = getString(R.string.remain_8_57);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick300) && (posWeight == 2)) {
                        timeStr = getString(R.string.remain_7_50);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick300) && (posWeight == 3)) {
                        timeStr = getString(R.string.remain_6_58);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick300) && (posWeight == 4)) {
                        timeStr = getString(R.string.remain_6_16);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick500) && (posWeight == 0)) {
                        timeStr = getString(R.string.remain_17_24);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick500) && (posWeight == 1)) {
                        timeStr = getString(R.string.remain_13_03);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick500) && (posWeight == 2)) {
                        timeStr = getString(R.string.remain_13_03);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick500) && (posWeight == 3)) {
                        timeStr = getString(R.string.remain_11_36);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick500) && (posWeight == 4)) {
                        timeStr = getString(R.string.remain_10_26);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    break;
                case 6:
//                    30%
                    if ((isClick100) && (posWeight == 0)) {
                        timeStr = getString(R.string.remain_4_21);
//                        timeStr = getResources().getStringArray(R.array.remain_array)[26];
                        textViewRemainTime.setText(strRemain + timeStr + strHours); }
                    if ((isClick100) && (posWeight == 1)) {
                        timeStr = getString(R.string.remain_3_44);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick100) && (posWeight == 2)) {
                        timeStr = getString(R.string.remain_3_16);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick100) && (posWeight == 3)) {
                        timeStr = getString(R.string.remain_2_54);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick100) && (posWeight == 4)) {
                        timeStr = getString(R.string.remain_2_37);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick300) && (posWeight == 0)) {
                        timeStr = getString(R.string.remain_13_03);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick300) && (posWeight == 1)) {
                        timeStr = getString(R.string.remain_11_11);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick300) && (posWeight == 2)) {
                        timeStr = getString(R.string.remain_9_47);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick300) && (posWeight == 3)) {
                        timeStr = getString(R.string.remain_8_42);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick300) && (posWeight == 4)) {
                        timeStr = getString(R.string.remain_7_50);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick500) && (posWeight == 0)) {
                        timeStr = getString(R.string.remain_21_45);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick500) && (posWeight == 1)) {
                        timeStr = getString(R.string.remain_18_39);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick500) && (posWeight == 2)) {
                        timeStr = getString(R.string.remain_16_19);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick500) && (posWeight == 3)) {
                        timeStr = getString(R.string.remain_14_30);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick500) && (posWeight == 4)) {
                        timeStr = getString(R.string.remain_13_03);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    break;
                case 7:
//                    40%
                    if ((isClick100) && (posWeight == 0)) {
                        timeStr = getString(R.string.remain_5_48);
//                        timeStr = getResources().getStringArray(R.array.remain_array)[26];
                        textViewRemainTime.setText(strRemain + timeStr + strHours); }
                    if ((isClick100) && (posWeight == 1)) {
                        timeStr = getString(R.string.remain_4_58);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick100) && (posWeight == 2)) {
                        timeStr = getString(R.string.remain_4_21);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick100) && (posWeight == 3)) {
                        timeStr = getString(R.string.remain_3_52);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick100) && (posWeight == 4)) {
                        timeStr = getString(R.string.remain_3_29);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick300) && (posWeight == 0)) {
                        timeStr = getString(R.string.remain_17_24);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick300) && (posWeight == 1)) {
                        timeStr = getString(R.string.remain_14_55);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick300) && (posWeight == 2)) {
                        timeStr = getString(R.string.remain_13_03);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick300) && (posWeight == 3)) {
                        timeStr = getString(R.string.remain_11_36);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick300) && (posWeight == 4)) {
                        timeStr = getString(R.string.remain_10_26);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick500) && (posWeight == 0)) {
                        timeStr = getString(R.string.remain_29_00);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick500) && (posWeight == 1)) {
                        timeStr = getString(R.string.remain_24_51);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick500) && (posWeight == 2)) {
                        timeStr = getString(R.string.remain_21_45);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick500) && (posWeight == 3)) {
                        timeStr = getString(R.string.remain_19_20);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick500) && (posWeight == 4)) {
                        timeStr = getString(R.string.remain_17_24);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    break;
                case 8:
//                    42%
                    if ((isClick100) && (posWeight == 0)) {
                        timeStr = getString(R.string.remain_6_05);
//                        timeStr = getResources().getStringArray(R.array.remain_array)[26];
                        textViewRemainTime.setText(strRemain + timeStr + strHours); }
                    if ((isClick100) && (posWeight == 1)) {
                        timeStr = getString(R.string.remain_5_13);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick100) && (posWeight == 2)) {
                        timeStr = getString(R.string.remain_4_34);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick100) && (posWeight == 3)) {
                        timeStr = getString(R.string.remain_4_04);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick100) && (posWeight == 4)) {
                        timeStr = getString(R.string.remain_3_39);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick300) && (posWeight == 0)) {
                        timeStr = getString(R.string.remain_18_16);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick300) && (posWeight == 1)) {
                        timeStr = getString(R.string.remain_15_40);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick300) && (posWeight == 2)) {
                        timeStr = getString(R.string.remain_13_42);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick300) && (posWeight == 3)) {
                        timeStr = getString(R.string.remain_12_11);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick300) && (posWeight == 4)) {
                        timeStr = getString(R.string.remain_10_59);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick500) && (posWeight == 0)) {
                        timeStr = getString(R.string.remain_30_27);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick500) && (posWeight == 1)) {
                        timeStr = getString(R.string.remain_26_06);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick500) && (posWeight == 2)) {
                        timeStr = getString(R.string.remain_22_50);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick500) && (posWeight == 3)) {
                        timeStr = getString(R.string.remain_20_18);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    if ((isClick500) && (posWeight == 4)) {
                        timeStr = getString(R.string.remain_18_16);
                        textViewRemainTime.setText(strRemain + timeStr + strHours);}
                    break;
                default: break;

            }

         }
        else {
            Toast.makeText(getApplicationContext(), "Enter ml", Toast.LENGTH_SHORT).show();
        }
        }
   private void runTimer() {
       final TextView timeView = (TextView) findViewById(R.id.textTimer);
       final Handler handler = new Handler();
       //  seconds = secondsChoice;

       handler.post(new Runnable() {
           @Override
           public void run() {



               int hours = seconds / 3600;
               int minutes = (seconds % 3600) / 60;
               int secs = seconds % 60;



               String time = String.format("%02d:%02d:%02d",hours,minutes, secs);
               wasRunning = true;
               // for the last minute
               //    String time_min = String.format("%02d:%02d", secs, secs100);
               if (seconds == 0) {
                   Toast.makeText(MainActivity.this, "You are not a drinker!",
                           Toast.LENGTH_SHORT).show();
                   Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                   MediaPlayer mp = MediaPlayer.create(getApplicationContext(), notification);
                   mp.start();
                   running = false;
//                   wasRunning = false;
                   return;
//                   first_click = false;
               }
               if ( (running) && (seconds >= 0)) {
                   seconds--;
                   timeView.setText(time);
                   wasRunning = false;
               }
               handler.postDelayed(this, 1000);
           }
       });
   }

    public void onClickTimer(View view) {
//todo: error with clicking start timer when starting program
        if (timeStr == null) {
            Toast.makeText(MainActivity.this, "Enter values!",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            makeSeconds();
        }
        running = true;

       if (wasRunning)
            runTimer();
    }

    public void makeSeconds(){
//        testStr = "0.35";
        float  h_float = Float.parseFloat(timeStr);
        int h = (int) (h_float);
        int h_sec = h*3600;
        int index = timeStr.lastIndexOf(".");
        String m_str = timeStr.substring(index+1,timeStr.length());
        int min = Integer.parseInt(m_str);
        int min_sec = min *60;
        seconds = h_sec + min_sec;
    }
}