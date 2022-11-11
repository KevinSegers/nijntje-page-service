package com.example.pageservice.model;

import org.springframework.data.annotation.Id;

import java.util.List;

public class Page {

    @Id
    private String id;
    private int pageNumber;
    private List<String> items;
    private Boolean seen;
    private String bookTitle;


    public Page() {
    }


    public Page( int pageNumber, List<String> items,  Boolean seen, String bookTitle) {
        this.pageNumber = pageNumber;
        this.items = items;
        this.seen = seen;
        this.bookTitle = bookTitle;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public Boolean isSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public void setBookTitle(String bookTitle){this.bookTitle = bookTitle;}

    public String getBookTitle(){return bookTitle;}
}
