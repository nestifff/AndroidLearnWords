package com.example.learnenglishwordssecondtry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import java.util.UUID;

public class ViewChangeWordActivity extends AppCompatActivity {

    EditText textEng;
    EditText textRus;

    private Word word;

    private UUID wordID;
    private String wordRus;
    private String wordEng;

    private WordsInProcessSet wordsInProcessSet;

    public static final String WORDID = "wordID";


    public static Intent newIntent (Context packageContext, UUID aWordID) {
        Intent intent = new Intent(packageContext, ViewChangeWordActivity.class);
        intent.putExtra(WORDID, aWordID);

        return intent;
    }

    @Override
    public void onPause() {

        super.onPause();
        WordsInProcessSet.get(getApplicationContext()).updateWord(word);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veiw_word);

        textEng = findViewById(R.id.change_one_word_engValue);
        textRus = findViewById(R.id.change_one_word_rusValue);

        wordID = (UUID) getIntent().getSerializableExtra(WORDID);

        wordsInProcessSet = WordsInProcessSet.get(getApplicationContext());
        word = wordsInProcessSet.getByID(wordID);

        if (word != null) {
            textEng.setText(word.wordEng);
            textRus.setText(word.wordRus);
        }

        textEng.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    wordEng = textEng.getText().toString();
                    word.wordEng = wordEng;

                    return true;
                }

                return false;
            }
        });

        textRus.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    wordRus = textRus.getText().toString();
                    word.wordRus = wordRus;

                    return true;
                }

                return false;
            }
        });

    }
}
