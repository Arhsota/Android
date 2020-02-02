package com.arhsota.easy;

/*
 *
 *  * Created by Andrey Sevastianov on 03.12.19 10:18
 *  * Copyright (c) 2019,2020 . All rights reserved.
 *  * Last modified 30.01.20 22:13
 *
 *

 *  * for Nikita Lisenko
 *  * december 2019, january 2020
 *  * Sabetta release
 *  * Arkhangelsk
 */

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// Using help from https://startandroid.ru/ru/uroki/vse-uroki-spiskom/254-urok-131-kamera-ispolzuem-sistemnoe-prilozhenie.html

public class MainActivity extends AppCompatActivity {

    File directory;
    final int TYPE_PHOTO = 1;
    final int TYPE_VIDEO = 2;

    final int REQUEST_CODE_PHOTO = 1;
    final int REQUEST_CODE_VIDEO = 2;

    final String TAG = "myLogs";

    ImageView ivPhoto;

    private File file;
    private EditText editTxtClientPhone;
    private String strPhone;

    private CheckBox chBox;

    public static String PACKAGE_NAME;

    private boolean fillTextLength = false;  //for ckecking length
    private boolean checkFieldPhone = false;

    private TextView textView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        textView.setMovementMethod(new ScrollingMovementMethod());
        editTxtClientPhone = findViewById(R.id.txtClientPhone);
 //     for checking empty or not Client Phone field and length is less then 10
        editTxtClientPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                editTxtClientPhone.setText("");

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                fillTextLength = editable.toString().trim().length() > 7;
                if (fillTextLength) {
//                    editTxtClientPhone.setEnabled((true));
                    checkFieldPhone = true;
                }
                else {
//                    editTxtClientPhone.setEnabled((false));
//                    strPhone = "Вы не ввели номер телефона";
                    checkFieldPhone = false;
                }

            }
        });



//    for further maybe, was taken from example

//        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


// Calling manager Lavinia

            final FloatingActionButton callphone = findViewById(R.id.callph);

            callphone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                  Toast.makeText(MainActivity.this, "Звонок менеджеру",
                            Toast.LENGTH_LONG).show();

                   callPhone(getString (R.string.phone_number));

                }


            });


//   Send docs from folder Easy by mail

        FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    chBox = findViewById(R.id.checkBox);
                    strPhone = editTxtClientPhone.getText().toString();
                    if ((!chBox.isChecked()) || (!checkFieldPhone)){

                        Toast.makeText(MainActivity.this, "Не приняли соглашение или не ввели номер",
                                Toast.LENGTH_LONG).show();
                        return;

                    }
                    else {
                        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                        StrictMode.setVmPolicy(builder.build());
                        Snackbar.make(view, "Отправляю документы...", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        String fileName = (MakePhoto.myPath);
                        String externalStorageDirectory = Environment.getRootDirectory().toString();
      //                String myDir = externalStorageDirectory + "/Pictures/Easy/"; // the // file will be in saved_images
                        directory = new File(
                                Environment
                                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                                "Easy");
                        String myPath = directory.toString();
//                        String myDir = myPath +"/21.jpg";

                       choiceEmail();

                    }

                }


            }
        );


    }
    private void choiceEmail() {
        String[] listOfPictures = directory.list();
        if (listOfPictures != null) {

            Uri uri;
            ArrayList<Uri> uris = new ArrayList<>();
            for (String file : listOfPictures) {
                uri = Uri.parse("file://" + directory.toString() + "/" + file);
                uris.add(uri);
            }

//            selector
          Intent emailSelectorIntent = new Intent(Intent.ACTION_SENDTO);
//            emailSelectorIntent.setDataAndType(Uri.parse("mailto:"),"text/plain");
             emailSelectorIntent.setData(Uri.parse("mailto:"));

           final  Intent shareIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
//            shareIntent.setType("message/rfc822");
//            shareIntent.setClassName("com.google.android.gm","com.google.android.gm.ComposeActivityGmail");
//            shareIntent.setDataAndType(Uri.parse(getString(R.string.email)),"message/rfc822");
//                            shareIntent.setType("text/html");
//                            shareIntent.setData(uriEmail);
            shareIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{getString(R.string.email)});

            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Содержимое каталога для: " + strPhone);
            shareIntent.setSelector( emailSelectorIntent );
//                            shareIntent.putExtra(Intent.A, getString (R.string.phone_number));
//                            shareIntent.putExtra("address", "12122222222");
//                              shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, addressViber);
//            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, strPhone);

//                         shareIntent.putExtra(android.content.Intent.EXTRA_STREAM, uris);
//                        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
//            shareIntent.putExtra(Intent.EXTRA_STREAM, uris);
//                            shareIntent.setComponent(new ComponentName("com.whatsapp","com.whatsapp.ContactPicker"));
//                            shareIntent.putExtra("jid", "79022865609");
//                        startActivity(Intent.createChooser(shareIntent,"Email:"));
//                           startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("viber://chat?number=7900000000")));
//                            shareIntent.setPackage("com.whatsapp");
//                            startActivity(shareIntent);
//            shareIntent.setSelector( emailSelectorIntent );
//            shareIntent.setSelector( emailSelectorIntent );


            if( shareIntent.resolveActivity(getPackageManager()) != null ) {
//                startActivity(shareIntent);
                startActivity(Intent.createChooser(shareIntent,"Выберите"));
               }
            else {
                Toast.makeText(MainActivity.this, "Не установлен почтовый клиент! Установите, пожалуйста",
                        Toast.LENGTH_LONG).show();
                return;
                 }

//            startActivity(shareIntent);
//              shareIntent.setSelector( emailSelectorIntent );
//            startActivity(Intent.createChooser(shareIntent,"choose"));

        }
         else {

            Toast.makeText(MainActivity.this, "Вы ничего не сфотографировали, папка пустая",
                    Toast.LENGTH_LONG).show();
            return;

         }
    }

    public void onClickPhoto(View view) {
        chBox = findViewById(R.id.checkBox);
        if ((!chBox.isChecked()) || (!checkFieldPhone)){

            Toast.makeText(MainActivity.this, "Вы не приняли соглашение или не ввели номер",
                    Toast.LENGTH_LONG).show();
            return;

        }
        else {
            strPhone = editTxtClientPhone.getText().toString();
            Intent make_photo = new Intent(this, MakePhoto.class);

            make_photo.setType("text/plain");
            make_photo.putExtra("TELE", strPhone);
            startActivity(make_photo);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, Help.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_callevacuator) {

           Toast.makeText(MainActivity.this, "Вызываю эвакуатор!!!",
                   Toast.LENGTH_LONG).show();

            callPhone(getString (R.string.phone_number_evacuator));

//           Toast.makeText(MainActivity.this, "В разработке",
//                    Toast.LENGTH_LONG).show();
            return true;
        }

        if (id == R.id.action_service) {

            Toast.makeText(MainActivity.this, "В разработке",
                    Toast.LENGTH_LONG).show();


            return true;
        }

        if (id == R.id.action_krown) {

            Toast.makeText(MainActivity.this, "В разработке",
                    Toast.LENGTH_LONG).show();


            return true;
        }

        return super.onOptionsItemSelected(item);
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
