# Java Streams Complete Reference Guide

## Table of Contents
1. [What are Streams?](#what-are-streams)
2. [Creating Streams](#creating-streams)
3. [Intermediate Operations](#intermediate-operations)
4. [Terminal Operations](#terminal-operations)
5. [Collectors](#collectors)
6. [Primitive Streams](#primitive-streams)
7. [Advanced Patterns](#advanced-patterns)
8. [Best Practices](#best-practices)
9. [Common Pitfalls](#common-pitfalls)

---

## What are Streams?

### Definition

A **Stream** is a sequence of elements that supports sequential and parallel operations to process collections in a functional style.

**Key Point:** Streams DON'T store data - they process data from a source (like a collection).

### Before Streams (Traditional Way)

```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David", "Eve");

// Filter names starting with 'A' and convert to uppercase
List<String> result = new ArrayList<>();
for (String name : names) {
    if (name.startsWith("A")) {
        result.add(name.toUpperCase());
    }
}
System.out.println(result);  // [ALICE]
```

### With Streams (Modern Way)

```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David", "Eve");

List<String> result = names.stream()           // Create stream
    .filter(name -> name.startsWith("A"))      // Filter
    .map(String::toUpperCase)                  // Transform
    .collect(Collectors.toList());             // Collect results

System.out.println(result);  // [ALICE]
```

### Stream Characteristics

1. **Not a data structure** - Just a view of data
2. **Lazy** - Operations don't execute until terminal operation
3. **Consumable** - Can only be used once
4. **Functional** - Doesn't modify the source
5. **Chainable** - Operations can be chained

---

## Creating Streams

### From Collections

```java
List<String> list = Arrays.asList("A", "B", "C");
Stream<String> stream = list.stream();

Set<Integer> set = new HashSet<>(Arrays.asList(1, 2, 3));
Stream<Integer> stream2 = set.stream();

// Parallel stream (for multi-threading)
Stream<String> parallelStream = list.parallelStream();
```

### From Arrays

```java
String[] array = {"A", "B", "C"};

// Method 1: Arrays.stream()
Stream<String> stream1 = Arrays.stream(array);

// Method 2: Stream.of()
Stream<String> stream2 = Stream.of("A", "B", "C");

// From array section
Stream<String> stream3 = Arrays.stream(array, 0, 2); // [A, B]
```

### From Values

```java
// Empty stream
Stream<String> empty = Stream.empty();

// Single value
Stream<String> single = Stream.of("Hello");

// Multiple values
Stream<Integer> numbers = Stream.of(1, 2, 3, 4, 5);

// Nullable stream (Java 9+)
Stream<String> nullable = Stream.ofNullable(null); // Empty stream
Stream<String> notNull = Stream.ofNullable("Hello"); // Stream with "Hello"
```

### Generate Streams

```java
// Infinite stream with supplier
Stream<Double> randoms = Stream.generate(Math::random);
Stream<String> hellos = Stream.generate(() -> "Hello");

// Infinite stream with iteration
Stream<Integer> evens = Stream.iterate(0, n -> n + 2);  // 0, 2, 4, 6...
Stream<Integer> powers = Stream.iterate(1, n -> n * 2); // 1, 2, 4, 8...

// Finite stream with predicate (Java 9+)
Stream<Integer> finiteEvens = Stream.iterate(0, n -> n < 100, n -> n + 2);

// Range streams (primitives - see Primitive Streams section)
IntStream.range(1, 5);        // 1, 2, 3, 4 (exclusive end)
IntStream.rangeClosed(1, 5);  // 1, 2, 3, 4, 5 (inclusive end)
```

### From Files

```java
// Read lines from file
try (Stream<String> lines = Files.lines(Paths.get("file.txt"))) {
    lines.forEach(System.out::println);
}

// List files in directory
try (Stream<Path> paths = Files.list(Paths.get("."))) {
    paths.forEach(System.out::println);
}
```

### From Strings

```java
String text = "Hello";

// Stream of characters
IntStream chars = text.chars();  // IntStream: 72, 101, 108, 108, 111

// Convert to Character stream
Stream<Character> charStream = text.chars()
    .mapToObj(c -> (char) c);

// Split and stream
Stream<String> words = Arrays.stream("Hello World Java".split(" "));
```

---

## Intermediate Operations

**Intermediate operations** return a Stream and are **lazy** (don't execute until terminal operation).

### filter() - Select Elements

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// Filter even numbers
List<Integer> evens = numbers.stream()
    .filter(n -> n % 2 == 0)
    .collect(Collectors.toList());
// Result: [2, 4, 6, 8, 10]

// Filter with multiple conditions
List<Integer> result = numbers.stream()
    .filter(n -> n > 3)
    .filter(n -> n < 8)
    .collect(Collectors.toList());
// Result: [4, 5, 6, 7]

// Filter non-null
List<String> names = Arrays.asList("Alice", null, "Bob", null, "Charlie");
List<String> nonNull = names.stream()
    .filter(name -> name != null)
    .collect(Collectors.toList());
// Result: [Alice, Bob, Charlie]
```

### map() - Transform Elements

```java
List<String> names = Arrays.asList("alice", "bob", "charlie");

// Transform to uppercase
List<String> upper = names.stream()
    .map(String::toUpperCase)
    .collect(Collectors.toList());
// Result: [ALICE, BOB, CHARLIE]

// Transform to lengths
List<Integer> lengths = names.stream()
    .map(String::length)
    .collect(Collectors.toList());
// Result: [5, 3, 7]

// Transform objects
List<Book> books = getBooks();
List<String> titles = books.stream()
    .map(Book::getTitle)
    .collect(Collectors.toList());
```

### flatMap() - Flatten Nested Structures

```java
// List of lists
List<List<Integer>> nested = Arrays.asList(
    Arrays.asList(1, 2),
    Arrays.asList(3, 4),
    Arrays.asList(5, 6)
);

// Flatten to single list
List<Integer> flat = nested.stream()
    .flatMap(list -> list.stream())
    .collect(Collectors.toList());
// Result: [1, 2, 3, 4, 5, 6]

// Split strings and flatten
List<String> sentences = Arrays.asList("Hello World", "Java Streams");
List<String> words = sentences.stream()
    .flatMap(s -> Arrays.stream(s.split(" ")))
    .collect(Collectors.toList());
// Result: [Hello, World, Java, Streams]

// Books with multiple authors
List<Book> books = getBooks();
List<String> allAuthors = books.stream()
    .flatMap(book -> book.getAuthors().stream())
    .distinct()
    .collect(Collectors.toList());
```

### distinct() - Remove Duplicates

```java
List<Integer> numbers = Arrays.asList(1, 2, 2, 3, 3, 3, 4, 5, 5);

List<Integer> unique = numbers.stream()
    .distinct()
    .collect(Collectors.toList());
// Result: [1, 2, 3, 4, 5]

// Case-insensitive distinct
List<String> names = Arrays.asList("Alice", "alice", "Bob", "BOB");
List<String> uniqueNames = names.stream()
    .map(String::toLowerCase)
    .distinct()
    .collect(Collectors.toList());
// Result: [alice, bob]
```

### sorted() - Sort Elements

```java
List<Integer> numbers = Arrays.asList(5, 2, 8, 1, 9, 3);

// Natural order
List<Integer> sorted = numbers.stream()
    .sorted()
    .collect(Collectors.toList());
// Result: [1, 2, 3, 5, 8, 9]

// Reverse order
List<Integer> reversed = numbers.stream()
    .sorted(Comparator.reverseOrder())
    .collect(Collectors.toList());
// Result: [9, 8, 5, 3, 2, 1]

// Custom comparator
List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David");
List<String> byLength = names.stream()
    .sorted(Comparator.comparing(String::length))
    .collect(Collectors.toList());
// Result: [Bob, Alice, David, Charlie]

// Multiple criteria
List<Book> books = getBooks();
List<Book> sorted = books.stream()
    .sorted(Comparator.comparing(Book::getYear)
                      .thenComparing(Book::getTitle))
    .collect(Collectors.toList());
```

### peek() - Debug/Side Effects

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

// Debug intermediate results
List<Integer> result = numbers.stream()
    .peek(n -> System.out.println("Original: " + n))
    .map(n -> n * 2)
    .peek(n -> System.out.println("Doubled: " + n))
    .filter(n -> n > 5)
    .peek(n -> System.out.println("Filtered: " + n))
    .collect(Collectors.toList());
```

### limit() - Take First N Elements

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

List<Integer> first3 = numbers.stream()
    .limit(3)
    .collect(Collectors.toList());
// Result: [1, 2, 3]

// With infinite stream
List<Integer> first10Evens = Stream.iterate(0, n -> n + 2)
    .limit(10)
    .collect(Collectors.toList());
// Result: [0, 2, 4, 6, 8, 10, 12, 14, 16, 18]
```

### skip() - Skip First N Elements

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

List<Integer> afterFirst3 = numbers.stream()
    .skip(3)
    .collect(Collectors.toList());
// Result: [4, 5, 6, 7, 8, 9, 10]

// Pagination: skip 10, take 5
List<Integer> page = numbers.stream()
    .skip(10)
    .limit(5)
    .collect(Collectors.toList());
```

### takeWhile() & dropWhile() (Java 9+)

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 4, 3, 2, 1);

// Take while condition is true
List<Integer> taken = numbers.stream()
    .takeWhile(n -> n < 4)
    .collect(Collectors.toList());
// Result: [1, 2, 3] (stops at first 4)

// Drop while condition is true
List<Integer> dropped = numbers.stream()
    .dropWhile(n -> n < 4)
    .collect(Collectors.toList());
// Result: [4, 5, 4, 3, 2, 1] (starts from first 4)
```

---

## Terminal Operations

**Terminal operations** trigger stream processing and produce a result. After this, the stream is consumed.

### forEach() - Iterate Elements

```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

// Print each
names.stream()
    .forEach(System.out::println);

// With operation
names.stream()
    .forEach(name -> System.out.println("Hello, " + name));

// forEachOrdered() - maintains order (useful with parallel streams)
names.parallelStream()
    .forEachOrdered(System.out::println);
```

### collect() - Accumulate to Collection

```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

// To List
List<String> list = names.stream()
    .collect(Collectors.toList());

// To Set
Set<String> set = names.stream()
    .collect(Collectors.toSet());

// To specific collection
ArrayList<String> arrayList = names.stream()
    .collect(Collectors.toCollection(ArrayList::new));

LinkedList<String> linkedList = names.stream()
    .collect(Collectors.toCollection(LinkedList::new));

TreeSet<String> treeSet = names.stream()
    .collect(Collectors.toCollection(TreeSet::new));
```

### toArray() - Convert to Array

```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

// Object array
Object[] objectArray = names.stream()
    .toArray();

// Typed array
String[] stringArray = names.stream()
    .toArray(String[]::new);

// With transformation
Integer[] lengths = names.stream()
    .map(String::length)
    .toArray(Integer[]::new);
```

### count() - Count Elements

```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David");

long count = names.stream()
    .count();
// Result: 4

// Count with filter
long longNames = names.stream()
    .filter(name -> name.length() > 4)
    .count();
// Result: 2 (Alice, Charlie)
```

### min() & max() - Find Minimum/Maximum

```java
List<Integer> numbers = Arrays.asList(5, 2, 8, 1, 9, 3);

// Min
Optional<Integer> min = numbers.stream()
    .min(Integer::compareTo);
System.out.println(min.get());  // 1

// Max
Optional<Integer> max = numbers.stream()
    .max(Integer::compareTo);
System.out.println(max.get());  // 9

// Max by property
List<Book> books = getBooks();
Optional<Book> mostRecent = books.stream()
    .max(Comparator.comparing(Book::getYear));
```

### findFirst() & findAny()

```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David");

// Find first
Optional<String> first = names.stream()
    .filter(name -> name.startsWith("C"))
    .findFirst();
System.out.println(first.get());  // "Charlie"

// Find any (useful with parallel streams)
Optional<String> any = names.parallelStream()
    .filter(name -> name.length() > 3)
    .findAny();
```

### anyMatch(), allMatch(), noneMatch()

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

// Any match
boolean hasEven = numbers.stream()
    .anyMatch(n -> n % 2 == 0);
// Result: true

// All match
boolean allPositive = numbers.stream()
    .allMatch(n -> n > 0);
// Result: true

// None match
boolean noNegative = numbers.stream()
    .noneMatch(n -> n < 0);
// Result: true
```

### reduce() - Combine Elements

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

// Sum
int sum = numbers.stream()
    .reduce(0, (a, b) -> a + b);
// Result: 15

// Product
int product = numbers.stream()
    .reduce(1, (a, b) -> a * b);
// Result: 120

// Max
Optional<Integer> max = numbers.stream()
    .reduce((a, b) -> a > b ? a : b);

// Concatenate strings
List<String> words = Arrays.asList("Java", "is", "awesome");
String sentence = words.stream()
    .reduce("", (a, b) -> a + " " + b);
// Result: " Java is awesome"

// Better with initial value
String sentence2 = words.stream()
    .reduce("Result:", (a, b) -> a + " " + b);
// Result: "Result: Java is awesome"
```

---

## Collectors

`Collectors` provide pre-built reduction operations.

### Basic Collectors

```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David", "Eve");

// To List
List<String> list = names.stream()
    .collect(Collectors.toList());

// To Set
Set<String> set = names.stream()
    .collect(Collectors.toSet());

// To Map
List<Book> books = getBooks();
Map<String, Book> byIsbn = books.stream()
    .collect(Collectors.toMap(Book::getIsbn, book -> book));

// To Map with duplicate key handling
Map<String, Book> byTitle = books.stream()
    .collect(Collectors.toMap(
        Book::getTitle,
        book -> book,
        (existing, replacement) -> existing  // Keep first on collision
    ));
```

### Joining Strings

```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

// Simple join
String joined = names.stream()
    .collect(Collectors.joining());
// Result: "AliceBobCharlie"

// With delimiter
String withComma = names.stream()
    .collect(Collectors.joining(", "));
// Result: "Alice, Bob, Charlie"

// With prefix and suffix
String formatted = names.stream()
    .collect(Collectors.joining(", ", "[", "]"));
// Result: "[Alice, Bob, Charlie]"
```

### Grouping

```java
List<Book> books = getBooks();

// Group by category
Map<Category, List<Book>> byCategory = books.stream()
    .collect(Collectors.groupingBy(Book::getCategory));

// Group by year
Map<Integer, List<Book>> byYear = books.stream()
    .collect(Collectors.groupingBy(Book::getYear));

// Group with counting
Map<Category, Long> countByCategory = books.stream()
    .collect(Collectors.groupingBy(
        Book::getCategory,
        Collectors.counting()
    ));

// Group with custom downstream collector
Map<Category, List<String>> titlesByCategory = books.stream()
    .collect(Collectors.groupingBy(
        Book::getCategory,
        Collectors.mapping(Book::getTitle, Collectors.toList())
    ));
```

### Partitioning

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// Partition by even/odd
Map<Boolean, List<Integer>> evenOdd = numbers.stream()
    .collect(Collectors.partitioningBy(n -> n % 2 == 0));
// Result: {false=[1,3,5,7,9], true=[2,4,6,8,10]}

List<Integer> evens = evenOdd.get(true);
List<Integer> odds = evenOdd.get(false);

// Partition books by availability
List<Book> books = getBooks();
Map<Boolean, List<Book>> availability = books.stream()
    .collect(Collectors.partitioningBy(book -> !book.isBorrowed()));
```

### Summarizing

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

// Summary statistics
IntSummaryStatistics stats = numbers.stream()
    .collect(Collectors.summarizingInt(Integer::intValue));

System.out.println("Count: " + stats.getCount());      // 5
System.out.println("Sum: " + stats.getSum());          // 15
System.out.println("Min: " + stats.getMin());          // 1
System.out.println("Max: " + stats.getMax());          // 5
System.out.println("Average: " + stats.getAverage());  // 3.0

// For books
List<Book> books = getBooks();
DoubleSummaryStatistics ratingStats = books.stream()
    .collect(Collectors.summarizingDouble(Book::getRating));
```

### Averaging, Summing

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

// Average
Double avg = numbers.stream()
    .collect(Collectors.averagingInt(Integer::intValue));
// Result: 3.0

// Sum
Integer sum = numbers.stream()
    .collect(Collectors.summingInt(Integer::intValue));
// Result: 15

// For books
List<Book> books = getBooks();
Double avgRating = books.stream()
    .collect(Collectors.averagingDouble(Book::getRating));
```

---

## Primitive Streams

Special streams for `int`, `long`, and `double` to avoid boxing/unboxing.

### IntStream

```java
// Range (exclusive end)
IntStream.range(1, 5)           // 1, 2, 3, 4
    .forEach(System.out::println);

// Range (inclusive end)
IntStream.rangeClosed(1, 5)     // 1, 2, 3, 4, 5
    .forEach(System.out::println);

// Sum
int sum = IntStream.range(1, 11).sum();  // 55

// Average
OptionalDouble avg = IntStream.range(1, 11).average();

// Max/Min
OptionalInt max = IntStream.of(1, 5, 3, 9, 2).max();

// Convert to object stream
Stream<Integer> boxed = IntStream.range(1, 5).boxed();
```

### LongStream

```java
// Large ranges
LongStream.rangeClosed(1L, 1_000_000L)
    .parallel()
    .sum();

// Generate
LongStream.generate(() -> 100L)
    .limit(5)
    .forEach(System.out::println);
```

### DoubleStream

```java
// Random doubles
DoubleStream.generate(Math::random)
    .limit(5)
    .forEach(System.out::println);

// Average
OptionalDouble avg = DoubleStream.of(1.5, 2.5, 3.5).average();
```

### Converting Between Primitive Streams

```java
// IntStream to DoubleStream
DoubleStream doubles = IntStream.range(1, 5)
    .asDoubleStream();

// IntStream to LongStream
LongStream longs = IntStream.range(1, 5)
    .asLongStream();

// To object Stream
Stream<Integer> boxed = IntStream.range(1, 5)
    .boxed();

// From object Stream
IntStream ints = Stream.of(1, 2, 3, 4, 5)
    .mapToInt(Integer::intValue);
```

---

## Advanced Patterns

### Optional with Streams

```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

// Find first and process
names.stream()
    .filter(name -> name.startsWith("B"))
    .findFirst()
    .ifPresent(System.out::println);  // "Bob"

// Find first or default
String result = names.stream()
    .filter(name -> name.startsWith("Z"))
    .findFirst()
    .orElse("Not found");

// Find first or throw
String found = names.stream()
    .filter(name -> name.startsWith("A"))
    .findFirst()
    .orElseThrow(() -> new RuntimeException("Not found"));
```

### Parallel Streams

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// Parallel processing
int sum = numbers.parallelStream()
    .mapToInt(Integer::intValue)
    .sum();

// Convert to parallel
int sum2 = numbers.stream()
    .parallel()
    .mapToInt(Integer::intValue)
    .sum();

// Check if parallel
boolean isParallel = numbers.parallelStream().isParallel();  // true

// Convert back to sequential
numbers.parallelStream()
    .sequential()
    .forEach(System.out::println);
```

### Chaining Complex Operations

```java
List<Book> books = getBooks();

// Complex query
List<String> result = books.stream()
    .filter(book -> book.getYear() > 2000)           // Modern books
    .filter(book -> book.getRating() >= 4.0)         // Highly rated
    .filter(book -> !book.isBorrowed())              // Available
    .sorted(Comparator.comparing(Book::getRating)
                      .reversed())                   // Best first
    .limit(5)                                        // Top 5
    .map(Book::getTitle)                             // Get titles
    .collect(Collectors.toList());                   // To list
```

### FlatMap for Nested Collections

```java
// Books with multiple authors
class Book {
    private String title;
    private List<String> authors;
    // getters...
}

List<Book> books = getBooks();

// Get all unique authors
List<String> allAuthors = books.stream()
    .flatMap(book -> book.getAuthors().stream())
    .distinct()
    .sorted()
    .collect(Collectors.toList());

// Words from multiple sentences
List<String> sentences = Arrays.asList(
    "Java Streams are powerful",
    "Streams make code cleaner",
    "Learn Streams today"
);

List<String> uniqueWords = sentences.stream()
    .flatMap(sentence -> Arrays.stream(sentence.split(" ")))
    .map(String::toLowerCase)
    .distinct()
    .collect(Collectors.toList());
```

---

## Best Practices

### 1. Use Method References When Possible

```java
// Good - method reference
names.stream()
    .map(String::toUpperCase)
    .forEach(System.out::println);

// Less concise - lambda
names.stream()
    .map(name -> name.toUpperCase())
    .forEach(name -> System.out.println(name));
```

### 2. Don't Reuse Streams

```java
// Bad - stream already consumed
Stream<String> stream = names.stream();
stream.forEach(System.out::println);
stream.forEach(System.out::println);  // ❌ IllegalStateException!

// Good - create new stream
names.stream().forEach(System.out::println);
names.stream().forEach(System.out::println);  // ✓ OK
```

### 3. Use Appropriate Terminal Operations

```java
// Bad - collecting when not needed
names.stream()
    .filter(name -> name.length() > 3)
    .collect(Collectors.toList())
    .forEach(System.out::println);

// Good - forEach directly
names.stream()
    .filter(name -> name.length() > 3)
    .forEach(System.out::println);
```

### 4. Prefer Streams for Large Collections

```java
// Small list - traditional loop is fine
for (String name : smallList) {
    System.out.println(name);
}

// Large list - streams can be more efficient
largeList.stream()
    .filter(...)
    .map(...)
    .forEach(...);
```

### 5. Use Parallel Streams Carefully

```java
// Good - CPU-intensive operations on large data
List<Integer> result = hugelist.parallelStream()
    .map(this::expensiveOperation)
    .collect(Collectors.toList());

// Bad - small list or I/O operations
List<String> result = smallList.parallelStream()  // Overhead not worth it
    .map(...)
    .collect(Collectors.toList());
```

### 6. Chain Operations Logically

```java
// Good - filter before map (less work)
names.stream()
    .filter(name -> name.length() > 3)
    .map(String::toUpperCase)
    .collect(Collectors.toList());

// Less efficient - map before filter (more work)
names.stream()
    .map(String::toUpperCase)
    .filter(name -> name.length() > 3)
    .collect(Collectors.toList());
```

---

## Common Pitfalls

### 1. Modifying Source During Stream

```java
// Bad - modifying source
List<String> names = new ArrayList<>(Arrays.asList("Alice", "Bob", "Charlie"));
names.stream()
    .forEach(name -> names.remove(name));  // ❌ ConcurrentModificationException!

// Good - collect to new list
List<String> filtered = names.stream()
    .filter(name -> !name.equals("Bob"))
    .collect(Collectors.toList());
```

### 2. Null Pointer in Stream

```java
List<String> names = Arrays.asList("Alice", null, "Bob", null, "Charlie");

// Bad - NPE when mapping
names.stream()
    .map(String::toUpperCase)  // ❌ NullPointerException!
    .collect(Collectors.toList());

// Good - filter nulls first
names.stream()
    .filter(name -> name != null)
    .map(String::toUpperCase)
    .collect(Collectors.toList());
```

### 3. Incorrect Optional Handling

```java
// Bad - calling get() without checking
String name = names.stream()
    .filter(n -> n.startsWith("Z"))
    .findFirst()
    .get();  // ❌ NoSuchElementException if not found!

// Good - use orElse or ifPresent
String name = names.stream()
    .filter(n -> n.startsWith("Z"))
    .findFirst()
    .orElse("Not found");
```

### 4. Forgetting Terminal Operation

```java
// Bad - no terminal operation, nothing happens!
names.stream()
    .filter(name -> name.length() > 3)
    .map(String::toUpperCase);  // Stream created but never used

// Good - add terminal operation
names.stream()
    .filter(name -> name.length() > 3)
    .map(String::toUpperCase)
    .forEach(System.out::println);  // ✓ Actually executes
```

### 5. Expensive Operations in Streams

```java
// Bad - repeated expensive calls
books.stream()
    .filter(book -> expensiveCheck(book))
    .map(book -> expensiveTransform(book))
    .forEach(System.out::println);

// Good - cache results if possible
books.stream()
    .map(book -> new ProcessedBook(book, expensiveCheck(book)))
    .filter(ProcessedBook::passed)
    .forEach(System.out::println);
```

---

## Stream Cheat Sheet

### Common Patterns

```java
// Filter and collect
List<T> filtered = list.stream()
    .filter(predicate)
    .collect(Collectors.toList());

// Transform and collect
List<R> transformed = list.stream()
    .map(function)
    .collect(Collectors.toList());

// Filter + Transform + Collect
List<R> result = list.stream()
    .filter(predicate)
    .map(function)
    .collect(Collectors.toList());

// Count with condition
long count = list.stream()
    .filter(predicate)
    .count();

// Find first match
Optional<T> first = list.stream()
    .filter(predicate)
    .findFirst();

// Check if any/all/none match
boolean any = list.stream().anyMatch(predicate);
boolean all = list.stream().allMatch(predicate);
boolean none = list.stream().noneMatch(predicate);

// Sum/Average/Min/Max
int sum = list.stream().mapToInt(ToIntFunction).sum();
OptionalDouble avg = list.stream().mapToInt(ToIntFunction).average();
Optional<T> min = list.stream().min(Comparator);
Optional<T> max = list.stream().max(Comparator);

// Group by
Map<K, List<V>> grouped = list.stream()
    .collect(Collectors.groupingBy(classifier));

// Join strings
String joined = list.stream()
    .map(Object::toString)
    .collect(Collectors.joining(", "));
```

---

This guide covers everything you need to know about Java Streams!
