package com.example.learnenglishwordssecondtry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ChooseSetToViewActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_set_to_view);

        Button learnedWordsButton;
        Button wordsInProcessButton;

        learnedWordsButton = findViewById(R.id.showLearnedWordsButton);
        wordsInProcessButton = findViewById(R.id.showWordsInProcessButton);

        learnedWordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = ViewWordsActivity.newIntent(
                        ChooseSetToViewActivity.this, true);
                startActivity(intent);*/
                Toast.makeText(ChooseSetToViewActivity.this, "This option isn't available", Toast.LENGTH_SHORT).show();

            }
        });

        wordsInProcessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ViewWordsActivity.newIntent(
                        ChooseSetToViewActivity.this, false);
                startActivity(intent);
            }
        });


    }
}
