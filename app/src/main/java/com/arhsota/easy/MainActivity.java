package com.arhsota.easy;

// for Nikita Lisenko
// 12 nov 2019

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
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
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
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
    private EditText editRegistration;
    private String strPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                            == PackageManager.PERMISSION_DENIED)
                    {

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
                Snackbar.make(view, "Отправляю документы...", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                String fileName = "/Easy/11.png";
                String externalStorageDirectory = Environment.getRootDirectory().toString();
//                String myDir = externalStorageDirectory + "/Pictures/Easy/"; // the // file will be in saved_images
                String myDir = Environment.DIRECTORY_PICTURES + fileName;
                 Uri uri = Uri.parse( myDir);
                editRegistration = findViewById(R.id.btnRegistration);
                strPhone = editRegistration.getText().toString();

                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String [] {"arhsota@gmail.com"});
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                shareIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Фото документов1");
//                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "test1");
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,strPhone);
//                shareIntent.putExtra(android.content.Intent.EXTRA_STREAM, uri);
                startActivity(shareIntent);

//                Toast.makeText(MainActivity.this, "test",
//                        Toast.LENGTH_LONG).show();
                 /*   //Код, выполняемый при выборе элемента Share
                Intent intent_mail = new Intent(Intent.ACTION_SENDTO);
                intent_mail.setData(Uri.parse("mailto:")); // only email apps should handle this
                //   intent_mail.setType("text/plain");
                intent_mail.putExtra("IMT", str_IMT);
                intent_mail.putExtra(Intent.EXTRA_TEXT,str_Date + " IMT  " + str_IMT + " Weight " + str_Weight);
                startActivity(intent_mail);
              */
//                 todo:
 /*               Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String [] {"arhsota@gmail.com"});
                sendIntent.putExtra(Intent.EXTRA_SUBJECT,  "Документы" );
                sendIntent.putExtra(Intent.EXTRA_TEXT,  " Ваш ИМТ: " );
                sendIntent.setType("message/rfc822");
                sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                sendIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                Uri attachment = FileProvider.getUriForFile(this, getPackageName(), "Pictures/Easy/*",file);
                sendIntent.putExtra(Intent.EXTRA_STREAM, attachment);
*/
//          page 143 always choose intent
//                String chooserTitle = "Sharing";
//                Intent chosenIntent = Intent.createChooser(intent_mes, chooserTitle);

                // Verify that the intent will resolve to an activity
//                todo:
 /*               if (sendIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(sendIntent);
                }

  */
//                startActivity(sendIntend);

            }
        });


    }

    public void onClickPhoto(View view) {
        Intent make_photo = new Intent(this, MakePhoto.class);
        startActivity(make_photo);

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


    public void onClickConnectmanager(View view) {

// todo: try to change ACTION_DIAL to ACTION_CALL, due to dial immidiatly
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "+79022865609"));
        startActivity(intent);
    }


}
