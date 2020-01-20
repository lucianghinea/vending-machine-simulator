package com.vending.machine;

import com.vending.machine.enums.Coin;
import com.vending.machine.enums.Item;

import java.util.List;

public interface VendingMachine {

    long selectItemAndGetPrice(Item item);
    void insertCoin(Coin coin);
    List<Coin> refund();
    Bucket<Item, List<Coin>> collectItemAndChange();
    void reset();
}
