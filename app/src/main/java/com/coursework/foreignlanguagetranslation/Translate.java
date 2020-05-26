package com.coursework.foreignlanguagetranslation;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.cloud.sdk.core.http.HttpMediaType;
import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.developer_cloud.android.library.audio.StreamPlayer;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.ibm.watson.language_translator.v3.model.TranslateOptions;
import com.ibm.watson.language_translator.v3.model.TranslationResult;
import com.ibm.watson.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.text_to_speech.v1.model.SynthesizeOptions;

import java.util.ArrayList;

public class Translate extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // this class will crash and exit if langauge subscribed is null please add languages to language subscribed and this class will work


    private LanguageTranslator translationService;

    DbHelper dbHelp;
    ArrayList<String> phrases = new ArrayList<>();
    TextView viewTranslation, languageChoose, wordChoose;
    Button offlineSave, Translate, Listen;
    Spinner wordChooser, LanguageChooser;
    String sv;
    String chosenLanguage;
    String chosenWord;
    String ChosenTag;
    String step2;

    private StreamPlayer player = new StreamPlayer();
    private TextToSpeech textService;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        viewTranslation = findViewById(R.id.txt_view_translation);
        languageChoose = findViewById(R.id.chooseLanguage);
        wordChoose = findViewById(R.id.chooseText);
        offlineSave = findViewById(R.id.offlineSave);
        Listen = findViewById(R.id.Listen);
        Translate = findViewById(R.id.translator);
        wordChooser = findViewById(R.id.phraseChooser);
        LanguageChooser = findViewById(R.id.LanguageChooser);
        translationService = initLanguageTranslatorService();
        textService = initTextToSpeechService();
        dbHelp = new DbHelper(this);

        //updating first spinner with words
        final Cursor data = dbHelp.viewData();
        if (data.getCount() == 0) {
            Toast.makeText(this, "There are no contents in this list!", Toast.LENGTH_SHORT).show();
        } else {
            while (data.moveToNext()) {
                phrases.add(data.getString(1));
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, phrases);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                wordChooser.setAdapter(adapter);
                wordChooser.setOnItemSelectedListener(this);

            }
        }
        //updating second spinner with languages subscribed
        ArrayList<String> subLanguage = new ArrayList<>(demo.languageSubs);
        if(subLanguage.isEmpty()){
            Toast.makeText(this, "There are no contents in this list!", Toast.LENGTH_SHORT).show();
        }
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subLanguage);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        LanguageChooser.setAdapter(adapter1);
        LanguageChooser.setOnItemSelectedListener(this);

        LanguageChooser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                chosenLanguage = parent.getItemAtPosition(position).toString();
                String chosen= (String) demo.tags.get(position);

                ChosenTag=chosen;
                System.out.println(ChosenTag);
                //int positions=position;


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TranslationTask().execute(chosenWord);


            }
        });
        Listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SynthesisTask().execute(sv);

            }
        });
        offlineSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Boolean result= dbHelp.insertOffline(chosenLanguage,chosenWord,sv);
             if(result){
                 Toast.makeText(Translate.this,"Offline data added",Toast.LENGTH_SHORT).show();
             }else{
                 Toast.makeText(Translate.this,"Data adding failed",Toast.LENGTH_SHORT).show();
             }
            }
        });


    }
    // api code from IBM DOCS
    private LanguageTranslator initLanguageTranslatorService() {
        Authenticator authenticator = new IamAuthenticator(getString(R.string.language_translator_apikey));
        LanguageTranslator service = new LanguageTranslator("2018-05-01", authenticator);
        service.setServiceUrl(getString(R.string.language_translator_url));


        return service;
    }
    // api code from IBM DOCS
    private TextToSpeech initTextToSpeechService() {
        Authenticator authenticator = new
                IamAuthenticator(getString(R.string.text_speech_apikey));
        TextToSpeech service = new TextToSpeech(authenticator);
        service.setServiceUrl(getString(R.string.text_speech_url));
        return service;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        chosenWord = parent.getItemAtPosition(position).toString();


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    //translation task
    private class TranslationTask extends AsyncTask<String, Void,
            String> {
        @Override
        protected String doInBackground(String... params) {
            TranslateOptions translateOptions = new TranslateOptions.Builder()
                    .addText(params[0])
                    .source("en")
                    .target(ChosenTag)
                    .build();
            TranslationResult result = translationService.translate(translateOptions).execute().getResult();
            String firstTranslation = result.getTranslations().get(0).getTranslation();


            sv = firstTranslation;


            return firstTranslation;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(Translate.this, sv, Toast.LENGTH_SHORT).show();
            viewTranslation.setText(s);
        }
    }
    //text to speech task
    private class SynthesisTask extends AsyncTask<String, Void,
            String> {
        @Override
        protected String doInBackground(String... params) {
            SynthesizeOptions synthesizeOptions = new
                    SynthesizeOptions.Builder()
                    .text(params[0])
                    .voice(SynthesizeOptions.Voice.EN_US_LISAVOICE)
                    .accept(HttpMediaType.AUDIO_WAV).build();
            player.playStream(textService.synthesize(synthesizeOptions).execute().getResult());

            return "Did synthesize";
        }


    }
}

