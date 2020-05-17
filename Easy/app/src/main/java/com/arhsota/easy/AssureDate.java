package com.arhsota.easy;

/*******************************************************************************
 *
 *  * Created by Andrey Sevastianov on 18.05.20 1:16
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 18.05.20 0:00
 *
 ******************************************************************************/

import android.os.Bundle;
import android.os.Environment;
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
    String expDate,myPath;
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


/*            String selectedDate = new StringBuilder().append(mDay)
                        .append(mMonth +1 ).append(mYear)
                        .toString();

 */

//                Toast.makeText(getApplicationContext(), selectedDate, Toast.LENGTH_LONG).show();
                expDate = selectedDate;
 // TODO: 16.05.2020 remove old code using calendar etc...
                Calendar validDate = Calendar.getInstance();
                validDate.set(mYear,mMonth,mDay);
                Calendar currentDate = Calendar.getInstance();
                cYear = currentDate.get(Calendar.YEAR);
                cMonth = currentDate.get(Calendar.MONTH);
                cDay = currentDate.get(Calendar.DAY_OF_MONTH);
//      Check date to start broadcasting
// TODO: 16.05.2020 cDay only for testing
                if ((mYear == cYear) && (mMonth +1 == cMonth +1) && (cDay == 16) || (cDay == 30)){
                    isTime = true;
                    Toast.makeText(getApplicationContext(), "Time", Toast.LENGTH_LONG).show();

                }
                createDirectory();
                file = new File( directory,"expiredate.txt");
                writeFile();
/*               if(validDate.compareTo(currentDate) < 0) {
                    Toast.makeText(getApplicationContext(), "before today", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "after today", Toast.LENGTH_LONG).show();
                }

 */
/*                if (currentDate.after(validDate)) {
                    Toast.makeText(getApplicationContext(), "after", Toast.LENGTH_LONG).show();
                }
 */
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
            Log.d(LOG_TAG, "Файл записан");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
