package com.arhsota.easy;

/*******************************************************************************
 *
 *  * Created by Andrey Sevastianov on 12 nov 2019
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 02.08.20 23:54
 *
 ******************************************************************************/

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Help extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        textView = findViewById(R.id.textHelp);
        textView.setMovementMethod(new ScrollingMovementMethod());



        // Calling manager Lavinia

        final FloatingActionButton callphone = findViewById(R.id.fab);

        callphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(Help.this, "Звонок менеджеру",
                        Toast.LENGTH_LONG).show();

                callPhone(getString (R.string.phone_number));

            }


        });


    }
    private void callPhone (final String strCallNumber){

//         Calling total procedure


// Checking permissions. If Not asks owner to make it by himself

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d("permission", "permission denied to CALL PHONE - requesting it");
                String[] permissions = {Manifest.permission.CALL_PHONE};
                requestPermissions(permissions, 1);


            }
            else {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + strCallNumber));
                startActivity(intent);
            }

        }


    }
}
