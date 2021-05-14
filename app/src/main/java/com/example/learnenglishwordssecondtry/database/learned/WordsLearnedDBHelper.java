package com.example.learnenglishwordssecondtry.database.learned;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.learnenglishwordssecondtry.model.WordLearned;

public class WordsLearnedDBHelper extends SQLiteOpenHelper {

    public static final int VERSION = 1;

    public WordsLearnedDBHelper(Context context) {
        super(context, WordsLearnedDB.WordsLearnedTable.NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + WordsLearnedDB.WordsLearnedTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                WordsLearnedDB.WordsLearnedTable.Cols.UUID + ", " +
                WordsLearnedDB.WordsLearnedTable.Cols.ENG + ", " +
                WordsLearnedDB.WordsLearnedTable.Cols.RUS + ")"
        );

        WordsLearnedDBHelper.StartPlaceholder placeholder = new WordsLearnedDBHelper.StartPlaceholder(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists " + WordsLearnedDB.WordsLearnedTable.NAME);
        onCreate(db);
    }


    private class StartPlaceholder {

        private SQLiteDatabase mDatabase;

        public StartPlaceholder(SQLiteDatabase db) {
            mDatabase = db;
        }

        public ContentValues getContentValues(WordLearned word) {
            ContentValues values = new ContentValues();

            values.put(WordsLearnedDB.WordsLearnedTable.Cols.UUID, word.id.toString());
            values.put(WordsLearnedDB.WordsLearnedTable.Cols.ENG, word.eng);
            values.put(WordsLearnedDB.WordsLearnedTable.Cols.RUS, word.rus);

            return values;
        }

        public void addWord(WordLearned word) {

            ContentValues values = getContentValues(word);
            mDatabase.insert(WordsLearnedDB.WordsLearnedTable.NAME, null, values);
        }
    }

}
