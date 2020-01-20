# _**Vending Machine**_
                            
**Description**

 Simulates a real world vending machine with the following requirements: 
 - Accepts coins of 1,5,10,25 Cents i.e. penny, nickel, dime, and quarter.
 - Allow user to select products Coke(25), Pepsi(35), Soda(45)
 - Allow user to take refund by canceling the request.
 - Return selected product and remaining change if any
 - Allow reset operation for vending machine supplier.


**1) High-level Design**

 1. Main interface, classes and Exceptions
    - VendingMachine - an interface which defines public API of VendingMachine
    - VendingMachineImpl - a general purpose implementation of VendingMachine interface
    - Inventory - A type-safe inventory for holding objects, which is and ADAPTER or WRAPPER over java.util.Map
    - Item - type-safe Enum to represent items supported by the vending machine
    - Coin - type-safe enum to represent acceptable coins by the vending machine
    - Bucket - A holder class for holding to types together
    - SoldOutException - thrown when a user selects a product which is sold out
    - NoSufficientChangeException - thrown when the vending machine doesn't have enough chage to support the current transaction
    - NotFullPaidException - thrown when the user tries to collect an item which is not full paid
    
 2. Data structures used
    - Map data structure to implement cash and item inventories
    - List data structure is used for returning change which can contain duplicate coins
 
 3. Motivation behind design
    - Factory design pattern is used to encapsulate the creation of VendingMachine
    - Adapter design pattern is used create Inventory by wrapping java.util.Map
    - java.Lang.Enum is used to represent Item and Coins for the following reasons: 
      - compile time safety against entering an invalid Item or Coin
      - no need to write code for checking if selected item or inserted coin is valid
      - reusable and well encapsulated
    - long is used to represent price and totalSales, which are the amount because we are dealing with cents
    
 **Not used BigDecimal to represent money because the vending machine can only hold a limited amount**