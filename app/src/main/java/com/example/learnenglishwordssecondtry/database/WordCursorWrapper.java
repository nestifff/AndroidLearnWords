package com.example.learnenglishwordssecondtry.database;

// Cursor класс не слишком удобен для работы с данными талицы
// поэтому впихнем всю жуть сюда и будем пользоваться как будто так и надо

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.learnenglishwordssecondtry.model.Word;
import com.example.learnenglishwordssecondtry.database.WordsInProcessDB.WordsInProcessTable;

import java.util.UUID;

public class WordCursorWrapper extends CursorWrapper {

    public WordCursorWrapper (Cursor cursor) {
        super(cursor);
    }

    public Word getWord () {

        String uuid = getString(getColumnIndex(WordsInProcessTable.Cols.UUID));
        String eng = getString(getColumnIndex(WordsInProcessTable.Cols.ENG));
        String rus = getString(getColumnIndex(WordsInProcessTable.Cols.RUS));

        return new Word(UUID.fromString(uuid), eng, rus);
    }
}
