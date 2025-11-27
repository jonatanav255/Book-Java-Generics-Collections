package com.example;

/**
 * EXAMPLE 3: Generic Methods
 * Difficulty: Medium
 *
 * Demonstrates generic methods - methods that introduce their own type parameters.
 * The method itself is generic, not necessarily the class.
 */
public class Example3_GenericMethods {

    public static void main(String[] args) {
        System.out.println("=== EXAMPLE 3: Generic Methods ===\n");

        // Generic method for printing arrays
        String[] names = {"Alice", "Bob", "Charlie"};
        Integer[] numbers = {1, 2, 3, 4, 5};
        Double[] prices = {9.99, 19.99, 29.99};

        System.out.println("String array:");
        printArray(names);

        System.out.println("\nInteger array:");
        printArray(numbers);

        System.out.println("\nDouble array:");
        printArray(prices);

        System.out.println();

        // Generic method for finding middle element
        String middleName = getMiddleElement(names);
        Integer middleNumber = getMiddleElement(numbers);

        System.out.println("Middle of names: " + middleName);
        System.out.println("Middle of numbers: " + middleNumber);

        System.out.println();

        // Generic method for swapping
        Box<String> boxA = new Box<>("A");
        Box<String> boxB = new Box<>("B");

        System.out.println("Before swap: boxA=" + boxA + ", boxB=" + boxB);
        swap(boxA, boxB);
        System.out.println("After swap: boxA=" + boxA + ", boxB=" + boxB);

        System.out.println("\n" + "=".repeat(50) + "\n");
    }

    // Generic method - note the <T> before return type
    public static <T> void printArray(T[] array) {
        System.out.print("[ ");
        for (T element : array) {
            System.out.print(element + " ");
        }
        System.out.println("]");
    }

    // Generic method with return type
    public static <T> T getMiddleElement(T[] array) {
        if (array == null || array.length == 0) {
            return null;
        }
        return array[array.length / 2];
    }

    // Generic method that modifies objects
    public static <T> void swap(Box<T> box1, Box<T> box2) {
        T temp = box1.getBoxItem();
        box1.setBoxItem(box2.getBoxItem());
        box2.setBoxItem(temp);
    }

    // Generic method with multiple type parameters
    public static <K, V> void printPair(K key, V value) {
        System.out.println(key + " => " + value);
    }
}
