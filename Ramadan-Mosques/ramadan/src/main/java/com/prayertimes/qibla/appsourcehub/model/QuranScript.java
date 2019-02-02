package com.prayertimes.qibla.appsourcehub.model;


public class QuranScript
{

    public int ayah;
    public int sura;
    public String text;

    public QuranScript()
    {
    }

    public int getAyah()
    {
        return ayah;
    }

    public int getSura()
    {
        return sura;
    }

    public String getText()
    {
        return text;
    }

    public void setAyah(int i)
    {
        ayah = i;
    }

    public void setSura(int i)
    {
        sura = i;
    }

    public void setText(String s)
    {
        text = s;
    }
}
