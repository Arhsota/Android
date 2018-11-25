package com.arhsota.android.imt;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import static android.content.Context.MODE_PRIVATE;

public class History  {

    final String LOG_TAG = "myLogs";

    final String FILENAME = "file";

    final String DIR_SD = "MyFiles";
    final String FILENAME_SD = "fileSD";

    /** Called when the activity is first created. */

  /*  void writeFile() {
        try {
            // отрываем поток для записи
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
          /         openFileOutput(FILENAME, MODE_PRIVATE)));
            // пишем данные
            bw.write("Содержимое файла");
            // закрываем поток
            bw.close();
            Log.d(LOG_TAG, "Файл записан");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

*/
}
