package com.jnu.student.data;

import java.io.Serializable;

public class Book {

    public String getTitle() {
        return title;
    }

    public int getCoverResourceId() {
        return coverResourceId;
    }

    private final String title;
    private final int coverResourceId;

    public Book(String title_, int coverResourceId_) {
        this.title = title_;
        this.coverResourceId = coverResourceId_;
    }
}
