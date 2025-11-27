package com.example;

import java.util.ArrayList;
import java.util.List;

/**
 * EXAMPLE 5: Wildcards (?, ? extends, ? super)
 * Difficulty: Hard
 *
 * Demonstrates wildcard usage in generics.
 * Wildcards allow you to work with unknown types in a flexible way.
 */
public class Example5_Wildcards {

    public static void main(String[] args) {
        System.out.println("=== EXAMPLE 5: Wildcards ===\n");

        // Create lists of different types
        List<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(2);
        integers.add(3);

        List<Double> doubles = new ArrayList<>();
        doubles.add(1.1);
        doubles.add(2.2);
        doubles.add(3.3);

        List<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("World");

        // Unbounded wildcard - can accept any type
        System.out.println("--- Unbounded Wildcard (?) ---");
        printList(integers);
        printList(doubles);
        printList(strings);

        System.out.println();

        // Upper bounded wildcard - ? extends Number
        System.out.println("--- Upper Bounded Wildcard (? extends Number) ---");
        System.out.println("Sum of integers: " + sumNumbers(integers));
        System.out.println("Sum of doubles: " + sumNumbers(doubles));
        // sumNumbers(strings); // Would NOT compile - String is not a Number

        System.out.println();

        // Lower bounded wildcard - ? super Integer
        System.out.println("--- Lower Bounded Wildcard (? super Integer) ---");
        List<Number> numbers = new ArrayList<>();
        List<Object> objects = new ArrayList<>();

        addIntegers(integers);
        addIntegers(numbers);
        addIntegers(objects);

        System.out.println("Integers list: " + integers);
        System.out.println("Numbers list: " + numbers);
        System.out.println("Objects list: " + objects);

        System.out.println();

        // Practical example: copying with wildcards
        System.out.println("--- Copying Between Lists ---");
        List<Integer> source = new ArrayList<>();
        source.add(10);
        source.add(20);
        source.add(30);

        List<Number> destination = new ArrayList<>();
        copyList(source, destination);
        System.out.println("Copied from Integer to Number: " + destination);

        System.out.println();

        // Wildcard with Box example
        System.out.println("--- Wildcards with Box ---");
        Box<String> stringBox = new Box<>("Hello");
        Box<Integer> intBox = new Box<>(42);

        printBox(stringBox);
        printBox(intBox);

        Box<Integer> numBox1 = new Box<>(100);
        Box<Double> numBox2 = new Box<>(99.5);

        printNumberBox(numBox1);
        printNumberBox(numBox2);
        // printNumberBox(stringBox); // Would NOT compile

        System.out.println("\n" + "=".repeat(50) + "\n");
    }

    // Unbounded wildcard - accepts list of ANY type
    // Can only READ as Object, cannot ADD anything (except null)
    public static void printList(List<?> list) {
        System.out.print("List contents: [ ");
        for (Object item : list) {
            System.out.print(item + " ");
        }
        System.out.println("]");
    }

    // Upper bounded wildcard - accepts list of Number or any subtype
    // Can READ as Number, but cannot ADD anything (except null)
    public static double sumNumbers(List<? extends Number> list) {
        double sum = 0.0;
        for (Number num : list) {
            sum += num.doubleValue();
        }
        return sum;
    }

    // Lower bounded wildcard - accepts list of Integer or any supertype
    // Can ADD Integers, but can only READ as Object
    public static void addIntegers(List<? super Integer> list) {
        list.add(100);
        list.add(200);
    }

    // Combined example: source uses extends, destination uses super
    // This is the "PECS" principle: Producer Extends, Consumer Super
    public static <T> void copyList(List<? extends T> source, List<? super T> destination) {
        for (T item : source) {
            destination.add(item);
        }
    }

    // Wildcard with Box - accepts Box of any type
    public static void printBox(Box<?> box) {
        System.out.println("Box contains: " + box.getBoxItem());
    }

    // Upper bounded wildcard with Box - only accepts Box<Number> or subtypes
    public static void printNumberBox(Box<? extends Number> box) {
        Number num = box.getBoxItem();
        if (num != null) {
            System.out.println("Number box value: " + num.doubleValue());
        } else {
            System.out.println("Number box is empty");
        }
    }
}
