package com.test.packages;

// Skillberg.com
// Android lessons

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_APK = 1;
    private static final String TAG = "MainActivity";

    private AppManager appManager;
    private AppsAdapter appsAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Log.i(TAG, "onCreate");  for lesson 15

        setContentView(R.layout.activity_main);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);


        appManager = new AppManager(this);

        appsAdapter = new AppsAdapter();


        RecyclerView recyclerView = findViewById(R.id.apps_rv);
//        making deviding line, only for good view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(appsAdapter);
// Lesson 18
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(itemTouchHelperCallback);
        recyclerView.addItemDecoration(itemTouchHelper);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        reloadApps();
    }
// path to APK file
    @Override
    protected void onActivityResult(int requestCode, int
            resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_APK &&
                resultCode == RESULT_OK) {
            String apkPath =
                    data.getStringExtra(FilePickerActivity.EXTRA_FILE_PATH);
            Log.i(TAG, "APK: " + apkPath);

            startAppInstallation(apkPath);  //            lesson 17

        } else {
            super.onActivityResult(requestCode, resultCode,
                    data);
        }
    }



/*     for lesson 15
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }
*/
    private void reloadApps() {
        List<AppInfo> installedApps = appManager.getInstalledApps();
        appsAdapter.setApps(installedApps);
        appsAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.search_item);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i(TAG, "Text: " + newText);
                appsAdapter.setQuery(newText.toLowerCase().trim());
                appsAdapter.notifyDataSetChanged();
                return true;
            }
        });

      return true;
    }



// Toast for testing of working that item
    private void showToast()
    {
        Toast toast = Toast.makeText(this, "Hello", Toast.LENGTH_LONG);
        toast.show();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.install_item:
// check the working of item, should be deleted later
                showToast();
                startFilePickerActivity();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private final SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            reloadApps();
        }
    };

    private void startFilePickerActivity() {
        Intent intent = new Intent(this,
                FilePickerActivity.class);
        startActivityForResult(intent,
                REQUEST_CODE_PICK_APK);
    }


     private void startAppInstallation(String apkPath) {

      Intent installIntent = new Intent(Intent.ACTION_VIEW);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    new File(apkPath));
            installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(new File(apkPath));
        }
        installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
        installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

// Создаст новый процесс
        startActivity(installIntent);
    }

// Lesson 18
    private final ItemTouchHelper.Callback itemTouchHelperCallback = new ItemTouchHelper.Callback()
    {
        @Override
        public int getMovementFlags(RecyclerView
                                            recyclerView, RecyclerView.ViewHolder viewHolder) {
            return
                    makeMovementFlags(ItemTouchHelper.ACTION_STATE_IDLE,
                            ItemTouchHelper.END);
        }
        @Override
        public boolean onMove(RecyclerView recyclerView,
                              RecyclerView.ViewHolder viewHolder,
                              RecyclerView.ViewHolder target) {
            return false;
        }
        @Override
        public void onSwiped(RecyclerView.ViewHolder
                                     viewHolder, int direction) {
            AppInfo appInfo = (AppInfo) viewHolder.itemView.getTag(); //Lesson 18
            startAppUninstallation(appInfo);
//            RootHelper.uninstall("info"); // ??? why info, any string is working, but I put Info
        }
    };

// p 379
    private void uninstallWithRoot(AppInfo appInfo) {
        UninstallAsyncTask uninstallAsyncTask = new
                UninstallAsyncTask();
        uninstallAsyncTask.execute(appInfo.getPackageName());
    }

//    Lesson 18
    private void startAppUninstallation(AppInfo appInfo) {
        uninstallWithRoot(appInfo);
        /* Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
        intent.setData(Uri.parse("package:" +
                appInfo.getPackageName()));
        startActivity(intent);
        */
    }


}
