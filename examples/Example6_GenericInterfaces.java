package com.example;

import java.util.ArrayList;
import java.util.List;

/**
 * EXAMPLE 6: Generic Interfaces
 * Difficulty: Hard
 *
 * Demonstrates how to create and implement generic interfaces.
 * Shows how interfaces can define generic contracts.
 */
public class Example6_GenericInterfaces {

    public static void main(String[] args) {
        System.out.println("=== EXAMPLE 6: Generic Interfaces ===\n");

        // Using Container interface with different types
        System.out.println("--- Container Interface ---");
        Container<String> stringContainer = new SimpleContainer<>();
        stringContainer.add("Apple");
        stringContainer.add("Banana");
        stringContainer.add("Cherry");

        System.out.println("String container size: " + stringContainer.size());
        System.out.println("First item: " + stringContainer.get(0));
        System.out.println("All items: " + stringContainer.getAll());

        System.out.println();

        Container<Integer> intContainer = new SimpleContainer<>();
        intContainer.add(10);
        intContainer.add(20);
        intContainer.add(30);

        System.out.println("Integer container size: " + intContainer.size());
        System.out.println("All items: " + intContainer.getAll());

        System.out.println();

        // Using Transformer interface
        System.out.println("--- Transformer Interface ---");
        Transformer<String, Integer> stringLength = new StringLengthTransformer();
        System.out.println("Transform 'Hello': " + stringLength.transform("Hello"));
        System.out.println("Transform 'World': " + stringLength.transform("World"));

        System.out.println();

        Transformer<Integer, String> intToString = new IntegerFormatter();
        System.out.println("Transform 42: " + intToString.transform(42));
        System.out.println("Transform 100: " + intToString.transform(100));

        System.out.println();

        // Using Comparator-like interface
        System.out.println("--- Comparator Interface ---");
        Comparer<Integer> intComparer = new IntegerComparer();
        System.out.println("Compare 5 and 10: " + intComparer.compare(5, 10));
        System.out.println("Compare 10 and 5: " + intComparer.compare(10, 5));
        System.out.println("Compare 7 and 7: " + intComparer.compare(7, 7));

        System.out.println();

        Comparer<String> stringComparer = new StringLengthComparer();
        System.out.println("Compare 'cat' and 'elephant': " + stringComparer.compare("cat", "elephant"));
        System.out.println("Compare 'dog' and 'cat': " + stringComparer.compare("dog", "cat"));

        System.out.println();

        // Using Repository pattern with generics
        System.out.println("--- Repository Pattern ---");
        Repository<Person> personRepo = new PersonRepository();

        Person p1 = new Person(1, "Alice", 30);
        Person p2 = new Person(2, "Bob", 25);

        personRepo.save(p1);
        personRepo.save(p2);

        System.out.println("Find person 1: " + personRepo.findById(1));
        System.out.println("Find person 2: " + personRepo.findById(2));
        System.out.println("All persons: " + personRepo.findAll());

        personRepo.delete(1);
        System.out.println("After deleting person 1: " + personRepo.findAll());

        System.out.println("\n" + "=".repeat(50) + "\n");
    }

    // Generic interface - defines a contract for a container
    interface Container<T> {
        void add(T item);
        T get(int index);
        int size();
        List<T> getAll();
    }

    // Implementation of generic interface
    static class SimpleContainer<T> implements Container<T> {
        private List<T> items = new ArrayList<>();

        @Override
        public void add(T item) {
            items.add(item);
        }

        @Override
        public T get(int index) {
            return items.get(index);
        }

        @Override
        public int size() {
            return items.size();
        }

        @Override
        public List<T> getAll() {
            return new ArrayList<>(items);
        }
    }

    // Generic interface with two type parameters - transforms from one type to another
    interface Transformer<INPUT, OUTPUT> {
        OUTPUT transform(INPUT input);
    }

    // Implementation that converts String to its length
    static class StringLengthTransformer implements Transformer<String, Integer> {
        @Override
        public Integer transform(String input) {
            return input.length();
        }
    }

    // Implementation that converts Integer to formatted String
    static class IntegerFormatter implements Transformer<Integer, String> {
        @Override
        public String transform(Integer input) {
            return "Number: " + input;
        }
    }

    // Generic interface for comparing objects
    interface Comparer<T> {
        int compare(T obj1, T obj2);
    }

    // Implementation for comparing integers
    static class IntegerComparer implements Comparer<Integer> {
        @Override
        public int compare(Integer obj1, Integer obj2) {
            return obj1.compareTo(obj2);
        }
    }

    // Implementation for comparing strings by length
    static class StringLengthComparer implements Comparer<String> {
        @Override
        public int compare(String obj1, String obj2) {
            return Integer.compare(obj1.length(), obj2.length());
        }
    }

    // Generic interface for repository pattern (CRUD operations)
    interface Repository<T extends Entity> {
        void save(T entity);
        T findById(int id);
        List<T> findAll();
        void delete(int id);
    }

    // Base entity interface
    interface Entity {
        int getId();
    }

    // Person class implementing Entity
    static class Person implements Entity {
        private int id;
        private String name;
        private int age;

        public Person(int id, String name, int age) {
            this.id = id;
            this.name = name;
            this.age = age;
        }

        @Override
        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        @Override
        public String toString() {
            return "Person{id=" + id + ", name='" + name + "', age=" + age + "}";
        }
    }

    // Implementation of Repository for Person
    static class PersonRepository implements Repository<Person> {
        private List<Person> database = new ArrayList<>();

        @Override
        public void save(Person entity) {
            database.add(entity);
        }

        @Override
        public Person findById(int id) {
            for (Person p : database) {
                if (p.getId() == id) {
                    return p;
                }
            }
            return null;
        }

        @Override
        public List<Person> findAll() {
            return new ArrayList<>(database);
        }

        @Override
        public void delete(int id) {
            database.removeIf(p -> p.getId() == id);
        }
    }
}
