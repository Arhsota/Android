/*******************************************************************************
 *
 *  * Created by Andrey Sevastianov on 10 january 2019
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 25.01.2022, 19:53
 *
 ******************************************************************************/

package com.arhsota.tablo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;


public class Player extends AppCompatActivity {
    private EditText vPlayer;
    String[] playerNames =  {"player0","player1","player2","player3","player4","player5",
            "player6","player7","player8","player9"};
    String[] subStr = new String[9];
    String[] data = new String[9];
    ArrayList<String> users = new ArrayList();
    ArrayList<String> selectedUsers = new ArrayList();
    ArrayAdapter<String> adapter;
    ListView usersList;

    final String LOG_TAG = "myLogs";
    final String FILEDATA = "file";
    final String DATAEXIST = "dataexist";
    private String str_Main_Output;
    private String strUser;
    File directory;
    private boolean fillTextLength0 = true;  //for ckecking length
    private boolean fillTextLength8 = true;  //for ckecking length
    private boolean checkFieldUser = true;  //for ckecking length > 8
    private EditText userName;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

//        vPlayer = findViewById(R.id.player);
//        playerNames[0] = "Player1";
//        playerNames[1] = "Player2";
//        File dataExist = new File(DATAEXIST);
        File fileData = new File(FILEDATA);
        File fileDir = getBaseContext().getFilesDir();
//        File sdDir = Environment.getExternalStorageDirectory();
//        String fileName = fileData.getName(); // get file name
        File destination= new File( fileDir+"/"+FILEDATA ); // create destination file w
        if (destination.exists()) {
            readFile(fileData);
            strUser = strUser.replace("null","player");
            strUser = strUser.replace("[","");
            strUser = strUser.replace("]","");
            strUser = strUser.replace(" ","");
            String delimeter = ","; // Разделитель
            subStr = strUser.split(delimeter); // Разделения строки str с помощью метода split()

            int n = subStr.length;
            for (int i = 0; i < n; i++) {
                data[i] = subStr[i];
            }
            int k = subStr.length;
            for (int i = 0; i < k; i++){
                users.add (i,data[i]);
            }
       }
        else{
            // добавляем начальные элементы
            Collections.addAll(users,playerNames);
            int n = playerNames.length;
            for (int i = 0; i < n; i++)
            str_Main_Output = str_Main_Output + playerNames[i];
            writeFile(fileData,str_Main_Output);
        }
        // получаем элемент ListView
        usersList = findViewById(R.id.playersList);

        // устанавливаем режим выбора пунктов списка
        usersList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        // находим список
//        ListView pList = (ListView) findViewById(R.id.playersList);

        // получаем ресурс. If have
//        String[] players = getResources().getStringArray(R.array.players);
//        players[2] = "test";

        // создаем адаптер
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, users);

        // присваиваем адаптер списку
        usersList.setAdapter(adapter);


        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {

                // получаем нажатый элемент
                String user = adapter.getItem(position);
                if(usersList.isItemChecked(position))
                    selectedUsers.add(user);
                else
                    selectedUsers.remove(user);
            }

        });

    }


    public void add(View view){

        userName = findViewById(R.id.userName);
        String user = userName.getText().toString();
        userName.setEnabled(true);
        checkFieldUser = true;
        if(!user.isEmpty() && users.size() < 9){
            userName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                editTxtClientPhone.setText("");
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                public void afterTextChanged(Editable editable) {
                    fillTextLength0 = editable.toString().trim().length() > 0;
                    fillTextLength8 = editable.toString().trim().length() < 9;
                    if (fillTextLength0 && fillTextLength8){
                        userName.setEnabled(true);
                        checkFieldUser = true;
                    }
                    else{
                        userName.setEnabled(false);
                        checkFieldUser = false;
                        Toast.makeText(Player.this, "Only 8 chars in player's name",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
            if (checkFieldUser) {
                adapter.add(user);
                userName.setText("");
                adapter.notifyDataSetChanged();
                userName.setEnabled(true);
                checkFieldUser = true;
            }
            else{
                Toast.makeText(Player.this, "Only 8 name",
                        Toast.LENGTH_SHORT).show();
            }
            str_Main_Output = users.toString();
//            File dataExist = new File(DATAEXIST);
//            writeFile(dataExist,"1");
            File fileData = new File(FILEDATA);
            writeFile(fileData,str_Main_Output);

        }

    }
    public void edit(View view){

        EditText userName = findViewById(R.id.userName);
        String user = userName.getText().toString();
        if(!user.isEmpty() && users.size() < 9 ){
            adapter.add(user);
            userName.setText("");
            str_Main_Output = users.toString();
//            File dataExist = new File(DATAEXIST);
//            writeFile(dataExist,"1");
            File fileData = new File(FILEDATA);
            writeFile(fileData,str_Main_Output);
            adapter.notifyDataSetChanged();
        }
        else {
            Toast.makeText(Player.this, "Only 9 players allowed!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void remove(View view){
        // получаем и удаляем выделенные элементы

        for(int i=0; i< selectedUsers.size();i++){
            adapter.remove(selectedUsers.get(i));
        }
        // снимаем все ранее установленные отметки
        usersList.clearChoices();
        // очищаем массив выбраных объектов
        selectedUsers.clear();
        str_Main_Output = users.toString();
//        str_Main_Output= str_Main_Output.replace("[","");
//        str_Main_Output= str_Main_Output.replace("]","");
//        File dataExist = new File(DATAEXIST);
//        writeFile(dataExist,"1");
        File fileData = new File(FILEDATA);
        writeFile(fileData,str_Main_Output);
        adapter.notifyDataSetChanged();
    }
    void writeFile(File file, String strWrite) {
        str_Main_Output= str_Main_Output.replace("[","");
        str_Main_Output= str_Main_Output.replace("]","");
        str_Main_Output= str_Main_Output.replace(" ","");

        try {

            // отрываем поток для записи

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    openFileOutput(String.valueOf(file), MODE_PRIVATE)));
            // пишем данные
            bw.write(strWrite);


            // закрываем поток
            bw.close();
            Log.d(LOG_TAG, "Файл записан " + String.valueOf(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void readFile(File file) {
//        File  fileData = new File (FILEDATA);
        try {
            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    openFileInput(String.valueOf(file))));
            String str  = "";

            // TODO: 11.08.2020 clean history

            // читаем содержимое
//            while ((str = br.readLine()) != null) {
            while ((str =br.readLine()) != null) {
                Log.d(LOG_TAG, str);
                strUser = str;
            }
           strUser = strUser.replace("null","player");
           strUser = strUser.replace("[","");
           strUser = strUser.replace("]","");
           strUser = strUser.replace(" ","");
//           str_History[10] =  "\n";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}