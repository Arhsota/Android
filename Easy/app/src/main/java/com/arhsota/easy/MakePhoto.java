package com.arhsota.easy;
/*******************************************************************************
 *
 *  * Created by Andrey Sevastianov on 13.05.20 0:05
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12.05.20 21:42
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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.EXTRA_SUBJECT;

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
   // TODO: 12.05.2020 may be there is no need of type txt
            case TYPE_TXT:
                file = new File(directory.getPath() + "/" + "txt"
                        + System.currentTimeMillis() + ".txt");
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



    public void onClickPhotoFabEmail(View view) {
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
                        Toast.LENGTH_SHORT).show();
                return;
            }

//            startActivity(shareIntent);
//              shareIntent.setSelector( emailSelectorIntent );
//            startActivity(Intent.createChooser(shareIntent,"choose"));

        } else {

            Toast.makeText(MakePhoto.this, "Вы ничего не сфотографировали, папка пустая",
                    Toast.LENGTH_SHORT).show();
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
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
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



             choiceWhatsApp();





    }
    private String contactIdByPhoneNumber(String phoneNumber) {
          String contactId = null;
        // Check the SDK version and whether the permission is already granted or not.
        //         If higher than 6.0 grants are already done
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
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
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
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

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
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

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
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
            shareIntentW.putExtra("jid",(getString(R.string.phone_number)) +"@s.whatsapp.net");
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


               startActivity(shareIntentW);
//              shareIntent.setSelector( emailSelectorIntent );
//            startActivity(Intent.createChooser(shareIntentW,"choose"));

        } else {

            Toast.makeText(MakePhoto.this, "Вы ничего не сфотографировали, папка пустая",
                    Toast.LENGTH_LONG).show();
            return;

        }


    }


}
