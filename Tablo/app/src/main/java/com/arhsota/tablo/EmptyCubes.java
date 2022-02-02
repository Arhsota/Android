/*******************************************************************************
 *
 *  * Created by Andrey Sevastianov on 10 january 2019
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 25.01.2022, 19:53
 *
 ******************************************************************************/

package com.arhsota.tablo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Random;

public class EmptyCubes extends Activity implements TextSwitcher.ViewFactory {

    private TextSwitcher mTextSwitcherLeft;
    private TextSwitcher mTextSwitcherRight;
    private int mCounter = 0;
    private int scorePlayer1,scorePlayer2;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_cubes);
        AdView mAdViewSecond;
        mAdViewSecond = findViewById(R.id.adViewAdd);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdViewSecond.loadAd(adRequest);
//      Left cube
        mTextSwitcherLeft = (TextSwitcher) findViewById(R.id.textSwitcherLeft);
        mTextSwitcherLeft.setFactory(this);

        Animation inAnimation = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_in);
        Animation outAnimation = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_out);
        mTextSwitcherLeft.setInAnimation(inAnimation);
        mTextSwitcherLeft.setOutAnimation(outAnimation);

//      Right cube
        mTextSwitcherRight = (TextSwitcher) findViewById(R.id.textSwitcherRight);
        mTextSwitcherRight.setFactory(this);

        Animation inAnimationR = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_in);
        Animation outAnimationR = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_out);
        mTextSwitcherRight.setInAnimation(inAnimationR);
        mTextSwitcherRight.setOutAnimation(outAnimationR);
        updateCounter();
    }

    public void onClick(View v) {

//        mCounter++;
        startCubes();
        updateCounter();
    }

    private void updateCounter() {
        mTextSwitcherLeft.setText(String.valueOf(scorePlayer1));
        mTextSwitcherRight.setText(String.valueOf(scorePlayer2));
    }

    @Override
    public View makeView() {
        TextView textView = new TextView(this);
        textView.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL);
        textView.setTextSize(150);
        textView.setTextColor(Color.RED);
        return textView;
    }
    public void startCubes(){

            Random r1 = new Random();
            int i1 = r1.nextInt(7 - 1) + 1;
            scorePlayer1 = i1;

            Random r2 = new Random();
            int i2 = r2.nextInt(7 - 1) + 1;
            scorePlayer2 = i2;

    /*    TextView scoreViewPlayer1 = (TextView) findViewById(R.id.);
        String strScorePlayer1 = String.format("%d",scorePlayer1);
        scoreViewPlayer1.setText(strScorePlayer1);

        TextView scoreViewPlayer2 = (TextView) findViewById(R.id.rightScore);
        String strScorePlayer2 = String.format("%d",scorePlayer2);
        scoreViewPlayer2.setText(strScorePlayer2);

     */
    }
}