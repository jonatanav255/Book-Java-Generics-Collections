package com.example;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * EXAMPLE 8: Intersection Types & Multiple Bounds
 * Difficulty: Expert
 *
 * Demonstrates:
 * - Multiple type bounds (T extends A & B & C)
 * - Intersection types in practice
 * - Type witness pattern
 * - Combining interfaces with generics
 */
public class Example8_IntersectionTypes {

    public static void main(String[] args) {
        System.out.println("=== EXAMPLE 8: Intersection Types & Multiple Bounds ===\n");

        // 1. Multiple bounds - must satisfy ALL constraints
        System.out.println("--- Multiple Bounds ---");

        SerializableEmployee emp1 = new SerializableEmployee("Alice", 75000, 1);
        SerializableEmployee emp2 = new SerializableEmployee("Bob", 85000, 2);

        // This method requires: Comparable AND Serializable AND Cloneable
        SerializableEmployee clonedEmp = cloneAndCompare(emp1, emp2);
        System.out.println("Cloned highest paid: " + clonedEmp);
        System.out.println("Original: " + emp2);
        System.out.println("Are they same object? " + (clonedEmp == emp2));

        System.out.println();

        // 2. Intersection types with custom interfaces
        System.out.println("--- Custom Intersection Types ---");

        SmartList<String> stringList = new SmartList<>();
        stringList.add("Apple");
        stringList.add("Banana");
        stringList.add("Cherry");

        // Requires BOTH Iterable AND Measurable
        processCollection(stringList);

        System.out.println();

        // 3. Type witness pattern - explicitly specify type at call site
        System.out.println("--- Type Witness Pattern ---");

        List<String> strings = List.of("Hello", "World");
        // Type witness: .<String, Integer> tells compiler the types
        List<Integer> lengths = transform(strings, String::length);
        System.out.println("Strings: " + strings);
        System.out.println("Lengths: " + lengths);

        List<Integer> numbers = List.of(1, 2, 3, 4, 5);
        List<Double> doubles = transform(numbers, Integer::doubleValue);
        System.out.println("Numbers: " + numbers);
        System.out.println("Doubles: " + doubles);

        System.out.println();

        // 4. Multiple bounds with different combinations
        System.out.println("--- Complex Bound Combinations ---");

        RichData data1 = new RichData("DataSet-1", 100);
        RichData data2 = new RichData("DataSet-2", 200);

        System.out.println("Comparing data1 and data2: " + compareAndSerialize(data1, data2));

        System.out.println();

        // 5. Bounded wildcards with intersection types
        System.out.println("--- Wildcards with Multiple Bounds ---");

        List<SerializableEmployee> employees = new ArrayList<>();
        employees.add(emp1);
        employees.add(emp2);

        SerializableEmployee max = findMaxSerializable(employees);
        System.out.println("Max employee: " + max);

        System.out.println();

        // 6. Three or more bounds
        System.out.println("--- Three or More Bounds ---");

        SuperType obj = new SuperType("Super", 42);
        processSuper(obj);

        System.out.println("\n" + "=".repeat(50) + "\n");
    }

    // 1. Multiple bounds: T must implement ALL three interfaces
    public static <T extends Comparable<T> & Serializable & Cloneable> T cloneAndCompare(T obj1, T obj2) {
        try {
            T max = obj1.compareTo(obj2) > 0 ? obj1 : obj2;

            // Can call clone() because T extends Cloneable
            // Must use reflection because clone() is protected in Object
            @SuppressWarnings("unchecked")
            T cloned = (T) max.getClass().getMethod("clone").invoke(max);

            System.out.println("Selected and cloned: " + cloned);
            return cloned;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 2. Custom interfaces for intersection
    interface Measurable {
        int getSize();
    }

    interface Processable {
        void process();
    }

    // Method requiring multiple custom interfaces
    public static <T extends Iterable<?> & Measurable> void processCollection(T collection) {
        System.out.println("Collection size: " + collection.getSize());
        System.out.print("Elements: ");
        for (Object item : collection) {
            System.out.print(item + " ");
        }
        System.out.println();
    }

    // SmartList implements multiple interfaces
    static class SmartList<E> extends ArrayList<E> implements Measurable {
        @Override
        public int getSize() {
            return size();
        }
    }

    // 3. Type witness pattern - helps compiler infer types
    public static <T, R> List<R> transform(List<T> input, Transformer<T, R> transformer) {
        List<R> result = new ArrayList<>();
        for (T item : input) {
            result.add(transformer.transform(item));
        }
        return result;
    }

    @FunctionalInterface
    interface Transformer<T, R> {
        R transform(T input);
    }

    // 4. Employee that satisfies multiple bounds
    static class SerializableEmployee implements Comparable<SerializableEmployee>, Serializable, Cloneable {
        private String name;
        private double salary;
        private int id;

        public SerializableEmployee(String name, double salary, int id) {
            this.name = name;
            this.salary = salary;
            this.id = id;
        }

        @Override
        public int compareTo(SerializableEmployee other) {
            return Double.compare(this.salary, other.salary);
        }

        @Override
        public SerializableEmployee clone() {
            try {
                return (SerializableEmployee) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public String toString() {
            return "Employee{name='" + name + "', salary=" + salary + ", id=" + id + "}";
        }
    }

    // Complex bound with Number
    public static <T extends Number & Comparable<T> & Serializable> String compareAndSerialize(T a, T b) {
        int comparison = a.compareTo(b);
        String result = comparison > 0 ? a + " > " + b : comparison < 0 ? a + " < " + b : a + " = " + b;

        // Can use Number methods
        System.out.println("Sum as doubles: " + (a.doubleValue() + b.doubleValue()));

        return result;
    }

    static class RichData extends Number implements Comparable<RichData>, Serializable {
        private String label;
        private double value;

        public RichData(String label, double value) {
            this.label = label;
            this.value = value;
        }

        @Override
        public int compareTo(RichData other) {
            return Double.compare(this.value, other.value);
        }

        @Override
        public int intValue() {
            return (int) value;
        }

        @Override
        public long longValue() {
            return (long) value;
        }

        @Override
        public float floatValue() {
            return (float) value;
        }

        @Override
        public double doubleValue() {
            return value;
        }

        @Override
        public String toString() {
            return label + "=" + value;
        }
    }

    // 5. Wildcard with multiple bounds
    public static <T extends Comparable<T> & Serializable> T findMaxSerializable(List<T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        T max = list.get(0);
        for (T item : list) {
            if (item.compareTo(max) > 0) {
                max = item;
            }
        }
        return max;
    }

    // 6. Three or more bounds
    public static <T extends Serializable & Cloneable & Comparable<T> & Processable> void processSuper(T obj) {
        System.out.println("Processing object: " + obj);
        obj.process();
        System.out.println("Object is Serializable: true");
        System.out.println("Object is Cloneable: true");
        System.out.println("Object is Comparable: true");
        System.out.println("Object is Processable: true");
    }

    static class SuperType implements Serializable, Cloneable, Comparable<SuperType>, Processable {
        private String name;
        private int value;

        public SuperType(String name, int value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public void process() {
            System.out.println("Processing: " + name + " with value " + value);
        }

        @Override
        public int compareTo(SuperType other) {
            return Integer.compare(this.value, other.value);
        }

        @Override
        protected SuperType clone() {
            try {
                return (SuperType) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public String toString() {
            return "SuperType{name='" + name + "', value=" + value + "}";
        }
    }
}
