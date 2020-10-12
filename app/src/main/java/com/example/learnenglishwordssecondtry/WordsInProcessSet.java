package com.example.learnenglishwordssecondtry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.example.learnenglishwordssecondtry.database.WordCursorWrapper;
import com.example.learnenglishwordssecondtry.database.WordsInProcessDB.WordsInProcessTable;
import com.example.learnenglishwordssecondtry.database.WordsInProcessDBHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

//singlet class, has only one object
public class WordsInProcessSet {

    private static WordsInProcessSet wordsInProcessSetObj;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static WordsInProcessSet get(Context context) {
        if (wordsInProcessSetObj == null) {
            wordsInProcessSetObj = new WordsInProcessSet(context); // context
        }
        return wordsInProcessSetObj;
    }

    public Word getByID(UUID wordID) {

        WordCursorWrapper cursor = queryWords(WordsInProcessTable.Cols.UUID + " = ?",
                new String[]{wordID.toString()});

        try {

            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getWord();

        } finally {
            cursor.close();
        }
    }

    private WordsInProcessSet(Context context) {

        mContext = context.getApplicationContext();
        mDatabase = new WordsInProcessDBHelper(mContext).getWritableDatabase();
    }

    public void addWord(Word word) {

        ContentValues values = getContentValues(word);
        mDatabase.insert(WordsInProcessTable.NAME, null, values);
    }

