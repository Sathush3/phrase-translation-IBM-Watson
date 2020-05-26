package com.coursework.foreignlanguagetranslation;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class View_Phrases extends AppCompatActivity {
    DbHelper dbHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__phrases);
        dbHelp=new DbHelper(this);
        final ArrayList<String> words= new ArrayList<>();
        final ListView listView=findViewById(R.id.view_sql);




        final Cursor data= dbHelp.viewData();
        if(data.getCount() == 0){
            Toast.makeText(this, "There are no contents in this list!",Toast.LENGTH_LONG).show();
        }else{
            while(data.moveToNext()){
                words.add(data.getString(1));
                ListAdapter listAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,words);

                listView.setAdapter(listAdapter);

            }
        }




    }
}
