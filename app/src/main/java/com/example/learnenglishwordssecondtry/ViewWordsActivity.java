package com.example.learnenglishwordssecondtry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ViewWordsActivity extends AppCompatActivity {

    private static final String IS_SHOW_LEARNED = "isItNecessaryToShowLearnedWords";

    private boolean isShowLearned;

    private RecyclerView wordsSetRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter mAdapter;
    private List<Word> currWordsSet;
    private WordsInProcessSet wordsInProcessSet;

    private static final String DIALOG_ADD_WORD = "DialogAddWord";

    public static Intent newIntent (Context packageContext, boolean isItNecessaryToShowLearnedWords) {
        Intent intent = new Intent(packageContext, ViewWordsActivity.class);
        intent.putExtra(IS_SHOW_LEARNED, isItNecessaryToShowLearnedWords);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_set_of_words);

        wordsInProcessSet = WordsInProcessSet.get(this);

        isShowLearned = getIntent().getBooleanExtra(IS_SHOW_LEARNED, false);

        if (!isShowLearned) {
            currWordsSet = WordsInProcessSet.get(getApplicationContext()).getWords();
        }

        wordsSetRecyclerView = findViewById(R.id.view_set_recyclerView);

        layoutManager = new LinearLayoutManager(this);
        wordsSetRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new MyAdapter(currWordsSet);
        wordsSetRecyclerView.setAdapter(mAdapter);

    }


    // in list the changed word is shown
    @Override
    public void onResume() {

        super.onResume();

        currWordsSet = WordsInProcessSet.get(getApplicationContext()).getWords();
        layoutManager = new LinearLayoutManager(this);
        wordsSetRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new MyAdapter(currWordsSet);
        wordsSetRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_word_set_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.new_word) {

            FragmentManager manager = getSupportFragmentManager();

            AddWordDialogFragment dial = new AddWordDialogFragment();
            dial.show(manager, DIALOG_ADD_WORD);

            return true;

        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        private List<Word> wordsSet;

        private MyAdapter(List<Word> aWordsSet) {

            wordsSet = aWordsSet;
        }


        public class MyViewHolder extends RecyclerView.ViewHolder
                            implements View.OnClickListener{

            private TextView rusTextView;
            private TextView engTextView;
            private ImageButton deleteWordButton;

            private Word word;

            private MyViewHolder(View itemView) {
                super(itemView);

                itemView.setOnClickListener(this);

                rusTextView = itemView.findViewById(R.id.word_view_rus);
                engTextView = itemView.findViewById(R.id.word_view_eng);
                deleteWordButton = itemView.findViewById(R.id.deleteWord_Button);

                deleteWordButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        wordsInProcessSet.deleteWord(word);
                    }
                });
            }

            @Override
            public void onClick(View view) {
                //Toast.makeText(ViewWordsActivity.this, "Clicked", Toast.LENGTH_SHORT).show();

                Intent intent = ViewChangeWordActivity.newIntent(
                        ViewWordsActivity.this, word.getID());
                startActivity(intent);

            }
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            View contactView = inflater.inflate(R.layout.view_set_item, parent, false);

            // Return a new holder instance
            return new MyViewHolder(contactView);
        }

        @Override
        public void onBindViewHolder(MyAdapter.MyViewHolder holder, int position) {
            // Get the data model based on position
            Word word = wordsSet.get(position);

            // Set item views based on your views and data model
            holder.word = word;
            TextView rusTextView = holder.rusTextView;
            rusTextView.setText(word.wordRus);
            TextView endTextView = holder.engTextView;
            endTextView.setText(word.wordEng + " - ");

        }

        @Override
        public int getItemCount() {
            return wordsSet.size();
        }
    }

}
