package com.example.learnenglishwordssecondtry.viewChangeWords.wordsListActivity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.learnenglishwordssecondtry.R
import com.example.learnenglishwordssecondtry.model.Utils
import com.example.learnenglishwordssecondtry.model.Word
import com.example.learnenglishwordssecondtry.model.WordsInFromFile.pullWordsFromFile
import com.example.learnenglishwordssecondtry.model.WordsInFromFile.pushWordsInFile
import com.example.learnenglishwordssecondtry.model.WordsInProcessSet
import com.example.learnenglishwordssecondtry.viewChangeWords.AddWordDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.util.*

class WordsListActivity :
        AppCompatActivity(), AddWordDialogFragment.AddWordDialogListener {

    private val learnedShowIdentifier = "isItNecessaryToShowLearnedWords"
    private var isShowLearned = false

    private var wordsSetRecyclerView: RecyclerView? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<WordsListViewHolder>? = null
    private var currWordsSet: MutableList<Word>? = null

    private val dialogAddWordIdentifier = "DialogAddWord"

    private val scope = CoroutineScope(Dispatchers.Main)

    private val pushWordsLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {

                    pushWordsInFile(currWordsSet!!)
                    Toast.makeText(this, "You grant permission", Toast.LENGTH_LONG).show()

                } else {
                    Toast.makeText(this, "You didn't grant permission", Toast.LENGTH_LONG).show()
                }
            }

    private val pullWordsLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {

                    val words = pullWordsFromFile()
                    addWordsIntoCurrWordsSet(words)
                    Toast.makeText(this, "You grant permission", Toast.LENGTH_LONG).show()

                } else {
                    Toast.makeText(this, "You didn't grant permission", Toast.LENGTH_LONG).show()
                }
            }


    fun newIntent(packageContext: Context?, isItNecessaryToShowLearnedWords: Boolean): Intent {
        val intent = Intent(packageContext, WordsListActivity::class.java)
        intent.putExtra(learnedShowIdentifier, isItNecessaryToShowLearnedWords)
        return intent
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_set_of_words)

        isShowLearned = intent.getBooleanExtra(learnedShowIdentifier, false)

        if (!isShowLearned) {
            currWordsSet = ArrayList(WordsInProcessSet
                    .get(applicationContext).allWords)
            Utils.sortWordsByRus(currWordsSet)
        }

        wordsSetRecyclerView = findViewById(R.id.view_set_recyclerView)
        layoutManager = LinearLayoutManager(this)
        wordsSetRecyclerView?.layoutManager = layoutManager

        adapter = WordsListAdapter(currWordsSet, wordsSetRecyclerView, this)
        wordsSetRecyclerView?.adapter = adapter
    }

    // in list the changed word is shown
    override fun onResume() {
        super.onResume()

        currWordsSet = ArrayList(WordsInProcessSet
                .get(applicationContext).allWords)
        Utils.sortWordsByRus(currWordsSet)

        layoutManager = LinearLayoutManager(this)
        wordsSetRecyclerView?.layoutManager = layoutManager
        adapter = WordsListAdapter(currWordsSet, wordsSetRecyclerView, this)
        wordsSetRecyclerView?.adapter = adapter
        adapter!!.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.pm_view_words_in_process, menu)
        return true
    }

    override fun addWordDialogOnClick(word: Word) {

        (adapter as WordsListAdapter).wordsSet?.add(word)
        Utils.sortWordsByRus((adapter as WordsListAdapter).wordsSet)

        (adapter as WordsListAdapter).addWordInRV(word)
    }

    @SuppressLint("NonConstantResourceId")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.pm_add_new_word -> {

                val dialog = AddWordDialogFragment()
                dialog.show(supportFragmentManager, dialogAddWordIdentifier)
                return true
            }

            R.id.pm_add_words_from_file -> {

                pullWordsLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                return true
            }

            R.id.pm_download_words_in_file -> {

                pushWordsLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                return true
            }
        }

        return super.onOptionsItemSelected(item)

    }

    private fun addWordsIntoCurrWordsSet(newWords: List<Word>) {

        for (newWord: Word in newWords) {

            if (currWordsSet?.any {
                        it.wordRus == newWord.wordRus &&
                                it.wordEng == newWord.wordEng
                    } != true) {

                currWordsSet?.add(newWord)
            }
        }

        Utils.sortWordsByRus(currWordsSet)
        (adapter as WordsListAdapter).wordsSet = currWordsSet
        adapter?.notifyDataSetChanged()
    }
}
