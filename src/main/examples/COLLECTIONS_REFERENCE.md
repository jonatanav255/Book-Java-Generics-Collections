# Java Collections Framework Complete Reference Guide

## Table of Contents
1. [Overview & Hierarchy](#overview--hierarchy)
2. [List Interface](#list-interface)
3. [Set Interface](#set-interface)
4. [Queue & Deque Interfaces](#queue--deque-interfaces)
5. [Map Interface](#map-interface)
6. [Utility Classes](#utility-classes)
7. [Algorithm Complexity](#algorithm-complexity)
8. [Best Practices & When to Use What](#best-practices--when-to-use-what)

---

## Overview & Hierarchy

### The Collection Framework Hierarchy

```
                    Iterable<E>
                        |
                   Collection<E>
                        |
        +---------------+---------------+
        |               |               |
     List<E>         Set<E>         Queue<E>
        |               |               |
   +---------+    +-----------+    +---------+
   |         |    |           |    |         |
ArrayList  LinkedList  HashSet  TreeSet  PriorityQueue
Vector              SortedSet       Deque<E>
  |                    |              |
Stack            NavigableSet    ArrayDeque
                      |           LinkedList
                  TreeSet


        Map<K,V> (separate hierarchy)
            |
    +-------+-------+
    |               |
HashMap         TreeMap
LinkedHashMap   SortedMap
Hashtable          |
Properties     NavigableMap
                   |
               TreeMap
```

### Core Interfaces

| Interface | Description | Ordered? | Duplicates? | Null? |
|-----------|-------------|----------|-------------|-------|
| `Collection<E>` | Root interface | Depends | Depends | Depends |
| `List<E>` | Ordered collection | Yes | Yes | Yes |
| `Set<E>` | No duplicates | No | No | Yes (one) |
| `Queue<E>` | FIFO processing | Yes | Yes | No |
| `Deque<E>` | Double-ended queue | Yes | Yes | No |
| `Map<K,V>` | Key-value pairs | No | Keys: No, Values: Yes | Depends |

---

## List Interface

### What is a List?

```java
List<E> extends Collection<E>
```

**Characteristics:**
- **Ordered** - Elements maintain insertion order
- **Indexed** - Access by position (0-based)
- **Duplicates allowed** - Same element can appear multiple times
- **Null allowed** - Can contain null elements

### List Implementations

#### 1. ArrayList<E>

```java
List<String> list = new ArrayList<>();
```

**Internal Structure:**
- Backed by **resizable array**
- Default initial capacity: 10
- Grows by 50% when full

**Performance:**
- `get(index)` - **O(1)** - Fast random access
- `add(element)` - **O(1)** amortized - Fast append
- `add(index, element)` - **O(n)** - Slow insertion in middle
- `remove(index)` - **O(n)** - Slow removal
- `contains(element)` - **O(n)** - Linear search

**When to use:**
- ✓ Frequent random access by index
- ✓ Iteration over elements
- ✓ Adding to end of list
- ✗ Frequent insertions/deletions in middle
- ✗ Thread-safe operations needed

**Example:**
```java
List<String> names = new ArrayList<>();
names.add("Alice");           // O(1)
names.add("Bob");             // O(1)
names.add(1, "Charlie");      // O(n) - shifts elements
String first = names.get(0);  // O(1) - fast access
names.remove(0);              // O(n) - shifts elements
```

#### 2. LinkedList<E>

```java
List<String> list = new LinkedList<>();
```

**Internal Structure:**
- **Doubly-linked list**
- Each node has: data, next pointer, previous pointer
- No index-based access

**Performance:**
- `get(index)` - **O(n)** - Must traverse from start/end
- `add(element)` - **O(1)** - Fast append
- `add(index, element)` - **O(n)** - O(1) if at ends
- `remove(index)` - **O(n)** - O(1) if at ends
- `contains(element)` - **O(n)** - Linear search

**When to use:**
- ✓ Frequent insertions/deletions at beginning or end
- ✓ Implementing stacks or queues
- ✓ Don't need random access
- ✗ Need fast index-based access
- ✗ Memory constrained (more overhead per element)

**Example:**
```java
LinkedList<String> list = new LinkedList<>();
list.addFirst("First");       // O(1) - fast
list.addLast("Last");         // O(1) - fast
list.add(1, "Middle");        // O(n) - traverse to position
String first = list.get(0);   // O(n) - slow access
```

#### 3. Vector<E> (Legacy - Avoid)

```java
List<String> list = new Vector<>();
```

**Characteristics:**
- Similar to ArrayList but **synchronized**
- Thread-safe but slower
- Grows by 100% (doubles) when full

**When to use:**
- ✗ **Don't use!** Use `Collections.synchronizedList()` instead
- Legacy code compatibility only

#### 4. Stack<E> (Legacy - Avoid)

```java
Stack<String> stack = new Stack<>();
```

**Characteristics:**
- Extends Vector
- LIFO (Last In First Out)

**Methods:**
- `push(E)` - Add to top
- `pop()` - Remove from top
- `peek()` - View top without removing

**When to use:**
- ✗ **Don't use!** Use `Deque<E>` instead
- `Deque<String> stack = new ArrayDeque<>();`

### Common List Operations

```java
List<String> list = new ArrayList<>();

// Adding elements
list.add("A");                    // Add to end
list.add(0, "B");                 // Add at index
list.addAll(Arrays.asList("C", "D")); // Add collection

// Accessing elements
String first = list.get(0);       // Get by index
int size = list.size();           // Get size
boolean empty = list.isEmpty();   // Check if empty

// Searching
boolean contains = list.contains("A");  // Check existence
int index = list.indexOf("A");          // Find first occurrence
int lastIndex = list.lastIndexOf("A");  // Find last occurrence

// Modifying
list.set(0, "Z");                 // Replace at index
list.remove(0);                   // Remove by index
list.remove("A");                 // Remove by object
list.clear();                     // Remove all

// Iteration
for (String item : list) {
    System.out.println(item);
}

// Using iterator
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    String item = it.next();
    // Can safely remove during iteration
    it.remove();
}

// Sublist (view)
List<String> subList = list.subList(0, 2);  // [0, 2) - changes reflect in original

// Sorting
Collections.sort(list);                      // Natural order
Collections.sort(list, Comparator.reverseOrder()); // Reverse order
list.sort(Comparator.naturalOrder());        // Java 8+ method

// Converting
String[] array = list.toArray(new String[0]); // To array
List<String> copy = new ArrayList<>(list);    // Copy constructor
```

---

## Set Interface

### What is a Set?

```java
Set<E> extends Collection<E>
```

**Characteristics:**
- **No duplicates** - Each element appears once
- **Unordered** (most implementations)
- Models mathematical set
- `equals()` and `hashCode()` important

### Set Implementations

#### 1. HashSet<E>

```java
Set<String> set = new HashSet<>();
```

**Internal Structure:**
- Backed by **HashMap**
- Uses hash table
- No ordering guarantees

**Performance:**
- `add(element)` - **O(1)** - Fast
- `remove(element)` - **O(1)** - Fast
- `contains(element)` - **O(1)** - Fast

**When to use:**
- ✓ Need fast lookups
- ✓ Don't care about order
- ✓ Want to eliminate duplicates
- ✗ Need sorted elements
- ✗ Need insertion order

**Example:**
```java
Set<String> set = new HashSet<>();
set.add("Apple");       // true - added
set.add("Banana");      // true - added
set.add("Apple");       // false - duplicate!
set.contains("Apple");  // true - O(1)
set.size();             // 2
```

#### 2. LinkedHashSet<E>

```java
Set<String> set = new LinkedHashSet<>();
```

**Internal Structure:**
- HashSet + doubly-linked list
- Maintains **insertion order**

**Performance:**
- Same as HashSet - **O(1)** for basic operations
- Slightly slower due to maintaining links

**When to use:**
- ✓ Need fast lookups AND insertion order
- ✓ Predictable iteration order
- ✗ Need sorting

**Example:**
```java
Set<String> set = new LinkedHashSet<>();
set.add("Zebra");
set.add("Apple");
set.add("Mango");
// Iteration: Zebra, Apple, Mango (insertion order)
```

#### 3. TreeSet<E>

```java
Set<String> set = new TreeSet<>();
```

**Internal Structure:**
- Backed by **TreeMap** (Red-Black tree)
- Elements **sorted**
- Implements `NavigableSet`

**Performance:**
- `add(element)` - **O(log n)**
- `remove(element)` - **O(log n)**
- `contains(element)` - **O(log n)**

**Requirements:**
- Elements must be `Comparable` OR provide `Comparator`

**When to use:**
- ✓ Need sorted elements
- ✓ Need range operations (first, last, subset)
- ✗ Need fastest possible operations

**Example:**
```java
Set<String> set = new TreeSet<>();
set.add("Zebra");
set.add("Apple");
set.add("Mango");
// Iteration: Apple, Mango, Zebra (sorted)

// NavigableSet operations
String first = ((TreeSet<String>)set).first();      // "Apple"
String last = ((TreeSet<String>)set).last();        // "Zebra"
Set<String> subset = ((TreeSet<String>)set).subSet("A", "N"); // [Apple, Mango]
```

#### 4. EnumSet<E extends Enum<E>> (Special)

```java
enum Day { MON, TUE, WED, THU, FRI, SAT, SUN }
Set<Day> weekend = EnumSet.of(Day.SAT, Day.SUN);
```

**Characteristics:**
- Extremely efficient for enum types
- Internally uses bit vector
- All elements must be from single enum type

**Performance:**
- **O(1)** for all operations
- Faster than HashSet

**When to use:**
- ✓ Working with enums
- Always prefer over HashSet for enums

### Common Set Operations

```java
Set<String> set = new HashSet<>();

// Adding
set.add("A");                     // Add element
set.addAll(Arrays.asList("B", "C")); // Add collection

// Checking
boolean contains = set.contains("A");
boolean empty = set.isEmpty();
int size = set.size();

// Removing
set.remove("A");
set.clear();

// Set operations
Set<String> set1 = new HashSet<>(Arrays.asList("A", "B", "C"));
Set<String> set2 = new HashSet<>(Arrays.asList("B", "C", "D"));

// Union
Set<String> union = new HashSet<>(set1);
union.addAll(set2);              // {A, B, C, D}

// Intersection
Set<String> intersection = new HashSet<>(set1);
intersection.retainAll(set2);    // {B, C}

// Difference
Set<String> difference = new HashSet<>(set1);
difference.removeAll(set2);      // {A}

// Iteration
for (String item : set) {
    System.out.println(item);
}
```

---

## Queue & Deque Interfaces

### Queue<E> Interface

```java
Queue<E> extends Collection<E>
```

**Characteristics:**
- **FIFO** - First In First Out
- Head and tail
- Two forms of methods: throwing vs returning special value

**Queue Methods:**

| Operation | Throws Exception | Returns Special Value |
|-----------|-----------------|----------------------|
| Insert | `add(e)` | `offer(e)` - returns false |
| Remove | `remove()` | `poll()` - returns null |
| Examine | `element()` | `peek()` - returns null |

#### PriorityQueue<E>

```java
Queue<Integer> pq = new PriorityQueue<>();
```

**Characteristics:**
- Elements ordered by natural ordering or Comparator
- **Heap** data structure
- **Not FIFO** - orders by priority

**Performance:**
- `offer(e)` - **O(log n)**
- `poll()` - **O(log n)**
- `peek()` - **O(1)**

**Example:**
```java
PriorityQueue<Integer> pq = new PriorityQueue<>();
pq.offer(5);
pq.offer(1);
pq.offer(3);

pq.poll();  // 1 (smallest first)
pq.poll();  // 3
pq.poll();  // 5

// Custom comparator (max heap)
PriorityQueue<Integer> maxPq = new PriorityQueue<>(Comparator.reverseOrder());
```

### Deque<E> Interface

```java
Deque<E> extends Queue<E>
```

**Characteristics:**
- **Double-Ended Queue**
- Can add/remove from both ends
- Can be used as Stack or Queue

**Deque Methods:**

| Operation | First Element | Last Element |
|-----------|--------------|--------------|
| Insert | `addFirst(e)` / `offerFirst(e)` | `addLast(e)` / `offerLast(e)` |
| Remove | `removeFirst()` / `pollFirst()` | `removeLast()` / `pollLast()` |
| Examine | `getFirst()` / `peekFirst()` | `getLast()` / `peekLast()` |

#### ArrayDeque<E>

```java
Deque<String> deque = new ArrayDeque<>();
```

**Internal Structure:**
- Resizable array
- No capacity restrictions
- Not thread-safe

**Performance:**
- Most operations **O(1)**
- Faster than LinkedList for stack/queue

**When to use:**
- ✓ Need a stack (better than Stack class)
- ✓ Need a queue (better than LinkedList)
- ✓ Need deque operations

**Example:**
```java
Deque<String> deque = new ArrayDeque<>();

// Use as Stack (LIFO)
deque.push("A");          // addFirst
deque.push("B");
deque.pop();              // removeFirst - returns "B"
deque.peek();             // peekFirst - returns "A"

// Use as Queue (FIFO)
deque.offer("A");         // addLast
deque.offer("B");
deque.poll();             // removeFirst - returns "A"

// Use as Deque
deque.addFirst("First");
deque.addLast("Last");
```

---

## Map Interface

### What is a Map?

```java
Map<K, V>
```

**Characteristics:**
- Key-value pairs
- Keys are unique
- Each key maps to one value
- Not a Collection (separate hierarchy)

### Map Implementations

#### 1. HashMap<K, V>

```java
Map<String, Integer> map = new HashMap<>();
```

**Internal Structure:**
- Array of buckets (linked lists/trees)
- Uses hashCode() and equals()
- No ordering

**Performance:**
- `put(key, value)` - **O(1)** average
- `get(key)` - **O(1)** average
- `remove(key)` - **O(1)** average

**When to use:**
- ✓ Need fast key-value lookups
- ✓ Don't care about order
- ✗ Need sorted keys
- ✗ Need thread safety

**Example:**
```java
Map<String, Integer> map = new HashMap<>();
map.put("Alice", 25);
map.put("Bob", 30);
map.put("Alice", 26);        // Overwrites previous value

int age = map.get("Alice");   // 26
boolean has = map.containsKey("Bob");  // true
map.remove("Bob");

// Iteration
for (Map.Entry<String, Integer> entry : map.entrySet()) {
    System.out.println(entry.getKey() + " => " + entry.getValue());
}

// Java 8+ forEach
map.forEach((key, value) -> System.out.println(key + " => " + value));
```

#### 2. LinkedHashMap<K, V>

```java
Map<String, Integer> map = new LinkedHashMap<>();
```

**Internal Structure:**
- HashMap + doubly-linked list
- Maintains **insertion order**

**When to use:**
- ✓ Need fast lookups AND insertion order
- ✓ LRU cache implementation (with access order mode)

**Example:**
```java
Map<String, Integer> map = new LinkedHashMap<>();
map.put("Z", 1);
map.put("A", 2);
map.put("M", 3);
// Iteration: Z, A, M (insertion order)

// Access order mode (for LRU cache)
Map<String, Integer> lru = new LinkedHashMap<>(16, 0.75f, true);
```

#### 3. TreeMap<K, V>

```java
Map<String, Integer> map = new TreeMap<>();
```

**Internal Structure:**
- Red-Black tree
- Keys **sorted**
- Implements `NavigableMap`

**Performance:**
- `put(key, value)` - **O(log n)**
- `get(key)` - **O(log n)**
- `remove(key)` - **O(log n)**

**Requirements:**
- Keys must be `Comparable` OR provide `Comparator`

**When to use:**
- ✓ Need sorted keys
- ✓ Need range operations
- ✗ Need fastest operations

**Example:**
```java
Map<String, Integer> map = new TreeMap<>();
map.put("Zebra", 1);
map.put("Apple", 2);
map.put("Mango", 3);
// Iteration: Apple, Mango, Zebra (sorted)

// NavigableMap operations
TreeMap<String, Integer> treeMap = (TreeMap<String, Integer>) map;
String firstKey = treeMap.firstKey();      // "Apple"
String lastKey = treeMap.lastKey();        // "Zebra"
Map<String, Integer> subMap = treeMap.subMap("A", "N"); // [Apple, Mango]
```

#### 4. Hashtable<K, V> (Legacy - Avoid)

```java
Map<String, Integer> map = new Hashtable<>();
```

**Characteristics:**
- Synchronized (thread-safe)
- No null keys or values
- Legacy class

**When to use:**
- ✗ **Don't use!** Use `ConcurrentHashMap` instead

#### 5. ConcurrentHashMap<K, V> (Thread-Safe)

```java
Map<String, Integer> map = new ConcurrentHashMap<>();
```

**Characteristics:**
- Thread-safe without locking entire map
- Better than synchronized HashMap
- No null keys or values

**When to use:**
- ✓ Multi-threaded environment
- ✓ High concurrency

### Common Map Operations

```java
Map<String, Integer> map = new HashMap<>();

// Adding/Updating
map.put("A", 1);              // Add or update
map.putIfAbsent("A", 2);      // Only if absent (returns 1)
map.putAll(otherMap);         // Add all from another map

// Accessing
Integer value = map.get("A"); // Get value (null if not found)
int val = map.getOrDefault("B", 0);  // Get with default

// Checking
boolean hasKey = map.containsKey("A");
boolean hasValue = map.containsValue(1);
boolean empty = map.isEmpty();
int size = map.size();

// Removing
map.remove("A");              // Remove by key
map.remove("A", 1);           // Remove if key maps to value
map.clear();                  // Remove all

// Iteration
// By entries
for (Map.Entry<String, Integer> entry : map.entrySet()) {
    String key = entry.getKey();
    Integer value = entry.getValue();
}

// By keys
for (String key : map.keySet()) {
    Integer value = map.get(key);
}

// By values
for (Integer value : map.values()) {
    System.out.println(value);
}

// Java 8+ operations
map.forEach((k, v) -> System.out.println(k + "=" + v));

map.compute("A", (k, v) -> v == null ? 1 : v + 1);  // Increment
map.merge("A", 1, Integer::sum);                    // Add to existing

map.replaceAll((k, v) -> v * 2);                    // Transform all values
```

---

## Utility Classes

### Collections Class

Static utility methods for collections.

```java
import java.util.Collections;

List<Integer> list = new ArrayList<>(Arrays.asList(3, 1, 4, 1, 5));

// Sorting
Collections.sort(list);                    // [1, 1, 3, 4, 5]
Collections.sort(list, Comparator.reverseOrder());  // [5, 4, 3, 1, 1]

// Searching (must be sorted first)
int index = Collections.binarySearch(list, 3);  // Binary search

// Reversing
Collections.reverse(list);                 // Reverse order

// Shuffling
Collections.shuffle(list);                 // Random order

// Min/Max
int min = Collections.min(list);
int max = Collections.max(list);

// Frequency
int count = Collections.frequency(list, 1);  // Count occurrences

// Replacing
Collections.replaceAll(list, 1, 10);       // Replace all 1s with 10s

// Filling
Collections.fill(list, 0);                 // Fill with 0s

// Copying
List<Integer> dest = new ArrayList<>(Collections.nCopies(list.size(), 0));
Collections.copy(dest, list);              // Copy list to dest

// Unmodifiable views
List<Integer> unmodifiable = Collections.unmodifiableList(list);
Set<Integer> unmodifiableSet = Collections.unmodifiableSet(new HashSet<>(list));
Map<String, Integer> unmodifiableMap = Collections.unmodifiableMap(new HashMap<>());

// Synchronized wrappers (thread-safe)
List<Integer> syncList = Collections.synchronizedList(new ArrayList<>());
Set<Integer> syncSet = Collections.synchronizedSet(new HashSet<>());
Map<String, Integer> syncMap = Collections.synchronizedMap(new HashMap<>());

// Singleton collections
Set<Integer> singleton = Collections.singleton(42);      // Immutable set with one element
List<Integer> singletonList = Collections.singletonList(42);
Map<String, Integer> singletonMap = Collections.singletonMap("key", 42);

// Empty collections (immutable)
List<Integer> empty = Collections.emptyList();
Set<Integer> emptySet = Collections.emptySet();
Map<String, Integer> emptyMap = Collections.emptyMap();

// Checked collections (runtime type checking)
List<String> checked = Collections.checkedList(new ArrayList<>(), String.class);
```

### Arrays Class

Static utility methods for arrays.

```java
import java.util.Arrays;

int[] arr = {3, 1, 4, 1, 5};

// Sorting
Arrays.sort(arr);                          // [1, 1, 3, 4, 5]

// Searching (must be sorted)
int index = Arrays.binarySearch(arr, 3);   // 2

// Filling
Arrays.fill(arr, 0);                       // [0, 0, 0, 0, 0]

// Copying
int[] copy = Arrays.copyOf(arr, arr.length);
int[] range = Arrays.copyOfRange(arr, 1, 4);  // [from, to)

// Comparing
boolean equal = Arrays.equals(arr, copy);

// Converting to List
List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);  // Fixed-size list

// Converting to String
String str = Arrays.toString(arr);         // "[1, 1, 3, 4, 5]"

// Stream operations (Java 8+)
int sum = Arrays.stream(arr).sum();
double avg = Arrays.stream(arr).average().orElse(0);
int max = Arrays.stream(arr).max().orElse(0);
```

---

## Algorithm Complexity

### Time Complexity Comparison

| Operation | ArrayList | LinkedList | HashSet | TreeSet | HashMap | TreeMap |
|-----------|-----------|------------|---------|---------|---------|---------|
| `add/put` | O(1)* | O(1) | O(1) | O(log n) | O(1) | O(log n) |
| `get` | O(1) | O(n) | - | - | O(1) | O(log n) |
| `contains` | O(n) | O(n) | O(1) | O(log n) | O(1) | O(log n) |
| `remove` | O(n) | O(n) | O(1) | O(log n) | O(1) | O(log n) |
| `Iteration` | O(n) | O(n) | O(n) | O(n) | O(n) | O(n) |

*ArrayList add is O(1) amortized (O(n) when resizing needed)

### Space Complexity

| Collection | Space per element | Notes |
|------------|------------------|-------|
| ArrayList | ~4 bytes overhead | Contiguous memory |
| LinkedList | ~24 bytes overhead | Node objects (data + 2 pointers) |
| HashSet | ~32 bytes overhead | HashMap + dummy value |
| TreeSet | ~40 bytes overhead | TreeMap + dummy value |
| HashMap | ~32 bytes overhead | Entry objects |

---

## Best Practices & When to Use What

### Choosing a List

```
Need random access by index?
├─ Yes → ArrayList
└─ No → Do you frequently add/remove at ends?
    ├─ Yes → LinkedList or ArrayDeque
    └─ No → ArrayList (general purpose)
```

### Choosing a Set

```
Need elements sorted?
├─ Yes → TreeSet
└─ No → Need insertion order?
    ├─ Yes → LinkedHashSet
    └─ No → HashSet (fastest)
```

### Choosing a Map

```
Need keys sorted?
├─ Yes → TreeMap
└─ No → Need insertion order?
    ├─ Yes → LinkedHashMap
    └─ No → Need thread-safety?
        ├─ Yes → ConcurrentHashMap
        └─ No → HashMap (fastest)
```

### Choosing a Queue/Deque

```
Need priority ordering?
├─ Yes → PriorityQueue
└─ No → Need double-ended?
    ├─ Yes → ArrayDeque
    └─ No → ArrayDeque (better than LinkedList)
```

### General Best Practices

1. **Program to Interface, Not Implementation**
   ```java
   // Good
   List<String> list = new ArrayList<>();
   Set<Integer> set = new HashSet<>();
   Map<String, Integer> map = new HashMap<>();

   // Bad
   ArrayList<String> list = new ArrayList<>();
   ```

2. **Specify Initial Capacity if Known**
   ```java
   List<String> list = new ArrayList<>(1000);  // Avoid resizing
   Map<String, Integer> map = new HashMap<>(1000);
   ```

3. **Use Enhanced For-Loop When Possible**
   ```java
   for (String item : list) {
       // Process item
   }
   ```

4. **Use Iterator for Safe Removal**
   ```java
   Iterator<String> it = list.iterator();
   while (it.hasNext()) {
       if (it.next().startsWith("A")) {
           it.remove();  // Safe removal during iteration
       }
   }
   ```

5. **Prefer `isEmpty()` over `size() == 0`**
   ```java
   if (list.isEmpty()) { }  // More readable
   ```

6. **Use Diamond Operator (Java 7+)**
   ```java
   Map<String, List<Integer>> map = new HashMap<>();  // Not: new HashMap<String, List<Integer>>()
   ```

7. **Use `Arrays.asList()` Carefully**
   ```java
   List<Integer> list = Arrays.asList(1, 2, 3);  // Fixed-size! Can't add/remove

   // For mutable list:
   List<Integer> mutable = new ArrayList<>(Arrays.asList(1, 2, 3));
   ```

8. **Null Handling**
   ```java
   // HashMap allows null key and values
   // TreeMap doesn't allow null keys
   // ConcurrentHashMap doesn't allow null keys or values
   // Most queues don't allow null
   ```

---

## Common Patterns

### Converting Between Collections

```java
// List → Set (remove duplicates)
Set<String> set = new HashSet<>(list);

// Set → List
List<String> list = new ArrayList<>(set);

// Array → List
Integer[] array = {1, 2, 3};
List<Integer> list = Arrays.asList(array);  // Fixed size
List<Integer> mutableList = new ArrayList<>(Arrays.asList(array));

// List → Array
String[] array = list.toArray(new String[0]);

// Map keys/values → List
List<String> keys = new ArrayList<>(map.keySet());
List<Integer> values = new ArrayList<>(map.values());
```

### Frequency Map Pattern

```java
// Count occurrences
Map<String, Integer> frequency = new HashMap<>();
for (String word : words) {
    frequency.put(word, frequency.getOrDefault(word, 0) + 1);
}

// Or with merge (Java 8+)
for (String word : words) {
    frequency.merge(word, 1, Integer::sum);
}
```

### Grouping Pattern

```java
// Group items by some property
Map<Integer, List<String>> byLength = new HashMap<>();
for (String word : words) {
    byLength.computeIfAbsent(word.length(), k -> new ArrayList<>()).add(word);
}

// Java 8+ Stream version
Map<Integer, List<String>> grouped = words.stream()
    .collect(Collectors.groupingBy(String::length));
```

---

This comprehensive guide covers all major aspects of Java Collections Framework!