    public void deleteWord(Word word) {
        String uuidString = word.getID().toString();
        mDatabase.delete(WordsInProcessTable.NAME,
                WordsInProcessTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    public void updateWord(Word word) {

        String uuidString = word.getID().toString();
        ContentValues values = getContentValues(word);

        mDatabase.update(WordsInProcessTable.NAME, values,
                WordsInProcessTable.Cols.UUID + " = ?",
                new String[]{uuidString});

    }

    private WordCursorWrapper queryWords(String whereClause, String[] whereArgs) {

        long numInDB = DatabaseUtils.queryNumEntries(mDatabase, WordsInProcessTable.NAME);
        Cursor cursor = mDatabase.query(WordsInProcessTable.NAME, null,
                whereClause, whereArgs, null,
                null, null);

        return new WordCursorWrapper(cursor);
    }

    public static ContentValues getContentValues(Word word) {
        ContentValues values = new ContentValues();

        values.put(WordsInProcessTable.Cols.UUID, word.getID().toString());
        values.put(WordsInProcessTable.Cols.ENG, word.wordEng);
        values.put(WordsInProcessTable.Cols.RUS, word.wordRus);

        return values;
    }

    public List<Word> getWords(int count) throws Exception {

        List<Word> words = new ArrayList<>();
        WordCursorWrapper cursor = queryWords(null, null);

        int numInBD = cursor.getCount();
        if (numInBD == 0) {
            throw  new Exception("No items in wordsInProcessDB");
        }

        if (count > numInBD) {
            count = numInBD;
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

    public List<Word> getWords() {

        List<Word> words = new ArrayList<>();
        WordCursorWrapper cursor = queryWords(null, null);

        try {

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                words.add(cursor.getWord());
                cursor.moveToNext();
            }

        } finally {
            cursor.close();
        }

        return words;
    }

    public void deleteAllWords() {
        mDatabase.delete(WordsInProcessTable.NAME, null, null);
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

    /*private void addToList() {

        //for (int i = 0; i <= 2; ++i) {
        int i = 0;

            switch (i) {

                case (0):

                    sets.get(i).add(new Word("disparity", "неравенство"));

                    sets.get(i).add(new Word("sophisticated", "сложный"));
                    sets.get(i).add(new Word("implementation", "реализация"));
                    sets.get(i).add(new Word("eventually", "в конце концов"));
                    sets.get(i).add(new Word("repeatedly", "неоднократно"));
                    *//*
                    sets.get(i).add(new Word("nascent", "возникающий"));
                    sets.get(i).add(new Word("comprised", "состоит"));
                    sets.get(i).add(new Word("shrink", "сокращаться"));
                    sets.get(i).add(new Word("enormous", "огромный"));
                    sets.get(i).add(new Word("preserve", "сохранять"));
                    sets.get(i).add(new Word("traversing", "перемещение"));
                    sets.get(i).add(new Word("incrementally", "пошагово"));
                    sets.get(i).add(new Word("ambit", "в пределах"));
                    sets.get(i).add(new Word("dashed line", "пунктирная линия"));

                    sets.get(i).add(new Word("further", "кроме того"));
                    sets.get(i).add(new Word("inference", "вывод"));
                    sets.get(i).add(new Word("resurgence", "восстановление"));
                    sets.get(i).add(new Word("controversial", "спорный"));
                    sets.get(i).add(new Word("grappling", "борьба"));

                    sets.get(i).add(new Word("internal", "внутренний"));
                    sets.get(i).add(new Word("validation", "проверка"));
                    sets.get(i).add(new Word("symmetric", "симметричный"));
                    sets.get(i).add(new Word("inequality", "неравенство"));

                    sets.get(i).add(new Word("underlie", "лежать в основе"));
                    sets.get(i).add(new Word("comprehensible", "понятный"));
                    sets.get(i).add(new Word("ultimately", "в конечном счёте"));
                    sets.get(i).add(new Word("invariably", "неизменно"));
                    sets.get(i).add(new Word("denote", "обозначать"));
                    sets.get(i).add(new Word("judgment", "мнение"));
                    sets.get(i).add(new Word("emphasis", "акцент"));
                    sets.get(i).add(new Word("loan", "заимствование"));
                    sets.get(i).add(new Word("detecting", "обнаружение"));
                    sets.get(i).add(new Word("hazard", "опасность"));
                    sets.get(i).add(new Word("preventative", "профилактический"));
                    sets.get(i).add(new Word("equate", "приравнивать"));
                    sets.get(i).add(new Word("discard", "отбрасывать"));
                    sets.get(i).add(new Word("regarded", "рассматривать"));

                    sets.get(i).add(new Word("enumerate", "перечислять"));
                    sets.get(i).add(new Word("finite", "ограниченный"));
                    sets.get(i).add(new Word("restrict", "ограничивать"));
                    sets.get(i).add(new Word("patently", "очевидно"));
                    sets.get(i).add(new Word("feasible", "выполнимый"));
                    sets.get(i).add(new Word("acceptable", "приемлемый"));
                    sets.get(i).add(new Word("eliminate", "ликвидировать"));
                    sets.get(i).add(new Word("sufficiently", "достаточно"));
                    sets.get(i).add(new Word("to capture", "охватывать"));
                    sets.get(i).add(new Word("assign", "назначать"));
                    sets.get(i).add(new Word("specified", "указанный"));
                    sets.get(i).add(new Word("strike out", "вычеркивать"));
                    sets.get(i).add(new Word("impose", "налагать"));
                    sets.get(i).add(new Word("constraint", "ограничение"));
                    sets.get(i).add(new Word("capable", "способный"));
                    sets.get(i).add(new Word("statement", "утверждение"));
                    sets.get(i).add(new Word("precision", "точность"));
                    sets.get(i).add(new Word("in either case", "В любом случае"));
                    sets.get(i).add(new Word("error rate", "частота ошибок"));
*//*
                    break;

                case (1):

                    sets.get(i).add(new Word("pertain", "относиться"));
                    sets.get(i).add(new Word("origin", "происхождение"));
                    sets.get(i).add(new Word("mimic", "подражать"));
                    sets.get(i).add(new Word("diminish", "уменьшить"));
                    sets.get(i).add(new Word("perform", "выполнять"));
                    sets.get(i).add(new Word("approximation", "приближение"));
                    sets.get(i).add(new Word("extent", "степень"));
                    sets.get(i).add(new Word("resolution", "разрешение"));
                    sets.get(i).add(new Word("correspond", "соответствовать"));
                    sets.get(i).add(new Word("figure out", "выяснять"));
                    sets.get(i).add(new Word("intermediate", "промежуточный"));
                    sets.get(i).add(new Word("propagation", "распространение"));
                    sets.get(i).add(new Word("negation", "отрицание"));
                    sets.get(i).add(new Word("feasible", "выполнимый"));
                    sets.get(i).add(new Word("summation", "суммирование"));
                    sets.get(i).add(new Word("fraudulent", "обманный"));
                    sets.get(i).add(new Word("absence", "отсуствие"));
                    sets.get(i).add(new Word("convey", "передавать"));
                    sets.get(i).add(new Word("threshold", "порог"));
                    sets.get(i).add(new Word("satisfy", "удовлетворять"));
                    sets.get(i).add(new Word("defer", "отложить"));
                    sets.get(i).add(new Word("curve", "кривая"));
                    sets.get(i).add(new Word("concise", "краткий"));
                    sets.get(i).add(new Word("installment", "взнос"));
                    sets.get(i).add(new Word("opaque", "непрозрачный"));
                    sets.get(i).add(new Word("lump", "свалить"));
                    sets.get(i).add(new Word("preconception", "предвзятость"));
                    sets.get(i).add(new Word("variance", "дисперсия"));
                    sets.get(i).add(new Word("depth", "глубина"));
                    sets.get(i).add(new Word("penalise", "наказывать"));

                    sets.get(i).add(new Word("prone", "склонный"));
                    sets.get(i).add(new Word("objective", "задача"));
                    sets.get(i).add(new Word("trade off", "компромисс"));
                    sets.get(i).add(new Word("invertibility", "обратимость"));
                    sets.get(i).add(new Word("omitted", "пропущено"));
                    sets.get(i).add(new Word("tricky", "запутанный"));
                    sets.get(i).add(new Word("subtle", "тонкий"));
                    sets.get(i).add(new Word("take on faith", "принять на веру"));
                    sets.get(i).add(new Word("slope", "наклон"));
                    sets.get(i).add(new Word("estimate", "оценка"));
                    sets.get(i).add(new Word("accuracy", "точность"));
                    sets.get(i).add(new Word("reiterate", "подтверждать"));
                    sets.get(i).add(new Word("clarity", "ясность"));
                    sets.get(i).add(new Word("indeed", "на самом деле"));
                    sets.get(i).add(new Word("evaluation", "оценка"));
                    sets.get(i).add(new Word("time-consuming", "трудоемкий"));


                    break;

                case (2):

                    sets.get(i).add(new Word("dismissal", "увольнение"));
                    sets.get(i).add(new Word("frightened", "испуганный"));
                    sets.get(i).add(new Word("outrage", "безобразие"));
                    sets.get(i).add(new Word("unblemished", "безупречный"));
                    sets.get(i).add(new Word("courtroom", "зал суда"));
                    sets.get(i).add(new Word("credibility", "доверие"));
                    sets.get(i).add(new Word("oath", "клятва"));
                    sets.get(i).add(new Word("sustain", "поддерживать"));
                    sets.get(i).add(new Word("adjourn", "закрывать"));
                    sets.get(i).add(new Word("contempt", "презрение"));
                    sets.get(i).add(new Word("retain", "сохранить"));
                    sets.get(i).add(new Word("guilty", "виновный"));
                    sets.get(i).add(new Word("stick around", "слоняться поблизости"));
                    sets.get(i).add(new Word("jaw", "челюсть"));
                    sets.get(i).add(new Word("arrangement", "расположение"));
                    sets.get(i).add(new Word("treat", "обращаться"));
                    sets.get(i).add(new Word("conviction", "уверенность"));
                    sets.get(i).add(new Word("conscience", "совесть"));
                    sets.get(i).add(new Word("deliberate", "совещаться"));
                    sets.get(i).add(new Word("appreciate", "ценить"));
                    sets.get(i).add(new Word("sealed", "запечатанный"));
                    sets.get(i).add(new Word("handle it", "справиться"));
                    sets.get(i).add(new Word("preacher", "проповедник"));
                    sets.get(i).add(new Word("tough", "жесткий"));
                    sets.get(i).add(new Word("amidst", "среди"));
                    sets.get(i).add(new Word("parole", "обещание"));
                    sets.get(i).add(new Word("rub off", "стирать"));

                    break;

            }
        //}

    }
    */

