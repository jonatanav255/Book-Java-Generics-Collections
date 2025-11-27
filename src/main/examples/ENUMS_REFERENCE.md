# Java Enums Complete Reference Guide

## Table of Contents
1. [What are Enums?](#what-are-enums)
2. [Basic Enum Syntax](#basic-enum-syntax)
3. [Enum with Fields and Methods](#enum-with-fields-and-methods)
4. [Enum Constructors](#enum-constructors)
5. [Abstract Methods in Enums](#abstract-methods-in-enums)
6. [Built-in Enum Methods](#built-in-enum-methods)
7. [EnumSet and EnumMap](#enumset-and-enummap)
8. [Enum Best Practices](#enum-best-practices)
9. [Common Patterns](#common-patterns)
10. [Advanced Techniques](#advanced-techniques)

---

## What are Enums?

### Definition

An **enum** (enumeration) is a special data type that represents a fixed set of constants.

**Before Enums (Old way - BAD):**
```java
public class Day {
    public static final int MONDAY = 0;
    public static final int TUESDAY = 1;
    public static final int WEDNESDAY = 2;
    // ...
}

// Problems:
// - Not type-safe: can pass any int
// - No namespace: naming conflicts
// - No easy way to iterate all values
// - No meaningful toString()
```

**With Enums (Modern way - GOOD):**
```java
public enum Day {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}

// Benefits:
// ✓ Type-safe: only valid Day values
// ✓ Namespace: Day.MONDAY
// ✓ Can iterate: Day.values()
// ✓ Meaningful toString(): "MONDAY"
```

### Characteristics

- **Type-safe** constants
- **Singleton** - each constant is a unique instance
- **Implicitly** `public static final`
- **Cannot** be instantiated with `new`
- **Can** implement interfaces
- **Cannot** extend other classes (already extends `java.lang.Enum`)
- **Can** have fields, methods, and constructors
- **Can** override methods for specific constants

---

## Basic Enum Syntax

### Simple Enum Declaration

```java
public enum Day {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}
```

**Breaking it down:**

- `public enum Day` - Enum declaration
  - `enum` - Keyword (like `class` or `interface`)
  - `Day` - Enum name (follows class naming conventions)
- `MONDAY, TUESDAY, ...` - **Enum constants**
  - By convention: ALL_CAPS
  - Comma-separated
  - Each is an instance of `Day`
  - Implicitly `public static final`

### Using Enums

```java
// Declaration
Day today = Day.MONDAY;

// Comparison (use ==, not equals)
if (today == Day.MONDAY) {
    System.out.println("Start of the week!");
}

// Switch statement (very common pattern)
switch (today) {
    case MONDAY:
        System.out.println("Monday blues");
        break;
    case FRIDAY:
        System.out.println("TGIF!");
        break;
    case SATURDAY:
    case SUNDAY:
        System.out.println("Weekend!");
        break;
    default:
        System.out.println("Midweek");
}

// Get name as String
String name = today.name();  // "MONDAY"

// Get position (ordinal)
int position = today.ordinal();  // 0 (MONDAY is first)

// Convert String to Enum
Day day = Day.valueOf("MONDAY");  // Returns Day.MONDAY
// Day invalid = Day.valueOf("INVALID");  // Throws IllegalArgumentException

// Get all values
Day[] allDays = Day.values();
for (Day d : allDays) {
    System.out.println(d);  // Uses toString(), prints name by default
}
```

---

## Enum with Fields and Methods

### Adding Fields

```java
public enum Planet {
    MERCURY(3.303e+23, 2.4397e6),
    VENUS(4.869e+24, 6.0518e6),
    EARTH(5.976e+24, 6.37814e6),
    MARS(6.421e+23, 3.3972e6);

    private final double mass;    // in kilograms
    private final double radius;  // in meters

    // Constructor
    Planet(double mass, double radius) {
        this.mass = mass;
        this.radius = radius;
    }

    // Getter methods
    public double getMass() {
        return mass;
    }

    public double getRadius() {
        return radius;
    }

    // Calculated property
    public double surfaceGravity() {
        final double G = 6.67300E-11;
        return G * mass / (radius * radius);
    }
}
```

**Breaking it down:**

- `MERCURY(3.303e+23, 2.4397e6)` - Enum constant with constructor arguments
  - Values passed to constructor
  - Must match constructor signature

- `private final double mass` - Instance field
  - Each enum constant has its own values
  - Should be `final` (immutable)

- `Planet(double mass, double radius)` - Constructor
  - **Always private** (implicitly or explicitly)
  - Cannot be called from outside
  - Called once per constant when enum is loaded

**Usage:**
```java
Planet earth = Planet.EARTH;
System.out.println("Earth mass: " + earth.getMass());
System.out.println("Earth gravity: " + earth.surfaceGravity());

// Calculate weight on different planets
double earthWeight = 175;  // pounds
for (Planet p : Planet.values()) {
    double weightOnPlanet = earthWeight / Planet.EARTH.surfaceGravity() * p.surfaceGravity();
    System.out.printf("Weight on %s: %.2f%n", p, weightOnPlanet);
}
```

### Adding Methods

```java
public enum Day {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;

    // Instance method
    public boolean isWeekend() {
        return this == SATURDAY || this == SUNDAY;
    }

    // Instance method
    public boolean isWeekday() {
        return !isWeekend();
    }

    // Instance method
    public Day next() {
        Day[] days = values();
        return days[(ordinal() + 1) % days.length];
    }

    // Static method
    public static Day fromString(String name) {
        try {
            return valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;  // or throw custom exception
        }
    }
}
```

**Usage:**
```java
Day today = Day.MONDAY;
System.out.println(today.isWeekend());  // false
System.out.println(today.isWeekday());  // true

Day tomorrow = today.next();  // TUESDAY

Day day = Day.fromString("monday");  // MONDAY (converted to uppercase)
```

---

## Enum Constructors

### Multiple Constructors

```java
public enum Size {
    SMALL("S", 1),
    MEDIUM("M", 2),
    LARGE("L", 3),
    EXTRA_LARGE("XL", 4);

    private final String abbreviation;
    private final int size;

    // Constructor 1
    Size(String abbreviation, int size) {
        this.abbreviation = abbreviation;
        this.size = size;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public int getSize() {
        return size;
    }

    // Static factory method
    public static Size fromAbbreviation(String abbr) {
        for (Size s : values()) {
            if (s.abbreviation.equalsIgnoreCase(abbr)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown abbreviation: " + abbr);
    }
}
```

**Usage:**
```java
Size size = Size.LARGE;
System.out.println(size.getAbbreviation());  // "L"

Size fromAbbr = Size.fromAbbreviation("XL");  // EXTRA_LARGE
```

### Default Values

```java
public enum TrafficLight {
    RED(30),
    YELLOW(5),
    GREEN(45);

    private final int duration;  // seconds

    TrafficLight(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public TrafficLight next() {
        return values()[(ordinal() + 1) % values().length];
    }
}
```

---

## Abstract Methods in Enums

### Constant-Specific Method Implementation

Each enum constant can provide its own implementation of an abstract method.

```java
public enum Operation {
    PLUS {
        @Override
        public double apply(double x, double y) {
            return x + y;
        }
    },
    MINUS {
        @Override
        public double apply(double x, double y) {
            return x - y;
        }
    },
    TIMES {
        @Override
        public double apply(double x, double y) {
            return x * y;
        }
    },
    DIVIDE {
        @Override
        public double apply(double x, double y) {
            return x / y;
        }
    };

    // Abstract method - each constant MUST implement
    public abstract double apply(double x, double y);
}
```

**Breaking it down:**

- `PLUS { ... }` - Enum constant with **body**
  - Creates an anonymous inner class
  - Must implement all abstract methods

- `public abstract double apply(...)` - Abstract method
  - Forces each constant to provide implementation

**Usage:**
```java
double x = 10;
double y = 5;

for (Operation op : Operation.values()) {
    System.out.printf("%f %s %f = %f%n", x, op, y, op.apply(x, y));
}
// Output:
// 10.0 PLUS 5.0 = 15.0
// 10.0 MINUS 5.0 = 5.0
// 10.0 TIMES 5.0 = 50.0
// 10.0 DIVIDE 5.0 = 2.0
```

### Combining Fields and Abstract Methods

```java
public enum PaymentType {
    CASH {
        @Override
        public void processPayment(double amount) {
            System.out.println("Processing cash payment: $" + amount);
            // Cash-specific logic
        }
    },
    CREDIT_CARD {
        @Override
        public void processPayment(double amount) {
            System.out.println("Processing credit card payment: $" + amount);
            // Credit card-specific logic
        }
    },
    PAYPAL {
        @Override
        public void processPayment(double amount) {
            System.out.println("Processing PayPal payment: $" + amount);
            // PayPal-specific logic
        }
    };

    // Abstract method
    public abstract void processPayment(double amount);

    // Concrete method available to all
    public String getDisplayName() {
        return name().charAt(0) + name().substring(1).toLowerCase().replace('_', ' ');
    }
}
```

**Usage:**
```java
PaymentType payment = PaymentType.CREDIT_CARD;
payment.processPayment(99.99);
// Output: Processing credit card payment: $99.99

System.out.println(payment.getDisplayName());  // "Credit card"
```

---

## Built-in Enum Methods

All enums automatically inherit these methods from `java.lang.Enum`:

### Instance Methods

```java
public enum Color {
    RED, GREEN, BLUE
}

Color color = Color.RED;
```

| Method | Description | Example | Result |
|--------|-------------|---------|--------|
| `name()` | Returns constant name | `color.name()` | `"RED"` |
| `ordinal()` | Returns position (0-based) | `color.ordinal()` | `0` |
| `toString()` | Returns name (can override) | `color.toString()` | `"RED"` |
| `compareTo(E)` | Compares by ordinal | `RED.compareTo(BLUE)` | `-2` |
| `equals(Object)` | Checks equality | `color.equals(Color.RED)` | `true` |
| `hashCode()` | Returns hash code | `color.hashCode()` | varies |
| `getDeclaringClass()` | Returns enum class | `color.getDeclaringClass()` | `Color.class` |

### Static Methods

| Method | Description | Example | Result |
|--------|-------------|---------|--------|
| `valueOf(String)` | Get enum from name | `Color.valueOf("RED")` | `Color.RED` |
| `values()` | Get all constants | `Color.values()` | `[RED, GREEN, BLUE]` |

### Important Notes

**1. Use `==` for comparison, not `equals()`**
```java
// Recommended
if (color == Color.RED) { }

// Also works, but unnecessary
if (color.equals(Color.RED)) { }
```

**2. Enums are comparable**
```java
Color.RED.compareTo(Color.BLUE) < 0  // true (RED comes before BLUE)
```

**3. `valueOf()` is case-sensitive**
```java
Color c = Color.valueOf("RED");   // ✓ OK
Color c = Color.valueOf("red");   // ✗ IllegalArgumentException
Color c = Color.valueOf("INVALID"); // ✗ IllegalArgumentException
```

**4. Overriding `toString()`**
```java
public enum Status {
    ACTIVE {
        @Override
        public String toString() {
            return "Currently Active";
        }
    },
    INACTIVE {
        @Override
        public String toString() {
            return "Currently Inactive";
        }
    }
}

// Or with field:
public enum Status {
    ACTIVE("Currently Active"),
    INACTIVE("Currently Inactive");

    private final String display;

    Status(String display) {
        this.display = display;
    }

    @Override
    public String toString() {
        return display;
    }
}
```

---

## EnumSet and EnumMap

### EnumSet<E extends Enum<E>>

Specialized `Set` implementation for enums - **extremely efficient**.

```java
import java.util.EnumSet;

public enum Day {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}
```

**Creating EnumSets:**
```java
// All values
EnumSet<Day> allDays = EnumSet.allOf(Day.class);

// None (empty set)
EnumSet<Day> noDays = EnumSet.noneOf(Day.class);

// Specific values
EnumSet<Day> weekend = EnumSet.of(Day.SATURDAY, Day.SUNDAY);

// Range
EnumSet<Day> weekdays = EnumSet.range(Day.MONDAY, Day.FRIDAY);

// Complement
EnumSet<Day> notWeekend = EnumSet.complementOf(weekend);

// Copy
EnumSet<Day> copy = EnumSet.copyOf(weekend);
```

**Operations:**
```java
EnumSet<Day> days = EnumSet.of(Day.MONDAY, Day.TUESDAY);

// Add
days.add(Day.WEDNESDAY);

// Remove
days.remove(Day.MONDAY);

// Contains
boolean hasTuesday = days.contains(Day.TUESDAY);  // true

// Set operations
EnumSet<Day> weekend = EnumSet.of(Day.SATURDAY, Day.SUNDAY);
EnumSet<Day> weekdays = EnumSet.range(Day.MONDAY, Day.FRIDAY);

// Union
EnumSet<Day> all = EnumSet.copyOf(weekdays);
all.addAll(weekend);

// Intersection
EnumSet<Day> intersection = EnumSet.copyOf(weekdays);
intersection.retainAll(weekend);  // Empty set

// Iteration
for (Day day : days) {
    System.out.println(day);
}
```

**Why use EnumSet?**
- **Faster** than HashSet (uses bit vector internally)
- **More memory efficient**
- **Type-safe**

### EnumMap<K extends Enum<K>, V>

Specialized `Map` implementation with enum keys - **extremely efficient**.

```java
import java.util.EnumMap;

public enum Day {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}
```

**Creating EnumMaps:**
```java
// Create empty map
EnumMap<Day, String> schedule = new EnumMap<>(Day.class);

// Add entries
schedule.put(Day.MONDAY, "Team meeting");
schedule.put(Day.WEDNESDAY, "Code review");
schedule.put(Day.FRIDAY, "Deploy");

// Get value
String mondayTask = schedule.get(Day.MONDAY);  // "Team meeting"

// Check
boolean hasTask = schedule.containsKey(Day.TUESDAY);  // false

// Iteration (maintains enum order!)
for (Map.Entry<Day, String> entry : schedule.entrySet()) {
    System.out.println(entry.getKey() + ": " + entry.getValue());
}
// Output is in Day enum order: MONDAY, WEDNESDAY, FRIDAY
```

**Example: Counting occurrences**
```java
Day[] days = {Day.MONDAY, Day.TUESDAY, Day.MONDAY, Day.FRIDAY, Day.MONDAY};

EnumMap<Day, Integer> frequency = new EnumMap<>(Day.class);
for (Day day : days) {
    frequency.put(day, frequency.getOrDefault(day, 0) + 1);
}

System.out.println(frequency);
// {MONDAY=3, TUESDAY=1, FRIDAY=1}
```

**Why use EnumMap?**
- **Faster** than HashMap (array-based internally)
- **More memory efficient**
- **Maintains enum declaration order**
- **Type-safe**

---

## Enum Best Practices

### 1. Always Use Enums Instead of int Constants

**Bad:**
```java
public static final int STATUS_ACTIVE = 1;
public static final int STATUS_INACTIVE = 2;

public void setStatus(int status) {
    // Can pass any int - not type-safe!
    this.status = status;
}
```

**Good:**
```java
public enum Status {
    ACTIVE, INACTIVE
}

public void setStatus(Status status) {
    // Only valid Status values accepted - type-safe!
    this.status = status;
}
```

### 2. Prefer Enum to boolean for Binary States

**Okay:**
```java
boolean isActive;
```

**Better (more extensible):**
```java
enum Status {
    ACTIVE, INACTIVE
    // Easy to add PENDING, SUSPENDED, etc. later
}
```

### 3. Make Fields Final

```java
public enum Planet {
    EARTH(5.976e+24, 6.37814e6);

    private final double mass;    // final - immutable
    private final double radius;  // final - immutable

    Planet(double mass, double radius) {
        this.mass = mass;
        this.radius = radius;
    }
}
```

### 4. Use EnumSet/EnumMap for Collections

**Bad:**
```java
Set<Day> days = new HashSet<>();
Map<Day, String> schedule = new HashMap<>();
```

**Good:**
```java
Set<Day> days = EnumSet.noneOf(Day.class);
Map<Day, String> schedule = new EnumMap<>(Day.class);
```

### 5. Don't Use ordinal() for Persistence

**Bad:**
```java
// Saving to database
int code = status.ordinal();  // Fragile! Breaks if order changes

// Don't do this!
database.save(status.ordinal());
```

**Good:**
```java
// Use name() or custom code
String code = status.name();

// Or better: custom code field
public enum Status {
    ACTIVE("A"), INACTIVE("I");

    private final String code;

    Status(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}

database.save(status.getCode());
```

### 6. Implement Descriptive toString()

```java
public enum Status {
    ACTIVE("Active User"),
    INACTIVE("Inactive User"),
    SUSPENDED("Suspended User");

    private final String description;

    Status(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
```

### 7. Use Static Import for Readability

```java
import static com.example.Day.*;

// Instead of:
Day today = Day.MONDAY;

// Can write:
Day today = MONDAY;
```

### 8. Enums Can Implement Interfaces

```java
public interface Describable {
    String getDescription();
}

public enum Status implements Describable {
    ACTIVE("Currently active"),
    INACTIVE("Currently inactive");

    private final String description;

    Status(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
```

---

## Common Patterns

### 1. Strategy Pattern with Enums

```java
public enum DiscountStrategy {
    NONE {
        @Override
        public double applyDiscount(double price) {
            return price;
        }
    },
    PERCENTAGE_10 {
        @Override
        public double applyDiscount(double price) {
            return price * 0.9;
        }
    },
    PERCENTAGE_20 {
        @Override
        public double applyDiscount(double price) {
            return price * 0.8;
        }
    },
    FIXED_10 {
        @Override
        public double applyDiscount(double price) {
            return Math.max(0, price - 10);
        }
    };

    public abstract double applyDiscount(double price);
}

// Usage:
double originalPrice = 100.0;
double discountedPrice = DiscountStrategy.PERCENTAGE_20.applyDiscount(originalPrice);
```

### 2. State Machine Pattern

```java
public enum OrderState {
    PENDING {
        @Override
        public OrderState next() {
            return PROCESSING;
        }
    },
    PROCESSING {
        @Override
        public OrderState next() {
            return SHIPPED;
        }
    },
    SHIPPED {
        @Override
        public OrderState next() {
            return DELIVERED;
        }
    },
    DELIVERED {
        @Override
        public OrderState next() {
            return this;  // Final state
        }
    };

    public abstract OrderState next();

    public boolean isFinal() {
        return this == DELIVERED;
    }
}

// Usage:
OrderState state = OrderState.PENDING;
while (!state.isFinal()) {
    state = state.next();
    System.out.println("Order is now: " + state);
}
```

### 3. Lookup by Field (Reverse Lookup)

```java
public enum Country {
    USA("US", "United States"),
    CANADA("CA", "Canada"),
    MEXICO("MX", "Mexico");

    private final String code;
    private final String fullName;

    // Lookup cache
    private static final Map<String, Country> BY_CODE = new HashMap<>();

    static {
        for (Country c : values()) {
            BY_CODE.put(c.code, c);
        }
    }

    Country(String code, String fullName) {
        this.code = code;
        this.fullName = fullName;
    }

    public String getCode() {
        return code;
    }

    public String getFullName() {
        return fullName;
    }

    // Reverse lookup
    public static Country fromCode(String code) {
        return BY_CODE.get(code);
    }
}

// Usage:
Country usa = Country.fromCode("US");  // Fast O(1) lookup
```

### 4. Grouping Enums

```java
public enum Day {
    MONDAY(DayType.WEEKDAY),
    TUESDAY(DayType.WEEKDAY),
    WEDNESDAY(DayType.WEEKDAY),
    THURSDAY(DayType.WEEKDAY),
    FRIDAY(DayType.WEEKDAY),
    SATURDAY(DayType.WEEKEND),
    SUNDAY(DayType.WEEKEND);

    private final DayType type;

    Day(DayType type) {
        this.type = type;
    }

    public DayType getType() {
        return type;
    }

    public boolean isWeekend() {
        return type == DayType.WEEKEND;
    }

    public enum DayType {
        WEEKDAY, WEEKEND
    }
}

// Usage:
if (Day.MONDAY.isWeekend()) {
    // Won't happen
}
```

### 5. Singleton Pattern

```java
public enum DatabaseConnection {
    INSTANCE;

    private Connection connection;

    DatabaseConnection() {
        // Initialize connection
        this.connection = createConnection();
    }

    public Connection getConnection() {
        return connection;
    }

    private Connection createConnection() {
        // Connection logic
        return null;  // Placeholder
    }
}

// Usage (thread-safe singleton):
Connection conn = DatabaseConnection.INSTANCE.getConnection();
```

---

## Advanced Techniques

### 1. Enums with Interfaces

```java
public interface MathOperation {
    int apply(int a, int b);
}

public enum BasicOperation implements MathOperation {
    ADD {
        @Override
        public int apply(int a, int b) {
            return a + b;
        }
    },
    SUBTRACT {
        @Override
        public int apply(int a, int b) {
            return a - b;
        }
    };
}

public enum AdvancedOperation implements MathOperation {
    MULTIPLY {
        @Override
        public int apply(int a, int b) {
            return a * b;
        }
    },
    DIVIDE {
        @Override
        public int apply(int a, int b) {
            return a / b;
        }
    };
}

// Can use polymorphically:
public int calculate(MathOperation op, int a, int b) {
    return op.apply(a, b);
}
```

### 2. Nested Enums

```java
public class GameCharacter {
    public enum Type {
        WARRIOR, MAGE, ARCHER
    }

    public enum Status {
        ALIVE, DEAD, STUNNED
    }

    private Type type;
    private Status status;
}

// Usage:
GameCharacter.Type type = GameCharacter.Type.WARRIOR;
GameCharacter.Status status = GameCharacter.Status.ALIVE;
```

### 3. Enums with Multiple Behaviors

```java
public enum FileType {
    IMAGE("jpg", "png", "gif") {
        @Override
        public void process(String file) {
            System.out.println("Processing image: " + file);
        }
    },
    VIDEO("mp4", "avi", "mov") {
        @Override
        public void process(String file) {
            System.out.println("Processing video: " + file);
        }
    },
    DOCUMENT("pdf", "doc", "txt") {
        @Override
        public void process(String file) {
            System.out.println("Processing document: " + file);
        }
    };

    private final String[] extensions;

    FileType(String... extensions) {
        this.extensions = extensions;
    }

    public boolean matches(String extension) {
        for (String ext : extensions) {
            if (ext.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    public abstract void process(String file);

    public static FileType fromExtension(String extension) {
        for (FileType type : values()) {
            if (type.matches(extension)) {
                return type;
            }
        }
        return null;
    }
}

// Usage:
FileType type = FileType.fromExtension("jpg");  // IMAGE
type.process("photo.jpg");  // "Processing image: photo.jpg"
```

### 4. Enum with Builder Pattern

```java
public enum Configuration {
    INSTANCE;

    private String databaseUrl;
    private int maxConnections;
    private boolean enableCache;

    public Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String databaseUrl = "localhost";
        private int maxConnections = 10;
        private boolean enableCache = true;

        public Builder databaseUrl(String url) {
            this.databaseUrl = url;
            return this;
        }

        public Builder maxConnections(int max) {
            this.maxConnections = max;
            return this;
        }

        public Builder enableCache(boolean enable) {
            this.enableCache = enable;
            return this;
        }

        public void apply() {
            INSTANCE.databaseUrl = this.databaseUrl;
            INSTANCE.maxConnections = this.maxConnections;
            INSTANCE.enableCache = this.enableCache;
        }
    }

    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public boolean isCacheEnabled() {
        return enableCache;
    }
}

// Usage:
Configuration.INSTANCE.builder()
    .databaseUrl("prod-server")
    .maxConnections(50)
    .enableCache(true)
    .apply();
```

---

## Complete Example: Real-World Usage

```java
/**
 * Complete example showing most enum features
 */
public enum HttpStatus {
    // 2xx Success
    OK(200, "OK", Category.SUCCESS),
    CREATED(201, "Created", Category.SUCCESS),

    // 4xx Client Error
    BAD_REQUEST(400, "Bad Request", Category.CLIENT_ERROR),
    UNAUTHORIZED(401, "Unauthorized", Category.CLIENT_ERROR),
    NOT_FOUND(404, "Not Found", Category.CLIENT_ERROR),

    // 5xx Server Error
    INTERNAL_ERROR(500, "Internal Server Error", Category.SERVER_ERROR),
    SERVICE_UNAVAILABLE(503, "Service Unavailable", Category.SERVER_ERROR);

    private final int code;
    private final String message;
    private final Category category;

    // Lookup cache
    private static final Map<Integer, HttpStatus> BY_CODE = new HashMap<>();

    static {
        for (HttpStatus status : values()) {
            BY_CODE.put(status.code, status);
        }
    }

    HttpStatus(int code, String message, Category category) {
        this.code = code;
        this.message = message;
        this.category = category;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isSuccess() {
        return category == Category.SUCCESS;
    }

    public boolean isError() {
        return category == Category.CLIENT_ERROR || category == Category.SERVER_ERROR;
    }

    public static HttpStatus fromCode(int code) {
        HttpStatus status = BY_CODE.get(code);
        if (status == null) {
            throw new IllegalArgumentException("Unknown HTTP status code: " + code);
        }
        return status;
    }

    @Override
    public String toString() {
        return code + " " + message;
    }

    public enum Category {
        SUCCESS, CLIENT_ERROR, SERVER_ERROR
    }
}

// Usage:
HttpStatus status = HttpStatus.OK;
System.out.println(status);           // "200 OK"
System.out.println(status.isSuccess()); // true

HttpStatus notFound = HttpStatus.fromCode(404);  // NOT_FOUND
System.out.println(notFound.getCategory());      // CLIENT_ERROR

// In a web framework:
public Response handleRequest() {
    if (/* success */) {
        return new Response(HttpStatus.OK, data);
    } else {
        return new Response(HttpStatus.BAD_REQUEST, error);
    }
}
```

---

## Summary

### When to Use Enums

✓ **Fixed set of constants** (days, months, states, etc.)
✓ **Type safety** needed
✓ **Switch statements** (better than strings/ints)
✓ **Singleton pattern** (enum with one value)
✓ **Strategy pattern** (behavior per constant)
✓ **State machines**

### Key Takeaways

1. Enums are **type-safe** constants
2. Each constant is a **singleton** instance
3. Can have **fields, methods, and constructors**
4. Can implement **interfaces** (not extend classes)
5. Use **EnumSet** and **EnumMap** for collections
6. Prefer **==** over `equals()` for comparison
7. Use `name()` for persistence, not `ordinal()`
8. Can have **constant-specific** implementations

---

This guide covers everything you need to know about Java enums!
