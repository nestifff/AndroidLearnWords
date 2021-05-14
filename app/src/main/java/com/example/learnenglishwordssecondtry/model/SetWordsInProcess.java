package com.example.learnenglishwordssecondtry.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.example.learnenglishwordssecondtry.database.inProcess.WordInProcessCursorWrapper;
import com.example.learnenglishwordssecondtry.database.inProcess.WordsInProcessDB.WordsInProcessTable;
import com.example.learnenglishwordssecondtry.database.inProcess.WordsInProcessDBHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.example.learnenglishwordssecondtry.GlobalVariablesKt.NUM_ON_FIRST_TRY_TO_MOVE;

public class SetWordsInProcess {

    private static SetWordsInProcess setWordsInProcess;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private List<Word> wordsList;

    public static SetWordsInProcess get(Context context) {
        if (setWordsInProcess == null) {
            setWordsInProcess = new SetWordsInProcess(context); // context
        }
        return setWordsInProcess;
    }

    private SetWordsInProcess(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new WordsInProcessDBHelper(mContext).getWritableDatabase();
    }

    public boolean addWord(WordInProcess word) {

        if (wordsList.contains(word)) {
            return false;
        }
        ContentValues values = getContentValues(word);
        mDatabase.insert(WordsInProcessTable.NAME, null, values);
        wordsList.add(word);

        return true;
    }

    public void deleteWord(WordInProcess word) {

        String uuidString = word.id.toString();
        mDatabase.delete(WordsInProcessTable.NAME,
                WordsInProcessTable.Cols.UUID + " = ?",
                new String[]{uuidString});
        wordsList.remove(word);
    }

    public void updateWord(WordInProcess word) {

        String uuidString = word.id.toString();
        ContentValues values = getContentValues(word);

        mDatabase.update(WordsInProcessTable.NAME, values,
                WordsInProcessTable.Cols.UUID + " = ?",
                new String[]{uuidString});

        if (wordsList != null) {
            for (Word w : wordsList) {
                if (w.id.equals(word.id)) {
                    w.rus = word.rus;
                    w.eng = word.eng;
                    ((WordInProcess) w).numOnFirstTry = word.numOnFirstTry;
                }
            }
        }

    }

    // true if add to learned (numOnFirstTry >= someValue)
    public boolean addNumOnFirstTry(WordInProcess word) {

        word.numOnFirstTry += 1;

        if (word.numOnFirstTry < NUM_ON_FIRST_TRY_TO_MOVE) {
            updateWord(word);
            return false;
        }

        deleteWord(word);
        WordLearned newWord = new WordLearned(word);
        SetWordsLearned.get(mContext).addWord(newWord);
        return true;
    }

    private WordInProcessCursorWrapper queryWords(String whereClause, String[] whereArgs) {

        long numInDB = DatabaseUtils.queryNumEntries(mDatabase, WordsInProcessTable.NAME);
        Cursor cursor = mDatabase.query(WordsInProcessTable.NAME, null,
                whereClause, whereArgs, null,
                null, null);

        return new WordInProcessCursorWrapper(cursor);
    }

    public static ContentValues getContentValues(WordInProcess word) {
        ContentValues values = new ContentValues();

        values.put(WordsInProcessTable.Cols.UUID, word.id.toString());
        values.put(WordsInProcessTable.Cols.ENG, word.eng);
        values.put(WordsInProcessTable.Cols.RUS, word.rus);
        values.put(WordsInProcessTable.Cols.NUM_ON_FIRST_TRY, word.numOnFirstTry);

        return values;
    }

    public List<Word> getWords(int count) throws Exception {


        List<Word> words = new ArrayList<>();
        WordInProcessCursorWrapper cursor = queryWords(null, null);

        int numInBD = cursor.getCount();
        if (numInBD == 0) {
            throw new Exception("SetWordsInProcess: List<Word> getWords(int count): No words in process");
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
        WordInProcessCursorWrapper cursor = queryWords(null, null);

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

    /*public void addDefaultWordsInDB() {

        addWord(new Word("disparity", "неравенство"));
        addWord(new Word("sophisticated", "сложный"));
        addWord(new Word("implementation", "реализация"));
        addWord(new Word("eventually", "в конце концов"));
        addWord(new Word("repeatedly", "неоднократно"));

        addWord(new Word("nascent", "возникающий"));
        addWord(new Word("comprised", "состоит"));
        addWord(new Word("shrink", "сокращаться"));
        addWord(new Word("enormous", "огромный"));*/

        /*addWord(new Word("preserve", "сохранять"));
        addWord(new Word("traversing", "перемещение"));
        addWord(new Word("incrementally", "пошагово"));
        addWord(new Word("ambit", "в пределах"));
        addWord(new Word("dashed line", "пунктирная линия"));

        addWord(new Word("further", "кроме того"));
        addWord(new Word("inference", "вывод"));
        addWord(new Word("resurgence", "восстановление"));
        addWord(new Word("controversial", "спорный"));
        addWord(new Word("grappling", "борьба"));

        addWord(new Word("internal", "внутренний"));
        addWord(new Word("validation", "проверка"));
        addWord(new Word("symmetric", "симметричный"));
        addWord(new Word("inequality", "неравенство"));

        addWord(new Word("underlie", "лежать в основе"));
        addWord(new Word("comprehensible", "понятный"));
        addWord(new Word("ultimately", "в конечном счёте"));
        addWord(new Word("invariably", "неизменно"));
        addWord(new Word("denote", "обозначать"));
        addWord(new Word("judgment", "мнение"));
        addWord(new Word("emphasis", "акцент"));
        addWord(new Word("loan", "заимствование"));
        addWord(new Word("detecting", "обнаружение"));
        addWord(new Word("hazard", "опасность"));
        addWord(new Word("preventative", "профилактический"));
        addWord(new Word("equate", "приравнивать"));
        addWord(new Word("discard", "отбрасывать"));
        addWord(new Word("regarded", "рассматривать"));

        addWord(new Word("enumerate", "перечислять"));
        addWord(new Word("finite", "ограниченный"));
        addWord(new Word("restrict", "ограничивать"));
        addWord(new Word("patently", "очевидно"));
        addWord(new Word("feasible", "выполнимый"));
        addWord(new Word("acceptable", "приемлемый"));
        addWord(new Word("eliminate", "ликвидировать"));
        addWord(new Word("sufficiently", "достаточно"));
        addWord(new Word("to capture", "охватывать"));
        addWord(new Word("assign", "назначать"));
        addWord(new Word("specified", "указанный"));
        addWord(new Word("strike out", "вычеркивать"));
        addWord(new Word("impose", "налагать"));
        addWord(new Word("constraint", "ограничение"));
        addWord(new Word("capable", "способный"));
        addWord(new Word("statement", "утверждение"));
        addWord(new Word("precision", "точность"));
        addWord(new Word("in either case", "В любом случае"));
        addWord(new Word("error rate", "частота ошибок"));
*/
//}



