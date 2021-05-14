package com.example.learnenglishwordssecondtry.database.inProcess;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.learnenglishwordssecondtry.model.WordInProcess;
import com.example.learnenglishwordssecondtry.database.inProcess.WordsInProcessDB.WordsInProcessTable;

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
                WordsInProcessTable.Cols.RUS + ", " +
                WordsInProcessTable.Cols.NUM_ON_FIRST_TRY + ")"
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

        public ContentValues getContentValues (WordInProcess word) {
            ContentValues values = new ContentValues();

            values.put(WordsInProcessTable.Cols.UUID, word.id.toString());
            values.put(WordsInProcessTable.Cols.ENG, word.eng);
            values.put(WordsInProcessTable.Cols.RUS, word.rus);
            values.put(WordsInProcessTable.Cols.NUM_ON_FIRST_TRY, word.numOnFirstTry);

            return values;
        }

        public void addWord(WordInProcess word) {

            ContentValues values = getContentValues(word);
            mDatabase.insert(WordsInProcessTable.NAME, null, values);
        }

        public void fillDBWithStartWords() {

            addWord(new WordInProcess("disparity", "неравенство"));
            addWord(new WordInProcess("sophisticated", "сложный"));
            addWord(new WordInProcess("implementation", "реализация"));
            /*addWord(new Word("eventually", "в конце концов"));
            addWord(new Word("repeatedly", "неоднократно"));

            addWord(new Word("nascent", "возникающий"));
            addWord(new Word("comprise", "содержать в себе"));
            addWord(new Word("shrink", "сокращаться"));
            addWord(new Word("enormous", "огромный"));

            addWord(new Word("preserve", "сохранять"));
            addWord(new Word("traversing", "перемещение"));
            addWord(new Word("incrementally", "пошагово"));
            addWord(new Word("ambit", "в пределах"));
            addWord(new Word("dashed line", "пунктирная линия"));*/
        }
    }
}
