package com.example.learnenglishwordssecondtry.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Utils {

    public static void sortWordsByRus(List<Word> words) {
        Collections.sort(words, new Comparator<Word>() {
            @Override
            public int compare(Word o1, Word o2) {
                return o1.wordRus.compareTo(o2.wordRus);
            }
        });
    }
}
