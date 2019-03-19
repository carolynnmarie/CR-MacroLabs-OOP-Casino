package io.zipcoder.casino;

import io.zipcoder.casino.Cards.Card;
import io.zipcoder.casino.People.Dealer;
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
        Integer actual = dealer.getDealerWallet().checkChipAmount();
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void getHandTest() {
        Dealer dealer = new Dealer();
        ArrayList<Card> expected = new ArrayList<Card>();
        ArrayList<Card> actual = dealer.getHand().toArrayList();
        Assert.assertEquals(actual, expected);
    }
}
