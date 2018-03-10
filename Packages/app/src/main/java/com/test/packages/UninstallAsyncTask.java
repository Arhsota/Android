package com.test.packages;

import android.os.AsyncTask;

/**
 * Created by arhso on 11.03.2018.
 * Lesson 18
 */

public class UninstallAsyncTask extends
        AsyncTask<String, Void, Boolean> {
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected Boolean doInBackground(String... params) {
        String packageName = params[0];
        boolean result = RootHelper.uninstall(packageName);
        return result;
    }
    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
    }
}
