package io.zipcoder.casino.people;

import io.zipcoder.casino.cards.Card;
import io.zipcoder.casino.people.Dealer;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class DealerTest {

    @Test
    public void constructorTest() {
        Dealer dealer = new Dealer();
        String expected = "Dealer";
        String actual = dealer.getName();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getWalletTest() {
        Dealer dealer = new Dealer();
        Integer expected = Integer.MAX_VALUE;
        Integer actual = dealer.getDealerWallet().checkChips();
        Assert.assertEquals(actual, expected);
    }

}