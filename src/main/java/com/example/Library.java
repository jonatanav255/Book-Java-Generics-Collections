package com.example;

import java.util.ArrayList;
import java.util.List;

public class Library {
    private List<Book> books;
    private String name;

    public Library(String name) {
        this.name = name;
        this.books = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
        System.out.println("Added: " + book);
    }

    public void removeBook(String isbn) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getIsbn().equals(isbn)) {
                Book removed = books.remove(i);
                System.out.println("Removed: " + removed);
                return;
            }
        }
        System.out.println("Book not found with ISBN: " + isbn);
    }

    public Book findByTitle(String title) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                return book;
            }
        }
        return null;
    }

    public List<Book> findByAuthor(String author) {
        List<Book> result = new ArrayList<>();
        for (Book book : books) {
            if (book.getAuthor().equalsIgnoreCase(author)) {
                result.add(book);
            }
        }
        return result;
    }

    public void listAllBooks() {
        System.out.println("\n=== " + name + " ===");
        if (books.isEmpty()) {
            System.out.println("No books in the library.");
        } else {
            for (int i = 0; i < books.size(); i++) {
                System.out.println((i + 1) + ". " + books.get(i));
            }
        }
        System.out.println("Total books: " + books.size());
    }

    public boolean borrowBook(String title, String personName) {
        Book book = findByTitle(title);
        if (book == null) {
            System.out.println("Book not found: " + title);
            return false;
        }

        if (book.borrowBook(personName)) {
            System.out.println(personName + " borrowed: " + book.getTitle());
            return true;
        } else {
            System.out.println("Book already borrowed by: " + book.getBorrowedBy());
            return false;
        }
    }

    public boolean returnBook(String title) {
        Book book = findByTitle(title);
        if (book == null) {
            System.out.println("Book not found: " + title);
            return false;
        }

        if (book.returnBook()) {
            System.out.println("Book returned: " + book.getTitle());
            return true;
        } else {
            System.out.println("Book was not borrowed: " + book.getTitle());
            return false;
        }
    }

    public List<Book> getAvailableBooks() {
        List<Book> available = new ArrayList<>();
        for (Book book : books) {
            if (!book.isBorrowed()) {
                available.add(book);
            }
        }
        return available;
    }

    public List<Book> getBorrowedBooks() {
        List<Book> borrowed = new ArrayList<>();
        for (Book book : books) {
            if (book.isBorrowed()) {
                borrowed.add(book);
            }
        }
        return borrowed;
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
        List<Book> result = new ArrayList<>();
        for (Book book : books) {
            if (book.getCategory() == category) {
                result.add(book);
            }
        }
        return result;
    }

    public List<Book> findByYearRange(int minYear, int maxYear) {
        List<Book> result = new ArrayList<>();
        for (Book book : books) {
            if (book.getYear() >= minYear && book.getYear() <= maxYear) {
                result.add(book);
            }
        }
        return result;
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
}
