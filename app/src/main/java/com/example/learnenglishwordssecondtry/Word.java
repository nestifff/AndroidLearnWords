package com.example.learnenglishwordssecondtry;

import java.lang.Comparable;
import java.util.UUID;

public class Word implements Comparable<Word> {

    public String wordEng;
    public String wordRus;
    private UUID wordID;

    public Word (String eng, String rus) {
        wordEng = eng;
        wordRus = rus;
        wordID = UUID.randomUUID();
    }

    public Word() {
        wordEng = "-";
        wordRus = "-";
    }

    public Word (UUID id, String eng, String rus) {
        wordEng = eng;
        wordRus = rus;
        wordID = id;
    }


    public boolean isCorrect(boolean rusEngWay, String tryAns) {

        String rightAns;

        if (rusEngWay) {
            rightAns = wordEng;
        } else {
            rightAns = wordRus;
        }

        rightAns = rightAns.toLowerCase();
        tryAns = tryAns.toLowerCase();

        if (rightAns.equals(tryAns)) {
            return true;
        }
        // one incorrect letter
        else if (rightAns.length() == tryAns.length()) {

            boolean oneIncorrect = false;
            for (int i = 0; i < rightAns.length(); ++i) {
                if (rightAns.charAt(i) != tryAns.charAt(i)) {
                    if(oneIncorrect) {
                        return false;
                    }
                    else {
                        oneIncorrect = true;
                    }
                }
            }

            return true;

        }

        // one letter is skipped
        else if (rightAns.length() == tryAns.length() + 1) {

            boolean oneIsSkipped = false;
            for (int i = 0; i < rightAns.length() - 1; ++i) {

                int ind = i;
                if (oneIsSkipped) {
                    ++ind;
                }

                if (rightAns.charAt(ind) != tryAns.charAt(i)) {
                    if (!oneIsSkipped) {
                        oneIsSkipped = true;
                    }
                    else {
                        return false;
                    }
                }
            }

            return true;

        }

        return false;
    }

    public String getAnswer(boolean rusEngWay) {
        if (rusEngWay) {
            return wordEng;
        }
        return wordRus;
    }


    public int compareTo(Word w) {

        return this.wordEng.compareToIgnoreCase(w.wordEng);
    }

    public UUID getID() {
        return wordID;
    }


}
