package com.eltigreapps.eltigreandroid.model;

/**
 * Created by nadirhussain on 23/05/2018.
 */

public class Book {

    private String eisbn;
    private String title;
    private String description;
    private String author;

    public Book(){

    }
    public Book(String eisbn) {
        this.eisbn = eisbn;
    }

    public String getEisbn() {
        return eisbn;
    }

    public void setEisbn(String eisbn) {
        this.eisbn = eisbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCoverUrl() {
        return "http://eltigre.imgix.net/" + eisbn + "_cover.jpg?w=230&dpr=2";
    }


}
