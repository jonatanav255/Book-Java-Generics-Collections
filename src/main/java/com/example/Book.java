package com.example;

import java.time.LocalDate;
import java.util.Objects;

public class Book {
    public static final int DEFAULT_BORROW_DAYS = 14;
    private static final int MIN_VALID_YEAR = 1000;
    private static final int MAX_VALID_YEAR = LocalDate.now().getYear() + 1;

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
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author cannot be null or empty");
        }
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be null or empty");
        }
        if (year < MIN_VALID_YEAR || year > MAX_VALID_YEAR) {
            throw new IllegalArgumentException("Year must be between " + MIN_VALID_YEAR + " and " + MAX_VALID_YEAR);
        }
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }

        this.title = title.trim();
        this.author = author.trim();
        this.year = year;
        this.isbn = isbn.trim();
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
        if (personName == null || personName.trim().isEmpty()) {
            throw new IllegalArgumentException("Person name cannot be null or empty");
        }
        if (daysToReturn <= 0) {
            throw new IllegalArgumentException("Days to return must be positive");
        }
        if (!isBorrowed) {
            isBorrowed = true;
            borrowedBy = personName.trim();
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return isbn.equals(book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }

    @Override
    public String toString() {
        String status = isBorrowed ? " [Borrowed by " + borrowedBy + "]" : " [Available]";
        String ratingStr = rating > 0 ? " ★" + rating : "";
        String overdueStr = isOverdue() ? " OVERDUE!" : "";
        return "'" + title + "' by " + author + " (" + year + ") - " + category + ratingStr + status + overdueStr;
    }
}
