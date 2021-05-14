package com.example.learnenglishwordssecondtry.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.example.learnenglishwordssecondtry.database.learned.WordLearnedCursorWrapper;
import com.example.learnenglishwordssecondtry.database.learned.WordsLearnedDB;
import com.example.learnenglishwordssecondtry.database.learned.WordsLearnedDBHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SetWordsLearned {
    
    private static SetWordsLearned setWordsLearned;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private List<Word> wordsList;

    public static SetWordsLearned get(Context context) {
        if (setWordsLearned == null) {
            setWordsLearned = new SetWordsLearned(context); // context
        }
        return setWordsLearned;
    }

    private SetWordsLearned(Context context) {

        mContext = context.getApplicationContext();
        mDatabase = new WordsLearnedDBHelper(mContext).getWritableDatabase();
    }

    public boolean addWord(WordLearned word) {

        if (wordsList.contains(word)) {
            return false;
        }
        ContentValues values = getContentValues(word);
        mDatabase.insert(WordsLearnedDB.WordsLearnedTable.NAME, null, values);
        wordsList.add(word);

        return true;
    }

    public void deleteWord(WordLearned word) {

        String uuidString = word.id.toString();
        mDatabase.delete(WordsLearnedDB.WordsLearnedTable.NAME,
                WordsLearnedDB.WordsLearnedTable.Cols.UUID + " = ?",
                new String[]{uuidString});
        wordsList.remove(word);
    }


    public void moveToInProcess(WordLearned word) {
        deleteWord(word);
        WordInProcess newWord = new WordInProcess(word);
        SetWordsInProcess.get(mContext).addWord(newWord);
    }

    private WordLearnedCursorWrapper queryWords(String whereClause, String[] whereArgs) {

        long numInDB = DatabaseUtils.queryNumEntries(mDatabase, WordsLearnedDB.WordsLearnedTable.NAME);
        Cursor cursor = mDatabase.query(WordsLearnedDB.WordsLearnedTable.NAME, null,
                whereClause, whereArgs, null,
                null, null);

        return new WordLearnedCursorWrapper(cursor);
    }

    public static ContentValues getContentValues(WordLearned word) {
        ContentValues values = new ContentValues();

        values.put(WordsLearnedDB.WordsLearnedTable.Cols.UUID, word.id.toString());
        values.put(WordsLearnedDB.WordsLearnedTable.Cols.ENG, word.eng);
        values.put(WordsLearnedDB.WordsLearnedTable.Cols.RUS, word.rus);

        return values;
    }

    public List<Word> getWords(int count) throws Exception {


        List<Word> words = new ArrayList<>();
        WordLearnedCursorWrapper cursor = queryWords(null, null);

        int numInBD = cursor.getCount();
        if (numInBD == 0) {
            throw new Exception("SetWordsLearned: List<Word> getWords(int count): No learned words");
        }

        if (count >= numInBD) {
            return getAllWords();
        }

        // indexes of randomly chosen words
        ArrayList<Integer> inds = new ArrayList<>();

        Random random = new Random();
        for (int i = 0; i < count; ++i) {

            int temp = -1;
            do {
                temp = random.nextInt((int) numInBD);
            } while (inds.contains(temp));
            inds.add(temp);
        }

        Collections.sort(inds);

        int numOfSelected = 0;
        int ind = 0;

        // choose only count random words
        try {

            if (cursor.moveToFirst()) {

                while (!cursor.isAfterLast() && numOfSelected < count) {
                    if (ind == inds.get(numOfSelected)) {
                        words.add(cursor.getWord());
                        ++numOfSelected;
                    }
                    cursor.moveToNext();
                    ++ind;
                }
            }

        } finally {
            cursor.close();
        }

        return words;
    }

    public List<Word> getAllWords() {

        if (wordsList != null) {
            return new ArrayList<>(wordsList);
        }

        List<Word> words = new ArrayList<>();
        WordLearnedCursorWrapper cursor = queryWords(null, null);

        try {

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                words.add(cursor.getWord());
                cursor.moveToNext();
            }

        } finally {
            cursor.close();
        }

        wordsList = words;
        return new ArrayList<>(words);
    }

    public int getWordsNum() {

        if (wordsList == null) {
            wordsList = getAllWords();
        }
        return wordsList.size();
    }
}
