package com.example;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
           // Create a box for Strings
        Box<String> stringBox = new Box<>("Hello");
        System.out.println(stringBox);  // Should print: Box[Hello]
        
        // Create a box for Integers
        Box<Integer> intBox = new Box<>(42);
        System.out.println(intBox.getBoxItem());  // Should print: 42
        
        // Create a box for BigDecimal
        Box<BigDecimal> moneyBox = new Box<>(new BigDecimal("100.50"));
        System.out.println(moneyBox.getBoxItem());  // Should print: 100.50
        
        // Change the value
        intBox.setBoxItem(99);
        System.out.println(intBox.getBoxItem());  // Should print: 99

        
        System.out.println(intBox.toString());
    }
}
