package com.arhsota.easy;
/*******************************************************************************
 *
 *  * Created by Andrey Sevastianov on 12 nov 2019
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 02.08.20 23:54
 *
 ******************************************************************************/

//
import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.EXTRA_SUBJECT;
import static android.content.Intent.createChooser;
import static android.os.Build.*;

public class MakePhoto extends AppCompatActivity {

    private Button btnDoc;
    File directory;
    final int TYPE_PHOTO = 1;
    final int TYPE_VIDEO = 2;
    final int TYPE_TXT = 3;

    final int REQUEST_CODE_PHOTO = 1;
    final int REQUEST_CODE_VIDEO = 2;

    final String TAG = "myLogs";
    static String myPath;

    ImageView ivPhoto;

    private String strPhone;
    private String myPhone;
//    private static final int PERMISSIONS_REQUEST_WRITE_CONTACTS = 1;
//    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 200;
    ReviewManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_make);
        myPhone = getIntent().getStringExtra("TELE");
        createDirectory();
        btnDoc = findViewById(R.id.btnPhotoDoc1);

        if (Build.VERSION.SDK_INT >= 23) {
            if (!isStoragePermissionGranted()) {
                ActivityCompat.requestPermissions(this, new String[]
                        {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }


    }

    @Override
    protected void onStop() {
        manager = ReviewManagerFactory.create(this);
        super.onStop();
        Review();
    }

    private void Review() {
        Task<ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // We can get the ReviewInfo object
                ReviewInfo reviewInfo = task.getResult();
                Task<Void> flow = manager.launchReviewFlow(this, reviewInfo);
                flow.addOnCompleteListener(task1-> {
                    // The flow has finished. The API does not indicate whether the user
                    // reviewed or not, or even whether the review dialog was shown. Thus, no
                    // matter the result, we continue our app flow.
//                    Toast.makeText(MakePhoto.this, "Review Completed, Thank You!", Toast.LENGTH_SHORT).show();
                });
            } else {
                // There was some problem, continue regardless of the result.
                Toast.makeText(MakePhoto.this, "In-App Request Failed", Toast.LENGTH_SHORT).show();
            }
        });

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

        // Nikita asks not to duplicate help
/*

        if (id == R.id.action_help) {
            Intent intent = new Intent(this, Help.class);
            startActivity(intent);
            return true;
        }
*/
        if (id == R.id.action_callevacuator) {

            Toast.makeText(MakePhoto.this, "Вызываю эвакуатор!!!",
                    Toast.LENGTH_LONG).show();

            callPhone(getString (R.string.phone_number_evacuator));

//           Toast.makeText(MainActivity.this, "В разработке",
//                    Toast.LENGTH_LONG).show();
            return true;
        }

        if (id == R.id.action_krown) {

            Toast.makeText(MakePhoto.this, "Звонок в Krown",
                    Toast.LENGTH_LONG).show();
            callPhone(getString (R.string.phone_number_krown));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void callPhone (final String strCallNumber){

//         Calling total procedure


// Checking permissions. If Not asks owner to make it by himself

        if (VERSION.SDK_INT >= VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d("permission", "permission denied to CALL PHONE - requesting it");
                String[] permissions = {Manifest.permission.CALL_PHONE};
                requestPermissions(permissions, 1);


            }
            else {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + strCallNumber));
                startActivity(intent);
            }

        }


    }

    public void onClickPhotoDoc(View view) {

        if (VERSION.SDK_INT >= VERSION_CODES.M) {
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




    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.
                    WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {
                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new
                        String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions,
                grantResults);
        if(grantResults.length > 0 && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ " was "+grantResults[0]);
    //write file to external storage


        }
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



    public void onClickPhotoEmail(View view) {
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
//                        String myDir = myPath +"/21.jpg";

        choiceEmail();
    }

    private void choiceEmail() {
        strPhone = "Фото документов для: " + myPhone;
        String[] listOfPictures = directory.list();
        if (listOfPictures != null) {

            Uri uri;
            ArrayList<Uri> uris = new ArrayList<>();
            for (String file : listOfPictures) {
                uri = Uri.parse("file://" + directory.toString() + "/" + file);
                uris.add(uri);
            }

//            selector
            Intent emailSelectorIntent = new Intent(Intent.ACTION_SENDTO);
//            emailSelectorIntent.setDataAndType(Uri.parse("mailto:"),"text/plain");
            emailSelectorIntent.setData(Uri.parse("mailto:"));

            final Intent shareIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);

            shareIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{getString(R.string.email)});

            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, strPhone);
            shareIntent.setSelector(emailSelectorIntent);


            shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);

            if (shareIntent.resolveActivity(getPackageManager()) != null) {
//                startActivity(shareIntent);
                startActivity(Intent.createChooser(shareIntent, "Выберите"));
            } else {
                Toast.makeText(MakePhoto.this, "Не установлен почтовый клиент! Установите, пожалуйста",
                        Toast.LENGTH_LONG).show();
                return;
            }

