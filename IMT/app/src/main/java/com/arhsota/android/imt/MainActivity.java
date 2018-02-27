package com.arhsota.android.imt;

//* my first real soft based on lesson 8 Skillberg
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private EditText editTextW;
    private EditText editTextL;
    private Button button;
    private   double myweight;
    private   double mylength;
    private   double myresult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.result_out);
        final Button button = findViewById(R.id.calculate_btn);
        editTextW = findViewById(R.id.weight);
        editTextL = findViewById(R.id.length);

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
                if (editable.toString().trim().length() > 0) {
                    button.setEnabled(true);
                } else {
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
                    if (editable.toString().trim().length() > 0) {
                      button.setEnabled(true);
                   } else {
                      button.setEnabled(false);
                   }
             }
        }
        );

        button.setOnClickListener(onClickListener);
    }


    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String weight = editTextW.getText().toString();
            String length = editTextL.getText().toString();


            myweight = Double.parseDouble(weight);
            mylength = Double.parseDouble(length);
            myresult = myweight/(mylength * mylength); //calculatin IMT


//            String myresultStr = Double.toString(myresult);
            String myresultStrFormat = String.format("%.2f",myresult);

//            if (weight.equals ("11")) {                // for string
            if (myresult >= 30) {
                textView.setText(getString(R.string.name_text_format,myresultStrFormat));
            }
            else {
                textView.setText(getString(R.string.name_text_format_normal,myresultStrFormat));
            }

        }

    };
}
