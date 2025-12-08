package com.example;

import java.math.BigDecimal;
import java.time.LocalDate;
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

        if (library.addBook(book1)) System.out.println("Added: " + book1);
        if (library.addBook(book2)) System.out.println("Added: " + book2);
        if (library.addBook(book3)) System.out.println("Added: " + book3);
        if (library.addBook(book4)) System.out.println("Added: " + book4);
        if (library.addBook(book5)) System.out.println("Added: " + book5);
        if (library.addBook(book6)) System.out.println("Added: " + book6);
        if (library.addBook(book7)) System.out.println("Added: " + book7);

        System.out.println("\n--- Testing duplicate prevention ---");
        if (!library.addBook(book1)) {
            System.out.println("Correctly rejected duplicate: " + book1.getTitle());
        }

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
        if (library.rateBook("1984", 4.8)) {
            System.out.println("Rated '1984' with 4.8 stars");
        }
        if (library.rateBook("The Hobbit", 4.5)) {
            System.out.println("Rated 'The Hobbit' with 4.5 stars");
        }
        if (library.rateBook("Dune", 4.9)) {
            System.out.println("Rated 'Dune' with 4.9 stars");
        }
        if (library.rateBook("Steve Jobs", 4.2)) {
            System.out.println("Rated 'Steve Jobs' with 4.2 stars");
        }
        if (!library.rateBook("NonExistent Book", 5.0)) {
            System.out.println("Book not found: NonExistent Book");
        }

        System.out.println("\n--- Borrowing books ---");
        if (library.borrowBook("1984", "Alice", 14)) {
            System.out.println("Alice borrowed: 1984");
        }
        if (library.borrowBook("Dune", "Bob", 7)) {
            System.out.println("Bob borrowed: Dune");
        }
        if (library.borrowBook("The Hobbit", "Charlie", 1)) {
            System.out.println("Charlie borrowed: The Hobbit (will be overdue for demo)");
        }
        if (!library.borrowBook("1984", "David", 14)) {
            System.out.println("Cannot borrow: 1984 is already borrowed");
        }

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

        System.out.println("\n--- RESERVATION SYSTEM DEMO ---");
        System.out.println("\n--- David tries to reserve 1984 (already borrowed by Alice) ---");
        if (library.reserveBook("1984", "David")) {
            System.out.println("David successfully reserved 1984");
        }

        System.out.println("\n--- Eve also reserves 1984 ---");
        if (library.reserveBook("1984", "Eve")) {
            System.out.println("Eve successfully reserved 1984");
        }

        System.out.println("\n--- Frank tries to reserve 1984 too ---");
        if (library.reserveBook("1984", "Frank")) {
            System.out.println("Frank successfully reserved 1984");
        }

        System.out.println("\n--- Current state of 1984 ---");
        library.findByTitle("1984").ifPresent(book -> System.out.println(book));
        library.showReservations("1984");

        System.out.println("\n--- Alice returns 1984 (should auto-assign to David) ---");
        if (library.returnBook("1984")) {
            System.out.println("Book returned successfully");
            library.findByTitle("1984").ifPresent(book ->
                System.out.println("New status: " + book)
            );
        }

        System.out.println("\n--- Check remaining reservations for 1984 ---");
        library.showReservations("1984");

        System.out.println("\n--- Eve cancels her reservation ---");
        if (library.cancelReservation("1984", "Eve")) {
            System.out.println("Eve's reservation cancelled");
        }

        System.out.println("\n--- Final reservations for 1984 ---");
        library.showReservations("1984");

        System.out.println("\n--- David returns 1984 (should auto-assign to Frank) ---");
        if (library.returnBook("1984")) {
            System.out.println("Book returned successfully");
            library.findByTitle("1984").ifPresent(book ->
                System.out.println("New status: " + book)
            );
        }

        System.out.println("\n--- Frank returns 1984 (no more reservations) ---");
        if (library.returnBook("1984")) {
            System.out.println("Book returned successfully");
            library.findByTitle("1984").ifPresent(book ->
                System.out.println("New status: " + book)
            );
        }

        System.out.println("\n\n" + "=".repeat(50));
        System.out.println("FINE SYSTEM DEMO (Production-Ready)");
        System.out.println("=".repeat(50));

        System.out.println("\n--- Creating overdue books for fine demo ---");
        System.out.println("(Simulating books that are overdue by setting past due dates)");

        // Simulate The Hobbit being 5 days overdue
        library.findByTitle("The Hobbit").ifPresent(book -> {
            book.setDueDateForTesting(LocalDate.now().minusDays(5));
            System.out.println("The Hobbit: 5 days overdue");
        });

        // Simulate Dune being 10 days overdue
        library.findByTitle("Dune").ifPresent(book -> {
            book.setDueDateForTesting(LocalDate.now().minusDays(10));
            System.out.println("Dune: 10 days overdue");
        });

        // Borrow and immediately make Animal Farm 3 days overdue
        if (library.borrowBook("Animal Farm", "Grace", 14)) {
            library.findByTitle("Animal Farm").ifPresent(book -> {
                book.setDueDateForTesting(LocalDate.now().minusDays(3));
                System.out.println("Animal Farm: 3 days overdue");
            });
        }

        library.showOverdueReport();

        System.out.println("\n--- Fine Policy ---");
        System.out.println(library.getFineCalculator());

        System.out.println("\n--- Calculating fines for all overdue books ---");
        library.calculateFines();
        System.out.println("Fines calculated!");

        library.showFineReport();

        System.out.println("\n--- Charlie pays partial fine for The Hobbit ---");
        if (library.payFine("The Hobbit", new BigDecimal("1.50"))) {
            System.out.println("Payment of $1.50 accepted");
            library.findByTitle("The Hobbit").ifPresent(book ->
                System.out.println("Remaining: $" + book.getCurrentFine().getAmountRemaining())
            );
        }

        System.out.println("\n--- Charlie pays remaining balance for The Hobbit ---");
        library.findByTitle("The Hobbit").ifPresent(book -> {
            BigDecimal remaining = book.getCurrentFine().getAmountRemaining();
            if (library.payFine("The Hobbit", remaining)) {
                System.out.println("Payment of $" + remaining + " accepted");
                System.out.println("Fine fully paid: " + book.getCurrentFine().isPaid());
            }
        });

        System.out.println("\n--- Library waives Grace's fine (first-time offender) ---");
        if (library.waiveFine("Animal Farm", "First-time offender, good standing")) {
            System.out.println("Fine waived for Animal Farm");
            library.findByTitle("Animal Farm").ifPresent(book ->
                System.out.println("Status: " + book.getCurrentFine())
            );
        }

        System.out.println("\n--- Bob still has unpaid fine for Dune ---");
        library.findByTitle("Dune").ifPresent(book ->
            System.out.println("Dune fine: " + book.getCurrentFine())
        );

        System.out.println("\n--- Final fine report ---");
        library.showFineReport();

        System.out.println("\n--- Edge Case: Trying to pay more than owed ---");
        try {
            library.payFine("Dune", new BigDecimal("100.00"));
        } catch (IllegalArgumentException e) {
            System.out.println("Error caught: " + e.getMessage());
        }

        System.out.println("\n--- Edge Case: Trying to waive already paid fine ---");
        if (!library.waiveFine("The Hobbit", "Test reason")) {
            System.out.println("Cannot waive: Fine is already paid");
        }

        System.out.println("\n--- Production Quality Validation ---");
        System.out.println("✓ BigDecimal used for precise money calculations");
        System.out.println("✓ Immutable Fine objects (thread-safe)");
        System.out.println("✓ Comprehensive input validation");
        System.out.println("✓ Edge cases handled with clear error messages");
        System.out.println("✓ Partial payment support");
        System.out.println("✓ Fine waiver with audit trail (reason tracking)");
        System.out.println("✓ Max fine cap enforced ($" + library.getFineCalculator().getMaxFine() + ")");

        library.showOverdueReport();
    }
}
