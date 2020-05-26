package com.coursework.foreignlanguagetranslation;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class Offline extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // this class wont work if there is no stored data please store data in translate class and this will work

    DbHelper  dbhelp=new DbHelper(this);
    ArrayList<String> inputWord;
    ArrayList<String> translatedWord;
    ArrayList<String> languages= new ArrayList<>();
    ListAdapter listAdapter ;
    Spinner savedLangs;
    Button open;
    String langChosen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);

        inputWord= new ArrayList<>();
        translatedWord= new ArrayList<>();
        open=findViewById(R.id.search);
        savedLangs=findViewById(R.id.saveLang);
        dbhelp=new DbHelper(this);

        //get offline data grouped by langauge into spinner
        Cursor data = dbhelp.viewOfflineLangs();
        if (data.getCount() == 0) {
            Toast.makeText(this, "There are no contents in this list!", Toast.LENGTH_SHORT).show();
        }
        else {
            while (data.moveToNext()) {
                languages.add(data.getString(1));
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, languages);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                savedLangs.setAdapter(adapter);
                savedLangs.setOnItemSelectedListener(this);

            }
        }

        // show the translation on same langauge in dialog box
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Cursor res= dbhelp.viewOffline(langChosen);
               if(res.getCount()==0){
                   //show message
                   showMessage("ERROR","Nothing found");
               }else{

               StringBuilder buffer= new StringBuilder();
               while(res.moveToNext()){
                   buffer.append(" input_word :").append(res.getString(2)).append("\n");
                   buffer.append(" translated_word :").append(res.getString(3)).append("\n");

               }
                //show all data
                showMessage("Data",buffer.toString());
            }}
        });

    }

    //dialog box builder
    public void showMessage(String title,String message){

        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        langChosen = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
