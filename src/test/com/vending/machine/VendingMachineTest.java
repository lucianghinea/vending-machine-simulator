package test.com.vending.machine;

import com.vending.machine.Bucket;
import com.vending.machine.VendingMachine;
import com.vending.machine.VendingMachineFactory;
import com.vending.machine.enums.Coin;
import com.vending.machine.enums.Item;
import com.vending.machine.exceptions.NotFullPaidException;
import com.vending.machine.exceptions.NotSufficientChangeException;
import com.vending.machine.exceptions.SoldOutException;
import org.junit.*;

import java.util.List;

import static org.junit.Assert.*;

public class VendingMachineTest {

    private static VendingMachine vm;

    @BeforeClass
    public static void setUp() {
        vm = VendingMachineFactory.createVendingMachine();
    }

    @AfterClass
    public static void tearDown() {
        vm = null;
    }

    @After
    public void refund() {
        vm.refund(); // Refund after every test to start with a clear balance
    }

    @Test
    public void testBuyItemWithExactPrice() {
        // Select item, price in cents
        long price = vm.selectItemAndGetPrice(Item.COKE);
        // price should be Coke's price
        assertEquals(Item.COKE.getPrice(), price);
        // 25 cents paid
        vm.insertCoin(Coin.QUARTER);

        Bucket<Item, List<Coin>> bucket = vm.collectItemAndChange();
        Item item = bucket.getFirst();
        List<Coin> change = bucket.getSecond();

        // Should be Coke
        assertEquals(Item.COKE, item);
        // There should be no change
        assertTrue(change.isEmpty());
    }

    @Test
    public void testBuyItemWithChange() {
        long price = vm.selectItemAndGetPrice(Item.SODA);
        assertEquals(Item.SODA.getPrice(), price);

        vm.insertCoin(Coin.QUARTER);
        vm.insertCoin(Coin.QUARTER);

        Bucket<Item, List<Coin>> bucket = vm.collectItemAndChange();
        Item item = bucket.getFirst();
        List<Coin> change = bucket.getSecond();

        // Should be Soda
        assertEquals(Item.SODA, item);
        // There should be any change
        assertFalse(change.isEmpty());
        // Compare change
        assertEquals(50 - Item.SODA.getPrice(), getTotal(change));
    }

    @Test
    public void testRefund() {
        long price = vm.selectItemAndGetPrice(Item.PEPSI);
        assertEquals(Item.PEPSI.getPrice(), price);
        vm.insertCoin(Coin.DIME);
        vm.insertCoin(Coin.NICKLE);
        vm.insertCoin(Coin.PENNY);
        vm.insertCoin(Coin.QUARTER);

        assertEquals(41, getTotal(vm.refund()));
    }

    @Test(expected = SoldOutException.class)
    public void testSoldOut() {
        vm.reset();
        vm.selectItemAndGetPrice(Item.COKE);
        vm.insertCoin(Coin.QUARTER);
        vm.collectItemAndChange();

    }

    @Test(expected = NotSufficientChangeException.class)
    public void testNotSufficientChangeException() {
        for (int i = 0; i < 5; i++) {
            vm.selectItemAndGetPrice(Item.SODA);
            vm.insertCoin(Coin.QUARTER);
            vm.insertCoin(Coin.QUARTER);
            vm.collectItemAndChange();

            vm.selectItemAndGetPrice(Item.PEPSI);
            vm.insertCoin(Coin.QUARTER);
            vm.insertCoin(Coin.QUARTER);
            vm.collectItemAndChange();
        }
    }

    @Test(expected = NotFullPaidException.class)
    public void testNotFullPaidException() {
        long price = vm.selectItemAndGetPrice(Item.SODA);
        assertEquals(Item.SODA.getPrice(), price);

        vm.insertCoin(Coin.PENNY);
        vm.collectItemAndChange();
    }

    @Test(expected = SoldOutException.class)
    public void testReset() {
        VendingMachine vendingMachine = VendingMachineFactory.createVendingMachine();
        vendingMachine.reset();

        vendingMachine.selectItemAndGetPrice(Item.SODA);
    }

    private long getTotal(List<Coin> change) {
        long total = 0;

        for (Coin c : change) {
            total += c.getDenomination();
        }

        return total;
    }
}
