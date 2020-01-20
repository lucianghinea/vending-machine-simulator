package com.vending.machine;

public class VendingMachineFactory {

    public static VendingMachineImpl createVendingMachine() {
        return new VendingMachineImpl();
    }
}
