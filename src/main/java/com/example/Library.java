package com.example;

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

    public Library(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Library name cannot be null or empty");
        }
        this.name = name.trim();
        this.books = new HashMap<>();
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
}