//            startActivity(shareIntent);
//              shareIntent.setSelector( emailSelectorIntent );
//            startActivity(Intent.createChooser(shareIntent,"choose"));

        } else {

            Toast.makeText(MakePhoto.this, "Вы ничего не сфотографировали, папка пустая",
                    Toast.LENGTH_LONG).show();
            return;

        }
    }

    /*   public void onClickPhotoSendWhatsApp(View view){
           Toast.makeText(MakePhoto.this, "В разработке!!",
                   Toast.LENGTH_SHORT).show();
           return;
       }
   */
//  RESERVED for later


    public void onClickPhotoSendWhatsApp(View view) {
        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            if (checkSelfPermission(Manifest.permission.WRITE_CONTACTS)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d("permission", "permission denied to Contacts - requesting it in onClickPhotoSendWhatsApp");
                String[] permissions = {Manifest.permission.WRITE_CONTACTS};
                requestPermissions(permissions, 1);

            }


        }

              Snackbar.make(view, "Отправляю документы...", Snackbar.LENGTH_LONG).setAction("Action", null).show();
              String fileName = (MakePhoto.myPath);
              String externalStorageDirectory = Environment.getRootDirectory().toString();
              //                String myDir = externalStorageDirectory + "/Pictures/Easy/"; // the // file will be in saved_images
              directory = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "Easy");
                    String myPath = directory.toString();

              String sWhatsAppNo = getString(R.string.phone_number);
//                    ID from contacts of Phone
                    String sContactId = contactIdByPhoneNumber(getString(R.string.phone_number));
//                    ID from WhatsApp contacts
                    String sContactIdWhatsApp = hasWhatsApp(sWhatsAppNo);

        /*
                  * Once We get the contact id, we check whether contact has a registered with WhatsApp or not.
                  * this hasWhatsApp(hasWhatsApp) method will return null,
                  * if contact doesn't associate with whatsApp services.
                  * */

        // this contact does not exist in any WhatsApp application
//                  String sWhatsAppNo = hasWhatsApp(sContactId);


                 if ( sContactId == null){

//                   Toast.makeText(this, "Contact not found in Contacts !!", Toast.LENGTH_SHORT).show();
                   Toast.makeText(this,
                           " номер Easy Осаго сохранен в тел. книге, просто добавьте его в отправители", Toast.LENGTH_LONG).show();
                   onAddContact();
                 }
             choiceWVT("com.whatsapp","WhatsApp");
    }

    public void onClickPhotoSendViber(View view) {
//        Toast.makeText(this, "В разработке!!", Toast.LENGTH_SHORT).show();
        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            if (checkSelfPermission(Manifest.permission.WRITE_CONTACTS)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d("permission", "permission denied to Contacts - requesting it in onClickPhotoSendWhatsApp");
                String[] permissions = {Manifest.permission.WRITE_CONTACTS};
                requestPermissions(permissions, 1);
            }
        }

        Snackbar.make(view, "Отправляю документы...", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        String fileName = (MakePhoto.myPath);
        String externalStorageDirectory = Environment.getRootDirectory().toString();
        //                String myDir = externalStorageDirectory + "/Pictures/Easy/"; // the // file will be in saved_images
        directory = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "Easy");
        String myPath = directory.toString();

        String sWhatsAppNo = getString(R.string.phone_number);
//                    ID from contacts of Phone
        String sContactId = contactIdByPhoneNumber(getString(R.string.phone_number));
//                    ID from WhatsApp contacts
        String sContactIdWhatsApp = hasWhatsApp(sWhatsAppNo);

        /*
         * Once We get the contact id, we check whether contact has a registered with WhatsApp or not.
         * this hasWhatsApp(hasWhatsApp) method will return null,
         * if contact doesn't associate with whatsApp services.
         * */

        // this contact does not exist in any WhatsApp application
//                  String sWhatsAppNo = hasWhatsApp(sContactId);


        if ( sContactId == null){

//                   Toast.makeText(this, "Contact not found in Contacts !!", Toast.LENGTH_SHORT).show();
            Toast.makeText(this,
                    " номер Easy Осаго сохранен в тел. книге, просто добавьте его в отправители", Toast.LENGTH_LONG).show();
            onAddContact();
        }
