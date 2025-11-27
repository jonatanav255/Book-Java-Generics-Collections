package com.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * EXAMPLE 9: Covariance, Contravariance, and Invariance
 * Difficulty: Expert
 *
 * Demonstrates:
 * - The difference between array covariance and generic invariance
 * - PECS principle (Producer Extends, Consumer Super) in depth
 * - Variance in function types
 * - Real-world applications of variance
 */
public class Example9_CovarianceContravariance {

    public static void main(String[] args) {
        System.out.println("=== EXAMPLE 9: Covariance & Contravariance ===\n");

        // 1. Arrays are COVARIANT (dangerous!)
        System.out.println("--- Array Covariance (The Problem) ---");
        demonstrateArrayCovariance();

        System.out.println();

        // 2. Generics are INVARIANT (safe!)
        System.out.println("--- Generic Invariance (The Solution) ---");
        demonstrateGenericInvariance();

        System.out.println();

        // 3. PECS in action - Producer Extends
        System.out.println("--- Producer Extends (Covariance) ---");
        demonstrateProducerExtends();

        System.out.println();

        // 4. PECS in action - Consumer Super
        System.out.println("--- Consumer Super (Contravariance) ---");
        demonstrateConsumerSuper();

        System.out.println();

        // 5. Function variance
        System.out.println("--- Function Type Variance ---");
        demonstrateFunctionVariance();

        System.out.println();

        // 6. Real-world PECS example: Collections.copy
        System.out.println("--- Real-World PECS: Copy Method ---");
        demonstrateCopyMethod();

        System.out.println();

        // 7. Contravariance in Comparators
        System.out.println("--- Contravariance in Comparators ---");
        demonstrateComparatorContravariance();

        System.out.println("\n" + "=".repeat(50) + "\n");
    }

    // 1. Array covariance - dangerous and can cause runtime errors!
    static void demonstrateArrayCovariance() {
        // Arrays are covariant: if Dog extends Animal, then Dog[] is a subtype of Animal[]
        Animal[] animals = new Dog[3]; // COMPILES! But dangerous...
        animals[0] = new Dog("Buddy");
        animals[1] = new Dog("Max");

        System.out.println("Added dogs successfully");

        try {
            // This compiles but fails at RUNTIME!
            animals[2] = new Cat("Whiskers"); // ArrayStoreException!
        } catch (ArrayStoreException e) {
            System.out.println("ERROR: Can't add Cat to Dog[] array (caught ArrayStoreException)");
        }
    }

    // 2. Generic invariance - prevents the problem!
    static void demonstrateGenericInvariance() {
        // Generics are INVARIANT: List<Dog> is NOT a subtype of List<Animal>
        List<Dog> dogs = new ArrayList<>();
        dogs.add(new Dog("Buddy"));

        // This does NOT compile (uncomment to see error):
        // List<Animal> animals = dogs; // COMPILE ERROR - prevents the problem!

        System.out.println("Generics prevent the array covariance problem at compile time!");
    }

    // 3. Producer Extends - when you READ from a structure
    static void demonstrateProducerExtends() {
        List<Dog> dogs = Arrays.asList(new Dog("Buddy"), new Dog("Max"));
        List<Cat> cats = Arrays.asList(new Cat("Whiskers"), new Cat("Fluffy"));

        // <? extends Animal> means "some unknown subtype of Animal"
        // You can READ as Animal, but can't ADD anything (except null)

        System.out.println("Processing dogs:");
        processAnimals(dogs); // Works! Dog extends Animal

        System.out.println("Processing cats:");
        processAnimals(cats); // Works! Cat extends Animal

        // Why "Producer Extends"? The list PRODUCES Animal objects for us to read
    }

    // Producer - we READ from the list (produces values)
    static void processAnimals(List<? extends Animal> animals) {
        for (Animal animal : animals) {
            animal.makeSound();
        }

        // Can't add to producer:
        // animals.add(new Dog("Fido")); // COMPILE ERROR!
        // Why? We don't know the exact type - could be List<Dog>, List<Cat>, etc.
    }

    // 4. Consumer Super - when you WRITE to a structure
    static void demonstrateConsumerSuper() {
        List<Animal> animals = new ArrayList<>();
        List<Object> objects = new ArrayList<>();

        // <? super Dog> means "some unknown supertype of Dog"
        // You can ADD Dogs, but can only READ as Object

        System.out.println("Adding dogs to animals list:");
        addDogs(animals); // Works! Animal is super of Dog

        System.out.println("Adding dogs to objects list:");
        addDogs(objects); // Works! Object is super of Dog

        System.out.println("Animals list: " + animals);
        System.out.println("Objects list: " + objects);

        // Why "Consumer Super"? The list CONSUMES Dog objects we add
    }

