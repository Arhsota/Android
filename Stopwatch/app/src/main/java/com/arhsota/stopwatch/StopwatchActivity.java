// не соответствует работа виртуального устройства и реального, таймер,при отсутствии фокуса и затем при появлении
// его, на реальном устройстве сбрасывается

package com.arhsota.stopwatch;

import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import static com.arhsota.stopwatch.R.id.radioButton5;

public class StopwatchActivity extends AppCompatActivity {
    //Количество секунд на секундомере
    private int seconds = 300;
    private int secondsChoice = 300;
    private int scorePlayer1 = 0;
    private int scorePlayer2 = 0;
    //Секундомер работает?
    private boolean running;
    private boolean wasRunning;
    private boolean player1;
    private boolean player2;
    private RadioButton radioButton5;
    private RadioButton radioButton10;

    Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);
       // seconds = secondsChoice;
        if (savedInstanceState != null) {
            seconds = savedInstanceState.getInt("seconds");
            running = savedInstanceState.getBoolean("running");
            wasRunning = savedInstanceState.getBoolean("wasRunning");
            player1 = savedInstanceState.getBoolean("player1");
        }
        runTimer();

    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState); // этой строки нет в книге
     //   seconds = secondsChoice;
        savedInstanceState.putInt("seconds", seconds);
        savedInstanceState.putBoolean("running", running);
        savedInstanceState.putBoolean("wasRunning", wasRunning);
        savedInstanceState.putBoolean("player1", player1);
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
        running = false;
        seconds = secondsChoice;
    }

    private void runTimer() {
        final TextView timeView = (TextView) findViewById(R.id.time_view);
        final Handler handler = new Handler();
        seconds = secondsChoice;

        handler.post(new Runnable() {
            @Override
            public void run() {

                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;
                String time = String.format("%d:%02d:%02d",hours, minutes, secs);
                if (seconds == 0) {
                    Toast.makeText(StopwatchActivity.this, "Game OVER",
                            Toast.LENGTH_SHORT).show();
                    }
                timeView.setText(time);
                if ((running) && (seconds > 0)) {
                    seconds--;
                }
                handler.postDelayed(this, 1000);

            }
        });
    }

    public void onClickPlayer1(View view) {
        TextView scoreViewPlayer1 = (TextView) findViewById(R.id.leftScore);
        scorePlayer1 = scorePlayer1+1;
        String strScorePlayer1 = String.format("%d",scorePlayer1);
        scoreViewPlayer1.setText(strScorePlayer1);
    }

    public void onClickPlayer2(View view) {
        TextView scoreViewPlayer2 = (TextView) findViewById(R.id.rightScore);
        scorePlayer2 = scorePlayer2+1;
        String strScorePlayer2 = String.format("%d",scorePlayer2);
        scoreViewPlayer2.setText(strScorePlayer2);
    }

    public void onClickSecondsRadio(View view) {
        radioButton5 = (RadioButton) findViewById(R.id.radioButton5);
        radioButton10 = (RadioButton) findViewById(R.id.radioButton10);

        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked.
        switch (view.getId()) {
            case R.id.radioButton5:
                if (checked) {
                    secondsChoice = 300;
                    radioButton10.setChecked(false);

                }
                break;
            case R.id.radioButton10:
                if (checked) {
                    secondsChoice = 600;
                    radioButton5.setChecked(false);
                }
                break;

            default:
                // Do nothing.
                break;
        }
    }


}