//        Viber choice
        choiceWVT("com.viber.voip","Viber");
    }
    
    private String contactIdByPhoneNumber(String phoneNumber) {
          String contactId = null;
        // Check the SDK version and whether the permission is already granted or not.
        //         If higher than 6.0 grants are already done
        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            if (checkSelfPermission(Manifest.permission.READ_CONTACTS)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d("permission", "permission denied to Contacts - requesting it");
                String[] permissions = {Manifest.permission.READ_CONTACTS};
                requestPermissions(permissions, 1);
            }
            else {

                if (phoneNumber != null && phoneNumber.length() > 0) {
                    ContentResolver contentResolver = getContentResolver();
                    Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
                    String[] projection = new String[]{ContactsContract.PhoneLookup._ID};

                    Cursor cursor = contentResolver.query(uri, projection, null, null, null);

                    if (cursor != null) {
                        while (cursor.moveToNext()) {
                            contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));
                        }
                        cursor.close();
                    }

                }
            }
        }
        return contactId;



    }


    public String hasWhatsApp(String contactID) {
        String rowContactId = null;
        boolean hasWhatsApp;
        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            if (checkSelfPermission(Manifest.permission.READ_CONTACTS)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d("permission", "permission denied to Contacts - requesting it");
                String[] permissions = {Manifest.permission.READ_CONTACTS};
                requestPermissions(permissions, 1);

            } else {
                String[] projection = new String[]{ContactsContract.RawContacts._ID};
                String selection = ContactsContract.RawContacts.CONTACT_ID + " = ? AND " + ContactsContract.RawContacts.ACCOUNT_TYPE + " = ?";
                String[] selectionArgs = new String[]{contactID, "com.whatsapp"};
                Cursor cursor = getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, projection, selection, selectionArgs, null);
                if (cursor != null) {
                    hasWhatsApp = cursor.moveToNext();
                    if (hasWhatsApp) {
                        rowContactId = cursor.getString(0);
                    }
                    cursor.close();
                }
            }
        }
        return rowContactId;
    }
    public void onAddContact() {



//        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//        StrictMode.setVmPolicy(builder.build());

        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            if (checkSelfPermission(Manifest.permission.WRITE_CONTACTS)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d("permission", "permission denied to Contacts - requesting it");
                String[] permissions = {Manifest.permission.WRITE_CONTACTS};
                requestPermissions(permissions, 1);

            }
        }
        
            ArrayList<ContentProviderOperation> op = new ArrayList<ContentProviderOperation>();

            /* Добавляем пустой контакт */
            op.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                    .build());
            /* Добавляем данные имени */
            op.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, (getString(R.string.insurname)))
                    .build());
            /* Добавляем данные телефона */
            op.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, (getString(R.string.phone_number)))
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build());

            try {
                getContentResolver().applyBatch(ContactsContract.AUTHORITY, op);

            } catch (Exception e) {
                Log.e("Exception: ", e.getMessage());
            }

    }

    private void choiceWhatsApp() {

        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d("permission", "permission denied to Contacts - requesting it");
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions, 1);

            }
        }

        String[] listOfPictures = directory.list();
        if (listOfPictures != null) {

            Uri uri;
            ArrayList<Uri> uris = new ArrayList<>();
            for (String file : listOfPictures) {
                uri = Uri.parse("file://" + directory.toString() + "/" + file);
                uris.add(uri);
            }

//            String url = "https://api.whatsapp.com/send?phone=79600058880";
//            String url = "whatsapp://send?phone=70000000000";
//              String url = "smsto:79600058880";


            Intent shareIntentW = new Intent(Intent.ACTION_SEND_MULTIPLE);
//            Intent shareIntentW =new Intent("android.intent.action.MAIN");
//              shareIntentW.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
//            shareIntentW.setAction(Intent.ACTION_SEND_MULTIPLE);
             shareIntentW.setPackage("com.whatsapp");

//              shareIntentW.setData(Uri.parse(url));
              shareIntentW.setType("text/plain");
//             shareIntentW.setDataAndType(Uri.parse(url),"text/plain");
//           shareIntentW.setType("message/rfc822");
//            shareIntent.setClassName("com.google.android.gm","com.google.android.gm.ComposeActivityGmail");

//
//            shareIntentW.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{getString(R.string.email)});
//            shareIntentW.setComponent(new  ComponentName("com.whatsapp","com.whatsapp.Conversation"));
//            shareIntentW.putExtra("jid",(getString(R.string.phone_number)) +"@s.whatsapp.net");
            shareIntentW.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntentW.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//            shareIntentW.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//           shareIntentW.putExtra(Intent.EXTRA_SUBJECT, "Содержимое каталога для: " + strPhone);
//            shareIntentW.setSelector( emailSelectorIntent );
//                            shareIntent.putExtra(Intent.A, getString (R.string.phone_number));
//                            shareIntent.putExtra("address", "12122222222");
//                              shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, addressViber);
//            shareIntentW.putExtra(android.content.Intent.EXTRA_TEXT, strPhone);

//                         shareIntent.putExtra(android.content.Intent.EXTRA_STREAM, uris);
//                        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            shareIntentW.putExtra(Intent.EXTRA_STREAM, uris);
            shareIntentW.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
//            shareIntent.putExtra(Intent.EXTRA_STREAM, uris);
//                            shareIntent.setComponent(new ComponentName("com.whatsapp","com.whatsapp.ContactPicker"));
//                            shareIntent.putExtra("jid", "79022865609");
//                        startActivity(Intent.createChooser(shareIntent,"Email:"));
//                           startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("viber://chat?number=7900000000")));
//                            shareIntent.setPackage("com.whatsapp");
//                            startActivity(shareIntent);
//            shareIntent.setSelector( emailSelectorIntent );
//            shareIntent.setSelector( emailSelectorIntent );


//                startActivity(shareIntent);
//                shareIntentW.setPackage("com.whatsapp");
//                startActivity(Intent.createChooser(shareIntentW,"Выберите"));


//               startActivity(shareIntentW);

//              shareIntent.setSelector( emailSelectorIntent );
                startActivity(Intent.createChooser(shareIntentW,"choose WhatsApp"));

        } else {

            Toast.makeText(MakePhoto.this, "Вы ничего не сфотографировали, папка пустая",
                    Toast.LENGTH_LONG).show();
            return;

        }


    }



