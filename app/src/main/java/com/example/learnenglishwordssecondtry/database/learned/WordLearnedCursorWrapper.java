package com.example.learnenglishwordssecondtry.database.learned;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.learnenglishwordssecondtry.database.inProcess.WordsInProcessDB;
import com.example.learnenglishwordssecondtry.model.WordInProcess;
import com.example.learnenglishwordssecondtry.model.WordLearned;

import java.util.UUID;

public class WordLearnedCursorWrapper extends CursorWrapper {

    public WordLearnedCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public WordLearned getWord () {

        String uuid = getString(getColumnIndex(WordsInProcessDB.WordsInProcessTable.Cols.UUID));
        String eng = getString(getColumnIndex(WordsInProcessDB.WordsInProcessTable.Cols.ENG));
        String rus = getString(getColumnIndex(WordsInProcessDB.WordsInProcessTable.Cols.RUS));

        WordLearned word = new WordLearned(UUID.fromString(uuid), eng, rus);
        return word;
    }
}