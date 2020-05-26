package com.coursework.foreignlanguagetranslation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class add_phrases extends AppCompatActivity {
    EditText add_texts;
    Button add;

    DbHelper dbHelp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_phrases);
        dbHelp=new DbHelper(this);
        add_texts=(EditText)findViewById(R.id.enter_text); //initializing elements
        add=(Button)findViewById(R.id.add_to_sql);


        //adding data to database
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String add_phrase=add_texts.getText().toString();
                if(add_texts.length()!=0){
                    addData(add_phrase);
                    add_texts.setText("");
                    Log.d("the data entered",add_texts.getText().toString());
                }else{
                    Log.d("nthing entered","empty");
                    Toast.makeText(add_phrases.this, "no data.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void addData(String entry){

        boolean isInserted= dbHelp.insertData(entry);
        if(isInserted==true){
            Toast.makeText(this, "Data Successfully Inserted!", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Something went wrong :(.", Toast.LENGTH_LONG).show();
        }




    }
}
