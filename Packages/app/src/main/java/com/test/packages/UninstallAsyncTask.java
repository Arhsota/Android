package com.test.packages;

import android.os.AsyncTask;
import android.widget.Toast;

import java.lang.ref.WeakReference;

/**
 * Created by arhso on 11.03.2018.
 * Lesson 18
 */

public class UninstallAsyncTask extends
        AsyncTask<String, Void, Boolean> {

    private final WeakReference<UninstallListener>uninstallListenerWeakReference; //L19
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
        // Получаем сильную ссылку
        UninstallListener uninstallListener =
                uninstallListenerWeakReference.get();
        // Проверяем на null
        if (uninstallListener != null) {
// Вызываем соответствующий метод
            if (result) {
                uninstallListener.onUninstalled();
            } else {
                uninstallListener.onFailed();
            }
        }
    }
    public interface UninstallListener {
        void onUninstalled();
        void onFailed();
    }
// L19
    public UninstallAsyncTask(UninstallListener
                                      uninstallListener) {
        super();
        this.uninstallListenerWeakReference = new
                WeakReference<>(uninstallListener);
    }

}
