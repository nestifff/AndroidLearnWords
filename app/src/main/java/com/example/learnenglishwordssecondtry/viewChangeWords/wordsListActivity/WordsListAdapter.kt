package com.example.learnenglishwordssecondtry.viewChangeWords.wordsListActivity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.learnenglishwordssecondtry.R
import com.example.learnenglishwordssecondtry.model.Word
import com.example.learnenglishwordssecondtry.model.WordsInProcessSet

class WordsListAdapter(
        var wordsSet: MutableList<Word>?,
        private var view: View?,
        val context: Context
) : RecyclerView.Adapter<WordsListViewHolder>(), WordsListViewHolder.DeleteButtonClickListener {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordsListViewHolder {

        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView: View = inflater.inflate(R.layout.view_set_item, parent, false)

        return WordsListViewHolder(contactView, this)
    }

    override fun onBindViewHolder(holder: WordsListViewHolder, position: Int) {

        holder.blockChangeWord.visibility = View.GONE
        holder.deleteWordButton.visibility = View.VISIBLE

        val word = wordsSet!![position]
        holder.word = word
        holder.changeWordHandler?.setWord(word)

        val rusTextView = holder.rusTextView
        rusTextView.text = word.wordRus + " - "
        val endTextView = holder.engTextView
        endTextView.text = word.wordEng
    }

    override fun getItemCount() = wordsSet?.size ?: 0


    fun addWordInRV(word: Word?) {

        val position = wordsSet!!.indexOf(word)
        setEmptyViewVisibility()
        notifyItemInserted(position)

    }

    private fun setEmptyViewVisibility() {
        if (itemCount == 0) {
            view?.visibility = View.GONE
        } else {
            view?.visibility = View.VISIBLE
        }
    }

    override fun deleteButtonOnClick(word: Word) {

        WordsInProcessSet.get(context).deleteWord(word)

        val position = wordsSet!!.indexOf(word)
        wordsSet!!.remove(word)

        setEmptyViewVisibility()
        notifyItemRemoved(position)
    }


}