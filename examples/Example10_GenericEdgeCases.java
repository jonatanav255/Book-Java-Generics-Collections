package com.example;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * EXAMPLE 10: Generic Edge Cases, Type Erasure, and Compiler Magic
 * Difficulty: Expert
 *
 * Demonstrates the weird, wonderful, and frustrating corners of Java generics:
 * - Type erasure and its consequences
 * - Bridge methods (compiler-generated)
 * - Raw types and unchecked warnings
 * - Heap pollution
 * - @SafeVarargs
 * - Generic limitations and workarounds
 */
public class Example10_GenericEdgeCases {

    public static void main(String[] args) {
        System.out.println("=== EXAMPLE 10: Generic Edge Cases ===\n");

        // 1. Type Erasure - all generic info gone at runtime
        System.out.println("--- Type Erasure at Runtime ---");
        demonstrateTypeErasure();

        System.out.println();

        // 2. Raw Types - legacy compatibility (avoid!)
        System.out.println("--- Raw Types (Legacy) ---");
        demonstrateRawTypes();

        System.out.println();

        // 3. Bridge Methods - compiler magic
        System.out.println("--- Bridge Methods ---");
        demonstrateBridgeMethods();

        System.out.println();

        // 4. Heap Pollution with varargs
        System.out.println("--- Heap Pollution & @SafeVarargs ---");
        demonstrateHeapPollution();

        System.out.println();

        // 5. Cannot create instances of type parameter
        System.out.println("--- Creating Generic Instances ---");
        demonstrateGenericInstantiation();

        System.out.println();

        // 6. Cannot create generic arrays (and workarounds)
        System.out.println("--- Generic Array Creation ---");
        demonstrateGenericArrays();

        System.out.println();

        // 7. Type tokens and reflection
        System.out.println("--- Type Tokens with Reflection ---");
        demonstrateTypeTokens();

        System.out.println();

        // 8. Reification workaround
        System.out.println("--- Reification Workaround ---");
        demonstrateReification();

        System.out.println("\n" + "=".repeat(50) + "\n");
    }

    // 1. Type Erasure - generics disappear at runtime
    static void demonstrateTypeErasure() {
        List<String> strings = new ArrayList<>();
        List<Integer> integers = new ArrayList<>();

        // At runtime, BOTH are just List!
        System.out.println("List<String> class: " + strings.getClass().getName());
        System.out.println("List<Integer> class: " + integers.getClass().getName());
        System.out.println("Same class? " + (strings.getClass() == integers.getClass()));

        // Cannot check instanceof with type parameter
        // if (strings instanceof List<String>) {} // COMPILE ERROR!
        if (strings instanceof List<?>) { // Must use wildcard
            System.out.println("Can check instanceof List<?> only");
        }

        // Type erasure means List<T> becomes List at runtime
        // All type parameters are erased to their bounds (or Object if unbounded)
    }

    // 2. Raw Types - using generics without type parameters
    @SuppressWarnings("unchecked")
    static void demonstrateRawTypes() {
        // Raw type - no type parameter (legacy code compatibility)
        List rawList = new ArrayList(); // Warning: raw type!

        rawList.add("String");
        rawList.add(42); // No type safety!
        rawList.add(new Object());

        System.out.println("Raw list: " + rawList);

        // Using raw type loses ALL type safety
        List<String> strings = rawList; // Warning: unchecked assignment!

        try {
            // This compiles but fails at runtime!
            String s = strings.get(1); // ClassCastException! (42 is not a String)
        } catch (ClassCastException e) {
            System.out.println("ERROR: ClassCastException (raw types break type safety)");
        }

        System.out.println("Lesson: NEVER use raw types in new code!");
    }

    // 3. Bridge Methods - compiler generates these
    static void demonstrateBridgeMethods() {
        // When you override a generic method, the compiler creates a "bridge method"

        MyStringNode node = new MyStringNode("Hello");
        node.setData("World");
        System.out.println("Data: " + node.getData());

        // The compiler actually creates TWO setData methods:
        // 1. void setData(String data) - your method
        // 2. void setData(Object data) - bridge method (calls #1)

        // This maintains binary compatibility with type erasure
        System.out.println("Bridge methods are invisible but critical for generics!");
    }

    static class Node<T> {
        T data;

        void setData(T data) {
            this.data = data;
        }

        T getData() {
            return data;
        }
    }

    static class MyStringNode extends Node<String> {
        // After erasure, Node has: void setData(Object data)
        // But this has: void setData(String data)
        // Compiler creates bridge: void setData(Object data) { setData((String)data); }

        MyStringNode(String initialData) {
            this.data = initialData;
        }

        @Override
        void setData(String data) {
            System.out.println("Setting: " + data);
            this.data = data;
        }
    }

    // 4. Heap Pollution - when parameterized type variable refers to object not of that type
    static void demonstrateHeapPollution() {
        // Varargs with generics can cause heap pollution

        // This is UNSAFE but won't show warning due to @SafeVarargs
        List<String> list1 = Arrays.asList("A", "B");
        List<String> list2 = Arrays.asList("C", "D");

        safeMethod(list1, list2);

        // Without @SafeVarargs, we get warnings
        unsafeMethod(list1, list2);
    }

    @SafeVarargs // Promises this method is safe
    static void safeMethod(List<String>... lists) {
        // This is safe - we only read from the array
        for (List<String> list : lists) {
            System.out.println("Safe: " + list);
        }
    }

