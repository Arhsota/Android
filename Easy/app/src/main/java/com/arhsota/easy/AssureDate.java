package com.arhsota.easy;

/*******************************************************************************
 *
 *  * Created by Andrey Sevastianov on 16.05.20 23:57
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 16.05.20 23:50
 *
 ******************************************************************************/

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AssureDate extends AppCompatActivity {
    String expDate;
    int cYear,cMonth,cDay;
    boolean isTime = false;
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
                        .append(" ").toString();
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
}
