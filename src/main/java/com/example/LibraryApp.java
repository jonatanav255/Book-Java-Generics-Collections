package com.example;

import java.util.List;

public class LibraryApp {
    public static void main(String[] args) {
        Library library = new Library("City Library");

        Book book1 = new Book("1984", "George Orwell", 1949, "978-0451524935", Category.CLASSIC);
        Book book2 = new Book("To Kill a Mockingbird", "Harper Lee", 1960, "978-0061120084", Category.CLASSIC);
        Book book3 = new Book("The Great Gatsby", "F. Scott Fitzgerald", 1925, "978-0743273565", Category.CLASSIC);
        Book book4 = new Book("Animal Farm", "George Orwell", 1945, "978-0451526342", Category.CLASSIC);
        Book book5 = new Book("Dune", "Frank Herbert", 1965, "978-0441172719", Category.SCIENCE_FICTION);
        Book book6 = new Book("The Hobbit", "J.R.R. Tolkien", 1937, "978-0547928227", Category.FANTASY);
        Book book7 = new Book("Steve Jobs", "Walter Isaacson", 2011, "978-1451648539", Category.BIOGRAPHY);

        library.addBook(book1);
        library.addBook(book2);
        library.addBook(book3);
        library.addBook(book4);
        library.addBook(book5);
        library.addBook(book6);
        library.addBook(book7);

        library.listAllBooks();

        System.out.println("\n--- Search by Category: CLASSIC ---");
        List<Book> classics = library.findByCategory(Category.CLASSIC);
        for (Book book : classics) {
            System.out.println("  - " + book);
        }

        System.out.println("\n--- Search by Category: SCIENCE_FICTION ---");
        List<Book> sciFi = library.findByCategory(Category.SCIENCE_FICTION);
        for (Book book : sciFi) {
            System.out.println("  - " + book);
        }

        System.out.println("\n--- Books published between 1940 and 1950 ---");
        List<Book> booksInRange = library.findByYearRange(1940, 1950);
        for (Book book : booksInRange) {
            System.out.println("  - " + book);
        }

        System.out.println("\n--- Books published after 2000 ---");
        List<Book> modernBooks = library.findByYearRange(2000, 2024);
        for (Book book : modernBooks) {
            System.out.println("  - " + book);
        }

        library.listBooksByCategory();

        System.out.println("\n--- Rating some books ---");
        library.rateBook("1984", 4.8);
        library.rateBook("The Hobbit", 4.5);
        library.rateBook("Dune", 4.9);
        library.rateBook("Steve Jobs", 4.2);

        System.out.println("\n--- Alice borrows 1984 for 14 days ---");
        library.borrowBook("1984", "Alice", 14);

        System.out.println("\n--- Bob borrows Dune for 7 days ---");
        library.borrowBook("Dune", "Bob", 7);

        System.out.println("\n--- Charlie borrows The Hobbit for 1 day (will be overdue for demo) ---");
        library.borrowBook("The Hobbit", "Charlie", 1);

        library.listAllBooks();

        System.out.println("\n--- Available books ---");
        List<Book> availableBooks = library.getAvailableBooks();
        for (Book book : availableBooks) {
            System.out.println("  - " + book);
        }

        System.out.println("\n--- Statistics ---");
        System.out.println("Total books: " + library.getTotalBooks());
        System.out.println("Available: " + library.getAvailableCount());
        System.out.println("Borrowed: " + library.getBorrowedCount());

        System.out.println("\n--- Finding books by author ---");
        List<Book> orwellBooks = library.findByAuthor("George Orwell");
        System.out.println("Books by George Orwell:");
        for (Book book : orwellBooks) {
            System.out.println("  - " + book);
        }

        System.out.println("\n--- Books with rating 4.5 or higher ---");
        List<Book> highRated = library.findByMinimumRating(4.5);
        for (Book book : highRated) {
            System.out.println("  - " + book);
        }

        System.out.println("\n--- Most popular books (Top 3) ---");
        List<Book> popularBooks = library.getMostPopularBooks(3);
        for (Book book : popularBooks) {
            System.out.println("  - " + book + " (Read " + book.getTimesRead() + " times)");
        }

        library.showOverdueReport();
    }
}