//    WVT - means WhatsApp + Viber + Telegram. One void for all
    private void choiceWVT(String myPackage, String myPackageTitle) {

        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d("permission", "permission denied to Contacts - requesting it");
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions, 1);

            }
        }

        String[] listOfPictures = directory.list();
        if (listOfPictures != null) {

            Uri uri;
            ArrayList<Uri> uris = new ArrayList<>();
            for (String file : listOfPictures) {
                uri = Uri.parse("file://" + directory.toString() + "/" + file);
                uris.add(uri);
            }

            Intent shareIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
            shareIntent.setPackage(myPackage);
            shareIntent.setType("text/plain");

            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
            if (shareIntent.resolveActivity(getPackageManager()) != null) {
//                startActivity(shareIntent);
                startActivity(Intent.createChooser(shareIntent, "Выберите"));
            } else {
                Toast.makeText(MakePhoto.this, "Не установлен "+ myPackageTitle + " Установите, пожалуйста",
                        Toast.LENGTH_LONG).show();
                return;
            }
//            startActivity(Intent.createChooser(shareIntentT,"Telegram"));
        } else {

            Toast.makeText(MakePhoto.this, "Вы ничего не сфотографировали, папка пустая",
                    Toast.LENGTH_LONG).show();
            return;
        }
    }
    public void onClickPhotoSendTelegram(View view) {
//        Toast.makeText(this, "В разработке!!", Toast.LENGTH_SHORT).show();
        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            if (checkSelfPermission(Manifest.permission.WRITE_CONTACTS)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d("permission", "permission denied to Contacts - requesting it in onClickPhotoSendWhatsApp");
                String[] permissions = {Manifest.permission.WRITE_CONTACTS};
                requestPermissions(permissions, 1);
            }
        }

        Snackbar.make(view, "Отправляю документы...", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        String fileName = (MakePhoto.myPath);
        String externalStorageDirectory = Environment.getRootDirectory().toString();
        //                String myDir = externalStorageDirectory + "/Pictures/Easy/"; // the // file will be in saved_images
        directory = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "Easy");
        String myPath = directory.toString();

        String sWhatsAppNo = getString(R.string.phone_number);
//                    ID from contacts of Phone
        String sContactId = contactIdByPhoneNumber(getString(R.string.phone_number));
//                    ID from WhatsApp contacts
        String sContactIdWhatsApp = hasWhatsApp(sWhatsAppNo);

        /*
         * Once We get the contact id, we check whether contact has a registered with WhatsApp or not.
         * this hasWhatsApp(hasWhatsApp) method will return null,
         * if contact doesn't associate with whatsApp services.
         * */

        // this contact does not exist in any WhatsApp application
//                  String sWhatsAppNo = hasWhatsApp(sContactId);


        if ( sContactId == null){

//                   Toast.makeText(this, "Contact not found in Contacts !!", Toast.LENGTH_SHORT).show();
            Toast.makeText(this,
                    " номер Easy Осаго сохранен в тел. книге, просто добавьте его в отправители", Toast.LENGTH_LONG).show();
            onAddContact();
        }
//      Telegram choice
        choiceWVT("org.telegram.messenger","Telegram");


    }
}
