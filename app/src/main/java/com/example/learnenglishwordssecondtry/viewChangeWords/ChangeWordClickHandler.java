package com.example.learnenglishwordssecondtry.viewChangeWords;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.learnenglishwordssecondtry.R;
import com.example.learnenglishwordssecondtry.model.SetWordsInProcess;
import com.example.learnenglishwordssecondtry.model.Word;
import com.example.learnenglishwordssecondtry.model.WordInProcess;

public class ChangeWordClickHandler implements View.OnClickListener {

    private EditText rusWordEditText;
    private EditText engWordEditText;
    private LinearLayout linearLayout;
    private ImageButton deleteWordButton;

    private Word word;

    private Context context;

    TextView rusWordTextView;
    TextView engWordTextView;

    Button acceptButton;

    @Override
    public void onClick(View v) {

        if (!(word instanceof WordInProcess)) {
            return;
        }
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

        acceptButton = v.findViewById(R.id.button_changeWord);
        acceptButton.setEnabled(false);

        rusWordEditText = v.findViewById(R.id.editText_changeRusWord);
        engWordEditText = v.findViewById(R.id.editText_changeEngWord);

        rusWordTextView = v.findViewById(R.id.textView_viewRusWord);
        engWordTextView = v.findViewById(R.id.textView_viewEngWord);

        rusWordEditText.setText(word.rus);
        engWordEditText.setText(word.eng);
        rusWordEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        engWordEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);

        engWordEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                acceptButton.setEnabled(true);
            }
        });
        rusWordEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                acceptButton.setEnabled(true);
            }
        });

        acceptButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                changeWord();

                rusWordTextView.setText(word.rus + " - ");
                engWordTextView.setText(word.eng);

                linearLayout.setVisibility(View.GONE);
                deleteWordButton.setVisibility(View.VISIBLE);
            }
        });
    }

    private void changeWord() {

        if (!rusWordEditText.getText().toString().equals(word.rus) ||
                !engWordEditText.getText().toString().equals(word.eng)) {

            word.eng = engWordEditText.getText().toString();
            word.rus = rusWordEditText.getText().toString();

            SetWordsInProcess.get(context).updateWord((WordInProcess) word);
        }
    }

    public void setWord(Word word) {
        this.word = word;
    }

    public void setContext(Context context) {
        this.context = context;
    }

}
