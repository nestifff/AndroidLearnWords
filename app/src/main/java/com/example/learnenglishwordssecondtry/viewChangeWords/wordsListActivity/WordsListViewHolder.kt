package com.example.learnenglishwordssecondtry.viewChangeWords.wordsListActivity

import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.learnenglishwordssecondtry.R
import com.example.learnenglishwordssecondtry.model.Word
import com.example.learnenglishwordssecondtry.viewChangeWords.ChangeWordClickHandler

class WordsListViewHolder(
        view: View,
        private val deleteButtonListener: DeleteButtonClickListener
): RecyclerView.ViewHolder(view) {

    var rusTextView: TextView = itemView.findViewById(R.id.textView_viewRusWord)
    var engTextView: TextView = itemView.findViewById(R.id.textView_viewEngWord)
    var deleteWordButton: ImageButton = itemView.findViewById(R.id.button_deleteWord)
    var blockChangeWord: LinearLayout = itemView.findViewById(R.id.linearLayout_changeWord)

    var word: Word? = null

    var changeWordHandler: ChangeWordClickHandler? = null

    init {

        deleteWordButton.setOnClickListener {
            word?.let{
                deleteButtonListener.deleteButtonOnClick(it)
            }
        }

        changeWordHandler = ChangeWordClickHandler()
        itemView.setOnClickListener(changeWordHandler)
    }
    
    interface DeleteButtonClickListener {
        fun deleteButtonOnClick(word: Word)
    }
}