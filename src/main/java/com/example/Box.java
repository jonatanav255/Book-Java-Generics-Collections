package com.example;

public class Box<T> {

    // 1. Add a private field to store the item
    private T value;

    // 2. Create a constructor that takes an item
    public Box(T value) {
        this.value = value;

    }

    // 3. Create a method to get the item
    public T getBoxItem() {
        return this.value;

    }

    public void setBoxItem(T newItem) {
        this.value = newItem;
    }
    // 4. Create a method to set a new item
    // 5. Create a method that checks if box is empty
    // 6. Override toString() to display the item            
}
