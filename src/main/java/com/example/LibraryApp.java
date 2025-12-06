package com.example;

import java.util.List;

public class LibraryApp {
    public static void main(String[] args) {
        Library library = new Library("City Library");

        Book book1 = new Book("1984", "George Orwell", 1949, "978-0451524935");
        Book book2 = new Book("To Kill a Mockingbird", "Harper Lee", 1960, "978-0061120084");
        Book book3 = new Book("The Great Gatsby", "F. Scott Fitzgerald", 1925, "978-0743273565");
        Book book4 = new Book("Animal Farm", "George Orwell", 1945, "978-0451526342");

        library.addBook(book1);
        library.addBook(book2);
        library.addBook(book3);
        library.addBook(book4);

        library.listAllBooks();

        System.out.println("\n--- Alice borrows 1984 ---");
        library.borrowBook("1984", "Alice");

        System.out.println("\n--- Bob tries to borrow 1984 ---");
        library.borrowBook("1984", "Bob");

        System.out.println("\n--- Bob borrows Animal Farm ---");
        library.borrowBook("Animal Farm", "Bob");

        library.listAllBooks();

        System.out.println("\n--- Available books ---");
        List<Book> availableBooks = library.getAvailableBooks();
        for (Book book : availableBooks) {
            System.out.println("  - " + book);
        }

        System.out.println("\n--- Borrowed books ---");
        List<Book> borrowedBooks = library.getBorrowedBooks();
        for (Book book : borrowedBooks) {
            System.out.println("  - " + book);
        }

        System.out.println("\n--- Statistics ---");
        System.out.println("Total books: " + library.getTotalBooks());
        System.out.println("Available: " + library.getAvailableCount());
        System.out.println("Borrowed: " + library.getBorrowedCount());

        System.out.println("\n--- Alice returns 1984 ---");
        library.returnBook("1984");

        System.out.println("\n--- Bob tries to borrow 1984 now ---");
        library.borrowBook("1984", "Bob");

        library.listAllBooks();

        System.out.println("\n--- Finding books by author ---");
        List<Book> orwellBooks = library.findByAuthor("George Orwell");
        System.out.println("Books by George Orwell:");
        for (Book book : orwellBooks) {
            System.out.println("  - " + book);
        }
    }
}
