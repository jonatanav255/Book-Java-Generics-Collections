# Java Optional Reference Guide

## What is Optional?

`Optional<T>` is a container object that may or may not contain a non-null value. It helps avoid `NullPointerException` and makes your code more explicit about handling missing values.

**Introduced in:** Java 8

---

## Why Use Optional?

### Before Optional (The Problem)
```java
public Book findByTitle(String title) {
    for (Book book : books) {
        if (book.getTitle().equals(title)) {
            return book;
        }
    }
    return null;  // Caller must remember to check for null!
}

// Usage - easy to forget null check
Book book = findByTitle("1984");
book.borrowBook("Alice");  // NullPointerException if not found!
```

### After Optional (The Solution)
```java
public Optional<Book> findByTitle(String title) {
    return books.stream()
        .filter(book -> book.getTitle().equals(title))
        .findFirst();  // Returns Optional<Book>
}

// Usage - forces you to handle missing case
Optional<Book> bookOpt = findByTitle("1984");
if (bookOpt.isPresent()) {
    bookOpt.get().borrowBook("Alice");
}
```

---

## Creating Optionals

### 1. Optional.of() - Value must NOT be null
```java
Book book = new Book("1984", "Orwell", 1949, "123", Category.CLASSIC);
Optional<Book> opt = Optional.of(book);  // OK

Optional<Book> opt2 = Optional.of(null);  // NullPointerException!
```

### 2. Optional.ofNullable() - Value CAN be null
```java
Book book = findBookSomehow();  // might be null
Optional<Book> opt = Optional.ofNullable(book);  // OK even if null
```

### 3. Optional.empty() - Explicitly empty
```java
Optional<Book> opt = Optional.empty();  // Empty Optional
```

---

## Checking if Value Exists

### 1. isPresent() - Returns boolean
```java
Optional<Book> bookOpt = library.findByTitle("1984");

if (bookOpt.isPresent()) {
    System.out.println("Found: " + bookOpt.get());
} else {
    System.out.println("Not found");
}
```

### 2. isEmpty() - Opposite of isPresent() (Java 11+)
```java
if (bookOpt.isEmpty()) {
    System.out.println("Not found");
}
```

---

## Getting the Value

### 1. get() - Throws exception if empty
```java
Optional<Book> bookOpt = library.findByTitle("1984");
Book book = bookOpt.get();  // NoSuchElementException if empty!

// Only use after checking isPresent()
if (bookOpt.isPresent()) {
    Book book = bookOpt.get();  // Safe
}
```

### 2. orElse() - Return default value if empty
```java
Book book = bookOpt.orElse(defaultBook);

// Example from our library
Book book = library.findByTitle("Unknown")
    .orElse(new Book("N/A", "Unknown", 2000, "000", Category.FICTION));
```

### 3. orElseGet() - Supply default value lazily
```java
// orElse - always creates object even if not needed
Book book = bookOpt.orElse(createDefaultBook());  // createDefaultBook() always runs

// orElseGet - only creates if needed (better performance)
Book book = bookOpt.orElseGet(() -> createDefaultBook());  // runs only if empty
```

### 4. orElseThrow() - Throw exception if empty
```java
// Default exception (NoSuchElementException)
Book book = bookOpt.orElseThrow();

// Custom exception
Book book = bookOpt.orElseThrow(() ->
    new BookNotFoundException("Book not found"));
```

---

## Transforming Values

### 1. map() - Transform the value if present
```java
Optional<Book> bookOpt = library.findByTitle("1984");

// Get the title if book exists
Optional<String> titleOpt = bookOpt.map(Book::getTitle);
// or
Optional<String> titleOpt = bookOpt.map(book -> book.getTitle());

// Chain multiple transformations
Optional<Integer> yearOpt = bookOpt
    .map(Book::getYear)
    .map(year -> year + 10);  // Add 10 to year
```

### 2. flatMap() - Transform to another Optional (avoid Optional<Optional<T>>)
```java
public Optional<String> getBorrowerName(String bookTitle) {
    return library.findByTitle(bookTitle)
        .flatMap(book -> Optional.ofNullable(book.getBorrowedBy()));
}

// Without flatMap (wrong!)
Optional<Optional<String>> nested = library.findByTitle("1984")
    .map(book -> Optional.ofNullable(book.getBorrowedBy()));  // Nested!

// With flatMap (correct!)
Optional<String> flat = library.findByTitle("1984")
    .flatMap(book -> Optional.ofNullable(book.getBorrowedBy()));  // Flat!
```

### 3. filter() - Keep value only if matches condition
```java
Optional<Book> classicOpt = library.findByTitle("1984")
    .filter(book -> book.getCategory() == Category.CLASSIC);

// Chain filters
Optional<Book> popularClassicOpt = library.findByTitle("1984")
    .filter(book -> book.getCategory() == Category.CLASSIC)
    .filter(book -> book.getRating() >= 4.0);
```

---

## Consuming Values

### 1. ifPresent() - Do something if value exists
```java
library.findByTitle("1984")
    .ifPresent(book -> System.out.println("Found: " + book));

// More complex action
library.findByTitle("1984")
    .ifPresent(book -> {
        System.out.println("Found: " + book.getTitle());
        book.setRating(5.0);
    });
```

