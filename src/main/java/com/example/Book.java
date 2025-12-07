package com.example;

import java.time.LocalDate;

public class Book {
    private String title;
    private String author;
    private int year;
    private String isbn;
    private boolean isBorrowed;
    private String borrowedBy;
    private Category category;
    private double rating;
    private int timesRead;
    private LocalDate dueDate;

    public Book(String title, String author, int year, String isbn, Category category) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.isbn = isbn;
        this.category = category;
        this.isBorrowed = false;
        this.borrowedBy = null;
        this.rating = 0.0;
        this.timesRead = 0;
        this.dueDate = null;
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

    public double getRating() {
        return rating;
    }

    public int getTimesRead() {
        return timesRead;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setRating(double rating) {
        if (rating >= 0.0 && rating <= 5.0) {
            this.rating = rating;
        }
    }

    public boolean borrowBook(String personName, int daysToReturn) {
        if (!isBorrowed) {
            isBorrowed = true;
            borrowedBy = personName;
            timesRead++;
            dueDate = LocalDate.now().plusDays(daysToReturn);
            return true;
        }
        return false;
    }

    public boolean returnBook() {
        if (isBorrowed) {
            isBorrowed = false;
            borrowedBy = null;
            dueDate = null;
            return true;
        }
        return false;
    }

    public boolean isOverdue() {
        if (isBorrowed && dueDate != null) {
            return LocalDate.now().isAfter(dueDate);
        }
        return false;
    }

    public long getDaysOverdue() {
        if (isOverdue()) {
            return LocalDate.now().toEpochDay() - dueDate.toEpochDay();
        }
        return 0;
    }

    @Override
    public String toString() {
        String status = isBorrowed ? " [Borrowed by " + borrowedBy + "]" : " [Available]";
        String ratingStr = rating > 0 ? " ★" + rating : "";
        String overdueStr = isOverdue() ? " OVERDUE!" : "";
        return "'" + title + "' by " + author + " (" + year + ") - " + category + ratingStr + status + overdueStr;
    }
}
