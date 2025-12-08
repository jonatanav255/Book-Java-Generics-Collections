package com.example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Library {
    private Map<String, Book> books;
    private String name;
    private FineCalculator fineCalculator;

    public Library(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Library name cannot be null or empty");
        }
        this.name = name.trim();
        this.books = new HashMap<>();
        this.fineCalculator = new FineCalculator();
    }

    public Library(String name, FineCalculator fineCalculator) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Library name cannot be null or empty");
        }
        if (fineCalculator == null) {
            throw new IllegalArgumentException("FineCalculator cannot be null");
        }
        this.name = name.trim();
        this.books = new HashMap<>();
        this.fineCalculator = fineCalculator;
    }

    public boolean addBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        if (books.containsKey(book.getIsbn())) {
            return false;
        }
        books.put(book.getIsbn(), book);
        return true;
    }

    public boolean removeBook(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be null or empty");
        }
        return books.remove(isbn.trim()) != null;
    }

    public Optional<Book> findByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        return books.values().stream()
            .filter(book -> book.getTitle().equalsIgnoreCase(title.trim()))
            .findFirst();
    }

    public List<Book> findByAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author cannot be null or empty");
        }
        return books.values().stream()
            .filter(book -> book.getAuthor().equalsIgnoreCase(author.trim()))
            .collect(Collectors.toList());
    }

    public void listAllBooks() {
        System.out.println("\n=== " + name + " ===");
        if (books.isEmpty()) {
            System.out.println("No books in the library.");
        } else {
            List<Book> bookList = new ArrayList<>(books.values());
            for (int i = 0; i < bookList.size(); i++) {
                System.out.println((i + 1) + ". " + bookList.get(i));
            }
        }
        System.out.println("Total books: " + books.size());
    }

    public boolean borrowBook(String title, String personName) {
        return borrowBook(title, personName, Book.DEFAULT_BORROW_DAYS);
    }

    public boolean borrowBook(String title, String personName, int daysToReturn) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (personName == null || personName.trim().isEmpty()) {
            throw new IllegalArgumentException("Person name cannot be null or empty");
        }

        Optional<Book> bookOpt = findByTitle(title);
        if (!bookOpt.isPresent()) {
            return false;
        }

        Book book = bookOpt.get();
        return book.borrowBook(personName, daysToReturn);
    }

    public boolean returnBook(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }

        Optional<Book> bookOpt = findByTitle(title);
        if (!bookOpt.isPresent()) {
            return false;
        }

        return bookOpt.get().returnBook();
    }

    public boolean reserveBook(String title, String personName) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (personName == null || personName.trim().isEmpty()) {
            throw new IllegalArgumentException("Person name cannot be null or empty");
        }

        Optional<Book> bookOpt = findByTitle(title);
        if (!bookOpt.isPresent()) {
            return false;
        }

        return bookOpt.get().reserveBook(personName);
    }

    public boolean cancelReservation(String title, String personName) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (personName == null || personName.trim().isEmpty()) {
            throw new IllegalArgumentException("Person name cannot be null or empty");
        }

        Optional<Book> bookOpt = findByTitle(title);
        if (!bookOpt.isPresent()) {
            return false;
        }

        return bookOpt.get().cancelReservation(personName);
    }

    public void showReservations(String title) {
        Optional<Book> bookOpt = findByTitle(title);
        if (!bookOpt.isPresent()) {
            System.out.println("Book not found: " + title);
            return;
        }

        Book book = bookOpt.get();
        System.out.println("\n=== Reservations for: " + book.getTitle() + " ===");
        if (book.getReservationCount() == 0) {
            System.out.println("No reservations");
        } else {
            int position = 1;
            for (String person : book.getReservations()) {
                System.out.println(position + ". " + person);
                position++;
            }
        }
    }

    public List<Book> getAvailableBooks() {
        return books.values().stream()
            .filter(book -> !book.isBorrowed())
            .collect(Collectors.toList());
    }

    public List<Book> getBorrowedBooks() {
        return books.values().stream()
            .filter(Book::isBorrowed)
            .collect(Collectors.toList());
    }

    public int getTotalBooks() {
        return books.size();
    }

    public int getAvailableCount() {
        return getAvailableBooks().size();
    }

    public int getBorrowedCount() {
        return getBorrowedBooks().size();
    }

    public List<Book> findByCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        return books.values().stream()
            .filter(book -> book.getCategory() == category)
            .collect(Collectors.toList());
    }

    public List<Book> findByYearRange(int minYear, int maxYear) {
        if (minYear > maxYear) {
            throw new IllegalArgumentException("minYear cannot be greater than maxYear");
        }
        return books.values().stream()
            .filter(book -> book.getYear() >= minYear && book.getYear() <= maxYear)
            .collect(Collectors.toList());
    }

    public void listBooksByCategory() {
        System.out.println("\n=== Books by Category ===");
        for (Category category : Category.values()) {
            List<Book> booksInCategory = findByCategory(category);
            if (!booksInCategory.isEmpty()) {
                System.out.println("\n" + category + ":");
                for (Book book : booksInCategory) {
                    System.out.println("  - " + book);
                }
            }
        }
    }

    public List<Book> findByMinimumRating(double minRating) {
        if (minRating < 0.0 || minRating > 5.0) {
            throw new IllegalArgumentException("Rating must be between 0.0 and 5.0");
        }
        return books.values().stream()
            .filter(book -> book.getRating() >= minRating)
            .collect(Collectors.toList());
    }

    public List<Book> getOverdueBooks() {
        return books.values().stream()
            .filter(Book::isOverdue)
            .collect(Collectors.toList());
    }

    public List<Book> getMostPopularBooks(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Count must be positive");
        }
        return books.values().stream()
            .filter(book -> book.getTimesRead() > 0)
            .sorted(Comparator.comparingInt(Book::getTimesRead).reversed())
            .limit(count)
            .collect(Collectors.toList());
    }

    public boolean rateBook(String title, double rating) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (rating < 0.0 || rating > 5.0) {
            throw new IllegalArgumentException("Rating must be between 0.0 and 5.0");
        }

        Optional<Book> bookOpt = findByTitle(title);
        if (!bookOpt.isPresent()) {
            return false;
        }

        bookOpt.get().setRating(rating);
        return true;
    }

    public void showOverdueReport() {
        List<Book> overdueBooks = getOverdueBooks();
        System.out.println("\n=== Overdue Books Report ===");
        if (overdueBooks.isEmpty()) {
            System.out.println("No overdue books!");
        } else {
            for (Book book : overdueBooks) {
                System.out.println("  - " + book + " (" + book.getDaysOverdue() + " days overdue)");
            }
        }
    }

    // ==================== FINE MANAGEMENT ====================

    /**
     * Calculate and assign fines to all overdue books.
     * Updates existing fines for books still overdue.
     */
    public void calculateFines() {
        books.values().stream()
            .filter(Book::isOverdue)
            .forEach(book -> {
                Fine fine = fineCalculator.createFine(book);
                book.setCurrentFine(fine);
            });
    }

    /**
     * Calculate fine for a specific book.
     */
    public boolean calculateFine(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }

        Optional<Book> bookOpt = findByTitle(title);
        if (!bookOpt.isPresent()) {
            return false;
        }

        Book book = bookOpt.get();
        if (!book.isOverdue()) {
            return false;
        }

        Fine fine = fineCalculator.createFine(book);
        book.setCurrentFine(fine);
        return true;
    }

    /**
     * Pay a fine for a book.
     * Supports partial payments.
     */
    public boolean payFine(String title, BigDecimal amount) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive");
        }

        Optional<Book> bookOpt = findByTitle(title);
        if (!bookOpt.isPresent()) {
            return false;
        }

        Book book = bookOpt.get();
        if (!book.hasFine()) {
            return false;
        }

        Fine updatedFine = book.getCurrentFine().withPayment(amount);
        book.setCurrentFine(updatedFine);
        return true;
    }

    /**
     * Waive a fine for a book with a reason.
     */
    public boolean waiveFine(String title, String reason) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("Waiver reason required");
        }

        Optional<Book> bookOpt = findByTitle(title);
        if (!bookOpt.isPresent()) {
            return false;
        }

        Book book = bookOpt.get();
        if (!book.hasFine()) {
            return false;
        }

        Fine waivedFine = book.getCurrentFine().withWaiver(reason);
        book.setCurrentFine(waivedFine);
        return true;
    }

    /**
     * Get all books with unpaid fines.
     */
    public List<Book> getBooksWithFines() {
        return books.values().stream()
            .filter(Book::hasFine)
            .collect(Collectors.toList());
    }

    /**
     * Get total unpaid fines across all books.
     */
    public BigDecimal getTotalUnpaidFines() {
        return books.values().stream()
            .filter(Book::hasFine)
            .map(Book::getCurrentFine)
            .map(Fine::getAmountRemaining)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Show detailed fine report.
     */
    public void showFineReport() {
        List<Book> booksWithFines = getBooksWithFines();
        System.out.println("\n=== Fine Report ===");

        if (booksWithFines.isEmpty()) {
            System.out.println("No unpaid fines!");
        } else {
            System.out.println("Books with unpaid fines:");
            for (Book book : booksWithFines) {
                Fine fine = book.getCurrentFine();
                System.out.println("  - " + fine);
            }
            System.out.println("\nTotal unpaid fines: $" + getTotalUnpaidFines());
        }

        System.out.println("\nFine policy: " + fineCalculator);
    }

    public FineCalculator getFineCalculator() {
        return fineCalculator;
    }
}
