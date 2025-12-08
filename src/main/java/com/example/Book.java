package com.example;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

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
    private Queue<String> reservations;
    private Fine currentFine;

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
        this.reservations = new LinkedList<>();
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
            String nextPerson = reservations.poll();
            if (nextPerson != null) {
                borrowedBy = nextPerson;
                timesRead++;
                dueDate = LocalDate.now().plusDays(DEFAULT_BORROW_DAYS);
            } else {
                isBorrowed = false;
                borrowedBy = null;
                dueDate = null;
            }
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

    public boolean reserveBook(String personName) {
        if (personName == null || personName.trim().isEmpty()) {
            throw new IllegalArgumentException("Person name cannot be null or empty");
        }
        if (!isBorrowed) {
            return false;
        }
        if (reservations.contains(personName.trim())) {
            return false;
        }
        reservations.offer(personName.trim());
        return true;
    }

    public boolean cancelReservation(String personName) {
        if (personName == null || personName.trim().isEmpty()) {
            throw new IllegalArgumentException("Person name cannot be null or empty");
        }
        return reservations.remove(personName.trim());
    }

    public Queue<String> getReservations() {
        return new LinkedList<>(reservations);
    }

    public int getReservationCount() {
        return reservations.size();
    }

    public String getNextReservation() {
        return reservations.peek();
    }

    public Fine getCurrentFine() {
        return currentFine;
    }

    public void setCurrentFine(Fine fine) {
        this.currentFine = fine;
    }

    public boolean hasFine() {
        return currentFine != null && !currentFine.isPaid();
    }

    /**
     * FOR TESTING ONLY - Set a custom due date to simulate overdue books.
     */
    void setDueDateForTesting(LocalDate dueDate) {
        this.dueDate = dueDate;
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
        String reservationStr = reservations.size() > 0 ? " [" + reservations.size() + " reservation(s)]" : "";
        String fineStr = hasFine() ? " [FINE: $" + currentFine.getAmountRemaining() + "]" : "";
        return "'" + title + "' by " + author + " (" + year + ") - " + category + ratingStr + status + overdueStr + reservationStr + fineStr;
    }
}