    // Consumer - we WRITE to the list (consumes values)
    static void addDogs(List<? super Dog> list) {
        list.add(new Dog("Rover"));
        list.add(new Dog("Spot"));

        // Can read but only as Object:
        Object obj = list.get(0);
        // Dog dog = list.get(0); // COMPILE ERROR!
        // Why? We don't know exact type - could be List<Animal>, List<Object>, etc.

        System.out.println("Added 2 dogs to the list");
    }

    // 5. Function variance - contravariant in input, covariant in output
    static void demonstrateFunctionVariance() {
        // Function<INPUT, OUTPUT> is:
        // - Contravariant in INPUT (can accept supertypes)
        // - Covariant in OUTPUT (can return subtypes)

        Function<Animal, Dog> animalToDog = animal -> new Dog(animal.name + "-pup");

        // Can use where Function<Dog, Animal> is expected:
        // - INPUT: Animal is SUPERtype of Dog (contravariant) ✓
        // - OUTPUT: Dog is SUBtype of Animal (covariant) ✓

        Dog myDog = new Dog("Buddy");
        Dog result = animalToDog.apply(myDog);
        System.out.println("Function result: " + result.name);

        // Supplier is covariant (produces values)
        Supplier<Dog> dogSupplier = () -> new Dog("Random");
        Supplier<? extends Animal> animalSupplier = dogSupplier; // Works!
        Animal a = animalSupplier.get();
        System.out.println("Supplier produced: " + a.name);

        // Consumer is contravariant (consumes values)
        Consumer<Animal> animalConsumer = animal -> System.out.println("Consuming: " + animal.name);
        Consumer<? super Dog> dogConsumer = animalConsumer; // Works!
        dogConsumer.accept(new Dog("Consumed"));
    }

    // 6. Real-world PECS: implementing Collections.copy
    static <T> void copy(List<? super T> dest, List<? extends T> src) {
        // src is PRODUCER (extends) - we READ from it
        // dest is CONSUMER (super) - we WRITE to it

        for (T item : src) {
            dest.add(item);
        }
    }

    static void demonstrateCopyMethod() {
        List<Dog> dogs = Arrays.asList(new Dog("Buddy"), new Dog("Max"));
        List<Animal> animals = new ArrayList<>();

        // Copy from List<Dog> to List<Animal>
        copy(animals, dogs); // Works because of PECS!

        System.out.println("Copied dogs to animals: " + animals);

        // This flexibility is WHY we use wildcards!
    }

    // 7. Comparator contravariance
    static void demonstrateComparatorContravariance() {
        List<Dog> dogs = new ArrayList<>(Arrays.asList(
            new Dog("Buddy"),
            new Dog("Max"),
            new Dog("Charlie")
        ));

        // Comparator<Animal> can be used for List<Dog>
        // Because Comparator is contravariant in its type parameter
        java.util.Comparator<Animal> animalComparator = (a1, a2) -> a1.name.compareTo(a2.name);

        // This works! We can use a comparator for supertype
        dogs.sort(animalComparator);

        System.out.println("Sorted dogs: " + dogs);
    }

    // Helper classes
    static class Animal {
        String name;

        Animal(String name) {
            this.name = name;
        }

        void makeSound() {
            System.out.println(name + " makes a sound");
        }

        @Override
        public String toString() {
            return name;
        }
    }

    static class Dog extends Animal {
        Dog(String name) {
            super(name);
        }

        @Override
        void makeSound() {
            System.out.println(name + " barks: Woof!");
        }
    }

    static class Cat extends Animal {
        Cat(String name) {
            super(name);
        }

        @Override
        void makeSound() {
            System.out.println(name + " meows: Meow!");
        }
    }

    /*
     * SUMMARY:
     *
     * COVARIANCE (? extends T):
     * - Use when READING/PRODUCING values
     * - "Producer Extends"
     * - Can read as T, cannot write
     * - Example: List<? extends Animal> can be List<Dog>, List<Cat>, etc.
     *
     * CONTRAVARIANCE (? super T):
     * - Use when WRITING/CONSUMING values
     * - "Consumer Super"
     * - Can write T, can only read as Object
     * - Example: List<? super Dog> can be List<Dog>, List<Animal>, List<Object>
     *
     * INVARIANCE (T):
     * - Use when BOTH reading and writing
     * - Exact type match required
     * - Most flexible for mutation
     *
     * PECS: Producer Extends, Consumer Super
     * - If you're getting values OUT, use ? extends
     * - If you're putting values IN, use ? super
     * - If you're doing both, don't use wildcards
     */
}
