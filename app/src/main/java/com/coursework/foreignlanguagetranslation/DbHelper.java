package com.coursework.foreignlanguagetranslation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;


public class DbHelper extends SQLiteOpenHelper {
    //Database name
        private static final String DATABASE_NAME = "abc.db";
    //Table content for first table
        private static final String TABLE_NAME = "Words";
        private static final String id = "ID";
        private static final String words = "PHRASES";
    //Table content for Second table
        private static final String TABLE2_NAME = "Languages";
        private static final String languages = "LANGUAGES";
        private static final String tag = "TAG";
        private static final String subscribed = "SUBSCRIBED";
    //Table content for third table
        private static final String TABLE3_NAME = "offline";

        private static final String transLanguage = "LANGUAGE";
        private static final String input_word = "INPUT_WORD";
        private static final String translated_word = "TRANSLATED_WORD";

        DbHelper(Context context) {
                super(context, DATABASE_NAME, null, 1);
        }

        //Create the Relevant Tables in Databse
        @Override
        public void onCreate(SQLiteDatabase db) {
                db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        " PHRASES TEXT)");
                db.execSQL("CREATE TABLE " + TABLE2_NAME + " (LANGUAGES TEXT PRIMARY KEY ,"+" TAG TEXT,"+"SUBSCRIBED TEXT)");
                db.execSQL("CREATE TABLE " + TABLE3_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        " LANGUAGE TEXT, " + " INPUT_WORD TEXT," + " TRANSLATED_WORD TEXT)");
                new languageAdd().execute();


        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE2_NAME);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE3_NAME);
                onCreate(db);
        }

        // Insert values to Phrases table
        public Boolean insertData(String words) {
                SQLiteDatabase db = this.getWritableDatabase();
                ContentValues values = new ContentValues();

                values.put(DbHelper.words, words);
                long result = db.insert(TABLE_NAME, null, values);

                if (result == -1) {
                        return false;
                } else {
                        db.close();
                        return true;
                }
        }

    // Insert values to Offline table
        public Boolean insertOffline(String language, String inputWord, String transWord) {
                SQLiteDatabase db = this.getWritableDatabase();
                ContentValues values = new ContentValues();

                values.put(DbHelper.transLanguage, language);
                values.put(DbHelper.input_word, inputWord);
                values.put(DbHelper.translated_word, transWord);
                long result = db.insert(TABLE3_NAME, null, values);

                if (result == -1) {
                        return false;
                } else {
                        db.close();
                        return true;
                }
        }
    // View the contents of Phrases table
        public Cursor viewData() {
                SQLiteDatabase db = this.getWritableDatabase();

                Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY PHRASES COLLATE NOCASE ", null);

                return cursor;
        }
    // View the contents of Language table
        public Cursor viewLanguages() {
                SQLiteDatabase db = this.getWritableDatabase();

                Cursor cursor = db.rawQuery("SELECT " + languages + " FROM " + TABLE2_NAME, null);

                return cursor;
        }
         // View the Subscribed Languages
        public Cursor viewSubscribed() {
                SQLiteDatabase db = this.getWritableDatabase();

                Cursor cursor = db.rawQuery("SELECT " + tag + " FROM " + TABLE2_NAME,null);

                return cursor;
        }
    // View the contents of Offline table
        public Cursor viewOffline(String langs) {
                SQLiteDatabase db = this.getWritableDatabase();

                Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE3_NAME +" WHERE "+transLanguage+"='"+langs+"'", null);

                return cursor;
        }
        //get langauges from offline table
        public Cursor viewOfflineLangs() {
            SQLiteDatabase db = this.getWritableDatabase();

            Cursor cursor1 = db.rawQuery("SELECT * FROM " + TABLE3_NAME, null);

            return cursor1;
        }
    // Get the ID of selected word from phrases table
        public Cursor getItemID(String data) {
                SQLiteDatabase db = this.getWritableDatabase();
                String query = "SELECT " + id + " FROM " + TABLE_NAME + " WHERE " + words + " ='" + data + "'";
                Cursor getID = db.rawQuery(query, null);

                return getID;


        }
        //Update phrases table with new data
        public Boolean updateData(String Id, String word) {

                SQLiteDatabase db = this.getWritableDatabase();
                ContentValues update_values = new ContentValues();
                update_values.put(DbHelper.id, Id);
                update_values.put(words, word);
                db.update(TABLE_NAME, update_values, "ID =?", new String[]{Id});

                return true;
        }

        //Get specific tags of the selected languages
        public Cursor getLanguage(String data) {
                SQLiteDatabase db = this.getWritableDatabase();
                String query = "SELECT " + tag + " FROM " + TABLE2_NAME + " WHERE " + languages + " ='" + data + "'";
                Cursor getLanguage = db.rawQuery(query, null);

                return getLanguage;}
        //update language table with subscribed status
        public Boolean updateLanguages(String language, String word) {

                SQLiteDatabase db = this.getWritableDatabase();
                ContentValues update_values = new ContentValues();
                update_values.put(DbHelper.languages, language);
                update_values.put(subscribed, word);
                db.update(TABLE2_NAME, update_values, "LANGUAGES =?", new String[]{id});

                return true;
        }

        // delete phrases fromphrase table
        public void deleteItems(String id, String word) {
                SQLiteDatabase db = this.getWritableDatabase();
                String query = "DELETE FROM " + TABLE_NAME + " WHERE " + DbHelper.id + " = '" + id + "' AND " + words + " ='" + word + "' ";
                db.execSQL(query);
                db.close();

        }
        //inser the languages and tags
        // some languages are depreciated and creating errors when translating so tested and working data had been added to arrya and used instead of passing all languages
        // all languages in web also added for testing purpose
        public Boolean insertLanguages() {
                SQLiteDatabase db = this.getWritableDatabase();
                //all langauges
                //String[] langs = {"Afrikaans", "Arabic", "Azerbaijani", "Bashkir","Belarusian", "Bulgarian","Bengali","Catalan","Czech", "Chuvash","Danish","German","Greek","English","Esperanto","Spanish", "Estonian", "Basque","Persian", "Finnish" ,"French","Irish", "Gujarati","Hebrew","Hindi", "Croatian","Haitian","Hungarian", "Armenian","Icelandic","Italian", "Japanese","Georgian", "Kazakh","Central Khmer","Korean","Kurdish","Kirghiz", "Lithuanian", "Latvian","Malayalam","Mongolian", "Malay","Maltese", "Norwegian Bokmal", "Dutch","Norwegian Nynorsk","Panjabi", "Polish","Pushto","Portuguese","Romanian","Russian", "Slovakian","Slovenian","Somali","Albanian","Serbian","Swedish","Tamil","Telugu", "Thai","Turkish","Ukrainian", "Urdu","Vietnamese","Simplified Chinese","Traditional Chinese"};
                //Corresponding tags
                //String[] tags = {"af", "ar", "az", "ba","be", "bg","bn","ca","cs", "cv","da","de","el","en","eo","es", "et", "eu","fa", "fi" ,"fr","ga", "gu","he","hi", "hr","ht","hu", "hy","is","it", "ja","ka", "kk","km","ko","ku","ky", "lt", "lv","ml","mn", "ms","mt", "nb", "nl","nn","pa", "pl","ps","pt","ro","ru", "sk","sl","so","sq","sr","sv","ta","te", "th","tr","uk", "ur","vi","zh","zh-TW"};
                // working languages
                String[] workingLangs={"Arabic","Czech","Danish","German","Spannish","Finnish","French","Hindi","Hungarian","Italian","Japanese","Korean","Dutch","Norwegian_Bokmal","Polish","Portuguese","Russian","Sweedish","Turkish","Chinese"};
                //Corresponding tags
                String[] workingtags={"ar","cs","da","de","es","fi","fr","hi","hu","it","ja","ko","nl","nb","p1","pt","ru","sv","tr","zh"};

                //Adding to Database
                for(int i=0;i<workingLangs.length;i++){
                    ContentValues values = new ContentValues();
                    values.put(DbHelper.languages, workingLangs[i]);
                    values.put(DbHelper.tag, workingtags[i]);
                    db.insert(TABLE2_NAME, null, values);
                }
                return true;


        }

        //Async task to add languages as its a big process
        public class languageAdd extends AsyncTask<String, Void,
                String> {
                @Override
                protected String doInBackground(String... params) {
                        String task;
                        Boolean tasks=insertLanguages();
                        if (tasks){
                                task="Success";
                                System.out.println(task);
                        }else{
                                task="Failure";
                        }


                return task;
                }
                @Override
                protected void onPostExecute(String task) {
                        super.onPostExecute(task);
                        Log.d("Adding language table",task);
                        System.out.println(task);
                }
        }




}




