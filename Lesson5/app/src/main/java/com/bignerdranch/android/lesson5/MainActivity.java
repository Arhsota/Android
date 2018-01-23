package com.bignerdranch.android.lesson5;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity  {
    private TextView weight;
    private TextView length;
    private TextView imt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weight = (TextView) findViewById(R.id.weight);
        length = (TextView) findViewById(R.id.length);
        imt = (TextView) findViewById(R.id.imt);
        Button CalcBtn = (Button) findViewById(R.id.calc_btn);

     /*   helloTv.setOnClickListener(onClickListener); // Обработчик

      */
        CalcBtn.setOnClickListener(onClickListener);
    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v){

                    imt.setText("Hi!");




             }
         };
}
