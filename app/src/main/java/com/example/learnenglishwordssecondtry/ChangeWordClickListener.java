package com.example.learnenglishwordssecondtry;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChangeWordClickListener implements View.OnClickListener {

    private EditText rusWordEditText;
    private EditText engWordEditText;
    private LinearLayout linearLayout;
    private ImageButton deleteWordButton;

    private Word word;

    private Context context;

    TextView rusWordTextView;
    TextView engWordTextView;

    @Override
    public void onClick(View v) {

        linearLayout = v.findViewById(R.id.linearLayout_changeWord);
        deleteWordButton = v.findViewById(R.id.button_deleteWord);

        if (linearLayout.getVisibility() == View.VISIBLE) {
            linearLayout.setVisibility(View.GONE);
            deleteWordButton.setVisibility(View.VISIBLE);
            return;
        } else if (linearLayout.getVisibility() != View.GONE) {
            return;
        }

        linearLayout.setVisibility(View.VISIBLE);
        deleteWordButton.setVisibility(View.GONE);

        rusWordEditText = v.findViewById(R.id.editText_changeRusWord);
        engWordEditText = v.findViewById(R.id.editText_changeEngWord);
        Button acceptButton = v.findViewById(R.id.button_changeWord);

        rusWordTextView = v.findViewById(R.id.textView_viewRusWord);
        engWordTextView = v.findViewById(R.id.textView_viewEngWord);

        rusWordEditText.setText(word.wordRus);
        engWordEditText.setText(word.wordEng);

        acceptButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                changeWord();

                rusWordTextView.setText(word.wordRus);
                engWordTextView.setText(word.wordEng + " - ");

                linearLayout.setVisibility(View.GONE);
                deleteWordButton.setVisibility(View.VISIBLE);
            }
        });
    }

    private void changeWord() {

        if (!rusWordEditText.getText().toString().equals(word.wordRus) ||
                !engWordEditText.getText().toString().equals(word.wordEng)) {

            word.wordEng = engWordEditText.getText().toString();
            word.wordRus = rusWordEditText.getText().toString();

            WordsInProcessSet.get(context).updateWord(word);
        }
    }

    public void setWord(Word word) {
        this.word = word;
    }
    public void setContext(Context context) {
        this.context = context;
    }
}
