package com.burhanrashid52.imageeditor.models;


/**
 * Created by admin on 8/19/18.
 */

public class Quote {
    String content;
    String category;
    String author;
    Integer ref;

    public Quote(String content, String category, String author) {
        this.content = content;
        this.category = category;
        this.author = author;
    }


    public Integer getRef() {
        return ref;
    }

    public void setRef(Integer ref) {
        this.ref = ref;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
