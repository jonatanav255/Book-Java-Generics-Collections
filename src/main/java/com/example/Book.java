package com.example;

public class Book {
    private String title;
    private String author;
    private int year;
    private String isbn;
    private boolean isBorrowed;
    private String borrowedBy;
    private Category category;

    public Book(String title, String author, int year, String isbn, Category category) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.isbn = isbn;
        this.category = category;
        this.isBorrowed = false;
        this.borrowedBy = null;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getYear() {
        return year;
    }

    public String getIsbn() {
        return isbn;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public String getBorrowedBy() {
        return borrowedBy;
    }

    public Category getCategory() {
        return category;
    }

    public boolean borrowBook(String personName) {
        if (!isBorrowed) {
            isBorrowed = true;
            borrowedBy = personName;
            return true;
        }
        return false;
    }

    public boolean returnBook() {
        if (isBorrowed) {
            isBorrowed = false;
            borrowedBy = null;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        String status = isBorrowed ? " [Borrowed by " + borrowedBy + "]" : " [Available]";
        return "'" + title + "' by " + author + " (" + year + ") - " + category + status;
    }
}
