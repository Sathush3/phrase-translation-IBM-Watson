package com.coursework.foreignlanguagetranslation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // please explore each button one by one in the order and fill data
        //application will show null exception and may crash if there is null data
        //please add phrases and languages subscribed from list then translate activity work
        // when data is saved in offline in translate activity then offline activity will work



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            // animation to change background
        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.root_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();



        DbHelper dbHelper= new DbHelper(this);

        Button add_phrase_menu=(Button) findViewById(R.id.add_phrase);
        add_phrase_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent7= new Intent(view.getContext(),add_phrases.class);

                view.getContext().startActivity(intent7);


            }
        });
        Button view_phrase_menu=(Button) findViewById(R.id.view_phrase);
        view_phrase_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2= new Intent(view.getContext(),View_Phrases.class);

                view.getContext().startActivity(intent2);


            }
        });
        Button edit_phrase_menu=(Button) findViewById(R.id.edit_phrase);
        edit_phrase_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3= new Intent(view.getContext(),Edit_Phrases.class);

                view.getContext().startActivity(intent3);


            }
        });
        Button language_subs_menu=(Button) findViewById(R.id.language_subs);
        language_subs_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4= new Intent(view.getContext(),LanguageSubscription.class);

                view.getContext().startActivity(intent4);


            }
        });
        Button translation=(Button) findViewById(R.id.translate);
        translation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent5= new Intent(view.getContext(),Translate.class);

                view.getContext().startActivity(intent5);


            }
        });
        Button offline=(Button) findViewById(R.id.offlineDataBase);
        offline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent8= new Intent(view.getContext(),Offline.class);

                view.getContext().startActivity(intent8);


            }
        });




    }
}
