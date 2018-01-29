package com.test.lessons;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
  //*  private TextView textViewGB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.hello_tv);
        Button button =  findViewById(R.id.hello_btn);

     //*   TextView textViewGB = findViewById(R.id.goodbye_tv);
     //*   Button buttonGB = findViewById(R.id.goodbye_btn);

        button.setOnClickListener(onClickListener);
    }


    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            textView.setText("Hi!");
           //* textViewGB.setText("Empty");

        }
    };
}
