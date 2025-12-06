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

        System.out.println("\n--- Finding book by title ---");
        Book found = library.findByTitle("1984");
        if (found != null) {
            System.out.println("Found: " + found);
        } else {
            System.out.println("Book not found");
        }

        System.out.println("\n--- Finding books by author ---");
        List<Book> orwellBooks = library.findByAuthor("George Orwell");
        System.out.println("Books by George Orwell:");
        for (Book book : orwellBooks) {
            System.out.println("  - " + book);
        }

        System.out.println("\n--- Removing a book ---");
        library.removeBook("978-0743273565");

        library.listAllBooks();
    }
}
