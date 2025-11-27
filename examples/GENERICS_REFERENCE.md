# Java Generics Complete Reference Guide

## Table of Contents
1. [Basic Syntax & Terminology](#basic-syntax--terminology)
2. [Type Parameters](#type-parameters)
3. [Bounded Type Parameters](#bounded-type-parameters)
4. [Wildcards](#wildcards)
5. [Type Erasure](#type-erasure)
6. [Advanced Concepts](#advanced-concepts)

---

## Basic Syntax & Terminology

### Generic Class Declaration

```java
public class Box<T> {
    private T value;
}
```

**Breaking it down:**

- `public class Box` - Regular class declaration
- `<T>` - **Type parameter declaration**
  - `<` - Opening angle bracket (starts generic declaration)
  - `T` - **Type parameter name** (placeholder for actual type)
  - `>` - Closing angle bracket (ends generic declaration)

**What it means:** "Box is a generic class that works with some type T, which will be specified later"

### Common Type Parameter Names (Convention)

- `T` - **T**ype (most common, general purpose)
- `E` - **E**lement (used in collections)
- `K` - **K**ey (used in maps)
- `V` - **V**alue (used in maps)
- `N` - **N**umber
- `S`, `U`, `V` - 2nd, 3rd, 4th types (when multiple needed)

**Example:**
```java
Map<K, V>  // Key-Value pairs
List<E>    // Elements in a list
```

### Using Type Parameter Inside Class

```java
public class Box<T> {
    private T value;        // T used as field type

    public Box(T value) {   // T used as parameter type
        this.value = value;
    }

    public T getValue() {   // T used as return type
        return this.value;
    }

    public void setValue(T value) {  // T used as parameter type
        this.value = value;
    }
}
```

**Breaking it down:**

- `private T value;`
  - `T` is used as a **type** (like using `String`, `int`, etc.)
  - Means: "This field will be of whatever type T is"

- `public Box(T value)`
  - `T` as **parameter type**
  - Means: "Constructor accepts a parameter of type T"

- `public T getValue()`
  - `T` as **return type**
  - Means: "This method returns something of type T"

### Creating Generic Instances

```java
Box<String> stringBox = new Box<>("Hello");
```

**Breaking it down:**

- `Box<String>` - **Type on left side (variable declaration)**
  - `Box` - The generic class name
  - `<String>` - **Type argument** (actual type replacing T)
  - Means: "Declare a variable that holds a Box of Strings"

- `new Box<>("Hello")` - **Constructor call**
  - `new Box<>` - **Diamond operator** (Java 7+)
  - `<>` - Empty angle brackets (type inferred from left side)
  - `("Hello")` - Constructor argument
  - Means: "Create a new Box, compiler figures out it's Box<String>"

**Without diamond operator (verbose way):**
```java
Box<String> stringBox = new Box<String>("Hello");
//                                ^^^^^^^ - Explicit type argument
```

---

## Type Parameters

### Single Type Parameter

```java
public class Container<T> {
    private T item;
}
```

**Meaning:** Class works with ONE generic type

### Multiple Type Parameters

```java
public class Pair<K, V> {
    private K key;
    private V value;
}
```

**Breaking it down:**

- `<K, V>` - **Multiple type parameters**
  - `K` - First type parameter
  - `,` - Comma separator
  - `V` - Second type parameter

**Usage:**
```java
Pair<String, Integer> pair = new Pair<>("Age", 25);
//    ^^^^^^  ^^^^^^^
//    K type  V type
```

### Three or More Type Parameters

```java
public class Triple<A, B, C> {
    private A first;
    private B second;
    private C third;
}
```

**Usage:**
```java
Triple<String, Integer, Boolean> triple = new Triple<>("Name", 42, true);
```

---

## Bounded Type Parameters

### Upper Bound (extends)

```java
public class NumberBox<T extends Number> {
    private T value;
}
```

**Breaking it down:**

- `<T extends Number>` - **Upper bounded type parameter**
  - `T` - Type parameter name
  - `extends` - **Upper bound keyword** (means "is a" or "implements")
  - `Number` - **Bound** (T must be Number or subclass)

**What it means:** "T can be Number or any subclass of Number (Integer, Double, etc.)"

**Important:** Use `extends` for BOTH classes and interfaces!

```java
<T extends Serializable>  // Interface - still use "extends"!
<T extends Comparable<T>> // Interface - still use "extends"!
```

### Multiple Bounds

```java
public class Box<T extends Number & Comparable<T>> {
    private T value;
}
```

**Breaking it down:**

- `<T extends Number & Comparable<T>>` - **Multiple bounds**
  - `T extends Number` - First bound (must extend Number)
  - `&` - **Bound separator** (AND operator)
  - `Comparable<T>` - Second bound (must implement Comparable)

**What it means:** "T must be BOTH a Number AND implement Comparable"

**Rules:**
1. Class bound (if any) must come FIRST
2. Then interface bounds
3. Use `&` to separate bounds

```java
<T extends MyClass & Interface1 & Interface2>  // Correct order
//         ^^^^^^^^   ^^^^^^^^^^^  ^^^^^^^^^^^
//         class      interface    interface
```

### Recursive Type Bounds

```java
public class Employee implements Comparable<Employee> {
    // ...
}

public static <T extends Comparable<T>> T findMax(T a, T b) {
    return a.compareTo(b) > 0 ? a : b;
}
```

**Breaking it down:**

- `<T extends Comparable<T>>` - **Recursive bound**
  - `T` - Type parameter
  - `extends Comparable<T>` - T must be comparable to itself
  - `<T>` inside `Comparable<T>` - The same T!

**What it means:** "T must be a type that can compare to itself"

---

## Wildcards

### Unbounded Wildcard (?)

```java
public void printList(List<?> list) {
    for (Object item : list) {
        System.out.println(item);
    }
}
```

**Breaking it down:**

- `List<?>` - **Unbounded wildcard**
  - `?` - **Wildcard** (unknown type)
  - Means: "A list of something, but we don't know what"

**What you CAN do:**
- Read elements as `Object`
- Get the size
- Check if empty

**What you CANNOT do:**
- Add elements (except `null`)
- Know the specific type

**Why?** The list could be `List<String>`, `List<Integer>`, or anything!

### Upper Bounded Wildcard (? extends)

```java
public double sum(List<? extends Number> numbers) {
    double total = 0;
    for (Number num : numbers) {
        total += num.doubleValue();
    }
    return total;
}
```

**Breaking it down:**

- `List<? extends Number>` - **Upper bounded wildcard**
  - `?` - Unknown type
  - `extends` - Upper bound keyword
  - `Number` - The bound
  - Means: "A list of some type that extends Number"

**What it accepts:**
```java
List<Integer> ints = Arrays.asList(1, 2, 3);
List<Double> doubles = Arrays.asList(1.1, 2.2);
List<Number> numbers = Arrays.asList(1, 2.5);

sum(ints);     // ✓ Works - Integer extends Number
sum(doubles);  // ✓ Works - Double extends Number
sum(numbers);  // ✓ Works - Number extends Number
```

**What you CAN do:**
- **READ** as `Number` (or the bound type)
- Call methods on the bound type

**What you CANNOT do:**
- **ADD** elements (except `null`)

**Why can't you add?**
```java
List<? extends Number> list = new ArrayList<Integer>();
list.add(new Integer(5));  // ✗ COMPILE ERROR!
list.add(new Double(5.0)); // ✗ COMPILE ERROR!
```
We don't know if it's `List<Integer>` or `List<Double>`, so adding is unsafe!

### Lower Bounded Wildcard (? super)

```java
public void addNumbers(List<? super Integer> list) {
    list.add(1);
    list.add(2);
    list.add(3);
}
```

**Breaking it down:**

- `List<? super Integer>` - **Lower bounded wildcard**
  - `?` - Unknown type
  - `super` - **Lower bound keyword**
  - `Integer` - The bound
  - Means: "A list of some type that is Integer or a superclass"

**What it accepts:**
```java
List<Integer> ints = new ArrayList<>();
List<Number> numbers = new ArrayList<>();
List<Object> objects = new ArrayList<>();

addNumbers(ints);     // ✓ Works - Integer is super of Integer
addNumbers(numbers);  // ✓ Works - Number is super of Integer
addNumbers(objects);  // ✓ Works - Object is super of Integer
```

**What you CAN do:**
- **ADD** Integers (or subtypes)
- Add `null`

**What you CANNOT do:**
- **READ** as specific type (only as `Object`)

```java
List<? super Integer> list = new ArrayList<Number>();
list.add(5);           // ✓ OK - adding Integer
Integer x = list.get(0); // ✗ COMPILE ERROR!
Object obj = list.get(0); // ✓ OK - can only read as Object
```

**Why?** We don't know if it's `List<Integer>`, `List<Number>`, or `List<Object>`!

---

## PECS Principle

**PECS: Producer Extends, Consumer Super**

### Producer Extends

```java
// Method that PRODUCES/READS values FROM the list
public void processNumbers(List<? extends Number> numbers) {
    for (Number n : numbers) {  // READING from list
        System.out.println(n.doubleValue());
    }
}
```

**Meaning:** When you're **getting values OUT** (producing), use `? extends`

### Consumer Super

```java
// Method that CONSUMES/WRITES values TO the list
public void addIntegers(List<? super Integer> list) {
    list.add(1);  // WRITING to list
    list.add(2);
}
```

**Meaning:** When you're **putting values IN** (consuming), use `? super`

### Combined Example

```java
public static <T> void copy(
    List<? super T> dest,    // CONSUMER - we put T into dest
    List<? extends T> src    // PRODUCER - we get T from src
) {
    for (T item : src) {     // READ from producer
        dest.add(item);      // WRITE to consumer
    }
}
```

---

## Generic Methods

### Basic Generic Method

```java
public static <T> void printArray(T[] array) {
    for (T element : array) {
        System.out.println(element);
    }
}
```

**Breaking it down:**

- `public static <T>` - **Method-level type parameter**
  - `<T>` - Type parameter FOR THIS METHOD
  - Must come BEFORE return type
  - Different from class-level `<T>`

- `void` - Return type
- `printArray` - Method name
- `T[]` - Parameter uses the type parameter

**Usage:**
```java
String[] names = {"Alice", "Bob"};
Integer[] numbers = {1, 2, 3};

printArray(names);    // T inferred as String
printArray(numbers);  // T inferred as Integer
```

### Generic Method with Return Type

```java
public static <T> T getFirst(List<T> list) {
    return list.isEmpty() ? null : list.get(0);
}
```

**Breaking it down:**

- `<T>` - Type parameter declaration
- `T` - Return type (uses the type parameter)
- `getFirst` - Method name
- `List<T>` - Parameter type

### Multiple Type Parameters in Method

```java
public static <K, V> void printPair(K key, V value) {
    System.out.println(key + " => " + value);
}
```

**Breaking it down:**

- `<K, V>` - TWO type parameters for this method
- `void` - Return type
- `K key, V value` - Parameters of different types

**Usage:**
```java
printPair("Name", "Alice");     // K=String, V=String
printPair("Age", 25);           // K=String, V=Integer
printPair(1, "First");          // K=Integer, V=String
```

### Bounded Generic Method

```java
public static <T extends Comparable<T>> T findMax(T a, T b) {
    return a.compareTo(b) > 0 ? a : b;
}
```

**Breaking it down:**

- `<T extends Comparable<T>>` - Bounded type parameter
  - Method-level bound
  - T must implement Comparable

---

## Generic Interfaces

### Basic Generic Interface

```java
public interface Container<T> {
    void add(T item);
    T get(int index);
    int size();
}
```

**Breaking it down:**

- `interface Container<T>` - Generic interface declaration
  - `<T>` - Type parameter for the interface

### Implementing Generic Interface - Specify Type

```java
public class StringContainer implements Container<String> {
    //                                            ^^^^^^
    //                                     Type specified as String

    @Override
    public void add(String item) {  // T becomes String
        // ...
    }

    @Override
    public String get(int index) {  // T becomes String
        // ...
    }

    @Override
    public int size() {
        // ...
    }
}
```

**Meaning:** This implementation works ONLY with Strings

### Implementing Generic Interface - Keep Generic

```java
public class MyContainer<E> implements Container<E> {
    //                   ^^^                     ^^^
    //              Stays generic          E maps to T

    @Override
    public void add(E item) {  // Still generic
        // ...
    }

    @Override
    public E get(int index) {  // Still generic
        // ...
    }
}
```

**Meaning:** This implementation is still generic, works with any type

---

## Type Erasure

### What Happens at Compile Time vs Runtime

**Your Code:**
```java
List<String> strings = new ArrayList<String>();
List<Integer> integers = new ArrayList<Integer>();
```

**After Type Erasure (at runtime):**
```java
List strings = new ArrayList();  // No type info!
List integers = new ArrayList(); // No type info!
```

**Breaking it down:**

- **Compile time:** Generics are checked and enforced
- **Runtime:** All generic type info is ERASED
- **Result:** `List<String>` and `List<Integer>` are both just `List`

### Erasure Rules

**Unbounded Type:**
```java
class Box<T> {           →    class Box {
    T value;             →        Object value;
}
```
`T` erases to `Object`

**Bounded Type:**
```java
class Box<T extends Number> {    →    class Box {
    T value;                     →        Number value;
}
```
`T` erases to its bound (`Number`)

### Cannot Do at Runtime

**1. Cannot use instanceof with generic type:**
```java
if (obj instanceof List<String>) {}  // ✗ COMPILE ERROR
if (obj instanceof List<?>) {}       // ✓ OK - wildcard allowed
```

**2. Cannot create generic array:**
```java
T[] array = new T[10];               // ✗ COMPILE ERROR
List<String>[] arr = new List<String>[10];  // ✗ COMPILE ERROR
```

**3. Cannot instantiate type parameter:**
```java
T obj = new T();                     // ✗ COMPILE ERROR
```

---

## Advanced Concepts

### Diamond Operator

**Java 7+:**
```java
List<String> list = new ArrayList<>();
//                                 ^^
//                          Diamond operator
```

**Breaking it down:**
- `<>` - Empty angle brackets
- Compiler infers type from left side
- Shorthand for `new ArrayList<String>()`

### Type Witness

```java
Box.<String>createBox("Hello");
//  ^^^^^^^^
//  Type witness - explicitly tell compiler the type
```

**When needed:**
- Compiler can't infer type
- Ambiguous generic method calls

### Raw Types (Legacy - Avoid!)

```java
List rawList = new ArrayList();  // No generics!
rawList.add("String");
rawList.add(123);                // No type checking!
```

**Breaking it down:**
- `List` without `<T>` - **Raw type**
- No type safety
- Used for compatibility with pre-generics Java (before Java 5)
- **NEVER use in new code!**

### Bridge Methods

**Your code:**
```java
class Node<T> {
    void set(T data) { }
}

class StringNode extends Node<String> {
    @Override
    void set(String data) { }  // Your method
}
```

**Compiler generates (invisible to you):**
```java
class StringNode extends Node {
    void set(String data) { }       // Your method

    void set(Object data) {         // Bridge method (generated)
        set((String) data);         // Calls your method
    }
}
```

**Why?** Maintains binary compatibility after type erasure

### @SafeVarargs

```java
@SafeVarargs
public static <T> void printAll(T... items) {
    for (T item : items) {
        System.out.println(item);
    }
}
```

**Breaking it down:**
- `@SafeVarargs` - Annotation
- Suppresses varargs warnings with generics
- Use ONLY when method is truly safe
- Can only be used on `static`, `final`, or `private` methods

---

## Complete Example Breakdown

```java
public class DataStore<K extends Comparable<K>, V> {
    private Map<K, V> map = new HashMap<>();

    public <T extends V> void addAll(List<? super T> dest, List<? extends T> src) {
        for (T item : src) {
            dest.add(item);
        }
    }
}
```

**Breaking down every part:**

1. `public class DataStore<K extends Comparable<K>, V>`
   - Generic class with TWO type parameters
   - `K` must implement `Comparable<K>` (recursive bound)
   - `V` is unbounded

2. `private Map<K, V> map`
   - Uses both type parameters
   - Map from K to V

3. `public <T extends V>`
   - Method-level type parameter
   - `T` must be a subtype of `V`

4. `List<? super T> dest`
   - Lower bounded wildcard
   - Consumer (we add to it)

5. `List<? extends T> src`
   - Upper bounded wildcard
   - Producer (we read from it)

---

## Quick Reference Table

| Syntax | Name | Meaning | Can Read As | Can Write |
|--------|------|---------|-------------|-----------|
| `<T>` | Type Parameter | Generic type | T | T |
| `<?>` | Unbounded Wildcard | Unknown type | Object | null only |
| `<? extends T>` | Upper Bounded | T or subtype | T | null only |
| `<? super T>` | Lower Bounded | T or supertype | Object | T |
| `<T extends B>` | Bounded Type Param | T must extend B | T | T |
| `<T extends A & B>` | Multiple Bounds | T must extend both | T | T |

---

## Memory Aid: When to Use What

- **Creating a container/collection class?** → Use type parameter `<T>`
- **Method reads from collection?** → Use `<? extends T>` (Producer)
- **Method writes to collection?** → Use `<? super T>` (Consumer)
- **Need to restrict types?** → Use bounds `<T extends SomeClass>`
- **Don't care about type?** → Use wildcard `<?>`
- **Multiple type constraints?** → Use `<T extends A & B & C>`

---

This guide covers every syntactic element and concept in Java generics!
