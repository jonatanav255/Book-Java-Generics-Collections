# Method Chaining in Java - Complete Guide

## Table of Contents
1. [What is Method Chaining?](#what-is-method-chaining)
2. [Basic Method Chaining](#basic-method-chaining)
3. [Fluent Interface Pattern](#fluent-interface-pattern)
4. [Builder Pattern](#builder-pattern)
5. [Real-World Examples](#real-world-examples)
6. [Best Practices](#best-practices)
7. [Common Pitfalls](#common-pitfalls)

---

## What is Method Chaining?

**Method chaining** is calling multiple methods in a single statement by calling each method on the result of the previous method.

### Simple Example

```java
// Without chaining (multiple statements)
String text = "  hello world  ";
String trimmed = text.trim();
String upper = trimmed.toUpperCase();
String result = upper.replace("WORLD", "JAVA");
System.out.println(result);  // "HELLO JAVA"

// With chaining (one statement)
String result = "  hello world  "
    .trim()
    .toUpperCase()
    .replace("WORLD", "JAVA");
System.out.println(result);  // "HELLO JAVA"
```

**How it works:**
1. `"  hello world  ".trim()` → returns `"hello world"` (String)
2. `"hello world".toUpperCase()` → returns `"HELLO WORLD"` (String)
3. `"HELLO WORLD".replace("WORLD", "JAVA")` → returns `"HELLO JAVA"` (String)

Each method returns a String, so you can call another String method on it!

---

## Basic Method Chaining

### How Method Chaining Works

```java
object.methodA().methodB().methodC()
//     ^^^^^^^^  ^^^^^^^^  ^^^^^^^^
//     |         |         |
//     Returns   Returns   Returns
//     Type1     Type2     Type3
```

**Key requirement:** Each method must return an object (not `void`)

### Example with Built-in Java Classes

#### String Methods
```java
String name = "  john doe  ";

// Chain multiple String methods
String formatted = name.trim()           // "john doe"
                       .toUpperCase()    // "JOHN DOE"
                       .replace(" ", "_"); // "JOHN_DOE"

System.out.println(formatted);  // "JOHN_DOE"

// Check condition in chain
boolean hasJava = "Hello Java World"
    .toLowerCase()        // "hello java world"
    .contains("java");    // true

// Get length after transformations
int length = "  spaces  "
    .trim()              // "spaces"
    .length();           // 6
```

#### StringBuilder Methods
```java
// StringBuilder methods return 'this' for chaining
String result = new StringBuilder()
    .append("Hello")
    .append(" ")
    .append("World")
    .append("!")
    .toString();

System.out.println(result);  // "Hello World!"
```

#### List Methods (Java 8+ Streams)
```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// Chain stream operations
int sum = numbers.stream()           // Stream<Integer>
                 .filter(n -> n % 2 == 0)  // Stream<Integer> (even numbers)
                 .map(n -> n * 2)          // Stream<Integer> (doubled)
                 .reduce(0, Integer::sum);  // int (sum)

System.out.println(sum);  // 60 (2+4+6+8+10 doubled = 4+8+12+16+20)
```

### Example: Your Library Code

```java
// From your Library class
public int getAvailableCount() {
    return getAvailableBooks().size();
    //     ^^^^^^^^^^^^^^^^^^  ^^^^^^
    //     Returns List<Book>  Called on List<Book>, returns int
}

// More complex chaining
String firstAvailableTitle = library.getAvailableBooks()  // Returns List<Book>
                                    .get(0)                // Returns Book
                                    .getTitle()            // Returns String
                                    .toUpperCase();        // Returns String

// Breaking it down:
// Step 1: library.getAvailableBooks() → List<Book>
// Step 2: .get(0) → Book (first book)
// Step 3: .getTitle() → String (book's title)
// Step 4: .toUpperCase() → String (uppercase title)
```

---

## Fluent Interface Pattern

A **Fluent Interface** is a design where methods return `this` (the object itself) to enable chaining.

### Creating a Fluent Interface

```java
public class Person {
    private String name;
    private int age;
    private String city;

    // Traditional setters (can't chain)
    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    // Fluent setters (return 'this' for chaining)
    public Person withName(String name) {
        this.name = name;
        return this;  // Return the object itself
    }

    public Person withAge(int age) {
        this.age = age;
        return this;  // Return the object itself
    }

    public Person withCity(String city) {
        this.city = city;
        return this;  // Return the object itself
    }

    @Override
    public String toString() {
        return name + ", " + age + ", " + city;
    }
}

// Usage:

// Traditional way (no chaining)
Person person1 = new Person();
person1.setName("Alice");
person1.setAge(25);
// Can't chain!

// Fluent way (with chaining)
Person person2 = new Person()
    .withName("Bob")
    .withAge(30)
    .withCity("New York");

System.out.println(person2);  // "Bob, 30, New York"
```

**How it works:**
- Each `withXxx()` method modifies the object AND returns `this`
- Returning `this` allows the next method to be called on the same object

### Real Example: Query Builder

```java
public class Query {
    private String table;
    private List<String> columns = new ArrayList<>();
    private String whereClause;
    private String orderBy;
    private int limit;

    public Query from(String table) {
        this.table = table;
        return this;
    }

    public Query select(String... columns) {
        this.columns.addAll(Arrays.asList(columns));
        return this;
    }

    public Query where(String condition) {
        this.whereClause = condition;
        return this;
    }

    public Query orderBy(String field) {
        this.orderBy = field;
        return this;
    }

    public Query limit(int count) {
        this.limit = count;
        return this;
    }

    public String build() {
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(columns.isEmpty() ? "*" : String.join(", ", columns));
        sql.append(" FROM ").append(table);
        if (whereClause != null) {
            sql.append(" WHERE ").append(whereClause);
        }
        if (orderBy != null) {
            sql.append(" ORDER BY ").append(orderBy);
        }
        if (limit > 0) {
            sql.append(" LIMIT ").append(limit);
        }
        return sql.toString();
    }
}

// Usage - very readable!
String sql = new Query()
    .select("name", "age")
    .from("users")
    .where("age > 18")
    .orderBy("name")
    .limit(10)
    .build();

System.out.println(sql);
// SELECT name, age FROM users WHERE age > 18 ORDER BY name LIMIT 10
```

---

## Builder Pattern

The **Builder Pattern** uses method chaining to construct complex objects step by step.

### Basic Builder Example

```java
public class Pizza {
    private String size;
    private boolean cheese;
    private boolean pepperoni;
    private boolean bacon;
    private boolean mushrooms;

    // Private constructor - can only be created via Builder
    private Pizza(Builder builder) {
        this.size = builder.size;
        this.cheese = builder.cheese;
        this.pepperoni = builder.pepperoni;
        this.bacon = builder.bacon;
        this.mushrooms = builder.mushrooms;
    }

    // Static nested Builder class
    public static class Builder {
        private String size;
        private boolean cheese = false;
        private boolean pepperoni = false;
        private boolean bacon = false;
        private boolean mushrooms = false;

        public Builder(String size) {
            this.size = size;
        }

        public Builder cheese() {
            this.cheese = true;
            return this;
        }

        public Builder pepperoni() {
            this.pepperoni = true;
            return this;
        }

        public Builder bacon() {
            this.bacon = true;
            return this;
        }

        public Builder mushrooms() {
            this.mushrooms = true;
            return this;
        }

        public Pizza build() {
            return new Pizza(this);
        }
    }

    @Override
    public String toString() {
        return size + " pizza with: " +
               (cheese ? "cheese " : "") +
               (pepperoni ? "pepperoni " : "") +
               (bacon ? "bacon " : "") +
               (mushrooms ? "mushrooms" : "");
    }
}

// Usage:
Pizza pizza = new Pizza.Builder("Large")
    .cheese()
    .pepperoni()
    .bacon()
    .build();

System.out.println(pizza);
// "Large pizza with: cheese pepperoni bacon"

// Can customize each pizza differently
Pizza veggiePizza = new Pizza.Builder("Medium")
    .cheese()
    .mushrooms()
    .build();
```

### Advanced Builder with Validation

```java
public class User {
    private final String username;     // Required
    private final String email;        // Required
    private final String firstName;    // Optional
    private final String lastName;     // Optional
    private final int age;             // Optional

    private User(Builder builder) {
        this.username = builder.username;
        this.email = builder.email;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.age = builder.age;
    }

    public static class Builder {
        // Required parameters
        private final String username;
        private final String email;

        // Optional parameters
        private String firstName = "";
        private String lastName = "";
        private int age = 0;

        // Constructor for required fields
        public Builder(String username, String email) {
            if (username == null || email == null) {
                throw new IllegalArgumentException("Username and email are required");
            }
            this.username = username;
            this.email = email;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder age(int age) {
            if (age < 0 || age > 150) {
                throw new IllegalArgumentException("Invalid age");
            }
            this.age = age;
            return this;
        }

        public User build() {
            // Additional validation before building
            if (!email.contains("@")) {
                throw new IllegalArgumentException("Invalid email");
            }
            return new User(this);
        }
    }

    @Override
    public String toString() {
        return "User{" +
               "username='" + username + '\'' +
               ", email='" + email + '\'' +
               ", firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", age=" + age +
               '}';
    }
}

// Usage:
User user = new User.Builder("john_doe", "john@example.com")
    .firstName("John")
    .lastName("Doe")
    .age(30)
    .build();

System.out.println(user);

// Can omit optional parameters
User simpleUser = new User.Builder("jane", "jane@example.com")
    .build();
```

---

## Real-World Examples

### 1. Java Streams (Very Common!)

```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David", "Eve");

// Complex chaining with streams
List<String> result = names.stream()                    // Stream<String>
    .filter(name -> name.length() > 3)                  // Stream<String>
    .map(String::toUpperCase)                           // Stream<String>
    .sorted()                                           // Stream<String>
    .limit(3)                                           // Stream<String>
    .collect(Collectors.toList());                      // List<String>

System.out.println(result);  // [ALICE, CHARLIE, DAVID]

// Each method returns a Stream, allowing chaining
```

### 2. Optional Chaining (Java 8+)

```java
public class Address {
    private String city;

    public Address(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }
}

public class Person {
    private String name;
    private Address address;

    public Person(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    public Optional<Address> getAddress() {
        return Optional.ofNullable(address);
    }
}

// Chaining with Optional to avoid null checks
Person person = new Person("Alice", new Address("New York"));

String city = person.getAddress()                  // Optional<Address>
                    .map(Address::getCity)          // Optional<String>
                    .orElse("Unknown");             // String

System.out.println(city);  // "New York"

// Without chaining (traditional way with null checks)
String cityOldWay;
if (person.getAddress().isPresent()) {
    cityOldWay = person.getAddress().get().getCity();
} else {
    cityOldWay = "Unknown";
}
```

### 3. StringBuilder/StringBuffer

```java
// Common pattern for building strings
String html = new StringBuilder()
    .append("<html>")
    .append("<body>")
    .append("<h1>Hello World</h1>")
    .append("</body>")
    .append("</html>")
    .toString();

System.out.println(html);
// <html><body><h1>Hello World</h1></body></html>
```

### 4. Date/Time API (Java 8+)

```java
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// Chaining date operations
LocalDate futureDate = LocalDate.now()           // LocalDate
    .plusDays(7)                                 // LocalDate
    .plusMonths(2)                               // LocalDate
    .plusYears(1);                               // LocalDate

System.out.println(futureDate);

// Chaining with formatting
String formatted = LocalDate.now()
    .plusDays(10)
    .format(DateTimeFormatter.ISO_DATE);

System.out.println(formatted);  // "2025-12-15"
```

### 5. Collections (Java 9+)

```java
// Creating immutable collections with chaining
List<String> list = List.of("A", "B", "C");

Set<Integer> set = Set.of(1, 2, 3, 4, 5);

Map<String, Integer> map = Map.of(
    "one", 1,
    "two", 2,
    "three", 3
);
```

### 6. HTTP Client (Java 11+)

```java
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

// Chaining to build HTTP requests
HttpClient client = HttpClient.newBuilder()
    .version(HttpClient.Version.HTTP_2)
    .followRedirects(HttpClient.Redirect.NORMAL)
    .build();

HttpRequest request = HttpRequest.newBuilder()
    .uri(URI.create("https://api.example.com/data"))
    .header("Content-Type", "application/json")
    .GET()
    .build();
```

---

## Best Practices

### 1. Return 'this' for Fluent Interfaces

```java
// Good - enables chaining
public Person setName(String name) {
    this.name = name;
    return this;  // Return 'this'
}

// Bad - can't chain
public void setName(String name) {
    this.name = name;
    // Returns nothing (void)
}
```

### 2. Use Descriptive Method Names

```java
// Good - clear and readable
User user = new User.Builder("john", "john@example.com")
    .withFirstName("John")
    .withLastName("Doe")
    .withAge(30)
    .build();

// Also good - concise when context is clear
Pizza pizza = new Pizza.Builder("Large")
    .cheese()
    .pepperoni()
    .build();
```

### 3. Format Long Chains for Readability

```java
// Good - each method on new line
String result = text
    .trim()
    .toLowerCase()
    .replace(" ", "_")
    .substring(0, 10);

// Bad - hard to read
String result = text.trim().toLowerCase().replace(" ", "_").substring(0, 10);
```

### 4. Use Final Build() Method for Builders

```java
public class Configuration {
    private final String host;
    private final int port;

    private Configuration(Builder builder) {
        this.host = builder.host;
        this.port = builder.port;
    }

    public static class Builder {
        private String host = "localhost";
        private int port = 8080;

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public Configuration build() {
            // Validate and build
            if (port < 0 || port > 65535) {
                throw new IllegalArgumentException("Invalid port");
            }
            return new Configuration(this);
        }
    }
}
```

### 5. Make Objects Immutable When Using Builders

```java
public class ImmutablePerson {
    private final String name;  // final - can't change
    private final int age;      // final - can't change

    private ImmutablePerson(Builder builder) {
        this.name = builder.name;
        this.age = builder.age;
    }

    // No setters - object is immutable after creation

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public static class Builder {
        private String name;
        private int age;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder age(int age) {
            this.age = age;
            return this;
        }

        public ImmutablePerson build() {
            return new ImmutablePerson(this);
        }
    }
}
```

---

## Common Pitfalls

### 1. NullPointerException in Chains

```java
// Dangerous - if any method returns null, chain breaks
String result = getText()        // Could return null!
    .trim()                      // NullPointerException!
    .toUpperCase();

// Safe - check for null
String text = getText();
String result = text != null ? text.trim().toUpperCase() : "";

// Or use Optional
String result = Optional.ofNullable(getText())
    .map(String::trim)
    .map(String::toUpperCase)
    .orElse("");
```

### 2. Side Effects in Chains

```java
// Bad - modifying external state in chain
int sum = numbers.stream()
    .peek(n -> total += n)    // Side effect! Modifying external variable
    .count();

// Good - use proper reduction
int sum = numbers.stream()
    .reduce(0, Integer::sum);
```

### 3. Overly Long Chains

```java
// Bad - too long, hard to debug
String result = input.trim().toLowerCase().replace("a", "b")
    .replace("c", "d").replace("e", "f").substring(0, 10)
    .toUpperCase().concat("_SUFFIX").repeat(3);

// Good - break into logical steps
String cleaned = input.trim().toLowerCase();
String replaced = cleaned.replace("a", "b").replace("c", "d").replace("e", "f");
String result = replaced.substring(0, 10).toUpperCase().concat("_SUFFIX");
```

### 4. Forgetting to Call build()

```java
// Wrong - builder not executed!
Pizza.Builder builder = new Pizza.Builder("Large")
    .cheese()
    .pepperoni();
// Nothing created! builder is not a Pizza

// Correct - call build()
Pizza pizza = new Pizza.Builder("Large")
    .cheese()
    .pepperoni()
    .build();  // ← Don't forget this!
```

---

## Complete Example: Library Book Finder

```java
public class BookFinder {
    private List<Book> books;
    private String titleFilter;
    private String authorFilter;
    private Integer minYear;
    private Integer maxYear;
    private Boolean onlyAvailable;

    public BookFinder(List<Book> books) {
        this.books = books;
    }

    public BookFinder withTitle(String title) {
        this.titleFilter = title;
        return this;
    }

    public BookFinder byAuthor(String author) {
        this.authorFilter = author;
        return this;
    }

    public BookFinder publishedAfter(int year) {
        this.minYear = year;
        return this;
    }

    public BookFinder publishedBefore(int year) {
        this.maxYear = year;
        return this;
    }

    public BookFinder onlyAvailable() {
        this.onlyAvailable = true;
        return this;
    }

    public List<Book> find() {
        List<Book> results = new ArrayList<>(books);

        if (titleFilter != null) {
            results = results.stream()
                .filter(b -> b.getTitle().toLowerCase().contains(titleFilter.toLowerCase()))
                .collect(Collectors.toList());
        }

        if (authorFilter != null) {
            results = results.stream()
                .filter(b -> b.getAuthor().equalsIgnoreCase(authorFilter))
                .collect(Collectors.toList());
        }

        if (minYear != null) {
            results = results.stream()
                .filter(b -> b.getYear() >= minYear)
                .collect(Collectors.toList());
        }

        if (maxYear != null) {
            results = results.stream()
                .filter(b -> b.getYear() <= maxYear)
                .collect(Collectors.toList());
        }

        if (Boolean.TRUE.equals(onlyAvailable)) {
            results = results.stream()
                .filter(b -> !b.isBorrowed())
                .collect(Collectors.toList());
        }

        return results;
    }
}

// Usage - very fluent and readable!
List<Book> classicBooks = new BookFinder(library.getAllBooks())
    .publishedBefore(1950)
    .onlyAvailable()
    .find();

List<Book> orwellBooks = new BookFinder(library.getAllBooks())
    .byAuthor("George Orwell")
    .publishedAfter(1940)
    .find();
```

---

## Summary

### Key Takeaways

1. **Method chaining** = calling methods on the results of previous methods
2. **Fluent interfaces** = methods return `this` to enable chaining
3. **Builder pattern** = uses chaining to construct complex objects
4. **Benefits:**
   - More readable code
   - Less temporary variables
   - Cleaner API design
5. **Watch out for:**
   - NullPointerExceptions in chains
   - Chains that are too long
   - Missing `build()` calls

### When to Use Method Chaining

✓ Building complex objects (Builder pattern)
✓ Configuring objects (Fluent interface)
✓ Stream processing
✓ String manipulation
✓ Query building
✓ When it improves readability

✗ When methods have side effects
✗ When chains become too long
✗ When error handling is complex
✗ When it reduces clarity

---

This guide covers everything you need to know about method chaining in Java!
