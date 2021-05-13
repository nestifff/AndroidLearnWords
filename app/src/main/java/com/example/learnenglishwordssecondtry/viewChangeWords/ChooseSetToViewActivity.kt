package com.example.learnenglishwordssecondtry.viewChangeWords

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.learnenglishwordssecondtry.R
import com.example.learnenglishwordssecondtry.viewChangeWords.wordsListActivity.WordsListActivity

class ChooseSetToViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_set_to_view)

        val btnViewWordsLearned: Button = findViewById(R.id.showLearnedWordsButton)
        val btnViewWordsInProcess: Button = findViewById(R.id.showWordsInProcessButton)

        btnViewWordsLearned.setOnClickListener {
            Toast.makeText(this@ChooseSetToViewActivity, "This option isn't available", Toast.LENGTH_SHORT).show()
        }

        btnViewWordsInProcess.setOnClickListener {
            val intent: Intent = WordsListActivity().newIntent(this@ChooseSetToViewActivity, false)
            startActivity(intent)
        }

    }

}