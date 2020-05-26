package com.coursework.foreignlanguagetranslation;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Edit_Phrases extends AppCompatActivity {

    DbHelper dbHelp;
    EditText edit_words;
    Button edit,delete,save;
    String item;
    ListAdapter listAdapter1;
    ArrayAdapter listAdapter;
    final ArrayList<String> words= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__phrases);
        edit_words=(EditText)findViewById(R.id.edit_phrase_text);
        edit=(Button)findViewById(R.id.editor);
        delete=(Button)findViewById(R.id.deleter);
        save=(Button)findViewById(R.id.save);

        dbHelp=new DbHelper(this);

        final ListView listView1=findViewById(R.id.listView1);



        final Cursor data= dbHelp.viewData();
        if(data.getCount() == 0){
            Toast.makeText(this, "There are no contents in this list!",Toast.LENGTH_LONG).show();
        }else{
            while(data.moveToNext()){
                words.add(data.getString(1));
                //listAdapter1 = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,words);
                listAdapter= new ArrayAdapter<>(this,android.R.layout.simple_list_item_single_choice,words);
                listView1.setAdapter(listAdapter);
                //listAdapter.notifyDataSetChanged();




            }
        }
        //get the item clicked and its ID
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, int position, long id) {
                Log.d("item clicked",words.get(position));
                final String itemClicked=adapterView.getItemAtPosition(position).toString();
                //int idClicked = words.indexOf(itemClicked);
                Cursor dats=dbHelp.getItemID(itemClicked);
                int itemID=-1;
                while(dats.moveToNext()){
                    itemID=dats.getInt(0);

                }
                if(itemID >-1){
                    //Toast.makeText(Edit_Phrases.this,"Onclick items :"+itemID ,Toast.LENGTH_LONG).show();
                    //Toast.makeText(Edit_Phrases.this,itemClicked ,Toast.LENGTH_LONG).show();
                    item=String.valueOf(itemID);


                }else{
                    Toast.makeText(Edit_Phrases.this,"error no id found" ,Toast.LENGTH_LONG).show();
                }
                // edit function to set the text box from click retreived data
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edit_words.setText(itemClicked);


                    }
                });
                //save function to update data
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isUpdated=dbHelp.updateData(item,edit_words.getText().toString());

                        if(isUpdated){
                            Toast.makeText(Edit_Phrases.this,"Update success" ,Toast.LENGTH_SHORT).show();
                            listAdapter.notifyDataSetChanged();
                            finish();
                            startActivity(getIntent());



                        }else{
                            Toast.makeText(Edit_Phrases.this,"failure" ,Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                //delete function to delete data
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dbHelp.deleteItems(item,itemClicked);
                        listAdapter.notifyDataSetChanged();

                        Toast.makeText(Edit_Phrases.this,"Item deleted successfully",Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(getIntent());
                    }
                });

            }
        });


    }
}