    @SuppressWarnings("unchecked")
    static void unsafeMethod(List<String>... lists) {
        // This is potentially unsafe
        Object[] objectArray = lists; // Heap pollution starts here!

        // Could do: objectArray[0] = Arrays.asList(42); // Type safety violated!
        // But we won't :)

        System.out.println("Unsafe method (but we're being careful): " + Arrays.toString(lists));
    }

    // 5. Cannot create instances of type parameter
    static void demonstrateGenericInstantiation() {
        // This doesn't work:
        // Factory<String> factory = new Factory<>();
        // String s = factory.create(); // Can't do: new T()

        // Workaround 1: Pass a Supplier
        Factory<String> factory1 = new Factory<>(() -> "Default String");
        System.out.println("Created: " + factory1.create());

        // Workaround 2: Pass the class
        Factory2<String> factory2 = new Factory2<>(String.class);
        // Can't actually instantiate String without constructor args, but you get the idea
        System.out.println("Factory2 has class: " + factory2.getType().getSimpleName());
    }

    static class Factory<T> {
        private final java.util.function.Supplier<T> supplier;

        Factory(java.util.function.Supplier<T> supplier) {
            this.supplier = supplier;
        }

        T create() {
            return supplier.get();
        }
    }

    static class Factory2<T> {
        private final Class<T> type;

        Factory2(Class<T> type) {
            this.type = type;
        }

        Class<T> getType() {
            return type;
        }
    }

    // 6. Cannot create generic arrays
    static void demonstrateGenericArrays() {
        // This doesn't compile:
        // List<String>[] arrayOfLists = new List<String>[10]; // ERROR!

        // Workaround 1: Use List of Lists
        List<List<String>> listOfLists = new ArrayList<>();
        listOfLists.add(Arrays.asList("A", "B"));
        listOfLists.add(Arrays.asList("C", "D"));
        System.out.println("List of lists: " + listOfLists);

        // Workaround 2: Unchecked cast (use with caution!)
        @SuppressWarnings("unchecked")
        List<String>[] arrayOfLists = (List<String>[]) new List<?>[10];
        arrayOfLists[0] = Arrays.asList("X", "Y");
        System.out.println("Array of lists [0]: " + arrayOfLists[0]);

        // Workaround 3: Use reflection
        GenericArray<String> ga = new GenericArray<>(String.class, 5);
        ga.set(0, "First");
        ga.set(1, "Second");
        System.out.println("Generic array [0]: " + ga.get(0));
        System.out.println("Generic array [1]: " + ga.get(1));
    }

    static class GenericArray<T> {
        private final T[] array;

        @SuppressWarnings("unchecked")
        GenericArray(Class<T> type, int size) {
            // Use Array.newInstance for type-safe generic array
            array = (T[]) Array.newInstance(type, size);
        }

        void set(int index, T value) {
            array[index] = value;
        }

        T get(int index) {
            return array[index];
        }

        int length() {
            return array.length;
        }
    }

    // 7. Type Tokens - capturing generic type at runtime
    static void demonstrateTypeTokens() {
        TypeReference<List<String>> typeRef = new TypeReference<List<String>>() {};

        Type type = typeRef.getType();
        System.out.println("Captured type: " + type);

        if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            System.out.println("Raw type: " + pt.getRawType());
            System.out.println("Type arguments: " + Arrays.toString(pt.getActualTypeArguments()));
        }
    }

    // Super Type Token pattern (used by Jackson, Gson, etc.)
    static abstract class TypeReference<T> {
        private final Type type;

        protected TypeReference() {
            Type superclass = getClass().getGenericSuperclass();
            if (superclass instanceof ParameterizedType) {
                this.type = ((ParameterizedType) superclass).getActualTypeArguments()[0];
            } else {
                throw new RuntimeException("Missing type parameter");
            }
        }

        public Type getType() {
            return type;
        }
    }

    // 8. Reification workaround - making types available at runtime
    static void demonstrateReification() {
        ReifiedList<String> strings = new ReifiedList<>(String.class);
        strings.add("Hello");
        strings.add("World");

        System.out.println("Reified list type: " + strings.getComponentType().getSimpleName());
        System.out.println("Reified list: " + strings.getList());

        // Can check type at runtime!
        try {
            if (strings.getComponentType() == String.class) {
                System.out.println("Confirmed: this is a list of Strings");
            }
        } catch (Exception e) {
            System.out.println("Type check failed");
        }
    }

    static class ReifiedList<T> {
        private final Class<T> componentType;
        private final List<T> list = new ArrayList<>();

        ReifiedList(Class<T> componentType) {
            this.componentType = componentType;
        }

        void add(T item) {
            // Can perform runtime type check!
            if (!componentType.isInstance(item)) {
                throw new IllegalArgumentException("Wrong type!");
            }
            list.add(item);
        }

        List<T> getList() {
            return list;
        }

        Class<T> getComponentType() {
            return componentType;
        }
    }

    /*
     * KEY TAKEAWAYS:
     *
     * 1. Type Erasure: Generics are compile-time only, erased at runtime
     * 2. Raw Types: NEVER use them (they break type safety)
     * 3. Bridge Methods: Compiler magic to maintain compatibility
     * 4. Heap Pollution: Be careful with varargs and generics
     * 5. @SafeVarargs: Use when you know your varargs method is safe
     * 6. Cannot instantiate T: Use Supplier or Class<T>
     * 7. Cannot create T[]: Use reflection with Array.newInstance
     * 8. Type Tokens: Capture type info at runtime with anonymous subclasses
     * 9. Reification: Pass Class<T> to preserve type info
     *
     * These are the deep corners of Java generics.
     * Understanding these makes you a generics expert!
     */
}
