package com.example.learnenglishwordssecondtry;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

public class AddWordDialogFragment extends DialogFragment {

    private EditText editTextRus;
    private EditText editTextEng;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_add_word, null);
        builder.setView(v);

        editTextRus = v.findViewById(R.id.word_rus);
        editTextEng = v.findViewById(R.id.word_eng);

        builder.setTitle("Add new word to your set");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {

                WordsInProcessSet.get(getContext()).addWord(new
                            Word(editTextEng.getText().toString(),
                                editTextRus.getText().toString()));

            }
        });

        // Create the AlertDialog object and return it
        return builder.create();

    }
}

