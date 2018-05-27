package io.zipcoder.casino;

import io.zipcoder.casino.money.Wallet;
import org.junit.Assert;
import org.junit.Test;

public class WalletTest {

    @Test
    public void addChipsTest() {
        Wallet testWallet = new Wallet();
        int expected = 500;
        testWallet.addChips(expected);
        int actual = testWallet.checkChips();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void removeChipsTest() {
        Wallet testWallet = new Wallet();
        testWallet.addChips(500);
        testWallet.removeChips(200);
        int expected = 300;
        int actual = testWallet.checkChips();
        Assert.assertEquals(actual, expected);
    }
}
