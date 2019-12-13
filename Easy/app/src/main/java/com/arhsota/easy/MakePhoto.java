package com.arhsota.easy;
//
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;

import static android.content.Intent.EXTRA_SUBJECT;

public class MakePhoto extends AppCompatActivity {

    private Button btnDoc;
    File directory;
    final int TYPE_PHOTO = 1;
    final int TYPE_VIDEO = 2;

    final int REQUEST_CODE_PHOTO = 1;
    final int REQUEST_CODE_VIDEO = 2;

    final String TAG = "myLogs";
    static String myPath;

    ImageView ivPhoto;

    private String strPhone;
    private String myPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_make);
        myPhone = getIntent().getStringExtra("TELE");
        createDirectory();
        btnDoc = findViewById(R.id.btnPhotoDoc1);


    }
    public void onClickPhotoDoc(View view) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d("permission", "permission denied to CAMERA - requesting it");
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, 1);

            }
        }

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, generateFileUri(TYPE_PHOTO));
                startActivityForResult(intent, REQUEST_CODE_PHOTO);



    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_CODE_PHOTO) {
            if (resultCode == RESULT_OK) {
                if (intent == null) {
                    Log.d(TAG, "Intent is null");
                } else {
                    Log.d(TAG, "Photo uri: " + intent.getData());
                    Bundle bndl = intent.getExtras();
                    if (bndl != null) {
                        Object obj = intent.getExtras().get("data");
                        if (obj instanceof Bitmap) {
                            Bitmap bitmap = (Bitmap) obj;
                            Log.d(TAG, "bitmap " + bitmap.getWidth() + " x "
                                    + bitmap.getHeight());
                            ivPhoto.setImageBitmap(bitmap);
                        }
                    }
                }
            } else if (resultCode == RESULT_CANCELED) {
                Log.d(TAG, "Canceled");
            }
        }

        if (requestCode == REQUEST_CODE_VIDEO) {
            if (resultCode == RESULT_OK) {
                if (intent == null) {
                    Log.d(TAG, "Intent is null");
                } else {
                    Log.d(TAG, "Video uri: " + intent.getData());
                }
            } else if (resultCode == RESULT_CANCELED) {
                Log.d(TAG, "Canceled");
            }
        }
    }
    private Uri generateFileUri(int type) {
        File file = null;
        switch (type) {
            case TYPE_PHOTO:
                file = new File(directory.getPath() + "/" + "photo_"
                        + System.currentTimeMillis() + ".jpg");
                break;
            case TYPE_VIDEO:
                file = new File(directory.getPath() + "/" + "video_"
                        + System.currentTimeMillis() + ".mp4");
                break;
        }
        Log.d(TAG, "fileName = " + file);
        return Uri.fromFile(file);
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

    public void onClickPhotoSend(View view) {
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
        String myDir = myPath +"/21.jpg";

//        String myPhone = getIntent().getStringExtra(MainActivity."TELE");

        strPhone = "Фото документов для: " + myPhone ;
//                        Uri uri = Uri.parse(myDir);
//                       Uri uri = Uri.fromFile(new File((myDir)) );
//                       Uri uri = FileProvider.getUriForFile(MainActivity.this,"read",file) ;

        String[] listOfPictures = directory.list();
        Uri uri=null;
        ArrayList<Uri> uris = new ArrayList<>();
        for (String file : listOfPictures)
        {
            uri = Uri.parse("file://" + directory.toString() + "/" + file);
            uris.add (uri);
        }

        Intent shareIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.setType("rar/image");
        shareIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{getString(R.string.email)});
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, strPhone);
        shareIntent.putExtra(Intent.EXTRA_TEXT, myPhone);
        //                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "test1");
//                        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, strPhone);
//                        todo: make multiattacment
//                        shareIntent.putExtra(android.content.Intent.EXTRA_STREAM, uris);
//                        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
//                        startActivity(Intent.createChooser(shareIntent,"Email:"));
        startActivity(shareIntent);
    }
}
