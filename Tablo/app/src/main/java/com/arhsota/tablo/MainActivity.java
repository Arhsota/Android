// from stopwatch as pattern
// 4 DMitry Siz
// 2018, 2019

package com.arhsota.tablo;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    //Количество секунд на секундомере
    private int seconds = 305;
    private int secondsChoice = 305;
    private static final int MIN_10 = 605;
    private int secs100 = 60;
    //Score
    private int scorePlayer1 = 0;
    private int scorePlayer2 = 0;
    //Секундомер работает?
    private boolean running;
    private boolean wasRunning;

    //Clicks on button time_view
    private boolean first_click = false;
    private boolean second_click = false;
    private boolean clickCube = false;

    private RadioButton radioButton5;
    private RadioButton radioButton10;
    private RadioButton radioButtonCube;

    private AdView mAdView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this,
                "ca-app-pub-7279174300665421~2703649226");


        //test ad ca-app-pub-3940256099942544/6300978111
        //real ad ca-app-pub-7279174300665421/3496010231

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

       // seconds = secondsChoice;
        if (savedInstanceState != null) {
            seconds = savedInstanceState.getInt("seconds");
            secondsChoice = savedInstanceState.getInt("secondsChoice");
            running = savedInstanceState.getBoolean("running");
            wasRunning = savedInstanceState.getBoolean("wasRunning");

            scorePlayer1 = savedInstanceState.getInt("scorePlayer1");
            scorePlayer2 = savedInstanceState.getInt("scorePlayer2");
        }
        runTimer();
        // For changing orientation
        TextView scoreViewPlayer1 = (TextView) findViewById(R.id.leftScore);
        String strScorePlayer1 = String.format("%d",scorePlayer1);
        scoreViewPlayer1.setText(strScorePlayer1);

        TextView scoreViewPlayer2 = (TextView) findViewById(R.id.rightScore);
        String strScorePlayer2 = String.format("%d",scorePlayer2);
        scoreViewPlayer2.setText(strScorePlayer2);

    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState); // этой строки нет в книге

        savedInstanceState.putInt("seconds", seconds);
        savedInstanceState.putInt("secondsChoice",secondsChoice);
        savedInstanceState.putBoolean("running", running);
        savedInstanceState.putBoolean("wasRunning", wasRunning);
      //  savedInstanceState.putBoolean("player1", player1);
        savedInstanceState.putInt("scorePlayer1", scorePlayer1);
        savedInstanceState.putInt("scorePlayer2", scorePlayer2);
        seconds = secondsChoice;
    }
//    page 176
    @Override
    protected void onStop() {
        super.onStop();
        wasRunning = running;
        running = false;
    }
