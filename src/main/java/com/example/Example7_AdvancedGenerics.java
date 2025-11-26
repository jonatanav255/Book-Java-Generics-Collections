package com.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * EXAMPLE 7: Advanced Generics - Type Erasure, Recursive Types, and Generic Arrays
 * Difficulty: Very Hard
 *
 * Demonstrates advanced generic concepts including:
 * - Recursive type bounds (self-referential generics)
 * - Type erasure and its implications
 * - Generic arrays and workarounds
 * - Builder pattern with generics
 * - Method chaining with self-types
 */
public class Example7_AdvancedGenerics {

    public static void main(String[] args) {
        System.out.println("=== EXAMPLE 7: Advanced Generics ===\n");

        // 1. Recursive Type Bounds (Comparable pattern)
        System.out.println("--- Recursive Type Bounds ---");
        Employee emp1 = new Employee("Alice", 75000);
        Employee emp2 = new Employee("Bob", 85000);
        Employee emp3 = new Employee("Charlie", 70000);

        List<Employee> employees = Arrays.asList(emp1, emp2, emp3);
        Employee maxSalary = findMax(employees);
        System.out.println("Highest paid employee: " + maxSalary);

        System.out.println();

        // 2. Builder Pattern with Generics
        System.out.println("--- Builder Pattern with Generics ---");
        Pizza pizza = new Pizza.Builder()
                .size("Large")
                .addTopping("Cheese")
                .addTopping("Pepperoni")
                .addTopping("Mushrooms")
                .build();
        System.out.println(pizza);

        Burger burger = new Burger.Builder()
                .size("Medium")
                .addTopping("Lettuce")
                .addTopping("Tomato")
                .isVegetarian(true)
                .build();
        System.out.println(burger);

        System.out.println();

        // 3. Generic Array Workarounds
        System.out.println("--- Generic Arrays ---");
        GenericStack<String> stringStack = new GenericStack<>(5);
        stringStack.push("First");
        stringStack.push("Second");
        stringStack.push("Third");

        System.out.println("Pop: " + stringStack.pop());
        System.out.println("Pop: " + stringStack.pop());
        System.out.println("Size: " + stringStack.size());

        System.out.println();

        GenericStack<Integer> intStack = new GenericStack<>(3);
        intStack.push(100);
        intStack.push(200);
        intStack.push(300);

        System.out.println("Integer stack pop: " + intStack.pop());
        System.out.println("Integer stack size: " + intStack.size());

        System.out.println();

        // 4. Type Tokens (avoiding type erasure)
        System.out.println("--- Type Tokens ---");
        TypeSafeContainer<String> stringContainer = new TypeSafeContainer<>(String.class);
        stringContainer.set("Hello World");
        System.out.println("String container value: " + stringContainer.get());
        System.out.println("Container type: " + stringContainer.getType().getSimpleName());

        TypeSafeContainer<Integer> intContainer = new TypeSafeContainer<>(Integer.class);
        intContainer.set(42);
        System.out.println("Integer container value: " + intContainer.get());
        System.out.println("Container type: " + intContainer.getType().getSimpleName());

        System.out.println();

        // 5. Fluent API with self-types
        System.out.println("--- Fluent API ---");
        Query<Employee> query = new EmployeeQuery()
                .where(e -> e.getSalary() > 70000)
                .limit(2)
                .execute(employees);
        System.out.println("Filtered employees: " + query.getResults());

        System.out.println();

        // 6. Combining multiple advanced concepts
        System.out.println("--- Complex Generic Chains ---");
        List<List<Integer>> matrix = new ArrayList<>();
        matrix.add(Arrays.asList(1, 2, 3));
        matrix.add(Arrays.asList(4, 5, 6));
        matrix.add(Arrays.asList(7, 8, 9));

        List<Integer> flattened = flatten(matrix);
        System.out.println("Flattened matrix: " + flattened);

        System.out.println("\n" + "=".repeat(50) + "\n");
    }

