/*
 from stopwatch as pattern
 4 DMitry Siz
 2018, 2019, 2020
 Arkhangelsk
 ver 1.9.3
 nothing new, only improvement
*/


/*******************************************************************************
 *
 *  * Created by Andrey Sevastianov on 10 january 2019
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 28.01.2022, 15:37
 *
 ******************************************************************************/

package com.arhsota.tablo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import com.google.android.gms.ads.MobileAds;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.OnFailureListener;
import com.google.android.play.core.tasks.Task;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Random;

import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

//import android.support.v7.app.AppCompatActivity;

//import com.facebook.FacebookSdk;
// import com.facebook.appevents.AppEventsLogger;


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

    private String player;
    private EditText playerV;

    final String LOG_TAG = "myLogs";

    String FILEDATA = "file";
    String FILESAVE = "filesave";
    String MODEAPPEND = "MODE_APPEND";
    String MODEWRITE = "MODE_PRIVATE";
    String CLOSEFILEWRITE = "write";
    String CLOSEFILEAPPEND = "append";
    private Object userNameLeft = " ";
    private Object userNameRight = " ";
    private String strSave;
    private String strUser;
    private String strDate;
    private String str_Main_Output;
//    String a0,a1,a2,a3,a4,a5,a6,a7,a8,a9 = "";
    String[] playerNames = {"player0","player1","player2","player3","player4","player5",
            "player6","player7","player8","player9"};
    String[] subStr = new String[9];
    String[] data = new String[9]; // use to connect and display spinner adapter
    ArrayList<String> users = new ArrayList();
    ArrayAdapter<String> adapter;
    ListView usersList;
    private AdView mAdView;

    private InterstitialAd mInterstitialAd;
    ReviewInfo reviewInfo;
    ReviewManager manager;


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
            case R.id.action_save_result:
                //Код, выполняемый при выборе элемента Create Save
                Toast.makeText(MainActivity.this, R.string.save_result,
                        Toast.LENGTH_SHORT).show();
                // TODO: 30.09.2020 error om Review
                strSave = strDate + " " + userNameLeft + " " + scorePlayer1 +
                        ":" + scorePlayer2 + " " + userNameRight + ",";
//                File fileSave = new File(FILESAVE);
                appendFile(new File(FILESAVE),strSave);
                Review();
                return true;

            case R.id.action_read_result:
                //Код, выполняемый при выборе элемента Create Save
                Toast.makeText(MainActivity.this,R.string.read_result,
                        Toast.LENGTH_SHORT).show();
                Intent intent_history = new Intent(this, History.class);
                startActivity(intent_history);
                // TODO: 30.09.2020 error om Review
                return true;

            case R.id.action_share_mes:
                //Код, выполняемый при выборе элемента Create Save
//                Toast.makeText(MainActivity.this,R.string.share, Toast.LENGTH_SHORT).show();
                strSave = strDate + " " + userNameLeft + " " + scorePlayer1 +
                        ":" + scorePlayer2 + " " + userNameRight + ",";
                Intent intent_mes = new Intent(Intent.ACTION_SEND);
                intent_mes.setType("text/plain");
                intent_mes.putExtra(Intent.EXTRA_SUBJECT, "Current Result");
                intent_mes.putExtra(Intent.EXTRA_TEXT,strSave);
                startActivity(intent_mes);
                String chooserTitle = getString(R.string.share);
                Intent chosenIntent = Intent.createChooser(intent_mes, chooserTitle);
                startActivity(chosenIntent);
                return true;

            case R.id.action_share_history:
                //Код, выполняемый при выборе элемента Create Save
//                Toast.makeText(MainActivity.this,R.string.share, Toast.LENGTH_SHORT).show();
                File fileSave = new File(FILESAVE);
                File fileDir = getBaseContext().getFilesDir();

