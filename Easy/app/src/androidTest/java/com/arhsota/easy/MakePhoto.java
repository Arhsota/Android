package com.arhsota.easy;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;

public class MakePhoto extends Activity {

    private Button btnDoc;
    File directory;
    final int TYPE_PHOTO = 1;
    final int TYPE_VIDEO = 2;

    final int REQUEST_CODE_PHOTO = 1;
    final int REQUEST_CODE_VIDEO = 2;

    final String TAG = "myLogs";

    ImageView ivPhoto;

    String strPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_make);

        btnDoc = findViewById(R.id.btnPhotoDoc1);

    }
    public void onClickPhotoDoc(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, generateFileUri(TYPE_PHOTO));
        startActivityForResult(intent, REQUEST_CODE_PHOTO);
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

    public void onClickPhotoSend(View view) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Snackbar.make(view, "Отправляю документы...", Snackbar.LENGTH_LONG).setAction("Action", null).show();
//        String fileName = (MakePhoto.myPath);
        String externalStorageDirectory = Environment.getRootDirectory().toString();
        //                String myDir = externalStorageDirectory + "/Pictures/Easy/"; // the // file will be in saved_images
        directory = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "Easy");
        String myPath = directory.toString();
        String myDir = myPath +"/21.jpg";
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
        shareIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"arhsota@gmail.com"});
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Документы для: " + strPhone);
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
//                        startActivity(Intent.createChooser(shareIntent,"Email:"));
        startActivity(shareIntent);
    }


    public void onClickPhotoSendWhatsApp(View view) {
    }
}
