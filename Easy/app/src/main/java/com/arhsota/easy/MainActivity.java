package com.arhsota.easy;

// for Nikita Lisenko
// 28 november 2019

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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

    private boolean fillTextLength = false;  //for ckecking length
    private boolean checkFieldPhone = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTxtClientPhone = findViewById(R.id.txtClientPhone);
        //                        for checking empty or not Client Phone field and length is less then 10
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


//        checkFieldPhone = true;
//
        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



            FloatingActionButton callphone = findViewById(R.id.callph);

            callphone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

// Checking permissions. If Not asks owner to make it by himself

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

                        if (checkSelfPermission(Manifest.permission.CALL_PHONE)
                                == PackageManager.PERMISSION_DENIED) {

                            Log.d("permission", "permission denied to CALL PHONE - requesting it");
                            String[] permissions = {Manifest.permission.CALL_PHONE};
                            requestPermissions(permissions, 1);

                        }

                    }
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+79022865609"));
                    startActivity(intent);
                }


            });

            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    chBox = findViewById(R.id.checkBox);
                    strPhone = editTxtClientPhone.getText().toString();
                    if ((!chBox.isChecked()) || (!checkFieldPhone)){

                        Toast.makeText(MainActivity.this, "Не приняли соглашение или не ввели номер",
                                Toast.LENGTH_SHORT).show();
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
                        String myDir = myPath +"/21.jpg";
//                        Uri uri = Uri.parse(myDir);
//                       Uri uri = Uri.fromFile(new File((myDir)) );
//                       Uri uri = FileProvider.getUriForFile(MainActivity.this,"read",file) ;

                        String[] listOfPictures = directory.list();
                        Uri uri=null;
                        ArrayList<Uri> uris = new ArrayList<>();
                        for (String file : listOfPictures)
                        {
                            uri = Uri.parse("file://" + directory.toString() + "/" + file);
                            uris.add (uri);
                        }



                        Intent shareIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                        shareIntent.setType("rar/image");
                        shareIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"arhsota@gmail.com"});
                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        shareIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Содержимое каталога для: " +strPhone);
      //                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "test1");
//                        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, strPhone);
//                        todo: make multiattacment
//                        shareIntent.putExtra(android.content.Intent.EXTRA_STREAM, uris);
//                        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
//                        startActivity(Intent.createChooser(shareIntent,"Email:"));
                        startActivity(shareIntent);
                    }
                }
            }
        );


    }

    public void onClickPhoto(View view) {
        chBox = findViewById(R.id.checkBox);
        if ((!chBox.isChecked()) || (!checkFieldPhone)){

            Toast.makeText(MainActivity.this, "Вы не приняли соглашение или не ввели номер",
                    Toast.LENGTH_SHORT).show();
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