//    page 179
     @Override
     protected void onStart() {
        super.onStart();
        if (wasRunning) {
            running = true;
        }
     }

    //Запустить секундомер при щелчке на кнопке Start.
      public void onClickStart(View view) {running = true;  }
    //Остановить секундомер при щелчке на кнопке Stop.
       public void onClickStop(View view) {
        running = false;
    }
    //Обнулить секундомер при щелчке на кнопке Reset.
    public void onClickReset(View view) {
        final TextView timeView = (TextView) findViewById(R.id.time_view);
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;
        running = false;
        seconds = secondsChoice;
        String time = String.format("%02d:%02d", minutes, secs);
        timeView.setText(time);
    }

    private void runTimer() {
        final TextView timeView = (TextView) findViewById(R.id.time_view);
        final Handler handler = new Handler();
      //  seconds = secondsChoice;

        handler.post(new Runnable() {
            @Override
            public void run() {

                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;

                String time = String.format("%02d:%02d", minutes, secs);
                // for the last minute
            //    String time_min = String.format("%02d:%02d", secs, secs100);
                if (seconds == 0) {
                    Toast.makeText(MainActivity.this, "Game OVER",
                            Toast.LENGTH_SHORT).show();
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    MediaPlayer mp = MediaPlayer.create(getApplicationContext(), notification);
                    mp.start();
                    running = false;
                    first_click = false;
                }
                if ((running) && (seconds >= 0)) {
                    seconds--;
                    timeView.setText(time);


                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    public void onClickPlayer1(View view) {
        if  (!clickCube) {
            TextView scoreViewPlayer1 = (TextView) findViewById(R.id.leftScore);
            scorePlayer1 = scorePlayer1 + 1;
            String strScorePlayer1 = String.format("%d", scorePlayer1);
            scoreViewPlayer1.setText(strScorePlayer1);
        }
    }

    public void onClickPlayer2(View view) {
        if (!clickCube) {
            TextView scoreViewPlayer2 = (TextView) findViewById(R.id.rightScore);
            scorePlayer2 = scorePlayer2 + 1;
            String strScorePlayer2 = String.format("%d", scorePlayer2);
            scoreViewPlayer2.setText(strScorePlayer2);
        }
    }

    public void onClickSecondsRadio(View view) {
        radioButton5 = (RadioButton) findViewById(R.id.radioButton5);
        radioButton10 = (RadioButton) findViewById(R.id.radioButton10);
        radioButtonCube = (RadioButton) findViewById(R.id.radioButtonCube);

        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked.
        switch (view.getId()) {
            case R.id.radioButton5:
                if (checked) {
                    secondsChoice = 305;
                    radioButton10.setChecked(false);
                    radioButtonCube.setChecked(false);

                }
                break;
            case R.id.radioButton10:
                if (checked) {
                    secondsChoice = 605;
                    radioButton5.setChecked(false);
                    radioButtonCube.setChecked(false);
                }
                break;

            case R.id.radioButtonCube:
                if (checked) {
                    radioButton5.setChecked(false);
                    radioButton10.setChecked(false);
                    clickCube = true;
                    startCubes();
                 //   clickCube = false;
                }
                break;

            default:
                // Do nothing.
                break;
        }
    }
    public void startCubes(){
           if (clickCube){
               Random r1 = new Random();
               int i1 = r1.nextInt(7 - 1) + 1;
               scorePlayer1 = i1;

               Random r2 = new Random();
               int i2 = r2.nextInt(7 - 1) + 1;
               scorePlayer2 = i2;
           }
        TextView scoreViewPlayer1 = (TextView) findViewById(R.id.leftScore);
        String strScorePlayer1 = String.format("%d",scorePlayer1);
        scoreViewPlayer1.setText(strScorePlayer1);

        TextView scoreViewPlayer2 = (TextView) findViewById(R.id.rightScore);
        String strScorePlayer2 = String.format("%d",scorePlayer2);
        scoreViewPlayer2.setText(strScorePlayer2);
    }

    public void onClickScoreAdj1(View view) {
        if ((scorePlayer1 > 0) && (!clickCube)) {
            TextView scoreViewPlayer1 = (TextView) findViewById(R.id.leftScore);
            scorePlayer1 -= 1;
            String strScorePlayer1 = String.format("%d", scorePlayer1);
            scoreViewPlayer1.setText(strScorePlayer1);
        }

    }

    public void onClickScoreAdj2(View view) {
        if ((scorePlayer2 > 0) && (!clickCube)) {
            TextView scoreViewPlayer2 = (TextView) findViewById(R.id.rightScore);
            scorePlayer2 -= 1;
            String strScorePlayer2 = String.format("%d", scorePlayer2);
            scoreViewPlayer2.setText(strScorePlayer2);
        }
    }

    public void onClickResetScore(View view) {

    //  Reset Score to zero

            scorePlayer1 = 0;
            scorePlayer2 = 0;
            clickCube = false;
            radioButton5.setChecked(true);
            radioButton10.setChecked(false);
            radioButtonCube.setChecked(false);

            TextView scoreViewPlayer1 = (TextView) findViewById(R.id.leftScore);
            String strScorePlayer1 = String.format("%d", scorePlayer1);
            scoreViewPlayer1.setText(strScorePlayer1);

            TextView scoreViewPlayer2 = (TextView) findViewById(R.id.rightScore);
            String strScorePlayer2 = String.format("%d", scorePlayer2);
            scoreViewPlayer2.setText(strScorePlayer2);

    }

    //starting or not timer depending on clicks and timer in work or not
    public void onClickWorkTimer(View view) {


     if (!first_click) {
         running = true;
         first_click = true;
     }

        else {
            running = false;
         first_click = false;
        }

    }


}
