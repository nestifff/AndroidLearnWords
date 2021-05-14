package com.example.learnenglishwordssecondtry.menuMain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.learnenglishwordssecondtry.R;
import com.example.learnenglishwordssecondtry.learnWords.ChooseSetToLearnActivity;
import com.example.learnenglishwordssecondtry.viewChangeWords.ChooseSetToViewActivity;


public class MainActivity extends AppCompatActivity {

    private Button recallWordsButton;
    private Button learnWordsButton;
    private Button viewMyWordsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recallWordsButton = findViewById(R.id.recallWordsButton);
        learnWordsButton = findViewById(R.id.learnWordsButton);
        viewMyWordsButton = findViewById(R.id.viewMyWordsButton);

        recallWordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ChooseSetToLearnActivity.newIntent(MainActivity.this, false);
                startActivity(intent);
            }
        });

        learnWordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ChooseSetToLearnActivity.newIntent(MainActivity.this, true);
                startActivity(intent);
            }
        });

        viewMyWordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChooseSetToViewActivity.class);
                startActivity(intent);
            }
        });

    }
}

