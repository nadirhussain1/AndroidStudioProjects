package com.nippt.arabicamharicdictionary.free.database;

import com.nippt.arabicamharicdictionary.model.Word;

public class History extends Word {
    private int mFavourite;

    public History() {

    }

    public History(int id, String englishWord, String secondLanguageWord,String thirdWordLang) {
        super(id, englishWord, secondLanguageWord,thirdWordLang);
    }


    public int getmFavourite() {
        return mFavourite;
    }

    public void setmFavourite(int mFavourite) {
        this.mFavourite = mFavourite;
    }


}
