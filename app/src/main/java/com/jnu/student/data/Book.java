package com.jnu.student.data;

import java.io.Serializable;

public class Book implements Serializable {

    public void setTitle(String title){
        this.title = title;
    }

    public void setCoverResourceId(int id){
        coverResourceId = id;
    }

    public String getTitle() {
        return title;
    }

    public int getCoverResourceId() {
        return coverResourceId;
    }

    private String title;
    private int coverResourceId;

    public Book(String title_, int coverResourceId_) {
        this.title = title_;
        this.coverResourceId = coverResourceId_;
    }
}
