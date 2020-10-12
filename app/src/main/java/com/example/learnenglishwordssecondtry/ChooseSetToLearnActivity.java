package com.example.learnenglishwordssecondtry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


// TODO: количество слов для изучения сделать удобнее
// TODO: например, пару кнопок для самого частого количества и кнопку чтобы набрать количество вручную

public class ChooseSetToLearnActivity extends AppCompatActivity {

    private TextView wayToLearnText;
    boolean rusEngWay = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_set_to_learn);

        Button learnWordsInProcess_5_Button;
        Button learnWordsInProcess_10_Button;
        Button learnWordsInProcess_20_Button;
        Button learnWordsInProcess_30_Button;
        Button learnWordsInProcess_50_Button;

        Button changeWayOfLearnButton;

        learnWordsInProcess_5_Button = findViewById(R.id.learnWordsInProcess_5_Button);
        learnWordsInProcess_10_Button = findViewById(R.id.learnWordsInProcess_10_Button);
        learnWordsInProcess_20_Button = findViewById(R.id.learnWordsInProcess_20_Button);
        learnWordsInProcess_30_Button = findViewById(R.id.learnWordsInProcess_30_Button);
        learnWordsInProcess_50_Button = findViewById(R.id.learnWordsInProcess_50_Button);

        changeWayOfLearnButton = findViewById(R.id.button_changeWayToLearn);
        wayToLearnText =  findViewById(R.id.text_wayToLearn);

        changeWayOfLearnButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String text;
                if(rusEngWay) {
                    rusEngWay = false;
                    text  = getString(R.string.text_engRusWayToLearn);
                } else {
                    rusEngWay = true;
                    text  = getString(R.string.text_rusEngWayToLearn);
                }
                wayToLearnText.setText(text);
            }
        });

        learnWordsInProcess_5_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LearnWordsActivity.newIntent(
                        ChooseSetToLearnActivity.this, rusEngWay, 5);
                startActivity(intent);
            }
        });

        learnWordsInProcess_10_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LearnWordsActivity.newIntent(
                        ChooseSetToLearnActivity.this, rusEngWay, 10);
                startActivity(intent);
            }
        });

        learnWordsInProcess_20_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LearnWordsActivity.newIntent(
                        ChooseSetToLearnActivity.this, rusEngWay, 20);
                startActivity(intent);
            }
        });

        learnWordsInProcess_30_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LearnWordsActivity.newIntent(
                        ChooseSetToLearnActivity.this, rusEngWay, 30);
                startActivity(intent);
            }
        });

        learnWordsInProcess_50_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LearnWordsActivity.newIntent(
                        ChooseSetToLearnActivity.this, rusEngWay, 50);
                startActivity(intent);
            }
        });
    }
}
