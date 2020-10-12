package com.example.learnenglishwordssecondtry.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.learnenglishwordssecondtry.Word;
import com.example.learnenglishwordssecondtry.database.WordsInProcessDB.WordsInProcessTable;

public class WordsInProcessDBHelper extends SQLiteOpenHelper {

    public static final int VERSION = 1;

    public WordsInProcessDBHelper(Context context) {
        super (context, WordsInProcessTable.NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + WordsInProcessTable.NAME + "(" +
                        " _id integer primary key autoincrement, " +
                WordsInProcessTable.Cols.UUID + ", " +
                WordsInProcessTable.Cols.ENG + ", " +
                WordsInProcessTable.Cols.RUS + ")"
        );

        StartPlaceholder placeholder = new StartPlaceholder(db);
        placeholder.fillDBWithStartWords();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists " + WordsInProcessTable.NAME);
        onCreate(db);
    }


    private class StartPlaceholder {

        private SQLiteDatabase mDatabase;

        public StartPlaceholder(SQLiteDatabase db) {
            mDatabase = db;
        }

        public ContentValues getContentValues (Word word) {
            ContentValues values = new ContentValues();

            values.put(WordsInProcessTable.Cols.UUID, word.getID().toString());
            values.put(WordsInProcessTable.Cols.ENG, word.wordEng);
            values.put(WordsInProcessTable.Cols.RUS, word.wordRus);

            return values;
        }

        public void addWord(Word word) {

            ContentValues values = getContentValues(word);
            mDatabase.insert(WordsInProcessTable.NAME, null, values);
        }

        public void fillDBWithStartWords() {

            addWord(new Word("disparity", "неравенство"));
            addWord(new Word("sophisticated", "сложный"));
            addWord(new Word("implementation", "реализация"));
            addWord(new Word("eventually", "в конце концов"));
            addWord(new Word("repeatedly", "неоднократно"));

            addWord(new Word("nascent", "возникающий"));
            addWord(new Word("comprise", "содержать в себе"));
            addWord(new Word("shrink", "сокращаться"));
            addWord(new Word("enormous", "огромный"));

            addWord(new Word("preserve", "сохранять"));
            addWord(new Word("traversing", "перемещение"));
            addWord(new Word("incrementally", "пошагово"));
            addWord(new Word("ambit", "в пределах"));
            addWord(new Word("dashed line", "пунктирная линия"));
        }
    }
}