### 2. ifPresentOrElse() - Do one thing if present, another if empty (Java 9+)
```java
library.findByTitle("1984").ifPresentOrElse(
    book -> System.out.println("Found: " + book),      // if present
    () -> System.out.println("Book not found")         // if empty
);
```

---

## Real-World Examples from Our Library

### Example 1: Safe Book Borrowing
```java
// Library.java
public boolean borrowBook(String title, String personName, int daysToReturn) {
    Optional<Book> bookOpt = findByTitle(title);
    if (!bookOpt.isPresent()) {
        return false;  // Book not found
    }

    Book book = bookOpt.get();
    return book.borrowBook(personName, daysToReturn);
}
```

### Example 2: Using ifPresent()
```java
// LibraryApp.java
library.findByTitle("1984").ifPresent(book ->
    System.out.println("New status: " + book)
);
```

### Example 3: Chain Operations
```java
// Get book year, or 0 if not found
int year = library.findByTitle("1984")
    .map(Book::getYear)
    .orElse(0);

// Get borrower name, or "Not borrowed"
String borrower = library.findByTitle("1984")
    .map(Book::getBorrowedBy)
    .orElse("Not borrowed");
```

### Example 4: Filter and Transform
```java
// Find classic book with high rating
Optional<String> bestClassic = library.findByTitle("1984")
    .filter(book -> book.getCategory() == Category.CLASSIC)
    .filter(book -> book.getRating() >= 4.5)
    .map(Book::getTitle);

bestClassic.ifPresent(title ->
    System.out.println("Best classic: " + title)
);
```

---

## Common Patterns

### Pattern 1: Return Optional from methods that might not find results
```java
public Optional<Book> findByIsbn(String isbn) {
    return Optional.ofNullable(books.get(isbn));
}

public Optional<Book> findFirstAvailable() {
    return books.values().stream()
        .filter(book -> !book.isBorrowed())
        .findFirst();
}
```

### Pattern 2: Use orElseGet() for expensive defaults
```java
// BAD - creates object even if not needed
Book book = findByTitle("Unknown")
    .orElse(createComplexDefaultBook());

// GOOD - only creates if needed
Book book = findByTitle("Unknown")
    .orElseGet(() -> createComplexDefaultBook());
```

### Pattern 3: Chain with Streams
```java
List<String> borrowedBy = books.values().stream()
    .filter(Book::isBorrowed)
    .map(Book::getBorrowedBy)      // might return null
    .filter(Objects::nonNull)       // filter out nulls
    .collect(Collectors.toList());

// Better with Optional
List<String> borrowedBy = books.values().stream()
    .filter(Book::isBorrowed)
    .map(book -> Optional.ofNullable(book.getBorrowedBy()))
    .filter(Optional::isPresent)
    .map(Optional::get)
    .collect(Collectors.toList());
```

---

## Anti-Patterns (DON'T DO THIS!)

### ❌ Don't use get() without checking
```java
Optional<Book> bookOpt = findByTitle("Unknown");
Book book = bookOpt.get();  // CRASH if not found!
```

### ❌ Don't use Optional as field
```java
public class Book {
    private Optional<String> borrowedBy;  // NO! Use null instead
}
```

### ❌ Don't use Optional for collections
```java
// BAD
public Optional<List<Book>> getBooks() {
    return Optional.ofNullable(books);
}

// GOOD - return empty list instead
public List<Book> getBooks() {
    return books != null ? books : Collections.emptyList();
}
```

### ❌ Don't overuse Optional
```java
// BAD - too verbose for simple null check
Optional.ofNullable(title)
    .orElseThrow(() -> new IllegalArgumentException("Title required"));

// GOOD - simple null check is fine
if (title == null) {
    throw new IllegalArgumentException("Title required");
}
```

---

## Quick Reference Table

| Method | Returns | Use Case |
|--------|---------|----------|
| `Optional.of(value)` | Optional<T> | Create from non-null value |
| `Optional.ofNullable(value)` | Optional<T> | Create from nullable value |
| `Optional.empty()` | Optional<T> | Create empty Optional |
| `isPresent()` | boolean | Check if value exists |
| `isEmpty()` | boolean | Check if empty (Java 11+) |
| `get()` | T | Get value (throws if empty) |
| `orElse(default)` | T | Get value or default |
| `orElseGet(supplier)` | T | Get value or lazy default |
| `orElseThrow()` | T | Get value or throw |
| `ifPresent(consumer)` | void | Do action if present |
| `ifPresentOrElse(consumer, runnable)` | void | Do action if present, else other action |
| `map(function)` | Optional<U> | Transform value |
| `flatMap(function)` | Optional<U> | Transform to Optional (flatten) |
| `filter(predicate)` | Optional<T> | Keep only if matches |

---

## Summary

**Use Optional when:**
- Method might not find a result (search, find operations)
- Return value clearly communicates "might be absent"
- You want to chain operations safely

**Don't use Optional when:**
- As class fields (use null)
- For collections (return empty collection)
- Simple null checks are clearer

**Key Benefits:**
1. Makes "might be absent" explicit in API
2. Forces callers to handle missing case
3. Enables functional-style chaining
4. Prevents NullPointerException

**Remember:** Optional is about **API clarity**, not replacing all nulls!
