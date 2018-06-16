package com.arhsota.messenger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class CreateMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_message);
    }

    //Вызвать onSendMessage() при щелчке на кнопке
      public void onSendMessage(View view) {
          EditText messageView = (EditText) findViewById(R.id.message);
          String messageText = messageView.getText().toString();

// явный интент
//          Intent intent = new Intent(this, RecieveMessageActivity.class);
//          intent.putExtra(RecieveMessageActivity.EXTRA_MESSAGE, messageText);

//         Chapter 3 page 130
          Intent intent = new Intent(Intent.ACTION_SEND);
          intent.setType("text/plain");
          intent.putExtra(Intent.EXTRA_TEXT, messageText);
//          page 143 always choose intent
          String chooserTitle = getString(R.string.chooser);
          Intent chosenIntent = Intent.createChooser(intent, chooserTitle);
          startActivity(chosenIntent);
      }
}
