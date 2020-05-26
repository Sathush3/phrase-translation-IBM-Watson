package com.coursework.foreignlanguagetranslation;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;


public class LanguageSubscription extends AppCompatActivity {

    // please select this class twice to load all languages from database
    // initially only few data may load but second click will load everything
    // if this is null translate class wont work

    DbHelper dbHelper;
    ListView languageView;
    Button subscribed;
    ArrayList<String> subsLang= new ArrayList<>();
    ArrayList<String> temp= new ArrayList<>();
    String place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_subscription);
         dbHelper = new DbHelper(this);
         languageView=findViewById(R.id.viewLanguage);
        subscribed=findViewById(R.id.subscribed);

        ArrayList<String> languages= new ArrayList<>();

        //retreieving data from databse
        final Cursor data= dbHelper.viewLanguages();
        if(data.getCount() == 0){
            Toast.makeText(this, "There are no contents in this list!",Toast.LENGTH_LONG).show();
        }else{
            while(data.moveToNext()){
                languages.add(data.getString(0));

                ListAdapter listAdapter= new ArrayAdapter<>(this,android.R.layout.simple_list_item_single_choice,languages);
                languageView.setAdapter(listAdapter);
                //listAdapter.notifyDataSetChanged();




            }//onclick item gatherer
        }languageView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        languageView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item= (String)parent.getItemAtPosition(position);
                //Toast.makeText(LanguageSubscription.this,item,Toast.LENGTH_LONG).show();
                if(!(temp.contains(item))){
                    temp.add(item);
                }


            }
        });
        //subscribing the languages and passing data to translate activity
        subscribed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("temp"+temp);
                for(int i=0;i<temp.size();i++){
                   Cursor retrieved= dbHelper.getLanguage(temp.get(i));
                    Toast.makeText(LanguageSubscription.this,"Subscribed",Toast.LENGTH_LONG).show();
                    if (retrieved.moveToFirst()){
                        place = retrieved.getString( retrieved.getColumnIndex("TAG"));
                        if(!(subsLang.contains(place))){
                            subsLang.add(place);

                        }
                    }
                }
                demo.languageSubs=temp;
                demo.tags=subsLang;

            }
        });
    }
}