//        File sdDir = Environment.getExternalStorageDirectory();
//        String fileName = fileData.getName(); // get file name
                File destination= new File( fileDir+"/"+FILESAVE );
                if (destination.exists()) {
                    readFile(fileSave);
                }
                else {
                    Toast.makeText(MainActivity.this,"No History", Toast.LENGTH_SHORT).show();
                }

                Intent intent_his = new Intent(Intent.ACTION_SEND);
                intent_his.setType("text/plain");
                intent_his.putExtra(Intent.EXTRA_SUBJECT, "History Result");
                intent_his.putExtra(Intent.EXTRA_TEXT,str_Main_Output);
                startActivity(intent_his);
                String chooserTitle2 = getString(R.string.share);
                Intent chosenIntent2 = Intent.createChooser(intent_his, chooserTitle2);
                startActivity(chosenIntent2);
                return true;

            case R.id.action_clear_result:
                //Код, выполняемый при выборе элемента Create Save
                Toast.makeText(MainActivity.this,R.string.clear_result,
                        Toast.LENGTH_SHORT).show();
                historyClear();
                return true;

            case R.id.action_edit_player:
                //Код, выполняемый при выборе элемента Create Save
                Toast.makeText(MainActivity.this, R.string.edit_player, Toast.LENGTH_SHORT).show();
                Intent intent_player = new Intent(this, Player.class);
                startActivity(intent_player);
                return true;

            case R.id.action_create_timer:
                //Код, выполняемый при выборе элемента Create Save
                Toast.makeText(MainActivity.this, R.string.inwork, Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_help:
                Intent intent_help = new Intent(this, Help.class);
                startActivity(intent_help);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void Review() {
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
    private void readFile(File file) {
        try {
            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    openFileInput(String.valueOf(file))));
            String str  = "";
            // TODO: 11.08.2020 read not line by line but till the end of file

            // читаем содержимое
//            while ((str = br.readLine()) != null) {
            while ((str =br.readLine()) != null) {
                Log.d(LOG_TAG, str);
                strUser = str;
            }

           str_Main_Output = strUser;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void writeFile(File file, String strWrite) {

        try {
                // отрываем поток для записи

                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                        openFileOutput(String.valueOf(file), MODE_PRIVATE)));
                // пишем данные
                bw.write(strWrite);

            // закрываем поток
            bw.close();
            Log.d(LOG_TAG, "Файл записан " + String.valueOf(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void appendFile(File file, String strWrite) {

        try {
            // отрываем поток для записи

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    openFileOutput(String.valueOf(file), MODE_APPEND)));
            // пишем данные
            bw.append(strWrite);

            // закрываем поток
            bw.close();
            Log.d(LOG_TAG, "Файл записан " + String.valueOf(file));
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

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }

        });

//        MobileAds.initialize(this,
//                "ca-app-pub-7279174300665421~2703649226");


        //test ad ca-app-pub-3940256099942544/6300978111
        //real ad ca-app-pub-7279174300665421/3496010231

//      banner
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

//      interpage ad

        InterstitialAd.load(this,"ca-app-pub-7279174300665421/6564833801", adRequest, new InterstitialAdLoadCallback() {
            private static final String TAG = "";

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
                Log.i(TAG, "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                Log.i(TAG, loadAdError.getMessage());
                mInterstitialAd = null;
            }
        });

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

        File fileData = new File(FILEDATA);
        File fileDir = getBaseContext().getFilesDir();
//        File sdDir = Environment.getExternalStorageDirectory();
//        String fileName = fileData.getName(); // get file name
        File destination= new File( fileDir+"/"+FILEDATA ); // create destination file with name and dir.
        if (destination.exists()) {
            for (int i = 0; i < 9; i++) {
                 data[i] = "";
                }
            readFile(fileData);
            strUser = strUser.replace("null", "player");
            strUser = strUser.replace("]", "");
            strUser = strUser.replace("[", "");
            strUser = strUser.replace(" ", "");

            String delimeter = ","; // Разделитель
            subStr = strUser.split(delimeter); // Разделения строки str с помощью метода split()

            for (int i = 0; i < subStr.length; i++) {
                    data[i] = subStr[i];

            }
            int n = subStr.length;
              for (int i = 0; i < n; i++){
                users.add (i,data[i]);
              }

            }
        else{
            for (int i = 0; i < 9; i++) {
                data[i] = playerNames[i];
            }
        }


        // адаптер
        String date = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(Calendar.getInstance().getTime());
        strDate = date;
        Spinner spinner = (Spinner) findViewById(R.id.buttonPlayer1);
        int posAlcoType = spinner.getSelectedItemPosition();
//        userNameLeft = String.valueOf(spinner.getItemIdAtPosition(posAlcoType));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

//        manager = ReviewManagerFactory.create(this);
        spinner.setAdapter(adapter);
        // заголовок
        spinner.setPrompt("Choose player");
        // выделяем элемент
//        spinner.setSelection(1);
        // устанавливаем обработчик нажатия
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                userNameLeft = spinner.getSelectedItem();
                // показываем позиция нажатого элемента
//                Toast.makeText(getBaseContext(), "Position = " + position, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                userNameLeft = spinner.getSelectedItem();
            }
        });

//        String[] data2 = {"player1", "player2", "three", "four", "five"};
        // адаптер
        Spinner spinner2 = (Spinner) findViewById(R.id.buttonPlayer2);
//        int posAlcoType = spinner.getSelectedItemPosition();
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

