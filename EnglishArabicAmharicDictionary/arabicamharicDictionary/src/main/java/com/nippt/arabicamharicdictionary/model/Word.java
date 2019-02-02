package com.nippt.arabicamharicdictionary.model;

import java.io.Serializable;

public class Word implements Serializable {
    private int wordId;
    private String englishWord="";
    private String arabicWord="";
    private String amharicWord="";

    public Word() {

    }

    public Word(int id, String englishWord, String arabicWord, String amharicWord) {
        this.wordId = id;
        this.englishWord = englishWord;
        this.arabicWord = arabicWord;
        this.amharicWord = amharicWord;
    }

    public String getArabicWord() {
        return arabicWord;
    }

    public void setArabicWord(String arabicWord) {
        this.arabicWord = arabicWord;
    }

    public String getEnglishWord() {
        return englishWord;
    }

    public void setEnglishWord(String englishWord) {
        this.englishWord = englishWord;
    }

    public int getWordId() {
        return wordId;
    }

    public void setWordId(int wordId) {
        this.wordId = wordId;
    }

    public String getAmharicWord() {
        return amharicWord;
    }

    public void setAmharicWord(String amharicWord) {
        this.amharicWord = amharicWord;
    }


}
