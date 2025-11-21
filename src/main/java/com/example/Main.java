package com.example;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello from Java Generics and Collections!");
        System.out.println("Arguments passed: " + args.length);
        for (int i = 0; i < args.length; i++) {
            System.out.println("  arg[" + i + "]: " + args[i]);
        }
    }
}