//        manager = ReviewManagerFactory.create(this);
        spinner2.setAdapter(adapter2);
        // заголовок
        spinner2.setPrompt("Choose player");
        // выделяем элемент
        spinner2.setSelection(1);
        // устанавливаем обработчик нажатия
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                userNameRight = spinner2.getSelectedItem();
                // показываем позиция нажатого элемента
//                Toast.makeText(getBaseContext(), "Position = " + position, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                userNameRight = spinner2.getSelectedItem();
            }
        });
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
//        seconds = secondsChoice;

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
                    Toast.makeText(MainActivity.this, R.string.game_over, Toast.LENGTH_SHORT).show();
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
            /*
            TextView scoreViewPlayer1 = (TextView) findViewById(R.id.leftScore);
            scorePlayer1 = scorePlayer1 + 1;
            String strScorePlayer1 = String.format("%d", scorePlayer1);
            scoreViewPlayer1.setText(strScorePlayer1);
            */
//            changePlayer1();
        }
    }

    public void onClickPlayer2(View view) {
        if (!clickCube) {
            /*
            TextView scoreViewPlayer2 = (TextView) findViewById(R.id.rightScore);
            scorePlayer2 = scorePlayer2 + 1;
            String strScorePlayer2 = String.format("%d", scorePlayer2);
            scoreViewPlayer2.setText(strScorePlayer2);

             */
//            changePlayer2();
        }
    }
    public void onClickScore1(View view) {
//        No need to check clickCube as in earlier version
            TextView scoreViewPlayer1 = (TextView) findViewById(R.id.leftScore);
            scorePlayer1 = scorePlayer1 + 1;
            String strScorePlayer1 = String.format("%d", scorePlayer1);
            scoreViewPlayer1.setText(strScorePlayer1);
    }
    public void onClickScore2(View view) {
        //        No need to check clickCube as in earlier version
            TextView scoreViewPlayer2 = (TextView) findViewById(R.id.rightScore);
            scorePlayer2 = scorePlayer2 + 1;
            String strScorePlayer2 = String.format("%d", scorePlayer2);
            scoreViewPlayer2.setText(strScorePlayer2);
    }

    public void onClickSecondsRadio(View view) {
        radioButton5 = (RadioButton) findViewById(R.id.radioButton5);
        radioButton10 = (RadioButton) findViewById(R.id.radioButton10);
        radioButtonCube = (RadioButton) findViewById(R.id.radioButtonCube);


        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked.


        switch (view.getId()) {
            case R.id.radioButton5:
                // interpage adds
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(MainActivity.this);
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }
                if (checked) {
                    secondsChoice = 305;
                    radioButton10.setChecked(false);
                    radioButtonCube.setChecked(false);

                }
                break;
            case R.id.radioButton10:
                // interpage adds
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(MainActivity.this);
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }
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
//                    beforeCubes();
                    Intent intent_emptycubes = new Intent(this, EmptyCubes.class);
                    startActivity(intent_emptycubes);
//                    startCubes();
                 //   clickCube = false;
                }
                break;

            default:
                // Do nothing.
                break;
        }
    }
    public void beforeCubes() {
        if (clickCube) {
//            Random r1 = new Random();
//            int i1 = r1.nextInt(7 - 1) + 1;
            Handler handler = new Handler();
            for (int i = 0; i < 7; i++) {
                scorePlayer1 = i;
                TextView scoreViewPlayer1 = (TextView) findViewById(R.id.leftScore);
                String strScorePlayer1 = String.format("%d",scorePlayer1);
//                scoreViewPlayer1.setText(strScorePlayer1);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scoreViewPlayer1.setText(strScorePlayer1);
                    }
                }, 10000);
            }
//            Random r2 = new Random();
//            int i2 = r2.nextInt(7 - 1) + 1;
//            scorePlayer2 = i2;
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
        //        No need to check clickCube as in earlier version
        if (scorePlayer1 > 0) {
            TextView scoreViewPlayer1 = (TextView) findViewById(R.id.leftScore);
            scorePlayer1 -= 1;
            String strScorePlayer1 = String.format("%d", scorePlayer1);
            scoreViewPlayer1.setText(strScorePlayer1);
        }

    }

    public void onClickScoreAdj2(View view) {
        if (scorePlayer2 > 0 ) {
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
            radioButton5 = (RadioButton) findViewById(R.id.radioButton5);
            radioButton10 = (RadioButton) findViewById(R.id.radioButton10);
            radioButtonCube = (RadioButton) findViewById(R.id.radioButtonCube);
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

    private void historyClear() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.confirm);
        builder.setMessage(R.string.are_you_shure);

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                writeFile(new File(FILESAVE),"");
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



}
