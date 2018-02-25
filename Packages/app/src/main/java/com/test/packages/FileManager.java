package com.test.packages;

import android.content.Context;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by arhso on 20.02.2018.
 */

public class FileManager {
    private File currentDirectory;
    private final File rootDirectory;
    private FileManager fileManager;

    public FileManager(Context context) {
        File directory;
//        directory = ContextCompat.getDataDir(context);
//
       if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            directory = Environment.getRootDirectory();
        } else {
//           as in lesson 16
//           directory = Environment.getExternalStorageDirectory();

//           my variant
           directory = ContextCompat.getDataDir(context);
        }

        rootDirectory = directory;
        navigateTo(directory);
    }

    public boolean navigateTo(File directory) {
        // Проверим, является ли файл директорией
        if (!directory.isDirectory()) {
            Log.e(TAG, directory.getAbsolutePath() + " is not a directory!");
            return false;
        }

        // Проверим, не поднялись ли мы выше rootDirectory
        if (!directory.equals(rootDirectory) &&
                rootDirectory.getAbsolutePath().contains(directory.getAbsolutePath())) {
            Log.w(TAG, "Trying to navigate upper than root directory to " + directory.getAbsolutePath());
            return false;
        }

        currentDirectory = directory;

        return true;
    }

    public boolean navigateUp() {
        return navigateTo(currentDirectory.getParentFile());
    }

    public List<File> getFiles() {
        List<File> files = new ArrayList<>();
        files.addAll(Arrays.asList(currentDirectory.listFiles()));

        return files;
    }
}

