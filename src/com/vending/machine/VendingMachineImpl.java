package com.vending.machine;

import com.vending.machine.enums.Coin;
import com.vending.machine.enums.Item;

import java.util.List;

public class VendingMachineImpl implements VendingMachine {

    @Override
    public long selectItemAndGetPrice(Item item) {
        return 0;
    }

    @Override
    public void insertCoin(Coin Coin) {

    }

    @Override
    public List<Coin> refund() {
        return null;
    }

    @Override
    public Bucket<Item, List<Coin>> collectItemAndChanfe() {
        return null;
    }

    @Override
    public void reset() {

    }
}
