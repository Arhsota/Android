package com.bignerdranch.android.lesson5;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity  {
    private TextView helloTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helloTv = (TextView) findViewById(R.id.hello_tv);
        Button helloBtn = (Button) findViewById(R.id.hello_btn);

        helloTv.setOnClickListener(onClickListener); // Обработчик
        helloBtn.setOnClickListener(onClickListener);
    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v){
            switch (v.getId()) {

                case R.id.hello_btn:
                    helloTv.setText("Hi!");
                    break;

                case R.id.hello_tv:
                    helloTv.setText("Hello, World!");
                    break;

             }
        }
    };
}
