package com.arhsota.easy;

/*******************************************************************************
 *
 *  * Created by Andrey Sevastianov on 12 nov 2019
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 27.05.20 0:41
 *
 ******************************************************************************/

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AssureDate extends AppCompatActivity {
    String expDate="";
    String myPath;
    int cYear,cMonth,cDay;
    boolean isTime = false;

    File directory;
    private File file;
    final   String LOG_TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_assure_date);

        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView1);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year,
                                            int month, int dayOfMonth) {
                int mYear = year;
                int mMonth = month;
                int mDay = dayOfMonth;


               String selectedDate = new StringBuilder().append(mDay)
                        .append("-").append(mMonth +1 ).append("-").append(mYear)
                        .toString();


                expDate = selectedDate;
 // TODO: 16.05.2020 remove old code using calendar etc...
                Calendar validDate = Calendar.getInstance();
                validDate.set(mYear,mMonth,mDay);
                Calendar currentDate = Calendar.getInstance();
                cYear = currentDate.get(Calendar.YEAR);
                cMonth = currentDate.get(Calendar.MONTH);
                cDay = currentDate.get(Calendar.DAY_OF_MONTH);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());

                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED) {

                        Log.d("permission", "permission denied to external device - requesting it");
                        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permissions, 1);

                    }
                }

                createDirectory();
                file = new File( directory,"expiredate.txt");
                writeFile();

            }
        });


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

    private void writeFile() {
        try {
            // отрываем поток для записи
            BufferedWriter bw = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                bw = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream((String.valueOf(file)), false)));
            }
            // пишем данные
            assert bw != null;
            bw.write(expDate);
            // закрываем поток
            bw.close();
            Toast.makeText(AssureDate.this, "Дата окончания " +expDate + " записана",
                    Toast.LENGTH_LONG).show();
            Log.d(LOG_TAG, "Файл записан");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
//            Toast.makeText(AssureDate.this, "No file" +expDate + " записана",
//                    Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
