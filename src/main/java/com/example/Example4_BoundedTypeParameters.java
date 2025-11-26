package com.example;

/**
 * EXAMPLE 4: Bounded Type Parameters
 * Difficulty: Medium-Hard
 *
 * Demonstrates bounded type parameters using "extends".
 * This restricts what types can be used with the generic.
 */
public class Example4_BoundedTypeParameters {

    public static void main(String[] args) {
        System.out.println("=== EXAMPLE 4: Bounded Type Parameters ===\n");

        // NumberBox can only hold Number types
        NumberBox<Integer> intBox = new NumberBox<>(100);
        NumberBox<Double> doubleBox = new NumberBox<>(99.5);
        NumberBox<Long> longBox = new NumberBox<>(1000000L);

        System.out.println("Integer box double value: " + intBox.doubleValue());
        System.out.println("Double box double value: " + doubleBox.doubleValue());
        System.out.println("Long box double value: " + longBox.doubleValue());

        System.out.println();

        // This would NOT compile:
        // NumberBox<String> stringBox = new NumberBox<>("hello"); // ERROR!

        // Finding maximum using bounded type parameter
        Integer max1 = findMax(10, 20);
        Double max2 = findMax(3.14, 2.71);
        String max3 = findMax("zebra", "apple");

        System.out.println("Max of 10 and 20: " + max1);
        System.out.println("Max of 3.14 and 2.71: " + max2);
        System.out.println("Max of 'zebra' and 'apple': " + max3);

        System.out.println();

        // Multiple bounds - T must be BOTH a Number AND Comparable
        ComparableBox<Integer> cb1 = new ComparableBox<>(50);
        ComparableBox<Double> cb2 = new ComparableBox<>(3.14);

        System.out.println("50 compared to 100: " + cb1.compareTo(100));
        System.out.println("3.14 compared to 2.71: " + cb2.compareTo(2.71));

        // This would NOT compile - String is Comparable but NOT a Number:
        // ComparableBox<String> cb3 = new ComparableBox<>("Hello"); // ERROR!

        System.out.println("\n" + "=".repeat(50) + "\n");
    }

    // Generic class with upper bound - T must extend Number
    static class NumberBox<T extends Number> {
        private T value;

        public NumberBox(T value) {
            this.value = value;
        }

        public T getValue() {
            return value;
        }

        // Now we can call Number methods on T!
        public double doubleValue() {
            return value.doubleValue();
        }

        public int intValue() {
            return value.intValue();
        }
    }

    // Generic method with upper bound - T must implement Comparable
    public static <T extends Comparable<T>> T findMax(T a, T b) {
        return a.compareTo(b) > 0 ? a : b;
    }

    // Generic class with multiple bounds
    // T must extend Number AND implement Comparable
    static class ComparableBox<T extends Number & Comparable<T>> {
        private T value;

        public ComparableBox(T value) {
            this.value = value;
        }

        public int compareTo(T other) {
            return value.compareTo(other);
        }

        public double getDoubleValue() {
            return value.doubleValue();
        }
    }
}