    // 1. Recursive Type Bound - T must be comparable to itself
    public static <T extends Comparable<T>> T findMax(List<T> list) {
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

    // Employee class with recursive type bound
    static class Employee implements Comparable<Employee> {
        private String name;
        private double salary;

        public Employee(String name, double salary) {
            this.name = name;
            this.salary = salary;
        }

        public String getName() {
            return name;
        }

        public double getSalary() {
            return salary;
        }

        @Override
        public int compareTo(Employee other) {
            return Double.compare(this.salary, other.salary);
        }

        @Override
        public String toString() {
            return name + " ($" + salary + ")";
        }
    }

    // 2. Builder Pattern with self-referential generics
    static abstract class Food {
        protected String size;
        protected List<String> toppings;

        protected Food(Builder<?> builder) {
            this.size = builder.size;
            this.toppings = builder.toppings;
        }

        // Self-bounded generic builder
        abstract static class Builder<T extends Builder<T>> {
            protected String size;
            protected List<String> toppings = new ArrayList<>();

            // Self-type trick - returns T, not Builder
            protected abstract T self();

            public T size(String size) {
                this.size = size;
                return self();
            }

            public T addTopping(String topping) {
                toppings.add(topping);
                return self();
            }
        }
    }

    static class Pizza extends Food {
        private Pizza(Builder builder) {
            super(builder);
        }

        static class Builder extends Food.Builder<Builder> {
            @Override
            protected Builder self() {
                return this;
            }

            public Pizza build() {
                return new Pizza(this);
            }
        }

        @Override
        public String toString() {
            return "Pizza{size='" + size + "', toppings=" + toppings + "}";
        }
    }

    static class Burger extends Food {
        private boolean vegetarian;

        private Burger(Builder builder) {
            super(builder);
            this.vegetarian = builder.vegetarian;
        }

        static class Builder extends Food.Builder<Builder> {
            private boolean vegetarian = false;

            @Override
            protected Builder self() {
                return this;
            }

            public Builder isVegetarian(boolean vegetarian) {
                this.vegetarian = vegetarian;
                return this;
            }

            public Burger build() {
                return new Burger(this);
            }
        }

        @Override
        public String toString() {
            return "Burger{size='" + size + "', toppings=" + toppings + ", vegetarian=" + vegetarian + "}";
        }
    }

    // 3. Generic Array Workaround - using Object[] internally
    static class GenericStack<T> {
        private Object[] elements;
        private int size = 0;

        @SuppressWarnings("unchecked")
        public GenericStack(int capacity) {
            // Can't create generic array directly: new T[capacity]
            // Must use Object[] and cast
            elements = new Object[capacity];
        }

        public void push(T item) {
            if (size == elements.length) {
                throw new IllegalStateException("Stack is full");
            }
            elements[size++] = item;
        }

        @SuppressWarnings("unchecked")
        public T pop() {
            if (size == 0) {
                throw new IllegalStateException("Stack is empty");
            }
            T item = (T) elements[--size];
            elements[size] = null; // Help GC
            return item;
        }

        public int size() {
            return size;
        }
    }

    // 4. Type Token to preserve type information at runtime
    static class TypeSafeContainer<T> {
        private final Class<T> type;
        private T value;

        public TypeSafeContainer(Class<T> type) {
            this.type = type;
        }

        public void set(T value) {
            this.value = value;
        }

        public T get() {
            return value;
        }

        public Class<T> getType() {
            return type;
        }
    }

    // 5. Fluent API with self-types
    static abstract class Query<T> {
        protected List<T> results;

        public List<T> getResults() {
            return results;
        }
    }

    static class EmployeeQuery extends Query<Employee> {
        private Predicate<Employee> predicate;
        private int limitCount = Integer.MAX_VALUE;

        public EmployeeQuery where(Predicate<Employee> predicate) {
            this.predicate = predicate;
            return this;
        }

        public EmployeeQuery limit(int count) {
            this.limitCount = count;
            return this;
        }

        public EmployeeQuery execute(List<Employee> data) {
            results = new ArrayList<>();
            int count = 0;
            for (Employee emp : data) {
                if (predicate == null || predicate.test(emp)) {
                    results.add(emp);
                    count++;
                    if (count >= limitCount) {
                        break;
                    }
                }
            }
            return this;
        }
    }

    // 6. Nested generics - flattening a 2D list
    public static <T> List<T> flatten(List<List<T>> nestedList) {
        List<T> result = new ArrayList<>();
        for (List<T> innerList : nestedList) {
            result.addAll(innerList);
        }
        return result;
    }
}
