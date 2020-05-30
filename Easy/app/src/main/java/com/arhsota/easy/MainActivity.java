package com.arhsota.easy;

/*******************************************************************************
 *
 *  * Created by Andrey Sevastianov on 12 nov 2019
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 30.05.20 1:51
 *
 ******************************************************************************/

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.os.Environment;
import android.os.StrictMode;
import android.os.SystemClock;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.provider.Telephony.Mms.Part.FILENAME;

// Using help from https://startandroid.ru/ru/uroki/vse-uroki-spiskom/254-urok-131-kamera-ispolzuem-sistemnoe-prilozhenie.html

public class MainActivity extends AppCompatActivity {

    File directory;
    final int TYPE_PHOTO = 1;
    final int TYPE_VIDEO = 2;

    final int REQUEST_CODE_PHOTO = 1;
    final int REQUEST_CODE_VIDEO = 2;

    final String TAG = "myLogs";

    String expDate = "";
    String readStr;
    String strDealerCode = "";


    ImageView ivPhoto;

//    private File fileExpDate;
//    private File fileCode;
    private EditText editTxtClientPhone;
    private EditText textDealerCode;
    private String strPhone;
    final   String LOG_TAG = "myLogs";

    private CheckBox chBox;
    boolean isTime = false;

    public static String PACKAGE_NAME;
    public static String myPath;

    private boolean fillTextLength = false;  //for ckecking length
    private boolean checkFieldPhone = false;

    private boolean fillTextLengthCodeDealer = false;  //for ckecking length
    private boolean checkFieldCodeDealer = false;

    private TextView textView;
    private Button textViewAssure;
    private NotificationManager mNotificationManager;
    private static final int NOTIFICATION_ID = 0;
    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//   For notification
        mNotificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        Intent notifyIntent = new Intent(this, AlarmReceiver.class);
        boolean alarmUp = (PendingIntent.getBroadcast(this, NOTIFICATION_ID,
                notifyIntent, PendingIntent.FLAG_NO_CREATE) != null);
// TODO: 18.05.2020 may be need further to use toggle button
//        alarmToggle.setChecked(alarmUp);
        final PendingIntent notifyPendingIntent = PendingIntent.getBroadcast
                (this, NOTIFICATION_ID, notifyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        final AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

//   Help layout with scrolling
        textView = findViewById(R.id.textView);
        textView.setMovementMethod(new ScrollingMovementMethod());
//   Client's phone number
        editTxtClientPhone = findViewById(R.id.txtClientPhone);

//   Dealer's code for discount
        textDealerCode = findViewById(R.id.dealer_code);
//   Text date of expire date in field to check if it's exist or not
        textViewAssure = findViewById(R.id.txt_assure_end);

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
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d("permission", "permission denied to WRITE - requesting it");
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, 1);

            }
        }
        createDirectory();

//     Reading codedealer file and checking if it is empty or not. Is it have expire date
         File fileReadCode = new File( directory,"code.txt");
        if (fileReadCode.exists()) {
            readFile(fileReadCode);
            strDealerCode = readStr;
            textDealerCode.setText(strDealerCode);
        } else {
            writeFile(fileReadCode,strDealerCode);
        }


//     Reading expiredate file and checking if it is empty or not. Is it have expire date
        File fileReadExpDate = new File( directory,"expiredate.txt");
        if (fileReadExpDate.exists()) {
            readFile(fileReadExpDate);
            expDate = readStr;


            if (expDate != "") {
                isTime = true;
            }
        }
