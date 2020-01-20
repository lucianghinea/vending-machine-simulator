package com.vending.machine;

import com.vending.machine.enums.Coin;
import com.vending.machine.enums.Item;
import com.vending.machine.exceptions.NotFullPaidException;
import com.vending.machine.exceptions.NotSufficientChangeException;
import com.vending.machine.exceptions.SoldOutException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VendingMachineImpl implements VendingMachine {

    private Inventory<Coin> cashInventory = new Inventory<>();
    private Inventory<Item> itemInventory = new Inventory<>();
    private long totalSales;
    private Item currentItem;
    private long currentBalance;

    public VendingMachineImpl() {
        initialize();
    }

    @Override
    public long selectItemAndGetPrice(Item item) {
        if (itemInventory.hasItem(item)) {
            currentItem = item;
            return item.getPrice();
        }

        throw new SoldOutException("Sold out, please select another item.");
    }

    @Override
    public void insertCoin(Coin coin) {
        currentBalance = currentBalance + coin.getDenomination();
        cashInventory.add(coin);
    }

    @Override
    public List<Coin> refund() {
        List<Coin> refund = getChange(currentBalance);
        updateCashInventory(refund);
        currentBalance = 0;
        currentItem = null;
        return refund;
    }

    @Override
    public Bucket<Item, List<Coin>> collectItemAndChange() {
        Item item = collectItem();
        totalSales = totalSales + item.getPrice();

        List<Coin> change = collectChange();

        return new Bucket<>(item, change);
    }

    @Override
    public void reset() {
        cashInventory.clear();
        itemInventory.clear();
        totalSales = 0;
        currentItem = null;
        currentBalance = 0;
    }

    public void printStatus() {
        System.out.println("Total sales: " + totalSales);
        System.out.println("Current item inventory: " + itemInventory);
        System.out.println("Current cash inventory: " + cashInventory);
    }

    private void initialize() {
        // initialize machine with 5 coins of each denomination and 5 cans of each item
        for (Coin c : Coin.values()) {
            cashInventory.put(c, 5);
        }

        for (Item i : Item.values()) {
            itemInventory.put(i, 5);
        }
    }

    private void updateCashInventory(List<Coin> change) {
        for (Coin c : change) {
            cashInventory.deduct(c);
        }
    }

    private List<Coin> collectChange() {
        long changeAmount = currentBalance - currentItem.getPrice();
        List<Coin> change = getChange(changeAmount);
        updateCashInventory(change);
        currentBalance = 0;
        currentItem = null;
        return change;
    }

    private Item collectItem() throws NotSufficientChangeException, NotFullPaidException {
        if (isFullPaid()) {
            if (hasSufficientChange()) {
                itemInventory.deduct(currentItem);
                return currentItem;
            }

            throw new NotSufficientChangeException("Not sufficient change in inventory");
        }

        long remainingBalance = currentItem.getPrice() - currentBalance;
        throw new NotFullPaidException("Price not full paid, remaining: ", remainingBalance);
    }

    private boolean hasSufficientChange() {
        return hasSufficientChangeForAmount(currentBalance - currentItem.getPrice());
    }

    private List<Coin> getChange(long amount) throws NotSufficientChangeException {

        List<Coin> change = Collections.emptyList();

        if (amount > 0) {
            change = new ArrayList<>();
            long balance = amount;
            while (balance > 0) {
                if (balance >= Coin.QUARTER.getDenomination() && cashInventory.hasItem(Coin.QUARTER)) {
                    change.add(Coin.QUARTER);
                    balance -= Coin.QUARTER.getDenomination();
                } else if (balance >= Coin.DIME.getDenomination() && cashInventory.hasItem(Coin.DIME)) {
                    change.add(Coin.DIME);
                    balance -= Coin.DIME.getDenomination();
                } else if (balance >= Coin.NICKLE.getDenomination() && cashInventory.hasItem(Coin.NICKLE)) {
                    change.add(Coin.NICKLE);
                    balance -= Coin.NICKLE.getDenomination();
                } else if (balance >= Coin.PENNY.getDenomination() && cashInventory.hasItem(Coin.PENNY)) {
                    change.add(Coin.PENNY);
                    balance -= Coin.PENNY.getDenomination();
                } else {
                    throw new NotSufficientChangeException("Not sufficient change, please try another product.");
                }
            }
        }

        return change;
    }

    private boolean hasSufficientChangeForAmount(long amount) {

        try {
            getChange(amount);
        } catch (NotSufficientChangeException e) {
            return false;
        }

        return true;
    }

    private boolean isFullPaid() {
        return currentBalance >= currentItem.getPrice();
    }

    private long getTotalSales() {
        return this.totalSales;
    }
}
