package com.example.learnenglishwordssecondtry;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

public class LearnWordsActivity extends AppCompatActivity {

    private static final String TAG = "LearnWordsActivity";
    private static final String WORD_INDEX = "index";

    public String answer;
    public List<Word> words;

    private static final String WORD_COUNT = "wordCount";

    private static final String WAY = "wayToLearnIsRusEng";

    private EditText answerEditText;
    private Button nextWordButton;
    private TextView answerStatusText;
    private TextView wordToRecallText;
    private TextView wordsRemainText;

    public boolean rusEngWay;
    private Word lastWord;
    private int curWordInd;

    private HashMap<Word, Integer> wordsQAttempts;
    private TextView resText;

    private int wordCount;


    public static Intent newIntent (Context packageContext, boolean aRusEngWay, int aWordCount) {

        Intent intent = new Intent(packageContext, LearnWordsActivity.class);
        intent.putExtra(WORD_COUNT, aWordCount);
        intent.putExtra(WAY, aRusEngWay);

        return intent;
    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {

        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
        outState.putInt(WORD_INDEX, curWordInd);

    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_set_of_words);

        try {
            TextView wayToLearnText;
            Button goHomeButton;

            wayToLearnText = findViewById(R.id.text_wayToLearn);
            goHomeButton = findViewById(R.id.button_goHome);

            answerStatusText = findViewById(R.id.text_answerStatus);
            nextWordButton = findViewById(R.id.button_askNextWord);
            answerEditText = findViewById(R.id.editText_answer);
            wordToRecallText = findViewById(R.id.text_wordToRecall);
            wordsRemainText = findViewById(R.id.text_wordsRemain);

            resText = findViewById(R.id.text_resultsTemporary);

            rusEngWay = getIntent().getBooleanExtra(WAY, false);

            if (rusEngWay) {
                wayToLearnText.setText(getString(R.string.text_rusEngWayToLearn));
                answerEditText.setHint(getString(R.string.textHint_enterAnswerRusEng));
            } else {
                wayToLearnText.setText(getString(R.string.text_engRusWayToLearn));
                answerEditText.setHint(getString(R.string.textHint_enterAnswerEngRus));

            }

            wordsQAttempts = new HashMap<>();

            wordCount = getIntent().getIntExtra(WORD_COUNT, 0);

            createSet();

            wordsRemainText.setText(words.size() + " words");

            if (savedInstanceState != null) {
                curWordInd = savedInstanceState.getInt(WORD_INDEX, 0);
            }

            askWord(savedInstanceState == null);

            nextWordButton.setEnabled(false);

            // обработка введенного ответа
            answerEditText.setOnKeyListener(new View.OnKeyListener() {

                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN &&
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {

                        answer = answerEditText.getText().toString();
                        // разрешить кнопку next
                        nextWordButton.setEnabled(true);

                        // запретить поле для ввода
                        // показать ответ
                        determineAndShowStatusOfAnswer();
                        answerEditText.setEnabled(false);
                        return true;
                    }

                    return false;
                }
            });

            goHomeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LearnWordsActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });

            // спрашиваем новое слово
            nextWordButton.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("NewApi")
                @Override
                public void onClick(View v) {

                    nextWordButton.setEnabled(false);
                    answerEditText.setEnabled(true);
                    answerStatusText.setText("");
                    answerEditText.setText("");

                    // открыть клавиатуру
                    answerEditText.requestFocus();
                    @SuppressLint({"NewApi", "LocalSuppress"}) InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(answerEditText, InputMethodManager.SHOW_IMPLICIT);

                    if (words.size() != 0) {
                        askWord(true);
                    } else {
                        goToResults();
                        Toast.makeText(LearnWordsActivity.this, "You are win!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (Exception e) {

            Toast.makeText(LearnWordsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LearnWordsActivity.this, MainActivity.class);
            startActivity(intent);
        }

    }

    // не лезь в то, что работает
    private void determineAndShowStatusOfAnswer() {

        int n = wordsQAttempts.get(lastWord) + 1;
        wordsQAttempts.remove(lastWord);
        wordsQAttempts.put(lastWord, n);

        if (lastWord.isCorrect(rusEngWay, answer)) {

            words.remove(lastWord);
            answerStatusText.setText(":) :) :) :) :) \n" + lastWord.getAnswer(rusEngWay));
            wordsRemainText.setText(words.size() + " words");

        } else {
            answerStatusText.setText(":( ... \n" + lastWord.getAnswer(rusEngWay));
        }
    }

    private void createSet() throws Exception {

        words = WordsInProcessSet.get(this).getWords(wordCount);

        for (Word w : words) {
            wordsQAttempts.put(w, 0);
        }

        wordCount = words.size();
    }

    // переписывает lastWord и выводит вопрос
    private void askWord(boolean needNewWord) {

        if (needNewWord) {

            Random random = new Random();

            if (words.size() != 1) {
                do {
                    curWordInd = random.nextInt(words.size());
                } while (words.get(curWordInd) == lastWord);
            } else {
                curWordInd = 0;
            }

        }

        lastWord = words.get(curWordInd);

        if (rusEngWay) {
            wordToRecallText.setText(lastWord.wordRus);
        } else {
            wordToRecallText.setText(lastWord.wordEng);
        }

    }


    public LinkedHashMap<Word, Integer> sortHashMapByValues(
            HashMap<Word, Integer> passedMap) {

        List<Word> mapKeys = new ArrayList<>(passedMap.keySet());
        List<Integer> mapValues = new ArrayList<>(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        LinkedHashMap<Word, Integer> sortedMap =
                new LinkedHashMap<>();

        Iterator<Integer> valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            Integer val = valueIt.next();
            Iterator<Word> keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                Word key = keyIt.next();
                Integer comp1 = passedMap.get(key);
                Integer comp2 = val;

                if (comp1.equals(comp2)) {
                    keyIt.remove();
                    sortedMap.put(key, val);
                    break;
                }
            }
        }
        return sortedMap;
    }

    void goToResults() {

        answerEditText.setEnabled(false);

        LinkedHashMap<Word, Integer> sortedResults = sortHashMapByValues(wordsQAttempts);
        List<Word> listWords = new ArrayList<>(sortedResults.keySet());
        List<Integer> listAttempts = new ArrayList<>(sortedResults.values());

        String text = new String();
        int onTheFirstTry = 0;

        for (int i = 0; i < listAttempts.size(); ++i) {
            if (listAttempts.get(i) != 1) {
                break;
            }
            ++onTheFirstTry;
        }

        text += "On the first try " + onTheFirstTry + " words / " + wordCount + "\n" + "\n";

        for (int i = 1; i <= 5; ++i) {
            if ((listAttempts.get(listWords.size() - i) != 1) && i == 1) {
                text += "The most difficult words: \n";
            }
            if (listAttempts.get(listWords.size() - i) == 1) {
                break;
            }
            text += listWords.get(listWords.size() - i).wordEng + " - " +
                    listWords.get(listWords.size() - i).wordRus + ": " +
                    listAttempts.get(listWords.size() - i) + "\n";
        }

        int numOfAttempts = 0;
        for (Integer n : listAttempts) {
            numOfAttempts += n;
        }

        text += "\n";

        double av = (double)numOfAttempts / (double)listWords.size();
        text += "Average number of attempts for one word is " + av + "\n";

        resText.setText(text);
    }


}