//     Parsing, if we have something string in file to compare it with current date
        if (isTime) {
            textViewAssure.setText(expDate); // for checking expire date
            if (expDate==null) { expDate ="0-0-0";} // for excluding mistake when the first install
                String[] subStr;
                String delimeter = "-"; // Разделитель
                subStr = expDate.split(delimeter); // Разделения строки str с помощью метода split()
                int dd = Integer.parseInt(subStr[0]); // Day from file
                int mm = Integer.parseInt(subStr[1]); // Month from file
                int yy = Integer.parseInt(subStr[2]); // Year from file

//      Current date
            Calendar currentDate = Calendar.getInstance();
            int cYear = currentDate.get(Calendar.YEAR);
            int cMonth = currentDate.get(Calendar.MONTH);
            int cDay = currentDate.get(Calendar.DAY_OF_MONTH);
// TODO: cDay should be = 1 (the first day of month)
            if ((yy == cYear) && (mm  == cMonth +1) && (cDay == 1)){
//                Toast.makeText(getApplicationContext(), "Time", Toast.LENGTH_LONG).show();

                String toastMessage;
//  long repeatInterval = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
                //  in the original
                long repeatInterval = alarmManager.INTERVAL_HALF_DAY;
//                long repeatInterval = 10;
                long triggerTime = SystemClock.elapsedRealtime();
                // TODO repeatInterval may vary, depends upon how often to make alarm

                //If the Toggle is turned on, set the repeating alarm with a 15 minute interval
                if (alarmManager != null) {
                    alarmManager.setInexactRepeating
                            (AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                    triggerTime, repeatInterval, notifyPendingIntent);
                }
                //Set the toast message for the "on" case.
                toastMessage = "Stand Up Alarm On!";
            }
            else {
                // Cancel notification if the alarm is turned off.
                mNotificationManager.cancelAll();
                if (alarmManager != null) {
                    alarmManager.cancel(notifyPendingIntent);
                }
                //Set the toast message for the "off" case.
//                toastMessage = "Stand Up Alarm Off!";
            }
            createNotificationChannel();

        }

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
                    strDealerCode = textDealerCode.getText().toString();
                    strPhone = strPhone + " " +strDealerCode;
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
//    New layout on clicking button MAKE PHOTO
        chBox = findViewById(R.id.checkBox);
        if ((!chBox.isChecked()) || (!checkFieldPhone)){

            Toast.makeText(MainActivity.this, "Вы не приняли соглашение или не ввели номер",
                    Toast.LENGTH_LONG).show();
            return;

        }
        else {
            strPhone = editTxtClientPhone.getText().toString();
            strDealerCode = textDealerCode.getText().toString();
            strPhone = strPhone + " " +strDealerCode;
            createDirectory();
//   Dealer code is written to the same folder Easy, where photos
            File file = new File( this.directory,"code.txt");
            writeFile(file,strDealerCode);
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
        if (id == R.id.action_help) {
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

        if (id == R.id.action_krown) {

            Toast.makeText(MainActivity.this, "Звонок в Krown",
                    Toast.LENGTH_LONG).show();
            callPhone(getString (R.string.phone_number_krown));

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

    private void writeFile(File file,String strWrite) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d("permission", "permission denied to WRITE - requesting it");
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, 1);

            }
        }

        try {
            // отрываем поток для записи
            BufferedWriter bw = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                bw = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream((String.valueOf(file)), false)));
            }
            // пишем данные
            assert bw != null;
            bw.write(strWrite);
            // закрываем поток
            bw.close();
            Log.d(LOG_TAG, "Файл записан");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void readFile(File file) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d("permission", "permission denied to CAMERA - requesting it");
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions, 1);

            }
        }

        try {
            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(String.valueOf(file))));
            String str;
            // читаем содержимое
            while ((str = br.readLine()) != null) {
                Log.d(LOG_TAG, str);
                readStr = str;
            }
                     } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    private void createDirectory() {
        directory = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "Easy");
        if (!directory.exists())
            directory.mkdirs();
        myPath = directory.toString();
    }

    public void onClickDate(View view) {
        Intent intent = new Intent(this, AssureDate.class);
        startActivity(intent);
    }

    /**
     * Creates a Notification channel, for OREO and higher.
     */
    public void createNotificationChannel() {

        // Create a notification manager object.
        mNotificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Notification channels are only available in OREO and higher.
        // So, add a check on SDK version.
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {

            // Create the NotificationChannel with all the parameters.
            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID,
                            "Stand up notification",
                            NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription
                    ("Notifies every 15 minutes to stand up and walk");
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }

}
