package com.example;

/**
 * EXAMPLE 2: Multiple Type Parameters
 * Difficulty: Medium
 *
 * Demonstrates using multiple generic type parameters in a single class.
 * A Pair class that holds two different types.
 */
public class Example2_MultipleTypeParameters {

    public static void main(String[] args) {
        System.out.println("=== EXAMPLE 2: Multiple Type Parameters ===\n");

        // Pair of String and Integer
        Pair<String, Integer> nameAge = new Pair<>("Bob", 25);
        System.out.println(nameAge);
        System.out.println("First: " + nameAge.getFirst());
        System.out.println("Second: " + nameAge.getSecond());

        System.out.println();

        // Pair of Integer and String (reversed types)
        Pair<Integer, String> idName = new Pair<>(101, "Charlie");
        System.out.println(idName);

        System.out.println();

        // Pair of same types
        Pair<Double, Double> coordinates = new Pair<>(10.5, 20.3);
        System.out.println("Coordinates: " + coordinates);

        System.out.println();

        // Nested generics - Pair of Boxes!
        Box<String> box1 = new Box<>("First Box");
        Box<Integer> box2 = new Box<>(999);
        Pair<Box<String>, Box<Integer>> pairOfBoxes = new Pair<>(box1, box2);
        System.out.println("Pair of boxes: " + pairOfBoxes);

        System.out.println("\n" + "=".repeat(50) + "\n");
    }

    // Generic class with TWO type parameters
    static class Pair<K, V> {
        private K first;
        private V second;

        public Pair(K first, V second) {
            this.first = first;
            this.second = second;
        }

        public K getFirst() {
            return first;
        }

        public V getSecond() {
            return second;
        }

        public void setFirst(K first) {
            this.first = first;
        }

        public void setSecond(V second) {
            this.second = second;
        }

        @Override
        public String toString() {
            return "Pair[" + first + ", " + second + "]";
        }
    }
}
