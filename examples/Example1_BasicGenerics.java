package com.example;

/**
 * EXAMPLE 1: Basic Generic Class Usage
 * Difficulty: Easy
 *
 * Demonstrates how a single generic type parameter works.
 * The Box class can hold any type of object.
 */
public class Example1_BasicGenerics {

    public static void main(String[] args) {
        System.out.println("=== EXAMPLE 1: Basic Generics ===\n");

        // Create a Box for String
        Box<String> stringBox = new Box<>("Hello Generics!");
        System.out.println("String box: " + stringBox);
        System.out.println("Value: " + stringBox.getBoxItem());
        System.out.println("Is empty? " + stringBox.checkIfBoxIsEmpty());

        System.out.println();

        // Create a Box for Integer
        Box<Integer> intBox = new Box<>(42);
        System.out.println("Integer box: " + intBox);
        System.out.println("Value: " + intBox.getBoxItem());

        System.out.println();

        // Create a Box for custom object
        Box<Person> personBox = new Box<>(new Person("Alice", 30));
        System.out.println("Person box: " + personBox);
        System.out.println("Value: " + personBox.getBoxItem());

        System.out.println();

        // Empty box
        Box<String> emptyBox = new Box<>(null);
        System.out.println("Empty box: " + emptyBox);
        System.out.println("Is empty? " + emptyBox.checkIfBoxIsEmpty());

        System.out.println("\n" + "=".repeat(50) + "\n");
    }

    // Simple Person class for demonstration
    static class Person {
        String name;
        int age;

        Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return "Person{name='" + name + "', age=" + age + "}";
        }
    }
}
