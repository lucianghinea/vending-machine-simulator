package com.vending.machine;

import java.util.HashMap;
import java.util.Map;

public class Inventory<T> {

    private Map<T, Integer> inventory = new HashMap<>();

    public Inventory() {}

    void add(T item) {
        int count = inventory.get(item);
        inventory.put(item, count + 1);
    }

    void deduct(T item) {
        if (hasItem(item)) {
            int count = inventory.get(item);
            inventory.put(item, count - 1);
        }
    }

    boolean hasItem(T item) {
        return getQuantity(item) > 0;
    }

    void clear() {
        inventory.clear();
    }

    void put(T item, int quantity) {
        inventory.put(item, quantity);
    }

    private int getQuantity(T item) {
        Integer value = inventory.get(item);
        return value == null ? 0 : value;
    }
}
