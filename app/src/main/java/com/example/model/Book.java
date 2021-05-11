package com.example.model;

import java.util.Date;

public class Book {
    private int bookID;
    private String bookName;
    private String dateOfPublication;
    private int authorID;
    Author author;

    public Book() {
    }

    public Book(int bookID, String bookName, String dateOfPublication, int authorID) {
        this.bookID = bookID;
        this.bookName = bookName;
        this.dateOfPublication = dateOfPublication;
        this.authorID = authorID;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getDateOfPublication() {
        return dateOfPublication;
    }

    public void setDateOfPublication(String dateOfPublication) {
        this.dateOfPublication = dateOfPublication;
    }

    public int getAuthorID() {
        return authorID;
    }

    public void setAuthorID(int authorID) {
        this.authorID = authorID;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return this.bookID+"  ||  "+this.bookName+"  ||  "+this.dateOfPublication;
    }
}
